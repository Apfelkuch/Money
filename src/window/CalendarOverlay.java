package window;

import utilitis.CustomArrowButton;
import utilitis.CustomJButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

public class CalendarOverlay extends Overlays {

    private JPanel contentPanel;

    private LocalDate localDate = LocalDate.now();
    private int offset;
    private int dayIndex;

    private JLabel month;
    private JLabel year;
    private CustomJButton[] days;

    public CalendarOverlay(Point location, window.Window moneyWindow) {
        super(location, moneyWindow);
        this.setSize(contentPanel.getSize());
        this.setLocation(location);

        updateDate();
        updateDay(localDate.getDayOfMonth());
    }

    @Override
    public void build(java.awt.Window window) {
        Dimension buttonDim = new Dimension(20, 20);

        // build GUI
        contentPanel = new JPanel();
        contentPanel.setSize(250, 200);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new LineBorder(Color.BLACK, 2));
        this.add(contentPanel);

        // top
        JPanel monthYear = new JPanel(new GridLayout(1, 2, 0, 0));
        contentPanel.add(monthYear, BorderLayout.NORTH);

        JPanel monthPanel = new JPanel();
        monthYear.add(monthPanel);
        CustomArrowButton monthDown = new CustomArrowButton(SwingConstants.WEST);
        monthDown.setPreferredSize(buttonDim);
        monthDown.addActionListener(e -> {
            localDate = localDate.minusMonths(1);
            updateDate();
        });
        monthPanel.add(monthDown);
        month = new JLabel();
        month.setHorizontalAlignment(SwingConstants.CENTER);
        month.setPreferredSize(new Dimension(75, buttonDim.height));
        monthPanel.add(month);
        CustomArrowButton monthUp = new CustomArrowButton(SwingConstants.EAST);
        monthUp.setPreferredSize(buttonDim);
        monthUp.addActionListener(e -> {
            localDate = localDate.plusMonths(1);
            updateDate();
        });
        monthPanel.add(monthUp);

        JPanel yearPanel = new JPanel();
        monthYear.add(yearPanel);
        CustomArrowButton yearDown = new CustomArrowButton(SwingConstants.WEST);
        yearDown.setPreferredSize(buttonDim);
        yearDown.addActionListener(e -> {
            localDate = localDate.minusYears(1);
            updateDate();
        });
        yearPanel.add(yearDown);
        year = new JLabel();
        year.setHorizontalAlignment(SwingConstants.CENTER);
        year.setPreferredSize(new Dimension(40, buttonDim.height));
        yearPanel.add(year);
        CustomArrowButton yearUp = new CustomArrowButton(SwingConstants.EAST);
        yearUp.setPreferredSize(buttonDim);
        yearUp.addActionListener(e -> {
            localDate = localDate.plusYears(1);
            updateDate();
        });
        yearPanel.add(yearUp);

        // center
        int rows = 7, cols = 7;
        JPanel center = new JPanel(new GridLayout(rows, cols));
        center.setPreferredSize(new Dimension(10, 10));
        center.setSize(new Dimension(10, 10));
        center.setMaximumSize(new Dimension(10, 10));
        center.setMinimumSize(new Dimension(10, 10));
        contentPanel.add(center, BorderLayout.CENTER);

        center.add(buildWeekDayLabel("Mo"));
        center.add(buildWeekDayLabel("Di"));
        center.add(buildWeekDayLabel("Mi"));
        center.add(buildWeekDayLabel("Do"));
        center.add(buildWeekDayLabel("Fr"));
        center.add(buildWeekDayLabel("Sa"));
        center.add(buildWeekDayLabel("So"));

        days = new CustomJButton[42];
        for (int i = 0; i < days.length; i++) {
            CustomJButton customJButton = new CustomJButton();
            customJButton.setPreferredSize(buttonDim);
            customJButton.addActionListener(e -> updateDay(Integer.parseInt(customJButton.getText())));
            days[i] = customJButton;
            center.add(customJButton);
        }

        // bottom
        JPanel bottom = new JPanel(new BorderLayout());
        contentPanel.add(bottom, BorderLayout.SOUTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        CustomJButton use = new CustomJButton("use");
        use.setPreferredSize(new Dimension(50, buttonDim.height));
        use.addActionListener(e -> use());
        buttons.add(use);
        CustomJButton exit = new CustomJButton("exit");
        exit.setPreferredSize(new Dimension(50, buttonDim.height));
        exit.addActionListener(e -> exit());
        buttons.add(exit);
        bottom.add(buttons, BorderLayout.EAST);

        CustomJButton today = new CustomJButton("today");
        today.setPreferredSize(new Dimension(50, buttonDim.height));
        today.addActionListener(e -> {
            localDate = LocalDate.now();
            updateDate();
        });

        bottom.add(new JPanel(new FlowLayout(FlowLayout.CENTER)).add(today).getParent(), BorderLayout.WEST);
    }

    private void use() {
        super.moneyWindow.setInputDate(localDate);
        exit();
    }

    private void exit() {
        this.dispose();
        super.moneyWindow.setChoseDate(null);
    }

    private JLabel buildWeekDayLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void updateDay(int day) {
        days[dayIndex].setBackground(days[0].getBackground());
        dayIndex = offset + day - 1;
        days[dayIndex].setBackground(days[0].getBackground().darker());

        localDate = localDate.withDayOfMonth(day);
    }

    private void updateDate() {
        int day = localDate.getDayOfMonth();
        offset = localDate.withDayOfMonth(1).getDayOfWeek().getValue() - 1;
        if (offset == 0) {
            offset = 7;
        }
        for (int i = 0; i < days.length; i++) {
            if (i < offset) {
                days[i].setText("" + (localDate.minusMonths(1).lengthOfMonth() - offset + i));
                days[i].setEnabled(false);
            } else if (i < (offset + localDate.lengthOfMonth())) {
                days[i].setText("" + (i - offset + 1));
                days[i].setEnabled(true);
            } else {
                days[i].setText("" + (i - (localDate.lengthOfMonth() + offset) + 1));
                days[i].setEnabled(false);
            }
        }

        updateDay(day);
        month.setText(localDate.getMonth().toString());
        year.setText("" + localDate.getYear());
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        updateDate();
        updateDay(localDate.getDayOfMonth());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            use();
        } else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            exit();
        }
    }

    @Override
    public void setLocation(Point p) {
        if (contentPanel == null) {
            return;
        }
        this.setLocation(
                p.x - contentPanel.getWidth() + Window.extraButton.width,
                p.y - contentPanel.getHeight() + Window.extraButton.height
        );
    }
}
