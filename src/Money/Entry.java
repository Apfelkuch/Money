package Money;

import utilitis.Options;

import java.time.LocalDate;

public class Entry {

    private int number;
    private final LocalDate date;
    private final String receiverBy;
    private final String category;
    private final String purpose;
    private final double spending;
    private final double income;
    private double balance;
    private final Options option;

    public Entry(Options option, int number, LocalDate date, String receiverBy, String category, String purpose, double spending, double income, double balance) {
        this.option = option;
        this.number = number;
        this.date = date;
        this.receiverBy = receiverBy;
        this.category = category;
        this.purpose = purpose;
        this.spending = spending;
        this.income = income;
        this.balance = balance;
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
                date.toString() + Character.toString(160) +
                receiverBy + Character.toString(160) +
                category + Character.toString(160) +
                purpose + Character.toString(160) +
                spending + Character.toString(160) +
                income + Character.toString(160) +
//                balance + Character.toString(160); /* if the line causes trouble use the line beneath*/
                balance;
    }

    // GETTER && SETTER

    public int getNumber() {
        return number;
    }

    public LocalDate getLocalDate() {
        return date;
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

