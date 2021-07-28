package Money;

import utilitis.Options;

import java.time.LocalDate;

public class Entry {

    private int number;
    private LocalDate date;
    private String receiverBy;
    private String category;
    private String purpose;
    private double spending;
    private double income;
    private double balance;
    private Options option;

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

    public void update(double previousBalance) {
        this.balance = previousBalance + income - spending;
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

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLocalDate(LocalDate date) {
        this.date = date;
    }

    public void setReceiverBy(String receiverBy) {
        this.receiverBy = receiverBy;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setSpending(float spending) {
        this.spending = spending;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setOption(Options option) {
        this.option = option;
    }
}

