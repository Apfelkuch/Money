package window;

import filechooser.CustomFileChooser;
import money.Entry;
import money.Money;
import phrases.Phrases;
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
import java.util.Objects;

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
    private MiniCalculator miniCalculator;
    private CalendarOverlay selectDate;
    // menu bar
    private JMenuItem save;
    private JMenuItem exit;
    private JMenuItem deletePaths;
    private JMenuItem saveUnder;
    // table
    private JLabel controlsReceiver_by;
    private JPanel content;
    private int maxContentElements = 4;
    private int oldMaxContentElements;
    // controls
    private JPanel controlPanel;
    private JPanel controls;
    private int oldControlsWidth;
    private CustomJButton spending;
    private CustomJButton income;
    private boolean isSpending;
    private CustomJButton create;
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
        super(title);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.title = title;
        // start the window Maximized
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setSize(600, 470);
        this.setMinimumSize(this.getSize());
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/money.png"))).getImage());

        this.money = money;

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // adjust the position of the windows if the frame is resized
                if (miniCalculator != null) miniCalculator.setLocation(calcValue.getLocationOnScreen());
                if (selectDate != null) selectDate.setLocation(choiceDate.getLocationOnScreen());
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
                if (selectDate != null) selectDate.setLocation(choiceDate.getLocationOnScreen());
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
                closeProgram();
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

    public void programEdited() {
        if (programEdited)
            return;
        programEdited = true;
        this.setTitle(title + " *");
    }

    private void updateMaxContentElements(int amount) {
        maxContentElements += amount;
        content.setLayout(new GridLayout(maxContentElements, 1));
        money.updateAllEntries();
    }

    public void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        formatMenuComponent(menuBar);
        mainLayer.add(menuBar, BorderLayout.NORTH);

        // Option
        JMenu options = new JMenu(Phrases.options);
        formatMenuComponent(options);
        menuBar.add(options);

        save = new JMenuItem(Phrases.save);
        formatMenuComponent(save);
        save.addActionListener(this);
        options.add(save);

        saveUnder = new JMenuItem(Phrases.saveUnder);
        formatMenuComponent(saveUnder);
        saveUnder.addActionListener(this);
        options.add(saveUnder);

        options.addSeparator();

        exit = new JMenuItem(Phrases.exit);
        formatMenuComponent(exit);
        exit.addActionListener(this);
        options.add(exit);

        options.addSeparator();

        deletePaths = new JMenuItem(Phrases.deletePaths);
        formatMenuComponent(deletePaths);
        deletePaths.addActionListener(this);
        options.add(deletePaths);

    }

    private void formatMenuComponent(JComponent component) {
        component.setBackground(Phrases.BACKGROUND);
        component.setForeground(Phrases.FOREGROUND);
        component.setBorder(new LineBorder(Phrases.BACKGROUND));
    }

    private void addTable() {
        JPanel table = new JPanel();
        table.setLayout(new BorderLayout());
        mainLayer.add(table, BorderLayout.CENTER);

        // table head row
        JPanel headRow = new JPanel();
        headRow.setLayout(new GridLayout(1, 6));
        headRow.setBackground(Phrases.COLOR_TABLE_HEAD_ROW);
        table.add(headRow, BorderLayout.NORTH);

        JLabel tableNumber = new JLabel(Phrases.number);
        formatTableLabel(tableNumber);
        headRow.add(tableNumber);
        JLabel tableDate = new JLabel(Phrases.date);
        formatTableLabel(tableDate);
        headRow.add(tableDate);
        JLabel tableReceiverCategoryPurpose = new JLabel("<html>" + Phrases.receiver + "<br>" + Phrases.category + "<br>" + Phrases.purpose + "</html>");
        formatTableLabel(tableReceiverCategoryPurpose);
        headRow.add(tableReceiverCategoryPurpose);
        JLabel tableSpending = new JLabel(Phrases.tableSpending);
        formatTableLabel(tableSpending);
        headRow.add(tableSpending);
        JLabel tableIncome = new JLabel(Phrases.tableIncome);
        formatTableLabel(tableIncome);
        headRow.add(tableIncome);
        JLabel tableBalance = new JLabel(Phrases.balance);
        formatTableLabel(tableBalance);
        headRow.add(tableBalance);

        // table content
        content = new JPanel();
        content.setLayout(new GridLayout(maxContentElements, 1));
        content.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);

        content.addMouseWheelListener(e -> money.moveTopEntry((int) e.getPreciseWheelRotation()));

        table.add(content, BorderLayout.CENTER);

        // split
        JPanel split = new JPanel();
        split.setBackground(Phrases.BACKGROUND);
        split.setPreferredSize(new Dimension(0, split.getPreferredSize().height / 2));
        table.add(split, BorderLayout.SOUTH);
    }

    private void formatTableLabel(JLabel label) {
        label.setFont(Phrases.showFontBold);
        label.setBorder(new LineBorder(Color.BLACK, 2));
        label.setHorizontalAlignment(JLabel.CENTER);
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
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        controlPanel.setBackground(Phrases.BACKGROUND);
        mainLayer.add(controlPanel, BorderLayout.SOUTH);

        controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controlPanel.add(controls);

        // control Buttons for input mode (spending / income)
        JPanel controlInputMode = new JPanel();
        controlInputMode.setBackground(Phrases.COLOR_CONTROL_BUTTONS);
        controlInputMode.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        controls.add(controlInputMode);

        spending = new CustomJButton(Phrases.tableSpending);
        spending.setFont(Phrases.inputFont);
        spending.setPreferredSize(buttonsDimension);
        spending.addActionListener(this);
//        focusElements.add(spending);
        controlInputMode.add(spending);

        income = new CustomJButton(Phrases.tableIncome);
        income.setFont(Phrases.inputFont);
        income.setPreferredSize(buttonsDimension);
        income.addActionListener(this);
//        focusElements.add(income);
        controlInputMode.add(income);

        // control Buttons for actions (create, edit, enter, cancel)
        JPanel controlActions = new JPanel();
        controlActions.setBackground(Phrases.COLOR_CONTROL_BUTTONS);
        controlActions.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        controls.add(controlActions);

        create = new CustomJButton(Phrases.neu);
        create.setFont(Phrases.inputFont);
        create.setPreferredSize(buttonsDimension);
        create.addActionListener(this);
//        focusElements.add(neu);
        controlActions.add(create);

        edit = new CustomJButton(Phrases.edit);
        edit.setFont(Phrases.inputFont);
        edit.setPreferredSize(buttonsDimension);
        edit.addActionListener(this);
//        focusElements.add(edit);
        controlActions.add(edit);

        enter = new CustomJButton(Phrases.enter);
        enter.setFont(Phrases.inputFont);
        enter.setPreferredSize(buttonsDimension);
        enter.addActionListener(this);
//        focusElements.add(enter);
        controlActions.add(enter);

        cancel = new CustomJButton(Phrases.cancel);
        cancel.setFont(Phrases.inputFont);
        cancel.setPreferredSize(buttonsDimension);
        cancel.addActionListener(this);
//        focusElements.add(cancel);
        controlActions.add(cancel);

        // input
        JPanel input = new JPanel();
        input.setLayout(new GridLayout(3, 2));
        input.setMaximumSize(maxInputDim);
        input.setBackground(Phrases.COLOR_CONTROL_INPUTS);
        controls.add(input);

        // receiverBy
        JPanel jPanelReceiverBy = new JPanel();
        jPanelReceiverBy.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelReceiverBy.setOpaque(false);
        controlsReceiver_by = new JLabel("");
        controlsReceiver_by.setPreferredSize(textDimensionBig);
        controlsReceiver_by.setFont(Phrases.inputFont);
        jPanelReceiverBy.add(controlsReceiver_by);
        inputReceiver_by = new CustomJComboBox<>(this);
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
        inputCategory = new CustomJComboBox<>(this);
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
        inputPurpose = new CustomJComboBox<>(this);
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
                    addEntry();
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
                if (selectDate != null) {
                    selectDate.keyTyped(e);
                }
            }
        });
        choiceDate.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icons8-kalender-16.png"))));
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
                    addEntry();
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
        calcValue.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icons8-taschenrechner-16.png"))));
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
        return (inputReceiver_by.getSelectedItem() == null || inputReceiver_by.getSelectedItem().toString().isBlank()) ||
                (inputCategory.getSelectedItem() == null || inputCategory.getSelectedItem().toString().isBlank()) ||
                (inputPurpose.getSelectedItem() == null || inputPurpose.getSelectedItem().toString().isBlank());
    }

    public boolean isInputEnoughContentForEntry() {
        boolean inputReceiverBy_Filled = this.inputReceiver_by.getSelectedItem() != null && !this.inputReceiver_by.getSelectedItem().toString().isBlank();
        return !isInputEmpty() || inputReceiverBy_Filled;
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

    public void closeProgram() {
        if (!isProgramEdited()) {
            dispose();
            System.exit(0);
        }
        int result = ExtraWindow.confirmDialog(this, Phrases.saveDialogTitle, Phrases.saveDialogText, Phrases.showFontPlain, Phrases.BACKGROUND_LIGHT, Phrases.FOREGROUND, true);
        if (result == ExtraWindow.EXIT_WITH_CANCEL) {
            return;
        }
        if (result == ExtraWindow.EXIT_WITH_YES) {
            save();
        }
        dispose();
        System.exit(0);
    }

    public void enterEntry() {
        if (!this.inputReceiver_by.isEnabled() || !isInputEnoughContentForEntry())
            return;
        this.programEdited();
        if (editing) {
            money.confirmEdit();
        } else {
            money.addNewEntry();
        }
        editing = false;
        entryShown = false;
        clearInput();
    }

    public void addEntry() {
        enterEntry();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.revalidate();
        this.repaint();
        if (e.getSource() == spending) { // spending
            if (!editing && !entryShown && isInputEmpty()) {
                changeToSpending();
            }
        } else if (e.getSource() == income) { // income
            if (!editing && !entryShown && isInputEmpty()) {
                changeToIncome();
            }
        } else if (e.getSource() == create) { // create
            entryShown = false;
            editing = false;
            this.clearInput();
            this.changeEnabled(true);
            this.changeToSpending();
        } else if (e.getSource() == edit) {
            if (entryShown) { // edit
                entryShown = false;
                editing = true;
                this.changeEnabled(true);
            }
        } else if (e.getSource() == enter) {
            enterEntry();
        } else if (e.getSource() == cancel) { // cancel
            entryShown = false;
            editing = false;
            this.clearInput();
            this.changeEnabled(true);
        } else if (e.getSource() == choiceDate) { // choice date
            selectDate = new CalendarOverlay(choiceDate.getLocationOnScreen(), this);
            selectDate.setLocalDate(this.getInputLocalDate());
        } else if (e.getSource() == calcValue) { // calc value
            if (getInputValue() > Phrases.inputValueMax) {
                setInputValue(String.valueOf(Phrases.inputValueMax));
            }
            miniCalculator = new MiniCalculator(calcValue.getLocationOnScreen(), this);
            calcValue.requestFocus();
        } else if (e.getSource() == save) { // JMenuBar save
            this.save();
        } else if (e.getSource() == saveUnder) { // JMenuBar saveUnder
            // create the save-path
            CustomFileChooser customFileChooser = new CustomFileChooser(Phrases.FILE_PATHS);
            if (customFileChooser.showSaveDialog(this) == CustomFileChooser.APPROVE_OPTION) {
                File newFile = new File(customFileChooser.getSelectedFileMoneyFormat().getPath());
                try {
                    if (!newFile.createNewFile()) {
                        System.out.println("Window.actionPerformed >> saveUnder File already existed");
                        if (ExtraWindow.confirmDialog(this, Phrases.overrideWhenSavingTitle, Phrases.overrideWhenSavingMessage,
                                Phrases.showFontBold, Phrases.BACKGROUND, Phrases.FOREGROUND, true) != ExtraWindow.EXIT_WITH_OK) {
                            throw new Exception("Saving ist canceled");
                        }
                    } else {
                        System.out.println("Window.actionPerformed >> saveUnder has created a new File");
                    }
                    money.setPath(newFile.getPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    // Possibility to add a message that saving has been canceled
                }
            }
            // save
            save();
        } else if (e.getSource() == exit) { // JMenuBar exit
            closeProgram();
        } else if (e.getSource() == deletePaths) { // JMenuBar deletePaths
            money.clearPaths();
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

    public void setChoseDate(CalendarOverlay selectDate) {
        this.selectDate = selectDate;
    }

    public void setMiniCalculator(MiniCalculator miniCalculator) {
        this.miniCalculator = miniCalculator;
    }

    public boolean isProgramEdited() {
        return programEdited;
    }
}
