package window;

import Money.Entry;
import Phrases.Phrases;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.time.LocalDate;

public class Window extends JFrame {

    // table
    private Panel table;
    private Panel tableShow;

    private JLabel tableNumber;
    private JLabel tableDate;
    private JLabel tableReceiverCategoryPurpose;
    private JLabel tableCost;
    private JLabel tableGain;
    private JLabel tableBalance;

    private Panel content;
    private int contentElements = 7;

    // dimensions
    private Dimension tableDimension;

    // controls
    private Panel controls;

    private Panel controlButtons;
    private Button cost;
    private Button gain;

    private Panel controlButtonsI;
    private Button neu;
    private Button edit;
    private Button enter;
    private Button cancel;

    private Panel input;
    private Panel inputLeft;
    private JLabel controlsReceiver_by;
    private TextField inputReceiver_by;
    private JLabel ControlsCategory;
    private TextField inputCategory;
    private JLabel controlsPurpose;
    private TextField inputPurpose;
    private Panel inputRight;
    private JLabel controlsDate;
    private TextField inputDate;
    private Button choiceDate;
    private JLabel controlsValue;
    private TextField inputValue;
    private Button calcValue;

    private int bufferPageEnd = 50;

    public Window(String title) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(title);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setSize(1300, 1100);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        this.init();
        this.addTable();
        this.addControls();

        this.revalidate();
        this.repaint();

        // TODO for testing
        this.addContentToTable(new Entry(0, LocalDate.now(), "receiver1", "category1", "purpose1", 1f, 0f, 1f));
        this.addContentToTable(new Entry(0, LocalDate.now(), "receiver2", "category2", "purpose2", 2f, 0f, 3f));
        this.addContentToTable(new Entry(0, LocalDate.now(), "receiver3", "category3", "purpose3", 3f, 0f, 6f));
        this.addContentToTable(new Entry(0, LocalDate.now(), "receiver4", "category4", "purpose4", 0f, 1f, 5f));
        this.addContentToTable(new Entry(0, LocalDate.now(), "receiver5", "category5", "purpose5", 0f, 2f, 3f));
        this.addContentToTable(new Entry(0, LocalDate.now(), "receiver6", "category6", "purpose6", 1f, 0f, 4f));
        this.addContentToTable(new Entry(0, LocalDate.now(), "receiver7", "category7", "purpose7", 2f, 0f, 6f));
    }

    public void init() {
        new Phrases();
    }

    public void addTable() {
        tableDimension = new Dimension((int) (this.getWidth() * (2f / 3f)), 100);

        tableShow = new Panel();
        tableShow.setLayout(new FlowLayout(FlowLayout.CENTER));
        tableShow.setBackground(Phrases.COLOR_TABLE_BACKGROUND);
        tableShow.setPreferredSize(new Dimension(tableDimension.width, tableDimension.height * (1 + contentElements)));
        this.add(tableShow, BorderLayout.CENTER);

        table = new Panel();
        table.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
        table.setPreferredSize(new Dimension(tableDimension.width, tableDimension.height * (1 + contentElements)));
        tableShow.add(table);

        // table head row
        Panel headRow = new Panel();
        headRow.setLayout(new GridLayout(1, 6));
        headRow.setPreferredSize(tableDimension);
        headRow.setBackground(Phrases.COLOR_HEAD_ROW);
        table.add(headRow);

        tableNumber = new JLabel(Phrases.number);
        tableNumber.setFont(Phrases.showFontBold);
        tableNumber.setBorder(new LineBorder(Color.BLACK, 2));
        tableNumber.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableNumber);
        tableDate = new JLabel(Phrases.date);
        tableDate.setFont(Phrases.showFontBold);
        tableDate.setBorder(new LineBorder(Color.BLACK, 2));
        tableDate.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableDate);
        tableReceiverCategoryPurpose = new JLabel("<html>" + Phrases.receiver + "<br>" + Phrases.category + "<br>" + Phrases.purpose + "</html>");
        tableReceiverCategoryPurpose.setFont(Phrases.showFontBold);
        tableReceiverCategoryPurpose.setBorder(new LineBorder(Color.BLACK, 2));
        tableReceiverCategoryPurpose.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableReceiverCategoryPurpose);
        tableCost = new JLabel(Phrases.cost);
        tableCost.setFont(Phrases.showFontBold);
        tableCost.setBorder(new LineBorder(Color.BLACK, 2));
        tableCost.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableCost);
        tableGain = new JLabel(Phrases.gain);
        tableGain.setFont(Phrases.showFontBold);
        tableGain.setBorder(new LineBorder(Color.BLACK, 2));
        tableGain.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableGain);
        tableBalance = new JLabel(Phrases.balance);
        tableBalance.setFont(Phrases.showFontBold);
        tableBalance.setBorder(new LineBorder(Color.BLACK, 2));
        tableBalance.setHorizontalAlignment(JLabel.CENTER);
        headRow.add(tableBalance);

        // table content
        content = new Panel();
        content.setLayout(new GridLayout(contentElements, 1));
        content.setPreferredSize(new Dimension(tableDimension.width, (tableDimension.height-2) * contentElements));
        table.add(content);
    }

    public void addContentToTable(Entry entry) {
        Panel newEntry = new Panel();
        newEntry.setLayout(new GridLayout(1, 6));
        newEntry.add(buildLabel(JLabel.CENTER,"" + entry.getNumber()));
        newEntry.add(buildLabel(JLabel.CENTER,entry.getDate().getDayOfMonth() + "." + entry.getDate().getMonthValue() + "." + entry.getDate().getYear()));
        newEntry.add(buildLabel(JLabel.LEFT,"<html>" + entry.getReceiver() + "<br>" + entry.getCategory() + "<br>" + entry.getPurpose() + "</html>"));
        newEntry.add(buildLabel(JLabel.CENTER,entry.getCost() == 0.0 ? "" : entry.getCost() + " €"));
        newEntry.add(buildLabel(JLabel.CENTER,entry.getGain() == 0.0 ? "" : entry.getGain() + " €"));
        newEntry.add(buildLabel(JLabel.CENTER,entry.getBalance() + "€"));
        content.add(newEntry);

        this.revalidate();
        this.repaint();
    }

    private JLabel buildLabel(int alignment, String content) {
        JLabel label = new JLabel(content);
        label.setFont(Phrases.showFontPlain);
        label.setBorder(new LineBorder(Color.BLACK, 1));
        label.setHorizontalAlignment(alignment);
        return label;
    }

    private void addControls() {
        Dimension buttonsDimension = new Dimension(120, 20);

        controls = new Panel();
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        controls.setPreferredSize(new Dimension((int) (this.getWidth() * (2f / 3f)), ((buttonsDimension.height + 10) * 5) + bufferPageEnd));
        controls.setBackground(Phrases.COLOR_CONTROL_BACKGROUND);
        this.add(controls, BorderLayout.PAGE_END);

        // control Buttons
        controlButtons = new Panel();
        controlButtons.setBackground(Phrases.COLOR_CONTROLS);
        controlButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlButtons.setPreferredSize(new Dimension(controls.getPreferredSize().width, buttonsDimension.height + 10));
        controls.add(controlButtons);

        cost = new Button(Phrases.cost);
        cost.setFont(Phrases.inputFont);
        cost.setPreferredSize(buttonsDimension);
        controlButtons.add(cost);

        gain = new Button(Phrases.gain);
        gain.setFont(Phrases.inputFont);
        gain.setPreferredSize(buttonsDimension);
        controlButtons.add(gain);

        // control Buttons I
        controlButtonsI = new Panel();
        controlButtonsI.setBackground(Phrases.COLOR_CONTROLS_INPUT);
        controlButtonsI.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlButtonsI.setPreferredSize(controlButtons.getPreferredSize());
        controls.add(controlButtonsI);

        neu = new Button(Phrases.neu);
        neu.setFont(Phrases.inputFont);
        neu.setPreferredSize(buttonsDimension);
        controlButtonsI.add(neu);

        edit = new Button(Phrases.edit);
        edit.setFont(Phrases.inputFont);
        edit.setPreferredSize(buttonsDimension);
        controlButtonsI.add(edit);

        enter = new Button(Phrases.enter);
        enter.setFont(Phrases.inputFont);
        enter.setPreferredSize(buttonsDimension);
        controlButtonsI.add(enter);

        cancel = new Button(Phrases.cancel);
        cancel.setFont(Phrases.inputFont);
        cancel.setPreferredSize(buttonsDimension);
        controlButtonsI.add(cancel);

        // input
        Dimension inputDimensionBig = new Dimension(400, 20);
        Dimension inputDimensionSmall = new Dimension(100, 20);
        Dimension textDimensionBig = new Dimension(100, 20);
        Dimension textDimensionSmall = new Dimension(50, 20);
        Dimension extraButton = new Dimension(20, 20);

        input = new Panel();
        input.setLayout(new GridLayout(1, 2));
        input.setPreferredSize(new Dimension(controls.getPreferredSize().width, (buttonsDimension.height + 10) * 3));
        input.setBackground(controlButtonsI.getBackground());
        controls.add(input);

        inputLeft = new Panel();
        inputLeft.setLayout(new GridLayout(3, 1));
        input.add(inputLeft);

        Panel p1 = new Panel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlsReceiver_by = new JLabel(Phrases.receiver);
        controlsReceiver_by.setPreferredSize(textDimensionBig);
        controlsReceiver_by.setFont(Phrases.inputFont);
        p1.add(controlsReceiver_by);
        inputReceiver_by = new TextField();
        inputReceiver_by.setFont(Phrases.inputFont);
        inputReceiver_by.setPreferredSize(inputDimensionBig);
        p1.add(inputReceiver_by);
        inputLeft.add(p1);

        Panel p2 = new Panel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        ControlsCategory = new JLabel(Phrases.category);
        ControlsCategory.setPreferredSize(textDimensionBig);
        ControlsCategory.setFont(Phrases.inputFont);
        p2.add(ControlsCategory);
        inputCategory = new TextField();
        inputCategory.setFont(Phrases.inputFont);
        inputCategory.setPreferredSize(inputDimensionBig);
        p2.add(inputCategory);
        inputLeft.add(p2);

        Panel p3 = new Panel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlsPurpose = new JLabel(Phrases.purpose);
        controlsPurpose.setPreferredSize(textDimensionBig);
        controlsPurpose.setFont(Phrases.inputFont);
        p3.add(controlsPurpose);
        inputPurpose = new TextField();
        inputPurpose.setFont(Phrases.inputFont);
        inputPurpose.setPreferredSize(inputDimensionBig);
        p3.add(inputPurpose);
        inputLeft.add(p3);

        inputRight = new Panel();
        inputRight.setLayout(new GridLayout(3, 1));
        input.add(inputRight);

        Panel p4 = new Panel();
        p4.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlsDate = new JLabel(Phrases.date);
        controlsDate.setPreferredSize(textDimensionSmall);
        controlsDate.setFont(Phrases.inputFont);
        p4.add(controlsDate);
        inputDate = new TextField();
        inputDate.setFont(Phrases.inputFont);
        inputDate.setPreferredSize(inputDimensionSmall);
        p4.add(inputDate);
        choiceDate = new Button();
        choiceDate.setPreferredSize(extraButton);
        p4.add(choiceDate);
        inputRight.add(p4);

        Panel p5 = new Panel();
        p5.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlsValue = new JLabel(Phrases.value);
        controlsValue.setPreferredSize(textDimensionSmall);
        controlsValue.setFont(Phrases.inputFont);
        p5.add(controlsValue);
        inputValue = new TextField();
        inputValue.setFont(Phrases.inputFont);
        inputValue.setPreferredSize(inputDimensionSmall);
        p5.add(inputValue);
        calcValue = new Button();
        calcValue.setPreferredSize(extraButton);
        p5.add(calcValue);
        inputRight.add(p5);

    }
}