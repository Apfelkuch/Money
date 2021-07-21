package window;

import Input.KeyAdapterInput;
import Input.MouseAdapterEntry;
import Money.Entry;
import Phrases.Phrases;
import main.Money;
import utilitis.CustomJButton;
import utilitis.CustomJComboBox;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Window extends JFrame implements ActionListener {

    // table
    private Panel input;
    private JLabel controlsReceiver_by;
    private Panel content;
    private int contentElements = 7;

    // dimensions
    private Dimension tableDimension;
    // dimensions control
    Dimension buttonsDimension = new Dimension(120, 20);
    Dimension inputDimensionBig = new Dimension(400, 20);
    Dimension inputDimensionSmall = new Dimension(100, 20);
    Dimension textDimensionBig = new Dimension(100, 20);
    Dimension textDimensionSmall = new Dimension(50, 20);
    Dimension extraButton = new Dimension(20, 20);


    // controls
    private CustomJButton spending;
    private CustomJButton income;

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

    private ArrayList<Component> focusElements;

    private boolean editing = false;

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
        this.setVisible(true);

        this.money = money;

        this.init();
        this.addTable();
        try {
            this.addControls();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.repaint();

        // TODO for testing
        this.addContentToTable(new Entry(Entry.options.SPENDING, 0, LocalDate.of(2021, Calendar.AUGUST, 10), "receiver1", "category1", "purpose1", 1f, 0f, 1f));
        this.addContentToTable(new Entry(Entry.options.SPENDING, 0, LocalDate.of(2021, Calendar.AUGUST, 11), "receiver2", "category2", "purpose2", 2f, 0f, 3f));
        this.addContentToTable(new Entry(Entry.options.SPENDING, 0, LocalDate.of(2021, 7, 12), "receiver3", "category3", "purpose3", 3f, 0f, 6f));
        this.addContentToTable(new Entry(Entry.options.INCOME, 0, LocalDate.of(2021, 7, 13), "receiver4", "category4", "purpose4", 0f, 1f, 5f));
        this.addContentToTable(new Entry(Entry.options.INCOME, 0, LocalDate.of(2021, 7, 14), "receiver5", "category5", "purpose5", 0f, 2f, 3f));
        this.addContentToTable(new Entry(Entry.options.SPENDING, 0, LocalDate.of(2021, 7, 15), "receiver6", "category6", "purpose6", 1f, 0f, 4f));
        this.addContentToTable(new Entry(Entry.options.SPENDING, 0, LocalDate.of(2021, 7, 16), "receiver7", "category7", "purpose7", 2f, 0f, 6f));
        this.removeTopElement();
        this.addContentToTable(new Entry(Entry.options.SPENDING, 0, LocalDate.of(2021, 7, 17), "receiver8", "category8", "purpose8", 3f, 0f, 9f));
    }

    public void init() {
        new Phrases();

        focusElements = new ArrayList<>();

    }

    private void addTable() {
        tableDimension = new Dimension((int) (this.getWidth() * (2f / 3f)), 100);

        Panel tableShow = new Panel();
        tableShow.setLayout(new FlowLayout(FlowLayout.CENTER));
        tableShow.setBackground(Phrases.COLOR_TABLE_BACKGROUND);
        tableShow.setPreferredSize(new Dimension(tableDimension.width, tableDimension.height * (1 + contentElements)));
        this.add(tableShow, BorderLayout.CENTER);

        Panel table = new Panel();
        table.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
        table.setPreferredSize(new Dimension(tableDimension.width, tableDimension.height * (1 + contentElements)));
        tableShow.add(table);

        // table head row
        Panel headRow = new Panel();
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
//        JLabel tableIncome = new JLabel(Phrases.tableSpending + ":" + Phrases.tableIncome);
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
        content = new Panel();
        content.setLayout(new GridLayout(contentElements, 1));
        content.setPreferredSize(new Dimension(tableDimension.width, (tableDimension.height - 2) * contentElements));
        table.add(content);
    }

    public void addContentToTable(Entry entry) {
        Panel newEntry = new Panel();
        newEntry.setSize(content.getWidth(), content.getHeight() / contentElements);
        newEntry.setLayout(new GridLayout(1, 6));
        newEntry.add(buildLabel(JLabel.CENTER, "" + entry.getNumber(),0));
        newEntry.add(buildLabel(JLabel.CENTER, this.setDateOnTable(entry.getLocalDate()),0));

        //OPTION 1
        newEntry.add(buildLabel(JLabel.LEFT, "<html>" + entry.getReceiverBy() + "<br>" + entry.getCategory() + "<br>" + entry.getPurpose() + "</html>",2),2);
        newEntry.add(buildLabel(JLabel.CENTER, entry.getIncome() == 0.0 ? "" : entry.getIncome() + " €", 0));
        newEntry.add(buildLabel(JLabel.CENTER, entry.getSpending() == 0.0 ? "" : entry.getSpending() + " €",0));

//        // OPTION 2
//        newEntry.add(buildLabel(JLabel.CENTER, "",0));
//        if(entry.getSpending() == 0.0) {
//            newEntry.add(buildLabel(JLabel.LEFT, entry.getIncome() == 0.0 ? "" : entry.getIncome() + " €",0));
//        } else {
//            newEntry.add(buildLabel(JLabel.RIGHT, entry.getSpending() == 0.0 ? "" : entry.getSpending() + " €",0));
//        }
//        newEntry.add(buildLabel(JLabel.LEFT, "<html>" + entry.getReceiverBy() + "<br>" + entry.getCategory() + "<br>" + entry.getPurpose() + "</html>",2),2);

        newEntry.add(buildLabel(JLabel.CENTER, entry.getBalance() + " €",0));
        newEntry.addMouseListener(new MouseAdapterEntry(this, newEntry, entry));
        content.add(newEntry);

        this.revalidate();
        this.repaint();
    }

    public void removeTopElement() {
        this.content.remove(0);
    }

    private JLabel buildLabel(int alignment, String content, int width) {
        JLabel label = new JLabel(content);
        label.setFont(Phrases.showFontPlain);
        label.setBorder(new LineBorder(Color.BLACK, 1));
        label.setHorizontalAlignment(alignment);
        if (!(width == 0)) {
            label.setPreferredSize(new Dimension((this.content.getWidth() / 6) * width,this.content.getHeight() / contentElements));
        }
        return label;
    }

    private void addControls() throws ParseException {

        int bufferPageEnd = 50;

        Panel controls = new Panel();
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        controls.setPreferredSize(new Dimension((int) (this.getWidth() * (2f / 3f)), ((buttonsDimension.height + 10) * 5) + bufferPageEnd));
        controls.setBackground(Phrases.COLOR_CONTROL_BACKGROUND);
        this.add(controls, BorderLayout.PAGE_END);

        // control Buttons
        Panel controlButtons = new Panel();
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
        Panel controlButtonsI = new Panel();
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
        input = new Panel();
        input.setLayout(new GridLayout(1, 2));
        input.setPreferredSize(new Dimension(controls.getPreferredSize().width, (buttonsDimension.height + 10) * 3));
        input.setBackground(controlButtonsI.getBackground());
        controls.add(input);

        Panel inputLeft = new Panel();
        inputLeft.setLayout(new GridLayout(3, 1));
        input.add(inputLeft);

        Panel p1 = new Panel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlsReceiver_by = new JLabel(Phrases.receiver);
        controlsReceiver_by.setPreferredSize(textDimensionBig);
        controlsReceiver_by.setFont(Phrases.inputFont);
        p1.add(controlsReceiver_by);
        inputReceiver_by = new CustomJComboBox<String>(money.getList_receiverBy().toArray(new String[0]));
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

        Panel p2 = new Panel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel ControlsCategory = new JLabel(Phrases.category);
        ControlsCategory.setPreferredSize(textDimensionBig);
        ControlsCategory.setFont(Phrases.inputFont);
        p2.add(ControlsCategory);
        inputCategory = new CustomJComboBox<String>(money.getList_categories().toArray(new String[0]));
        inputCategory.setFont(Phrases.inputFont);
        inputCategory.setPreferredSize(inputDimensionBig);
        inputCategory.setEditable(true);
        inputCategory.setSelectedItem(null);
        inputCategory.addActionListener(this);
        inputCategory.setWindow(this);
        focusElements.add(inputCategory);
        p2.add(inputCategory);
        inputLeft.add(p2);

        Panel p3 = new Panel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel controlsPurpose = new JLabel(Phrases.purpose);
        controlsPurpose.setPreferredSize(textDimensionBig);
        controlsPurpose.setFont(Phrases.inputFont);
        p3.add(controlsPurpose);
        inputPurpose = new CustomJComboBox<String>(money.getList_purpose().toArray(new String[0]));
        inputPurpose.setFont(Phrases.inputFont);
        inputPurpose.setPreferredSize(inputDimensionBig);
        inputPurpose.setEditable(true);
        inputPurpose.setSelectedItem(null);
        inputPurpose.addActionListener(this);
        inputPurpose.setWindow(this);
        focusElements.add(inputPurpose);
        p3.add(inputPurpose);
        inputLeft.add(p3);

        Panel inputRight = new Panel();
        inputRight.setLayout(new GridLayout(3, 1));
        input.add(inputRight);

        Panel p4 = new Panel();
        p4.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel controlsDate = new JLabel(Phrases.date);
        controlsDate.setPreferredSize(textDimensionSmall);
        controlsDate.setFont(Phrases.inputFont);
        p4.add(controlsDate);

        // TODO date ??? function but not sure if it is perfect ???
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

        Panel p5 = new Panel();
        p5.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel controlsValue = new JLabel(Phrases.value);
        controlsValue.setPreferredSize(textDimensionSmall);
        controlsValue.setFont(Phrases.inputFont);
        p5.add(controlsValue);
        inputValue = new JFormattedTextField(NumberFormat.getCurrencyInstance());
        inputValue.setFont(Phrases.inputFont);
        inputValue.setPreferredSize(inputDimensionSmall);
        inputValue.setValue(0.0);
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
        focusElements.get(pos+1).requestFocus();
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
        inputValue.setValue(0.0);
    }

    private void changeToSpending() {
        this.controlsReceiver_by.setText(Phrases.receiver);
        this.clearInput();
        this.revalidate();
        this.repaint();
    }

    private void changeToIncome() {
        this.controlsReceiver_by.setText(Phrases.by);
        this.clearInput();
        this.revalidate();
        this.repaint();
    }

    public void showEntry(Entry entry) {
        this.clearInput();
        this.changeEdit(false);
        if (entry.getOption().equals(Entry.options.INCOME)) {
            changeToIncome();
            this.inputValue.setValue(entry.getIncome());
        } else if (entry.getOption().equals(Entry.options.SPENDING)) {
            changeToSpending();
            this.inputValue.setValue(entry.getSpending());
        }
        this.inputReceiver_by.setSelectedItem(entry.getReceiverBy());
        this.inputCategory.setSelectedItem(entry.getCategory());
        this.inputPurpose.setSelectedItem(entry.getPurpose());
        System.out.println(setDateOnControl(entry.getLocalDate()));
        this.inputDate.setValue(setDateOnControl(entry.getLocalDate()));
    }

    private void changeEdit(boolean editable) {
        this.inputReceiver_by.setEnabled(editable);
        this.inputCategory.setEnabled(editable);
        this.inputPurpose.setEnabled(editable);
        this.inputDate.setEnabled(editable);
        this.inputValue.setEnabled(editable);
    }

    @Override
    public void actionPerformed(ActionEvent e) { // TODO
        this.revalidate();
        this.repaint();
        if (e.getSource() == spending) {
            System.out.println("spending");
            if (isInputEmpty()) {
                changeToSpending();
            }
            editing = false;
        } else if (e.getSource() == income) {
            System.out.println("income");
            if (isInputEmpty()) {
                changeToIncome();
            }
            editing = false;
        } else if (e.getSource() == neu) {
            System.out.println("neu");
            this.clearInput();
            this.changeEdit(true);
            editing = false;
        } else if (e.getSource() == edit) {
            // TODO edit
            System.out.println("edit");
            this.changeEdit(true);
            editing = true;
        } else if (e.getSource() == enter) {
            // TODO enter
            System.out.println("enter");
            money.enter();
            editing = false;
        } else if (e.getSource() == cancel) {
            System.out.println("cancel");
            this.clearInput();
            this.changeEdit(true);
            editing = false;
        } else if (e.getSource() == choiceDate) {
            // TODO choiceDate
            System.out.println("choice date");
        } else if (e.getSource() == calcValue) {
            // TODO calcValue
            System.out.println("calc value");
        }
    }

    public void edit(Entry entry) {
        money.edit(entry);
    }

    public String setDateOnTable(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(dateTimeFormatter);
    }

    public String setDateOnControl(LocalDate date) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : date.getDayOfMonth());
        stringBuilder.append(".");
        stringBuilder.append(date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue());
        stringBuilder.append(".");
        stringBuilder.append(date.getYear() < 10 ? "0" + date.getYear() : date.getYear());
        return stringBuilder.toString();
    }

    // GETTER && SETTER

    public int getContentElements() {
        return contentElements;
    }

    public void setContentElements(int contentElements) {
        this.contentElements = contentElements;
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
        return (double) inputValue.getValue();
    }

    public Panel getContent() {
        return content;
    }
}
