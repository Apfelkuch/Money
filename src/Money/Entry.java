package Money;

import java.time.LocalDate;

public class Entry {

    private int number;
    private LocalDate date;
    private String receiver;
    private String category;
    private String purpose;
    private float cost;
    private float gain;
    private float balance;

    public Entry(int number, LocalDate date, String receiver, String category, String purpose, float cost, float gain, float balance) {
        this.number = number;
        this.date = date;
        this.receiver = receiver;
        this.category = category;
        this.purpose = purpose;
        this.cost = cost;
        this.gain = gain;
        this.balance = balance;
    }

    // GETTER && SETTER

    public int getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getCategory() {
        return category;
    }

    public String getPurpose() {
        return purpose;
    }

    public float getCost() {
        return cost;
    }

    public float getGain() {
        return gain;
    }

    public float getBalance() {
        return balance;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
