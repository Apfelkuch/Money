package Money;

import Input.MouseAdapterEntry;
import Phrases.Phrases;
import utilitis.Options;
import window.Window;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Entry {

    private int number;
    private final LocalDate localDate;
    private final String receiverBy;
    private final String category;
    private final String purpose;
    private final double spending;
    private final double income;
    private double balance;
    private final Options option;
    private final Money money;

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
        return option.toString() + Character.toString(160) +
                number + Character.toString(160) +
                localDate.toString() + Character.toString(160) +
                receiverBy + Character.toString(160) +
                category + Character.toString(160) +
                purpose + Character.toString(160) +
                spending + Character.toString(160) +
                income + Character.toString(160) +
                balance;
    }

    public JPanel showEntry(int width, int height, Window window) {
        JPanel newEntry = new JPanel();
        newEntry.setSize(width, height);
        newEntry.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
        newEntry.setLayout(new GridLayout(1, 6));
        newEntry.add(buildLabel(JLabel.CENTER, "" + number, Phrases.normalFontColor));
        newEntry.add(buildLabel(JLabel.CENTER, this.setDateOnTable(localDate), Phrases.normalFontColor));

        //OPTION 1
        newEntry.add(buildLabel(JLabel.LEFT, "<html>"
                + (receiverBy == null ? "" : receiverBy) + "<br>"
                + (category == null ? "" : category) + "<br>"
                + (purpose == null ? "" : purpose)
                + "</html>", Phrases.normalFontColor), 2);

        newEntry.add(buildLabel(JLabel.CENTER, option.equals(Options.SPENDING) ? "" : spending + " €", Phrases.normalFontColor));
        newEntry.add(buildLabel(JLabel.CENTER, option.equals(Options.INCOME) ? "" : income + " €", Phrases.normalFontColor));
        newEntry.add(buildLabel(JLabel.CENTER, balance + " €", balance < 0 ? Phrases.minusFontColor : Phrases.normalFontColor));
        newEntry.addMouseListener(new MouseAdapterEntry(window, newEntry, this, money));

        return newEntry;
    }

    private JLabel buildLabel(int alignment, String content, Color fontColor) {
        JLabel label = new JLabel(content);
        label.setForeground(fontColor);
        label.setFont(Phrases.showFontPlain);
        label.setBorder(new LineBorder(Color.BLACK, 1));
        label.setHorizontalAlignment(alignment);
        return label;
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

