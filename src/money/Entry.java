package money;

import phrases.Phrases;
import utilitis.CustomPopup;
import utilitis.Options;
import window.ExtraWindow;
import window.Window;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class Entry {

    /**
     * time in milliseconds that the mouse must remain still, to show a popup for the Entry description (receive, category, purpose)
     */
    private final long showTime = 1_000L;
    private final DecimalFormat numberFormat = new DecimalFormat("0.00");
    private final Money money;
    private LocalDate localDate;
    private String receiverBy;
    private String category;
    private String purpose;
    private double spending;
    private double income;
    private Options option;
    private int number;
    private double balance;
    private Timer showTimer = new Timer();
    private CustomPopup showDescriptionPopup;

    public Entry(Options option, int number, LocalDate localDate, String receiverBy, String category, String purpose, double spending, double income, double balance, Money money) {
        this.option = option;
        this.number = number;
        this.localDate = localDate;
        this.receiverBy = receiverBy;
        this.category = category;
        this.purpose = purpose;
        this.spending = formatNumber(spending);
        this.income = formatNumber(income);
        this.balance = formatNumber(balance);
        this.money = money;
    }

    public void updateBalance(double previousBalance) {
        this.balance = formatNumber(previousBalance + income - spending);
    }

    @Override
    public String toString() {
        return option.toString() + Phrases.DIVIDER +
                number + Phrases.DIVIDER +
                localDate.toString() + Phrases.DIVIDER +
                (receiverBy == null ? Phrases.PLACEHOLDER : receiverBy) + Phrases.DIVIDER +
                (category == null ? Phrases.PLACEHOLDER : category) + Phrases.DIVIDER +
                (purpose == null ? Phrases.PLACEHOLDER : purpose) + Phrases.DIVIDER +
                spending + Phrases.DIVIDER +
                income + Phrases.DIVIDER +
                balance;
    }

    public JPanel showEntry(int width, int height, Window window) {
        JPanel newEntry = new JPanel();
        newEntry.setSize(width, height);
        newEntry.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
        newEntry.setLayout(new GridLayout(1, 6));
        newEntry.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                cancelDescriptionPopup();
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    newEntry.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND.darker());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                cancelDescriptionPopup();
                if (e.getButton() == MouseEvent.BUTTON1 && (window.isInputEmpty() || window.isEntryShown())) {
                    newEntry.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
                    window.showEntry(Entry.this);
                    window.edit(Entry.this);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    int result = ExtraWindow.confirmDialog(window, Phrases.deleteEntry, Phrases.deleteEntryMessage + Entry.this.getNumber(), Phrases.inputFont, Phrases.BACKGROUND_LIGHT, Phrases.FOREGROUND, true);
                    if (result == ExtraWindow.EXIT_WITH_YES) {
                        money.updateAfterEntryDelete(Entry.this);
                        money.updateAllEntries();
                    }
                    newEntry.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
                }

            }
        });

        newEntry.add(buildLabel(String.valueOf(number), Phrases.normalFontColor));
        newEntry.add(buildLabel(this.setDateOnTable(localDate), Phrases.normalFontColor));

        //OPTION 1
        newEntry.add(buildTextArea((receiverBy == null ? "" : receiverBy) + "\n" + (category == null ? "" : category) + "\n" + (purpose == null ? "" : purpose), Phrases.normalFontColor, newEntry), 2);

        newEntry.add(buildLabel(option.equals(Options.SPENDING) ? spending + " " + Phrases.moneySymbol : "", Phrases.normalFontColor));
        newEntry.add(buildLabel(option.equals(Options.INCOME) ? income + " " + Phrases.moneySymbol : "", Phrases.normalFontColor));
        newEntry.add(buildLabel(balance + " " + Phrases.moneySymbol, balance < 0 ? Phrases.minusFontColor : Phrases.normalFontColor));

        return newEntry;
    }

    private JLabel buildLabel(String content, Color fontColor) {
        JLabel label = new JLabel(content);
        label.setForeground(fontColor);
        label.setFont(Phrases.showFontPlain);
        label.setBorder(new LineBorder(Color.BLACK, 1));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JTextArea buildTextArea(String content, Color fontColor, JPanel panel) {
        JTextArea textArea = new JTextArea(content);
        textArea.setForeground(fontColor);
        textArea.setDisabledTextColor(fontColor);
        textArea.setFont(Phrases.showFontPlain);
        textArea.setBorder(new LineBorder(Color.BLACK, 1));
        textArea.setAlignmentX(SwingConstants.LEFT);
        textArea.setLineWrap(false);
        textArea.setOpaque(false);
        textArea.setEnabled(false);
        textArea.addMouseListener(panel.getMouseListeners()[0]);

        textArea.addMouseWheelListener(e -> {
            cancelDescriptionPopup();
            textArea.getParent().dispatchEvent(e); // send the Event to the Listener of the Parent ==> scrolling of the entries is otherwise not possible
        });
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                cancelDescriptionPopup();
            }
        });
        textArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                cancelDescriptionPopup();
                showTimer = new Timer();
                showTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Dimension minSize = new Dimension(100, textArea.getHeight()); // (textArea.getFont().getSize() * 2)
                        int x = e.getXOnScreen() + Toolkit.getDefaultToolkit().getBestCursorSize(1, 1).width;
                        showDescriptionPopup = new CustomPopup(textArea, x, e.getYOnScreen(), textArea.getText(), 0L, minSize);
                    }
                }, showTime);
            }
        });
        return textArea;
    }

    private void cancelDescriptionPopup() {
        showTimer.cancel();
        if (showDescriptionPopup != null) {
            showDescriptionPopup.close();
        }
    }

    public String setDateOnTable(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(dateTimeFormatter);
    }

    public double formatNumber(double d) {
        return Double.parseDouble(numberFormat.format(d).replaceAll(",", "."));
    }

    // GETTER && SETTER

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getReceiverBy() {
        return receiverBy;
    }

    public void setReceiverBy(String receiverBy) {
        this.receiverBy = receiverBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Double getSpending() {
        return spending;
    }

    public void setSpending(double spending) {
        this.spending = spending;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public Options getOption() {
        return option;
    }

    public void setOption(Options option) {
        this.option = option;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

