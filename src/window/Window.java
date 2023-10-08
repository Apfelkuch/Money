package window;

import Money.Entry;
import Money.Money;
import Phrases.Phrases;
import utilitis.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

// improve enter zur nächsten Ziele weg
// improve enter für Eintragen
// improve Design
public class Window extends JFrame implements ActionListener {

    public static final Dimension extraButton = new Dimension(20, 20);
    // layers
    private final JPanel mainLayer;
    private final int contentHeight = 50;
    // dimensions
    private final Dimension maxInputDim = new Dimension(900, 0);
    // dimensions control
    private final Dimension buttonsDimension = new Dimension(100, 25);
    private final Dimension inputDimensionBig = new Dimension(200, 20);
    private final Dimension inputDimensionSmall = new Dimension(100, 20);
    private final Dimension textDimensionBig = new Dimension(80, 20);
    private final Dimension textDimensionSmall = new Dimension(50, 20);
    private final ArrayList<Component> focusElements;
    // logic
    private final Money money;
    // overlays
    private miniCalculator miniCalculator;
    private choseDate choseDate;
    private Settings settings;
    // menu bar
    private JMenuItem save;
    private JMenuItem exit;
    private JMenuItem deletePaths;
    private JMenuItem menuItemSettings;
    private JMenuItem saveUnder;
    // table
    private JLabel controlsReceiver_by;
    private JPanel headRow;
    private JPanel content;
    private JPanel split;
    private int maxContentElements = 4;
    private int oldMaxContentElements;
    // controls
    private JPanel controlPanel;
    private JPanel controls;
    private int oldControlsWidth;
    private JPanel input;
    private JPanel controlButtons, controlButtonsI;
    private CustomJButton spending;
    private CustomJButton income;
    private boolean isSpending;
    private CustomJButton neu;
    private CustomJButton edit;
    private CustomJButton enter;
    private CustomJButton cancel;
    private CustomJComboBox<String> inputReceiver_by;
    private CustomJComboBox<String> inputCategory;
    private CustomJComboBox<String> inputPurpose;
    private JFormattedTextField inputDate;
    private CustomJButton choiceDate;
    private InputField_fixes inputValue;
    private CustomJButton calcValue;
    private boolean editing = false;
    private boolean entryShown = false;
    // Window state
    private boolean programEdited = false;
    private final String title;

    public Window(String title, Money money) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(title);
        this.title = title;
        // start the window Maximized
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setSize(600, 470);
        this.setMinimumSize(this.getSize());
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon("res\\money.png").getImage());

        this.money = money;

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // adjust the position of the windows if the frame is resized
                if (miniCalculator != null) miniCalculator.setLocation(calcValue.getLocationOnScreen());
                if (choseDate != null) choseDate.setLocation(choiceDate.getLocationOnScreen());
                if (settings != null) settings.setLocation(null);
                // adjust the count of the content elements on the table
                if (Math.abs((content.getHeight() / contentHeight) - maxContentElements) <= 1) { // The Window is manually resized, for smooth content transition
                    if (content.getHeight() >= ((maxContentElements + 1) * contentHeight)) {
                        updateMaxContentElements(1);
                    }
                    if (content.getHeight() < (maxContentElements * contentHeight)) {
                        updateMaxContentElements(-1);
                    }
                } else { // The Window is resized step by step, change the amount of more than one content-line on the screen
                    updateMaxContentElements((content.getHeight() / contentHeight) - maxContentElements);
                    maxContentElements = content.getHeight() / contentHeight;
                }
                // adjust the input control size
                controls.setSize(new Dimension(Math.min(controlPanel.getWidth(), maxInputDim.width), controls.getHeight()));
                controls.setPreferredSize(controls.getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                // adjust the position of the windows if the frame is moved
                if (miniCalculator != null) miniCalculator.setLocation(calcValue.getLocationOnScreen());
                if (choseDate != null) choseDate.setLocation(choiceDate.getLocationOnScreen());
                if (settings != null) settings.setLocation(null);
            }
        });

        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getOldState() == Frame.NORMAL) { // leave from normal: save the old maxContentElements and controlsPanel width. And adjust the values.
//                    System.out.println("windowStateListener >> oldState = normal");
                    oldMaxContentElements = maxContentElements;
                    money.saveOldTopEntry();
                    oldControlsWidth = controls.getWidth();
                    revalidate();
                    setMaxContentElements(content.getHeight() / contentHeight);
                    controls.setSize(new Dimension(Math.min(controlPanel.getWidth(), maxInputDim.width), controls.getHeight()));
                    controls.setPreferredSize(controls.getSize());
                }
                if (e.getNewState() == Frame.NORMAL) { // return to normal: set the maxContentElements and controlsPanel width to the saved one.
//                    System.out.println("windowStateListener >> newState = normal");
                    // adjust the maxContentElements
                    money.loadOldTopEntry();
                    setMaxContentElements(oldMaxContentElements);
                    controls.setSize(new Dimension(oldControlsWidth, controls.getHeight()));
                    controls.setPreferredSize(controls.getSize());
                }
                revalidate();
                repaint();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = ExtraWindow.confirmDialog(null, Phrases.saveDialogTitle, Phrases.saveDialogText, Phrases.showFontPlain, Phrases.EXTRA_WINDOW_BACKGROUND, Phrases.EXTRA_WINDOW_FOREGROUND);
                if (result == ExtraWindow.EXIT_WITH_YES) {
                    save();
                }
            }
        });

        // init
        focusElements = new ArrayList<>();

        mainLayer = new JPanel();
        mainLayer.setLayout(new BorderLayout());
        this.buildMenuBar();
        this.addTable();
        try {
            this.addControls();
            this.isSpending = false;
            this.changeToSpending();
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(10);
        }
        this.setContentPane(mainLayer);


        this.revalidate();
        this.repaint();

    }

    public boolean programIsEdited() {
        if (programEdited)
            return false;
        programEdited = true;
        this.setTitle(title + " *");
        return true;
    }

    private void updateMaxContentElements(int amount) {
        maxContentElements += amount;
        content.setLayout(new GridLayout(maxContentElements, 1));
        money.updateAllEntries();
    }

    public void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        mainLayer.add(menuBar, BorderLayout.NORTH);

        // Option
        JMenu options = new JMenu(Phrases.options);

        menuBar.add(options);
        save = new JMenuItem(Phrases.save);
        save.addActionListener(this);
        options.add(save);

        saveUnder = new JMenuItem(Phrases.saveUnder);
        saveUnder.addActionListener(this);
        options.add(saveUnder);

        exit = new JMenuItem(Phrases.exit);
        exit.addActionListener(this);
        options.add(exit);

        deletePaths = new JMenuItem(Phrases.deletePaths);
        deletePaths.addActionListener(this);
        options.add(deletePaths);

        menuItemSettings = new JMenuItem(Phrases.settings);
        menuItemSettings.addActionListener(this);
        options.add(menuItemSettings);

    }

    private void addTable() {
        JPanel table = new JPanel();
        table.setLayout(new BorderLayout());
        mainLayer.add(table, BorderLayout.CENTER);

        // table head row
        headRow = new JPanel();
        headRow.setLayout(new GridLayout(1, 6));
        headRow.setBackground(Phrases.COLOR_TABLE_HEAD_ROW);
        table.add(headRow, BorderLayout.NORTH);

        JLabel tableNumber = new JLabel(Phrases.number);
        tableNumber.setFont(Phrases.showFontBold);
        tableNumber.setBorder(new LineBorder(Color.BLACK, 2));
        tableNumber.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableNumber);
        JLabel tableDate = new JLabel(Phrases.date);
        tableDate.setFont(Phrases.showFontBold);
        tableDate.setBorder(new LineBorder(Color.BLACK, 2));
        tableDate.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableDate);
        JLabel tableReceiverCategoryPurpose = new JLabel("<html>" + Phrases.receiver + "<br>" + Phrases.category + "<br>" + Phrases.purpose + "</html>");
        tableReceiverCategoryPurpose.setFont(Phrases.showFontBold);
        tableReceiverCategoryPurpose.setBorder(new LineBorder(Color.BLACK, 2));
        tableReceiverCategoryPurpose.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableReceiverCategoryPurpose);
        JLabel tableSpending = new JLabel(Phrases.tableSpending);
        tableSpending.setFont(Phrases.showFontBold);
        tableSpending.setBorder(new LineBorder(Color.BLACK, 2));
        tableSpending.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableSpending);
        JLabel tableIncome = new JLabel(Phrases.tableIncome);
        tableIncome.setFont(Phrases.showFontBold);
        tableIncome.setBorder(new LineBorder(Color.BLACK, 2));
        tableIncome.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableIncome);
        JLabel tableBalance = new JLabel(Phrases.balance);
        tableBalance.setFont(Phrases.showFontBold);
        tableBalance.setBorder(new LineBorder(Color.BLACK, 2));
        tableBalance.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableBalance);

        // table content
        content = new JPanel();
        content.setLayout(new GridLayout(maxContentElements, 1));
        content.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);

        content.addMouseWheelListener(e -> money.moveTopEntry((int) e.getPreciseWheelRotation()));

        table.add(content, BorderLayout.CENTER);

        // split
        split = new JPanel();
        split.setBackground(Phrases.COLOR_TABLE_SPLIT);
        split.setPreferredSize(new Dimension(0, split.getPreferredSize().height / 2));
        table.add(split, BorderLayout.SOUTH);
    }

    public void addContentToTable(Entry entry) {
        if (content.getComponents().length >= maxContentElements) {
            this.removeTopElement();
        }
        content.add(entry.showEntry(content.getWidth(), content.getHeight() / maxContentElements, this));

        content.requestFocus();

        this.revalidate();
        this.repaint();
    }

    public void removeTopElement() {
        this.content.remove(0);
    }

    private void addControls() throws ParseException {
        controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBackground(Phrases.COLOR_CONTROL_BACKGROUND);

        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        controlPanel.setBackground(Phrases.COLOR_CONTROL_PANEL_BACKGROUND);
        controlPanel.add(controls);
        mainLayer.add(controlPanel, BorderLayout.SOUTH);


        // control Buttons
        controlButtons = new JPanel();
        controlButtons.setBackground(Phrases.COLOR_CONTROL_1);
        controlButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        controls.add(controlButtons);

        spending = new CustomJButton(Phrases.tableSpending);
        spending.setFont(Phrases.inputFont);
        spending.setPreferredSize(buttonsDimension);
        spending.addActionListener(this);
//        focusElements.add(spending);
        controlButtons.add(spending);

        income = new CustomJButton(Phrases.tableIncome);
        income.setFont(Phrases.inputFont);
        income.setPreferredSize(buttonsDimension);
        income.addActionListener(this);
//        focusElements.add(income);
        controlButtons.add(income);

        // control Buttons I
        controlButtonsI = new JPanel();
        controlButtonsI.setBackground(Phrases.COLOR_CONTROL_2);
        controlButtonsI.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        controls.add(controlButtonsI);

        neu = new CustomJButton(Phrases.neu);
        neu.setFont(Phrases.inputFont);
        neu.setPreferredSize(buttonsDimension);
        neu.addActionListener(this);
//        focusElements.add(neu);
        controlButtonsI.add(neu);

        edit = new CustomJButton(Phrases.edit);
        edit.setFont(Phrases.inputFont);
        edit.setPreferredSize(buttonsDimension);
        edit.addActionListener(this);
//        focusElements.add(edit);
        controlButtonsI.add(edit);

        enter = new CustomJButton(Phrases.enter);
        enter.setFont(Phrases.inputFont);
        enter.setPreferredSize(buttonsDimension);
        enter.addActionListener(this);
//        focusElements.add(enter);
        controlButtonsI.add(enter);

        cancel = new CustomJButton(Phrases.cancel);
        cancel.setFont(Phrases.inputFont);
        cancel.setPreferredSize(buttonsDimension);
        cancel.addActionListener(this);
//        focusElements.add(cancel);
        controlButtonsI.add(cancel);

        // input
        input = new JPanel();
        input.setLayout(new GridLayout(3, 2));
        input.setMaximumSize(maxInputDim);
        input.setBackground(Phrases.COLOR_CONTROL_3);
        controls.add(input);

        // receiverBy
        JPanel jPanelReceiverBy = new JPanel();
        jPanelReceiverBy.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelReceiverBy.setOpaque(false);
        controlsReceiver_by = new JLabel("");
        controlsReceiver_by.setPreferredSize(textDimensionBig);
        controlsReceiver_by.setFont(Phrases.inputFont);
        jPanelReceiverBy.add(controlsReceiver_by);
        inputReceiver_by = new CustomJComboBox<>(new String[0], this);
        inputReceiver_by.setFont(Phrases.inputFont);
        inputReceiver_by.setPreferredSize(inputDimensionBig);
        inputReceiver_by.setEditable(true);
        inputReceiver_by.setSelectedItem(null);
        inputReceiver_by.setName("inputReceiver_by");
        focusElements.add(inputReceiver_by);
        jPanelReceiverBy.add(inputReceiver_by);

        // category
        JPanel jPanelCategory = new JPanel();
        jPanelCategory.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelCategory.setOpaque(false);
        JLabel ControlsCategory = new JLabel(Phrases.category);
        ControlsCategory.setPreferredSize(textDimensionBig);
        ControlsCategory.setFont(Phrases.inputFont);
        jPanelCategory.add(ControlsCategory);
        inputCategory = new CustomJComboBox<>(new String[0], this);
        inputCategory.setFont(Phrases.inputFont);
        inputCategory.setPreferredSize(inputDimensionBig);
        inputCategory.setEditable(true);
        inputCategory.setSelectedItem(null);
        inputCategory.setName("inputCategory");
        focusElements.add(inputCategory);
        jPanelCategory.add(inputCategory);

        // purpose
        JPanel jPanelPurpose = new JPanel();
        jPanelPurpose.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelPurpose.setOpaque(false);
        JLabel controlsPurpose = new JLabel(Phrases.purpose);
        controlsPurpose.setPreferredSize(textDimensionBig);
        controlsPurpose.setFont(Phrases.inputFont);
        jPanelPurpose.add(controlsPurpose);
        inputPurpose = new CustomJComboBox<>(new String[0], this);
        inputPurpose.setFont(Phrases.inputFont);
        inputPurpose.setPreferredSize(inputDimensionBig);
        inputPurpose.setEditable(true);
        inputPurpose.setSelectedItem(null);
        inputPurpose.setName("inputPurpose");
        focusElements.add(inputPurpose);
        jPanelPurpose.add(inputPurpose);

        // date
        JPanel jPanelDate = new JPanel();
        jPanelDate.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelDate.setOpaque(false);
        JLabel controlsDate = new JLabel(Phrases.date);
        controlsDate.setPreferredSize(textDimensionSmall);
        controlsDate.setFont(Phrases.inputFont);
        jPanelDate.add(controlsDate);

        inputDate = new JFormattedTextField();
        inputDate.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("##.##.####")));
        inputDate.setInputVerifier(new InputVerifier() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            @Override
            public boolean verify(JComponent input) {
                JTextComponent tc = (JTextComponent) input;
                String newContent = tc.getText();
                if (newContent.length() > 0) {
                    try {
                        Date d = sdf.parse(newContent);

                        if (!sdf.format(d).equals(newContent)) {
                            tc.selectAll();
                            return false;
                        }
                    } catch (ParseException ex) {
                        tc.selectAll();
                        return false;
                    }
                }
                return true;
            }
        });
        inputDate.setFont(Phrases.inputFont);
        inputDate.setPreferredSize(inputDimensionSmall);
        inputDate.setBorder(null);
        inputDate.setValue(setDateOnControl(LocalDate.now()));
        inputDate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    focusNext();
                }
            }
        });
        inputDate.setName("inputDate");
        focusElements.add(inputDate);
        jPanelDate.add(inputDate);
        choiceDate = new CustomJButton();
        choiceDate.setPreferredSize(extraButton);
        choiceDate.addActionListener(this);
        choiceDate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (choseDate != null) {
                    choseDate.keyTyped(e);
                }
            }
        });
//        focusElements.add(choiceDate);
        jPanelDate.add(choiceDate);

        JPanel jPanelValue = new JPanel();
        jPanelValue.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelValue.setOpaque(false);
        JLabel controlsValue = new JLabel(Phrases.value);
        controlsValue.setPreferredSize(textDimensionSmall);
        controlsValue.setFont(Phrases.inputFont);
        jPanelValue.add(controlsValue);

        char[] validChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',', '.', KeyEvent.VK_DELETE, KeyEvent.VK_ENTER};
        inputValue = new InputField_fixes("0,00");
        inputValue.setPrefix("");
        inputValue.setPostfix(" " + Phrases.moneySymbol);
        inputValue.setFont(Phrases.inputFont);
        inputValue.addKeyListener(new KeyAdapter() { // only typed valid key are added
            @Override
            public void keyTyped(KeyEvent e) {
                boolean valid = false;
                for (char c : validChars) {
                    if (e.getKeyChar() == c) {
                        valid = true;
                        break;
                    }
                }

                if (!valid) {
//                    System.out.println("Window.keyPressed: Input for the input value is invalid");
                    e.consume();
                }
            }
        });
        inputValue.setInputVerifier(new InputVerifier() {
            private final DecimalFormat df = new DecimalFormat("0.00");
            private JTextComponent tc = null;

            @Override
            public boolean verify(JComponent input) {
                tc = (JTextComponent) input;
                String content = tc.getText();
                // clean the
                content = content.replaceAll(",", "."); // replace ',' with '.'
                content = content.replaceAll(" ", "");
                content = content.replaceAll("\t", "");
                content = content.replaceAll(Character.toString(160), ""); // no-break space (Geschütztes Leerzeichen) removing. ASCI value: 160
                int euroSymbol = content.indexOf(Phrases.moneySymbol); // find the index of the moneySymbol
                if (euroSymbol != -1) { // shorten the content to the area before the first moneySymbol
                    content = content.substring(0, euroSymbol);
                }
                if (content.indexOf(".") != content.lastIndexOf(".")) { // if the input have more than one dot the verification fails
                    return failedVerification(Phrases.invalidInput);
                }
                char[] characters = content.toCharArray(); // check if the String only have valid Characters
                for (char c : characters) {
                    boolean result = false;
                    for (char c1 : validChars) {
                        if (c == c1) {
                            result = true;
                            break;
                        }
                    }
                    if (!result) { // if a char is invalid the verification fails
                        return failedVerification(Phrases.invalidInputChar);
                    }
                }
                // checks if the string is blank or only contains a dot
                if (content.isBlank() || (content.contains(".") && content.length() == 1)) {
                    content = "0.0";
                }
                double input_number;
                try {
                    input_number = Double.parseDouble(content);
                    if (input_number > Phrases.inputValueMax || input_number < Phrases.inputValueMin) {
                        System.err.println("[Error] Window.inputValue.Verifier: value is out of bounds");
                        throw new NumberFormatException();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    return failedVerification(Phrases.valueOutOfBounce);
                }
                // shorten the input on two digits behind the comma
                Double output_number = new BigDecimal(Double.toString(input_number)).setScale(2, RoundingMode.HALF_UP).doubleValue();
                inputValue.setValue(df.format(output_number)); // set the inputValue field to the build String
                return true;
            }

            private boolean failedVerification(String message) {
                new CustomPopup(inputValue.getLocationOnScreen().x, inputValue.getLocationOnScreen().y + inputValue.getHeight(), message);
                if (tc != null) {
                    tc.selectAll();
                }
                return false;
            }

        });
        inputValue.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                inputValue.getInputVerifier().verify(inputValue);
            }
        });
        inputValue.setPreferredSize(inputDimensionSmall);
        inputValue.setBorder(null);
        inputValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    focusNext();
                }
            }
        });
        inputValue.setName("inputValue");
        focusElements.add(inputValue);
        jPanelValue.add(inputValue);
        calcValue = new CustomJButton();
        calcValue.setPreferredSize(extraButton);
        calcValue.addActionListener(this);
        calcValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (miniCalculator != null) {
                    calcValue.requestFocus();
                    miniCalculator.keyTyped(e);
                } else {
                    if (e.getKeyChar() == '\n') {
                        actionPerformed(new ActionEvent(calcValue, 0, ""));
                    }
                }
            }
        });
//        focusElements.add(calcValue);
        jPanelValue.add(calcValue);


        input.add(jPanelReceiverBy);
        input.add(jPanelDate);
        input.add(jPanelCategory);
        input.add(jPanelValue);
        input.add(jPanelPurpose);

    }

    public void focusNext() {
        // The JFormattedTextFields are direct focus Owner.
        // The CustomJComboBox are not direct the focus Owner, their Parents are.
        Component component = getFocusOwner();
        if (component == null) {
            System.err.println("[Error] Window.focusNext >> component == null");
            return;
        }
        int pos = focusElements.contains(component) ? focusElements.indexOf(component) : focusElements.indexOf(component.getParent());
        if (pos == -1) {
            System.err.println("[Error] Window.focusNext >> pos == -1");
        }
        int newPos = pos + 1 >= focusElements.size() ? 0 : pos + 1;
        focusElements.get(newPos).requestFocus();
    }

    public boolean isInputEmpty() {
        return (inputReceiver_by.getSelectedItem() == null || inputReceiver_by.getSelectedItem().toString().isBlank()) &&
                (inputCategory.getSelectedItem() == null || inputCategory.getSelectedItem().toString().isBlank()) &&
                (inputPurpose.getSelectedItem() == null || inputPurpose.getSelectedItem().toString().isBlank());
    }

    public void clearInput() {
        inputReceiver_by.setSelectedIndex(-1);
        inputCategory.setSelectedIndex(-1);
        inputPurpose.setSelectedIndex(-1);
        inputDate.setValue(setDateOnControl(LocalDate.now()));
        inputValue.setValue("0,00");
    }

    private void changeToSpending() {
        if (isSpending)
            return;
        this.controlsReceiver_by.setText(Phrases.receiver);
        this.clearInput();
        this.income.setBackground(spending.getBackground());
        this.spending.setBackground(spending.getBackground().darker());
        this.revalidate();
        this.repaint();
        this.isSpending = true;
    }

    private void changeToIncome() {
        if (!isSpending)
            return;
        this.controlsReceiver_by.setText(Phrases.by);
        this.clearInput();
        this.spending.setBackground(income.getBackground());
        this.income.setBackground(income.getBackground().darker());
        this.revalidate();
        this.repaint();
        this.isSpending = false;
    }

    public void showEntry(Entry entry) {
        this.clearInput();
        this.changeEnabled(false);
        entryShown = true;
        if (entry.getOption().equals(Options.INCOME)) {
            changeToIncome();
            this.inputValue.setValue(entry.getIncome().toString().replaceAll("\\.", ","));
        } else if (entry.getOption().equals(Options.SPENDING)) {
            changeToSpending();
            this.inputValue.setValue(entry.getSpending().toString().replaceAll("\\.", ","));
        }
        this.inputReceiver_by.setSelectedItem(entry.getReceiverBy());
        this.inputCategory.setSelectedItem(entry.getCategory());
        this.inputPurpose.setSelectedItem(entry.getPurpose());
        this.inputDate.setValue(setDateOnControl(entry.getLocalDate()));
    }

    private void changeEnabled(boolean enabled) {
        this.inputReceiver_by.setEnabled(enabled);
        this.inputCategory.setEnabled(enabled);
        this.inputPurpose.setEnabled(enabled);
        this.inputDate.setEnabled(enabled);
        this.inputValue.setEnabled(enabled);
    }

    public void setInputReceiver_by(String[] strings) {
//        System.out.println("Window.setInputReceiver_by >> strings: " + Arrays.toString(strings));
        inputReceiver_by.setModel(new DefaultComboBoxModel<>(strings));
        inputReceiver_by.setSelectedItem(null);
    }

    public void reload() {
        // change color of panels
        headRow.setBackground(Phrases.COLOR_TABLE_HEAD_ROW);
        content.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
        split.setBackground(Phrases.COLOR_TABLE_SPLIT);
        controls.setBackground(Phrases.COLOR_CONTROL_BACKGROUND);
        controlPanel.setBackground(Phrases.COLOR_CONTROL_PANEL_BACKGROUND);
        controlButtons.setBackground(Phrases.COLOR_CONTROL_1);
        controlButtonsI.setBackground(Phrases.COLOR_CONTROL_2);
        input.setBackground(Phrases.COLOR_CONTROL_3);
        // change the input button colors
        spending.setBackground_BorderColor(Phrases.COLOR_BUTTON);
        income.setBackground_BorderColor(Phrases.COLOR_BUTTON);
        neu.setBackground_BorderColor(Phrases.COLOR_BUTTON);
        edit.setBackground_BorderColor(Phrases.COLOR_BUTTON);
        enter.setBackground_BorderColor(Phrases.COLOR_BUTTON);
        cancel.setBackground_BorderColor(Phrases.COLOR_BUTTON);
        choiceDate.setBackground_BorderColor(Phrases.COLOR_BUTTON);
        calcValue.setBackground_BorderColor(Phrases.COLOR_BUTTON);
        // change the ComboBox-Arrow colors
        inputReceiver_by.setArrowButtonColor(Phrases.COLOR_BUTTON);
        inputCategory.setArrowButtonColor(Phrases.COLOR_BUTTON);
        inputPurpose.setArrowButtonColor(Phrases.COLOR_BUTTON);

        // change to spending
        this.isSpending = false;
        this.changeToSpending();

        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.revalidate();
        this.repaint();
        if (e.getSource() == spending) { // spending
            entryShown = false;
            if (isInputEmpty()) {
                changeToSpending();
            }
            editing = false;
        } else if (e.getSource() == income) { // income
            entryShown = false;
            if (isInputEmpty()) {
                changeToIncome();
            }
            editing = false;
        } else if (e.getSource() == neu) { // new
            entryShown = false;
            this.clearInput();
            this.changeEnabled(true);
            this.changeToSpending();
            editing = false;
        } else if (e.getSource() == edit) {
            entryShown = false;
            if (!isInputEmpty() && !this.inputReceiver_by.isEnabled()) { // edit
                this.changeEnabled(true);
                editing = true;
            }
        } else if (e.getSource() == enter) {
            this.programIsEdited();
            entryShown = false;
            if (isInputEmpty() || !this.inputReceiver_by.isEnabled())
                return;
            if (editing) { // confirm edit
                money.confirmEdit();
                clearInput();
            } else { // enter
                money.enter();
                clearInput();
            }
            editing = false;

        } else if (e.getSource() == cancel) { // cancel
            entryShown = false;
            this.clearInput();
            this.changeEnabled(true);
            editing = false;
        } else if (e.getSource() == choiceDate) { // choice date
            choseDate = new choseDate(choiceDate.getLocationOnScreen(), this);
            choseDate.setLocalDate(this.getInputLocalDate());
        } else if (e.getSource() == calcValue) { // calc value
            if (getInputValue() > Phrases.inputValueMax) {
                setInputValue(String.valueOf(Phrases.inputValueMax));
            }
            miniCalculator = new miniCalculator(calcValue.getLocationOnScreen(), this);
            calcValue.requestFocus();
        } else if (e.getSource() == save) { // JMenuBar save
            this.save();
        } else if (e.getSource() == saveUnder) { // JMenuBar saveUnder
            // create the save-path
            FileChooser fileChooser = new FileChooser(Phrases.FILE_PATHS);
            if (fileChooser.showSaveDialog(null) == FileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getPath();
                File newFile = new File(path.substring(path.lastIndexOf('.') + 1).equals(Phrases.EXTENSION) ? path : path.concat("." + Phrases.EXTENSION));
                try {
                    if (newFile.createNewFile()) {
                        System.out.println("Window.actionPerformed >> saveUnder has created a new File");
                    } else {
                        System.out.println("Window.actionPerformed >> saveUnder File already existed");
                    }
                    money.setPath(newFile.getPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            // save
            save();
        } else if (e.getSource() == exit) { // JMenuBar exit
            if (save()) {
                System.exit(1);
            }
        } else if (e.getSource() == deletePaths) { // JMenuBar deletePaths
            money.clearPaths();
        } else if (e.getSource() == menuItemSettings) { // JMenuBar settings
            settings = new Settings(null, this);
        }
    }

    private boolean save() {
        this.setTitle(this.title);
        this.programEdited = false;
        return money.save();
    }

    public void edit(Entry entry) {
        money.edit(entry);
    }

    public String setDateOnControl(LocalDate date) {
        return (date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : date.getDayOfMonth()) +
                "." +
                (date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue()) +
                "." +
                (date.getYear() < 10 ? "0" + date.getYear() : date.getYear());
    }

    public void clearEntries() {
        content.removeAll();
    }

    public Money getMoney() {
        return money;
    }

    public boolean isSpending() {
        return isSpending;
    }

    // GETTER && SETTER

    public boolean isEntryShown() {
        return entryShown;
    }

    public int getMaxContentElements() {
        return maxContentElements;
    }

    private void setMaxContentElements(int maxContentElements) {
        this.maxContentElements = maxContentElements;
        content.setLayout(new GridLayout(maxContentElements, 1));
        money.updateAllEntries();
    }

    public String getInputReceiverBy() {
        return (String) inputReceiver_by.getSelectedItem();
    }

    public String getInputCategory() {
        String content = (String) inputCategory.getSelectedItem();
        return content == null ? " " : content;
    }

    public void setInputCategory(String[] strings) {
//        System.out.println("Window.setInputCategory >> strings: " + Arrays.toString(strings));
        inputCategory.setModel(new DefaultComboBoxModel<>(strings));
        inputCategory.setSelectedItem(null);
    }

    public String getInputPurpose() {
        String content = (String) inputPurpose.getSelectedItem();
        return content == null ? " " : content;
    }

    public void setInputPurpose(String[] strings) {
//        System.out.println("Window.setInputPurpose >> strings: " + Arrays.toString(strings));
        inputPurpose.setModel(new DefaultComboBoxModel<>(strings));
        inputPurpose.setSelectedItem(null);
    }

    public LocalDate getInputLocalDate() {
        String[] date = inputDate.getText().split("\\.");
        return LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
    }

    public double getInputValue() {
        return Double.parseDouble(inputValue.getValue().replaceAll(",", "."));
    }

    public void setInputValue(String value) {
        JTextField textField = new JTextField(value);
        this.inputValue.getInputVerifier().verify(textField);
        this.inputValue.setValue(textField.getText());
        inputValue.requestFocus();
    }

    public void setInputDate(LocalDate localDate) {
        this.inputDate.setValue(setDateOnControl(localDate));
    }

    public void setChoseDate(window.choseDate choseDate) {
        this.choseDate = choseDate;
    }

    public void setMiniCalculator(window.miniCalculator miniCalculator) {
        this.miniCalculator = miniCalculator;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
