package window;

import Phrases.Phrases;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    // table
    private Panel table;

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
    private JLabel receiver_by;
    private TextField inputReceiver_by;
    private JLabel category;
    private TextField inputCategory;
    private JLabel purpose;
    private TextField inputPurpose;
    private Panel inputRight;
    private JLabel date;
    private TextField inputDate;
    private Button choiceDate;
    private JLabel value;
    private TextField inputValue;
    private Button calcValue;

    private int bufferPageEnd = 50;

    public Window(String title) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(title);
        this.setSize(500, 500);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        this.init();
        this.addTable();
        this.addControls();

        this.revalidate();
        this.repaint();
    }

    public void init() {
        new Phrases();
    }

    public void addTable() {
        table = new Panel();
        table.setBackground(Color.CYAN);
        this.add(table, BorderLayout.CENTER);
    }

    private void addControls() {
        Dimension buttonsDimension = new Dimension(120, 20);

        controls = new Panel();
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        controls.setPreferredSize(new Dimension((int) (this.getWidth() * (2f / 3f)), ((buttonsDimension.height + 10) * 5) + bufferPageEnd));
        this.add(controls, BorderLayout.PAGE_END);

        // control Buttons
        controlButtons = new Panel();
        controlButtons.setBackground(Color.GRAY);
        controlButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlButtons.setPreferredSize(new Dimension(controls.getPreferredSize().width, buttonsDimension.height + 10));
        controls.add(controlButtons);

        cost = new Button(Phrases.cost);
        cost.setFont(Phrases.font);
        cost.setPreferredSize(buttonsDimension);
        controlButtons.add(cost);

        gain = new Button(Phrases.gain);
        gain.setFont(Phrases.font);
        gain.setPreferredSize(buttonsDimension);
        controlButtons.add(gain);

        // control Buttons I
        controlButtonsI = new Panel();
        controlButtonsI.setBackground(Color.LIGHT_GRAY);
        controlButtonsI.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlButtonsI.setPreferredSize(controlButtons.getPreferredSize());
        controls.add(controlButtonsI);

        neu = new Button(Phrases.neu);
        neu.setFont(Phrases.font);
        neu.setPreferredSize(buttonsDimension);
        controlButtonsI.add(neu);

        edit = new Button(Phrases.edit);
        edit.setFont(Phrases.font);
        edit.setPreferredSize(buttonsDimension);
        controlButtonsI.add(edit);

        enter = new Button(Phrases.enter);
        enter.setFont(Phrases.font);
        enter.setPreferredSize(buttonsDimension);
        controlButtonsI.add(enter);

        cancel = new Button(Phrases.cancel);
        cancel.setFont(Phrases.font);
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
        receiver_by = new JLabel(Phrases.receiver);
        receiver_by.setPreferredSize(textDimensionBig);
        receiver_by.setFont(Phrases.font);
        p1.add(receiver_by);
        inputReceiver_by = new TextField();
        inputReceiver_by.setFont(Phrases.font);
        inputReceiver_by.setPreferredSize(inputDimensionBig);
        p1.add(inputReceiver_by);
        inputLeft.add(p1);

        Panel p2 = new Panel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        category = new JLabel(Phrases.category);
        category.setPreferredSize(textDimensionBig);
        category.setFont(Phrases.font);
        p2.add(category);
        inputCategory = new TextField();
        inputCategory.setFont(Phrases.font);
        inputCategory.setPreferredSize(inputDimensionBig);
        p2.add(inputCategory);
        inputLeft.add(p2);

        Panel p3 = new Panel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        purpose = new JLabel(Phrases.purpose);
        purpose.setPreferredSize(textDimensionBig);
        purpose.setFont(Phrases.font);
        p3.add(purpose);
        inputPurpose = new TextField();
        inputPurpose.setFont(Phrases.font);
        inputPurpose.setPreferredSize(inputDimensionBig);
        p3.add(inputPurpose);
        inputLeft.add(p3);

        inputRight = new Panel();
        inputRight.setLayout(new GridLayout(3, 1));
        input.add(inputRight);

        Panel p4 = new Panel();
        p4.setLayout(new FlowLayout(FlowLayout.LEFT));
        date = new JLabel(Phrases.date);
        date.setPreferredSize(textDimensionSmall);
        date.setFont(Phrases.font);
        p4.add(date);
        inputDate = new TextField();
        inputDate.setFont(Phrases.font);
        inputDate.setPreferredSize(inputDimensionSmall);
        p4.add(inputDate);
        choiceDate = new Button();
        choiceDate.setPreferredSize(extraButton);
        p4.add(choiceDate);
        inputRight.add(p4);

        Panel p5 = new Panel();
        p5.setLayout(new FlowLayout(FlowLayout.LEFT));
        value = new JLabel(Phrases.value);
        value.setPreferredSize(textDimensionSmall);
        value.setFont(Phrases.font);
        p5.add(value);
        inputValue = new TextField();
        inputValue.setFont(Phrases.font);
        inputValue.setPreferredSize(inputDimensionSmall);
        p5.add(inputValue);
        calcValue = new Button();
        calcValue.setPreferredSize(extraButton);
        p5.add(calcValue);
        inputRight.add(p5);

    }
}