package window;

import Input.KeyAdapterInput;
import Money.Entry;
import Money.Money;
import Phrases.Phrases;
import utilitis.CustomJButton;
import utilitis.CustomJComboBox;
import utilitis.Options;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Window extends JFrame implements ActionListener {

    // layers
    private final JPanel mainLayer;

    // overlays
    private miniCalculator miniCalculator;
    private choseDate choseDate;
    private Settings settings;

    // menu bar
    private JMenuItem save;
    private JMenuItem exit;
    private JMenuItem path;
    private JMenuItem deletePaths;
    private JMenuItem menuItemSettings;

    // table
    private JLabel controlsReceiver_by;
    private JPanel content;
    private final int contentHeight = 50;
    private int maxContentElements = 4;
    private int oldMaxContentElements;

    // dimensions
    private final Dimension maxInputDim = new Dimension(900, 0);
    // dimensions control
    private final Dimension buttonsDimension = new Dimension(100, 25);
    private final Dimension inputDimensionBig = new Dimension(200, 20);
    private final Dimension inputDimensionSmall = new Dimension(100, 20);
    private final Dimension textDimensionBig = new Dimension(80, 20);
    private final Dimension textDimensionSmall = new Dimension(50, 20);
    public static final Dimension extraButton = new Dimension(20, 20);


    // controls
    private JPanel controlPanel;
    private JPanel controls;
    private int oldControlsWidth;

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
    private JFormattedTextField inputValue;
    private CustomJButton calcValue;


    private final ArrayList<Container> focusElements;

    private boolean editing = false;
    private boolean adding = true;
    private boolean entryShown = false;

    // logic
    private final Money money;

    public Window(String title, Money money) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(title);
        // start the window Maximized
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setSize(600, 470);
        this.setMinimumSize(this.getSize());
        this.setResizable(true);
        this.setLocationRelativeTo(null);

        this.money = money;

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // adjust the position of the windows if the frame is resized
                if (miniCalculator != null) miniCalculator.setLocation(calcValue.getLocationOnScreen());
                if (choseDate != null) choseDate.setLocation(choiceDate.getLocationOnScreen());
                if (settings != null) settings.setLocation(null);
                // adjust the count of the content elements on the table
                if (content.getHeight() >= ((maxContentElements + 1) * contentHeight)) {
                    updateMaxContentElements(1);
                }
                if (content.getHeight() < (maxContentElements * contentHeight)) {
                    updateMaxContentElements(-1);
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
        mainLayer.setBackground(Phrases.MAIN_LAYER_BACKGROUND);
        this.setContentPane(mainLayer);


        this.revalidate();
        this.repaint();

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

        exit = new JMenuItem(Phrases.exit);
        exit.addActionListener(this);
        options.add(exit);

        deletePaths = new JMenuItem(Phrases.deletePaths);
        deletePaths.addActionListener(this);
        options.add(deletePaths);

        path = new JMenuItem(money.getPath());
        path.addActionListener(this);
        options.add(path);

        menuItemSettings = new JMenuItem(Phrases.settings);
        menuItemSettings.addActionListener(this);
        options.add(menuItemSettings);

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
        JPanel split = new JPanel();
        split.setBackground(Phrases.COLOR_TABLE_SPLIT);
        table.add(split, BorderLayout.SOUTH);
    }

    public void addContentToTable(Entry entry) {
        if (content.getComponents().length >= maxContentElements) {
            this.removeTopElement();
        }
        content.add(entry.showEntry(content.getWidth(), content.getHeight() / maxContentElements, this));

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

        controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        controlPanel.setBackground(Phrases.COLOR_CONTROL_PANEL_BACKGROUND);
        controlPanel.add(controls);
        mainLayer.add(controlPanel, BorderLayout.SOUTH);


        // control Buttons
        JPanel controlButtons = new JPanel();
        controlButtons.setBackground(Phrases.COLOR_CONTROL_1);
        controlButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        controls.add(controlButtons);

        spending = new CustomJButton(Phrases.tableSpending);
        spending.setFont(Phrases.inputFont);
        spending.setPreferredSize(buttonsDimension);
        spending.addActionListener(this);
        focusElements.add(spending);
        controlButtons.add(spending);

        income = new CustomJButton(Phrases.tableIncome);
        income.setFont(Phrases.inputFont);
        income.setPreferredSize(buttonsDimension);
        income.addActionListener(this);
        focusElements.add(income);
        controlButtons.add(income);

        // control Buttons I
        JPanel controlButtonsI = new JPanel();
        controlButtonsI.setBackground(Phrases.COLOR_CONTROL_2);
        controlButtonsI.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        controls.add(controlButtonsI);

        neu = new CustomJButton(Phrases.neu);
        neu.setFont(Phrases.inputFont);
        neu.setPreferredSize(buttonsDimension);
        neu.addActionListener(this);
        focusElements.add(neu);
        controlButtonsI.add(neu);

        edit = new CustomJButton(Phrases.edit);
        edit.setFont(Phrases.inputFont);
        edit.setPreferredSize(buttonsDimension);
        edit.addActionListener(this);
        focusElements.add(edit);
        controlButtonsI.add(edit);

        enter = new CustomJButton(Phrases.enter);
        enter.setFont(Phrases.inputFont);
        enter.setPreferredSize(buttonsDimension);
        enter.addActionListener(this);
        focusElements.add(enter);
        controlButtonsI.add(enter);

        cancel = new CustomJButton(Phrases.cancel);
        cancel.setFont(Phrases.inputFont);
        cancel.setPreferredSize(buttonsDimension);
        cancel.addActionListener(this);
        focusElements.add(cancel);
        controlButtonsI.add(cancel);

        // input
        JPanel input = new JPanel();
        input.setLayout(new GridLayout(3, 2));
        input.setMaximumSize(maxInputDim);
        input.setBackground(controlButtonsI.getBackground());
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
        inputReceiver_by.addActionListener(this);
        inputReceiver_by.addKeyListener(new KeyAdapterInput(this));
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
        inputCategory.addActionListener(this);
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
        inputPurpose.addActionListener(this);
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

        // improve date ??? function but not sure if it is perfect ???
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
        focusElements.add(choiceDate);
        jPanelDate.add(choiceDate);

        JPanel jPanelValue = new JPanel();
        jPanelValue.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelValue.setOpaque(false);
        JLabel controlsValue = new JLabel(Phrases.value);
        controlsValue.setPreferredSize(textDimensionSmall);
        controlsValue.setFont(Phrases.inputFont);
        jPanelValue.add(controlsValue);

        char[] validChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
        inputValue = new JFormattedTextField();
//        inputValue.setFormatterFactory(new DefaultFormatterFactory());
        inputValue.setValue("0,00 " + Phrases.moneySymbol);
        inputValue.setFont(Phrases.inputFont);
        inputValue.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextComponent tc = (JTextComponent) input;
                String newContent = tc.getText();
                newContent = newContent.replaceAll(",", "."); // replace ',' to '.'
                newContent = newContent.replaceAll(" ", "");
                newContent = newContent.replaceAll("\t", "");
                newContent = newContent.replaceAll(Character.toString(160), ""); // no-break space (Geschütztes Leerzeichen) removing. ASCI value: 160
                int euroSymbol = newContent.indexOf(Phrases.moneySymbol); // find the index of the moneySymbol
                if (euroSymbol != -1) { // shorten the newContent to the area before the first moneySymbol
                    newContent = newContent.substring(0, euroSymbol);
                }
                if (newContent.indexOf(".") != newContent.lastIndexOf(".")) { // if the input have more than one dot the verification fails
                    showPopup(inputValue.getLocationOnScreen().x, inputValue.getLocationOnScreen().y + inputValue.getHeight(), Phrases.invalidInput);
                    tc.selectAll();
                    return false;
                }
                char[] characters = newContent.toCharArray(); // check if the String only have valid Characters
                for (char c : characters) {
                    boolean result = false;
                    for (char c1 : validChars) {
                        if (c == c1) {
                            result = true;
                            break;
                        }
                    }
                    if (!result) { // if a char is invalid the verification fails
                        showPopup(inputValue.getLocationOnScreen().x, inputValue.getLocationOnScreen().y + inputValue.getHeight(), Phrases.invalidInputChar);
                        tc.selectAll();
                        return false;
                    }
                }
                // checks if the string is blank or only contains a dot
                if (newContent.isBlank() || (newContent.contains(".") && newContent.length() == 1)) {
                    newContent = "0.0";
                }
                double d;
                try {
                    d = Double.parseDouble(newContent);
                    if (d > Phrases.inputValueMax || d < Phrases.inputValueMin) {
                        System.out.println("\033[1;31m" + "[Error] Window.inputValue.Verifier: value is out of bounds" + "\033[0m");
                        throw new NumberFormatException();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    showPopup(inputValue.getLocationOnScreen().x, inputValue.getLocationOnScreen().y + inputValue.getHeight(), Phrases.valueOutOfBounce);
                    tc.selectAll();
                    return false;
                }
                // shorten the input on two digits behind the comma
                Double d1 = new BigDecimal(Double.toString(d)).setScale(2, RoundingMode.HALF_UP).doubleValue();
                String d2; // Build the String which is displayed
                switch (d1.toString().substring(d1.toString().indexOf(".") + 1).length()) { // assure that the value have always to digits behind the comma
                    case 0 -> d2 = d1 + ".00";
                    case 1 -> d2 = d1 + "0";
                    default -> d2 = d1 + "";
                }
                String s = d2.replaceAll("\\.", ",") + " " + Phrases.moneySymbol; // Add the money symbol
                ((JTextComponent) input).setText(s); // set the inputValue field to the build String
                return true;
            }
        });
        inputValue.setPreferredSize(inputDimensionSmall);
        inputValue.setBorder(null);
        focusElements.add(inputValue);
        jPanelValue.add(inputValue);
        calcValue = new CustomJButton();
        calcValue.setPreferredSize(extraButton);
        calcValue.addActionListener(this);
        calcValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (miniCalculator != null)
                    miniCalculator.keyTyped(e);
            }
        });
        focusElements.add(calcValue);
        jPanelValue.add(calcValue);


        input.add(jPanelReceiverBy);
        input.add(jPanelDate);
        input.add(jPanelCategory);
        input.add(jPanelValue);
        input.add(jPanelPurpose);

    }

    private void showPopup(int x, int y, String message) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new JLabel(message));
        popupMenu.setLocation(x, y);
        popupMenu.setVisible(true);
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                popupMenu.setVisible(false);
            }
        }, 2000);
    }

    public void focusNext() {
        Component component = getFocusOwner().getParent();
        if (component == null) {
            return;
        }
        int pos = focusElements.indexOf(component);
        focusElements.get(pos + 1).requestFocus();
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
        inputValue.setValue("0,00 " + Phrases.moneySymbol);
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
            this.inputValue.setValue(entry.getIncome().toString().replaceAll("\\.", ",") + " " + Phrases.moneySymbol);
        } else if (entry.getOption().equals(Options.SPENDING)) {
            changeToSpending();
            this.inputValue.setValue(entry.getSpending().toString().replaceAll("\\.", ",") + " " + Phrases.moneySymbol);
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

    public void setInputCategory(String[] strings) {
//        System.out.println("Window.setInputCategory >> strings: " + Arrays.toString(strings));
        inputCategory.setModel(new DefaultComboBoxModel<>(strings));
        inputCategory.setSelectedItem(null);
    }

    public void setInputPurpose(String[] strings) {
//        System.out.println("Window.setInputPurpose >> strings: " + Arrays.toString(strings));
        inputPurpose.setModel(new DefaultComboBoxModel<>(strings));
        inputPurpose.setSelectedItem(null);
    }

    // TODO complete the actions and the functionality.
    @Override
    public void actionPerformed(ActionEvent e) {
        this.revalidate();
        this.repaint();
        if (e.getSource() == spending) {
//            System.out.println("spending");
            entryShown = false;
            if (isInputEmpty()) {
                changeToSpending();
            }
            editing = false;
            adding = true;
        } else if (e.getSource() == income) {
//            System.out.println("income");
            entryShown = false;
            if (isInputEmpty()) {
                changeToIncome();
            }
            editing = false;
            adding = true;
        } else if (e.getSource() == neu) {
//            System.out.println("neu");
            entryShown = false;
            this.clearInput();
            this.changeEnabled(true);
            this.changeToSpending();
            editing = false;
            adding = true;
        } else if (e.getSource() == edit) {
            entryShown = false;
            if (!isInputEmpty() && !this.inputReceiver_by.isEnabled()) {
//                System.out.println("edit");
                this.changeEnabled(true);
                editing = true;
                adding = false;
            }
        } else if (e.getSource() == enter) {
            entryShown = false;
            if (!isInputEmpty() && adding && !editing) {
//                System.out.println("enter");
                money.enter();
                editing = false;
                clearInput();
                adding = true;
            } else if (editing) {
//                System.out.println("confirm edit");
                money.confirmEdit();
                editing = false;
                clearInput();
                adding = true;
            }
        } else if (e.getSource() == cancel) {
//            System.out.println("cancel");
            entryShown = false;
            this.clearInput();
            this.changeEnabled(true);
            editing = false;
            adding = true;
        } else if (e.getSource() == choiceDate) {
//            System.out.println("choice date");
            choseDate = new choseDate(choiceDate.getLocationOnScreen(), this);
            choseDate.setLocalDate(this.getInputLocalDate());
        } else if (e.getSource() == calcValue) {
//            System.out.println("calc value");
            miniCalculator = new miniCalculator(calcValue.getLocationOnScreen(), this);
        } else if (e.getSource() == save) {
//            System.out.println("JMenuBar save");
            money.save();
        } else if (e.getSource() == exit) {
//            System.out.println("JMenuBar exit");
            if (money.save()) {
                System.exit(1);
            }
        } else if (e.getSource() == deletePaths) {
//            System.out.println("JMenuBar deletePaths")
            money.clearPaths();
        } else if (e.getSource() == path) {
//            System.out.println("JMenuBar path")
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String selectedFilePath = selectedFile.getPath();
                money.setPath(selectedFilePath);
                path.setText(selectedFilePath);
            }
        } else if (e.getSource() == menuItemSettings) {
            settings = new Settings(null, this);
        }
    }

    public void edit(Entry entry) {
        money.edit(entry);
        adding = false;
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

    // GETTER && SETTER

    public boolean isSpending() {
        return isSpending;
    }

    public boolean isEditing() {
        return editing;
    }

    public boolean isEntryShown() {
        return entryShown;
    }

    public int getMaxContentElements() {
        return maxContentElements;
    }

    public String getInputReceiverBy() {
        return (String) inputReceiver_by.getSelectedItem();
    }

    public String getInputCategory() {
        return (String) inputCategory.getSelectedItem();
    }

    public String getInputPurpose() {
        return (String) inputPurpose.getSelectedItem();
    }

    public LocalDate getInputLocalDate() {
        String[] date = inputDate.getText().split("\\.");
        return LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
    }

    public double getInputValue() {
        return Double.parseDouble(inputValue.getValue().toString().replaceAll(",", ".").replaceAll(" " + Phrases.moneySymbol, ""));
    }

    public void setInputValue(String value) {
        this.inputValue.setValue(value);
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

    private void setMaxContentElements(int maxContentElements) {
        this.maxContentElements = maxContentElements;
        content.setLayout(new GridLayout(maxContentElements, 1));
        money.updateAllEntries();
    }
}
