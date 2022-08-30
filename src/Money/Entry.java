package Money;

import Input.MouseAdapterEntry;
import Phrases.Phrases;
import utilitis.CustomPopup;
import utilitis.Options;
import window.Window;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Entry {

    private final LocalDate localDate;
    private final String receiverBy;
    private final String category;
    private final String purpose;
    private final double spending;
    private final double income;
    private final Options option;
    private final Money money;
    private int number;
    private double balance;

    public Entry(Options option, int number, LocalDate localDate, String receiverBy, String category, String purpose, double spending, double income, double balance, Money money) {
        this.option = option;
        this.number = number;
        this.localDate = localDate;
        this.receiverBy = receiverBy;
        this.category = category;
        this.purpose = purpose;
        this.spending = spending;
        this.income = income;
        this.balance = balance;
        this.money = money;
    }

    public void updateBalance(double previousBalance) {
        this.balance = previousBalance + income - spending;
    }

    public void updateNumber(int number) {
        this.number = number;
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
        newEntry.addMouseListener(new MouseAdapterEntry(window, newEntry, this, money));

        newEntry.add(buildLabel("" + number, Phrases.normalFontColor));
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
        textArea.addMouseListener(new MouseAdapter() {
            // TODO: Apfel 30.08.2022 Idea: adjust the position to the movement of the mouse??
            CustomPopup customPopup = new CustomPopup().setOpaque(false);

            @Override
            public void mouseEntered(MouseEvent e) {
                customPopup = customPopup.buildPopup(e.getXOnScreen(), e.getYOnScreen(), textArea.getText());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (customPopup != null) {
                    customPopup.close();
                }
            }
        });
        return textArea;
    }

    public String setDateOnTable(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(dateTimeFormatter);
    }

    // GETTER && SETTER

    public int getNumber() {
        return number;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public String getReceiverBy() {
        return receiverBy;
    }

    public String getCategory() {
        return category;
    }

    public String getPurpose() {
        return purpose;
    }

    public Double getSpending() {
        return spending;
    }

    public Double getIncome() {
        return income;
    }

    public Options getOption() {
        return option;
    }

    public double getBalance() {
        return balance;
    }

}

