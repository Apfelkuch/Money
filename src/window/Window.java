package window;

import Input.KeyAdapterInput;
import Money.Entry;
import Phrases.Phrases;
import Money.Money;
import utilitis.CustomJButton;
import utilitis.CustomJComboBox;
import utilitis.Options;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Window extends JFrame implements ActionListener {

    // menu bar
    private JMenuBar menuBar;
    private JMenuItem save;
    private JMenuItem exit;

    // table
    // TODO JTable
    private JLabel controlsReceiver_by;
    private JPanel content;
    private final int maxContentElements = 7;

    // dimensions
    private Dimension tableDimension;
    // dimensions control
    private final int bufferPageEnd = 40;
    private final Dimension buttonsDimension = new Dimension(120, 20);
    private final Dimension inputDimensionBig = new Dimension(400, 20);
    private final Dimension inputDimensionSmall = new Dimension(100, 20);
    private final Dimension textDimensionBig = new Dimension(100, 20);
    private final Dimension textDimensionSmall = new Dimension(50, 20);
    private final Dimension extraButton = new Dimension(20, 20);


    // controls
    private JPanel input;
    // TODO JTabbedPane
    private CustomJButton spending;
    private CustomJButton income;
    private boolean isSpending = true;

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

    // improve
    private final int inputValueMax = Integer.parseInt("1000000000");
    private final int inputValueMin = Integer.parseInt("-1000000000");

    private final ArrayList<JComponent> focusElements;

    private boolean editing = false;
    private boolean adding = true;
    private boolean entryShown = false;

    // logic
    private final Money money;

    public Window(String title, Money money) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(title);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setSize(1300, 1100);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.money = money;

        // init
        focusElements = new ArrayList<>();

        this.buildMenuBar();
        this.addTable();
        try {
            this.addControls();
            this.changeToSpending();
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(10);
        }

        this.revalidate();
        this.repaint();

    }

    public void buildMenuBar() {
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        // Option
        JMenu options = new JMenu(Phrases.options);
        menuBar.add(options);
        save = new JMenuItem(Phrases.save);
        save.addActionListener(this);
        options.add(save);
        exit = new JMenuItem(Phrases.exit);
        exit.addActionListener(this);
        options.add(exit);

    }

    private void addTable() {
        // TODO improve the layout of the segments of the table
        int maxWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        tableDimension = new Dimension((int) (maxWidth * (2f / 3f)), 100);

        JPanel tableShow = new JPanel();
        tableShow.setLayout(new FlowLayout(FlowLayout.CENTER));
        tableShow.setBackground(Phrases.COLOR_TABLE_BACKGROUND);
        tableShow.setPreferredSize(new Dimension(tableDimension.width, tableDimension.height * (1 + maxContentElements)));
        this.add(tableShow, BorderLayout.CENTER);

        JPanel table = new JPanel();
        table.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
        table.setPreferredSize(new Dimension(tableDimension.width, tableDimension.height * (1 + maxContentElements)));
        tableShow.add(table);

        // table head row
        JPanel headRow = new JPanel();
        headRow.setLayout(new GridLayout(1, 6));
        headRow.setPreferredSize(tableDimension);
        headRow.setBackground(Phrases.COLOR_HEAD_ROW);
        table.add(headRow);

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
        content.setPreferredSize(new Dimension(tableDimension.width, (tableDimension.height - 2) * maxContentElements));
        content.setBackground(Phrases.COLOR_TABLE_BACKGROUND);

        // TODO adjust that the scrolling is smoother
        content.addMouseWheelListener(e -> money.moveTopEntry((int) e.getPreciseWheelRotation()));

        table.add(content);
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
        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        controls.setPreferredSize(new Dimension(tableDimension.width, ((buttonsDimension.height + 10) * 5) + bufferPageEnd));
        controls.setBackground(Phrases.COLOR_CONTROL_BACKGROUND);
        this.add(controls, BorderLayout.PAGE_END);

        // control Buttons
        JPanel controlButtons = new JPanel();
        controlButtons.setBackground(Phrases.COLOR_CONTROLS);
        controlButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlButtons.setPreferredSize(new Dimension(controls.getPreferredSize().width, buttonsDimension.height + 10));
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
        controlButtonsI.setBackground(Phrases.COLOR_CONTROLS_INPUT);
        controlButtonsI.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlButtonsI.setPreferredSize(controlButtons.getPreferredSize());
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
        input = new JPanel();
        input.setLayout(new GridLayout(1, 2));
        input.setPreferredSize(new Dimension(controls.getPreferredSize().width, (buttonsDimension.height + 10) * 3));
        input.setBackground(controlButtonsI.getBackground());
        controls.add(input);

        JPanel inputLeft = new JPanel();
        inputLeft.setLayout(new GridLayout(3, 1));
        input.add(inputLeft);

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlsReceiver_by = new JLabel("");
        controlsReceiver_by.setPreferredSize(textDimensionBig);
        controlsReceiver_by.setFont(Phrases.inputFont);
        p1.add(controlsReceiver_by);
        inputReceiver_by = new CustomJComboBox<>(money.getList_receiverBy().toArray(new String[0]));
        inputReceiver_by.setFont(Phrases.inputFont);
        inputReceiver_by.setPreferredSize(inputDimensionBig);
        inputReceiver_by.setEditable(true);
        inputReceiver_by.setSelectedItem(null);
        inputReceiver_by.addActionListener(this);
        inputReceiver_by.addKeyListener(new KeyAdapterInput(this));
        inputReceiver_by.setWindow(this);
        focusElements.add(inputReceiver_by);
        p1.add(inputReceiver_by);
        inputLeft.add(p1);

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel ControlsCategory = new JLabel(Phrases.category);
        ControlsCategory.setPreferredSize(textDimensionBig);
        ControlsCategory.setFont(Phrases.inputFont);
        p2.add(ControlsCategory);
        inputCategory = new CustomJComboBox<>(money.getList_categories().toArray(new String[0]));
        inputCategory.setFont(Phrases.inputFont);
        inputCategory.setPreferredSize(inputDimensionBig);
        inputCategory.setEditable(true);
        inputCategory.setSelectedItem(null);
        inputCategory.addActionListener(this);
        inputCategory.setWindow(this);
        focusElements.add(inputCategory);
        p2.add(inputCategory);
        inputLeft.add(p2);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel controlsPurpose = new JLabel(Phrases.purpose);
        controlsPurpose.setPreferredSize(textDimensionBig);
        controlsPurpose.setFont(Phrases.inputFont);
        p3.add(controlsPurpose);
        inputPurpose = new CustomJComboBox<>(money.getList_purpose().toArray(new String[0]));
        inputPurpose.setFont(Phrases.inputFont);
        inputPurpose.setPreferredSize(inputDimensionBig);
        inputPurpose.setEditable(true);
        inputPurpose.setSelectedItem(null);
        inputPurpose.addActionListener(this);
        inputPurpose.setWindow(this);
        focusElements.add(inputPurpose);
        p3.add(inputPurpose);
        inputLeft.add(p3);

        JPanel inputRight = new JPanel();
        inputRight.setLayout(new GridLayout(3, 1));
        input.add(inputRight);

        JPanel p4 = new JPanel();
        p4.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel controlsDate = new JLabel(Phrases.date);
        controlsDate.setPreferredSize(textDimensionSmall);
        controlsDate.setFont(Phrases.inputFont);
        p4.add(controlsDate);

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
        p4.add(inputDate);
        choiceDate = new CustomJButton();
        choiceDate.setPreferredSize(extraButton);
        choiceDate.addActionListener(this);
        focusElements.add(choiceDate);
        p4.add(choiceDate);
        inputRight.add(p4);

        JPanel p5 = new JPanel();
        p5.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel controlsValue = new JLabel(Phrases.value);
        controlsValue.setPreferredSize(textDimensionSmall);
        controlsValue.setFont(Phrases.inputFont);
        p5.add(controlsValue);

        char[] validChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
        inputValue = new JFormattedTextField();
//        inputValue.setFormatterFactory(new DefaultFormatterFactory());
        inputValue.setValue("0,00 €");
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
                int euroSymbol = newContent.indexOf("€"); // find the index of the moneySymbol
                if (euroSymbol != -1) { // shorten the newContent to the area before the first € symbol
                    newContent = newContent.substring(0, euroSymbol);
                }
                if (newContent.indexOf(".") != newContent.lastIndexOf(".")) { // if the input have more than one dot the verification fails
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
                    if (d > inputValueMax || d < inputValueMin) {
                        // improve maybe show it to the user if this is the cause
                        System.out.println("\033[1;31m" + "[Error] Window.inputValue.Verifier: value is out of bounds" + "\033[0m");
                        throw new NumberFormatException();
                    }
                } catch (NullPointerException | NumberFormatException e) {
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
        p5.add(inputValue);
        calcValue = new CustomJButton();
        calcValue.setPreferredSize(extraButton);
        calcValue.addActionListener(this);
        focusElements.add(calcValue);
        p5.add(calcValue);
        inputRight.add(p5);

    }

    public void focusNext() {
        Component component = getFocusOwner();
//        System.out.println("Window.focusNext >> component: " + component.getParent());
        int pos = focusElements.indexOf(component.getParent());
//        System.out.println("Window.focusNext >> pos: " + pos);
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
        inputValue.setValue("0,00 €");
    }

    private void changeToSpending() {
        this.controlsReceiver_by.setText(Phrases.receiver);
        this.clearInput();
        this.spending.setBackground(getBackground().darker());
        this.income.setBackground(getBackground());
        this.revalidate();
        this.repaint();
        this.isSpending = true;
    }

    private void changeToIncome() {
        this.controlsReceiver_by.setText(Phrases.by);
        this.clearInput();
        this.spending.setBackground(getBackground());
        this.income.setBackground(getBackground().darker());
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
            this.inputValue.setValue(entry.getIncome().toString().replaceAll("\\.", ",") + " €");
        } else if (entry.getOption().equals(Options.SPENDING)) {
            changeToSpending();
            this.inputValue.setValue(entry.getSpending().toString().replaceAll("\\.", ",") + " €");
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
            // TODO choiceDate
            System.out.println("choice date");
        } else if (e.getSource() == calcValue) {
            // TODO calcValue
            System.out.println("calc value");
        } else if (e.getSource() == save) {
//            System.out.println("JMenuBar save");
            money.save();
        } else if (e.getSource() == exit) {
//            System.out.println("JMenuBar exit");
            if (money.save()) {
                System.exit(1);
            }
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

}
