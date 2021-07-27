package Money;

import storage.Load;
import utilitis.Options;
import window.Window;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class Money {

    private final Window window;

    private ArrayList<Entry> entries;
    private int currentEntry;

    private ArrayList<String> list_receiverBy;
    private ArrayList<String> list_categories;
    private ArrayList<String> list_purpose;

    public Money() {

        entries = new ArrayList<>();
        entries = Load.loadEntries();


        // TODO preload the lists
        list_receiverBy = new ArrayList<>();
        list_receiverBy.add("and");
        list_receiverBy.add("you");
        list_receiverBy.add("are");
        list_receiverBy.add("lucky");
        list_receiverBy.add("brot von MEffert");
        list_categories = new ArrayList<>(list_receiverBy);
        list_purpose = new ArrayList<>(list_receiverBy);

        window = new Window("Money", this);

        // TODO for testing
        entries.add(new Entry(Options.INCOME, 1, LocalDate.of(2021, Calendar.AUGUST, 10), "receiver1", "category1", "purpose1", 0f, 1f, 1f));
        entries.add(new Entry(Options.INCOME, 2, LocalDate.of(2021, Calendar.AUGUST, 11), "receiver2", "category2", "purpose2", 0f, 2f, 3f));
        entries.add(new Entry(Options.INCOME, 3, LocalDate.of(2021, 7, 12), "receiver3", "category3", "purpose3", 0f, 3f, 6f));
        entries.add(new Entry(Options.SPENDING, 4, LocalDate.of(2021, 7, 13), "receiver4", "category4", "purpose4", 1f, 0f, 5f));
        entries.add(new Entry(Options.SPENDING, 5, LocalDate.of(2021, 7, 14), "receiver5", "category5", "purpose5", 2f, 0f, 3f));
        entries.add(new Entry(Options.INCOME, 6, LocalDate.of(2021, 7, 15), "receiver6", "category6", "purpose6", 0f, 1f, 4f));
        entries.add(new Entry(Options.INCOME, 7, LocalDate.of(2021, 7, 16), "receiver7", "category7", "purpose7", 0f, 2f, 6f));
        entries.add(new Entry(Options.INCOME, 8, LocalDate.of(2021, 7, 17), "receiver8", "category8", "purpose8", 0f, 3f, 9f));

        for (Entry e : entries) {
            window.addContentToTable(e);
        }
    }

    public void neu() {
        // TODO neu methode
    }

    public void enter() {
        Options option = window.isSpending() ? Options.SPENDING : Options.INCOME;
        int number = entries.size();
        LocalDate date = window.getInputLocalDate();
        String receiverBy = window.getInputReceiverBy();
        String category = window.getInputCategory();
        String purpose = window.getInputPurpose();
        double spending;
        double income;
        double balance;
        if (option == Options.SPENDING) {
            spending = window.getInputValue();
            income = 0.0;
            balance = entries.get(entries.size()-1).getBalance() - spending;
        } else {
            spending = 0.0;
            income = window.getInputValue();
            balance = entries.get(entries.size()-1).getBalance() + income;
        }
        Entry entry = new Entry(option, number, date, receiverBy, category, purpose, spending, income, balance);
        entries.add(entry);
        window.addContentToTable(entry);
    }

    public void edit(Entry entry) {
        // TODO edit methode
    }

    public void save() {
        // TODO save the program
    }

    // GETTER && SETTER


    public ArrayList<String> getList_receiverBy() {
        return list_receiverBy;
    }

    public ArrayList<String> getList_categories() {
        return list_categories;
    }

    public ArrayList<String> getList_purpose() {
        return list_purpose;
    }

}

