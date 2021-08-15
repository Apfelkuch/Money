package Money;

import Phrases.Phrases;
import Storage.Save;
import Storage.Load;
import utilitis.Options;
import window.Window;
import window.startingWindow;

import java.time.LocalDate;
import java.util.ArrayList;

public class Money {

    private final Window window;

    private final ArrayList<Entry> entries;
    private int currentEntry;
    private int topEntry = 0;

    // TODO manage the lists
    private final ArrayList<String> list_receiverBy;
    private final ArrayList<String> list_categories;
    private final ArrayList<String> list_purpose;

    public Money() {

        startingWindow startingWindow = new startingWindow();

        new Phrases();

        // TODO preload the lists
        list_receiverBy = new ArrayList<>();
        list_receiverBy.add("and");
        list_receiverBy.add("you");
        list_receiverBy.add("are");
        list_receiverBy.add("lucky");
        list_receiverBy.add("happy");
        list_categories = new ArrayList<>(list_receiverBy);
        list_purpose = new ArrayList<>(list_receiverBy);

        window = new Window("Money", this);

        // loading

        entries = new ArrayList<>();

        boolean loading = Load.load(this, Phrases.PATH, window.getMaxContentElements());
        if (loading) {
            System.out.println("Entries loaded");
        } else {
            System.out.println("No Entries loaded");
        }

//        // values for testing
//        entries.add(new Entry(Options.INCOME, 1, LocalDate.of(2021, java.util.Calendar.AUGUST, 10), "receiver1", "category1", "purpose1", 0f, 1f, 1f));
//        entries.add(new Entry(Options.INCOME, 2, LocalDate.of(2021, java.util.Calendar.AUGUST, 11), "receiver2", "category2", "purpose2", 0f, 2f, 3f));
//        entries.add(new Entry(Options.INCOME, 3, LocalDate.of(2021, 7, 12), "receiver3", "category3", "purpose3", 0f, 3f, 6f));
//        entries.add(new Entry(Options.SPENDING, 4, LocalDate.of(2021, 7, 13), "receiver4", "category4", "purpose4", 1f, 0f, 5f));
//        entries.add(new Entry(Options.SPENDING, 5, LocalDate.of(2021, 7, 14), "receiver5", "category5", "purpose5", 2f, 0f, 3f));
//        entries.add(new Entry(Options.INCOME, 6, LocalDate.of(2021, 7, 15), "receiver6", "category6", "purpose6", 0f, 1f, 4f));
//        entries.add(new Entry(Options.INCOME, 7, LocalDate.of(2021, 7, 16), "receiver7", "category7", "purpose7", 0f, 2f, 6f));
//        entries.add(new Entry(Options.INCOME, 8, LocalDate.of(2021, 7, 17), "receiver8", "category8", "purpose8", 0f, 3f, 9f));
//
//        topEntry = 1;
//
//        for (Entry e : entries) {
//            window.addContentToTable(e);
//        }

        startingWindow.dispose();
        window.setVisible(true);

    }

    public void enter() {
        Options option = window.isSpending() ? Options.SPENDING : Options.INCOME;
        int number = entries.size() + 1;
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
            if (entries.isEmpty()) {
                balance = -spending;
            } else {
                balance = entries.get(entries.size() - 1).getBalance() - spending;
            }
        } else {
            spending = 0.0;
            income = window.getInputValue();
            if (entries.isEmpty()) {
                balance = income;
            } else {
                balance = entries.get(entries.size() - 1).getBalance() + income;
            }
        }
        Entry entry = new Entry(option, number, date, receiverBy, category, purpose, spending, income, balance, this);
        entries.add(entry);
        if (entries.size() >= window.getMaxContentElements()) {
            topEntry += 1;
        }
        window.addContentToTable(entry);
    }

    public void edit(Entry entry) {
        currentEntry = entries.indexOf(entry);
//        System.out.println("Money.edit >> currentEntry: " + currentEntry);
    }

    public void confirmEdit() {
        // get entry which is edited
        Entry tempEntry = entries.get(currentEntry);

        // get the new date from the input
        LocalDate date = window.getInputLocalDate();
        String receiverBy = window.getInputReceiverBy();
        String category = window.getInputCategory();
        String purpose = window.getInputPurpose();
        double spending;
        double income;
        double balance;
        if (tempEntry.getOption() == Options.SPENDING) {
            spending = window.getInputValue();
            income = 0.0;
            if (currentEntry == 0) {
                balance = -spending;
            } else {
                balance = entries.get(currentEntry - 1).getBalance() - spending;
            }
        } else {
            spending = 0.0;
            income = window.getInputValue();
            if (currentEntry == 0) {
                balance = income;
            } else {
                balance = entries.get(entries.size() - 1).getBalance() + income;
            }
        }

        // set a new entry at the place of the edited entry
        entries.set(currentEntry, new Entry(tempEntry.getOption(), tempEntry.getNumber(), date, receiverBy, category, purpose, spending, income, balance, this));

        // update all following entries.
        for (int i = currentEntry; i < entries.size(); i++) {
            entries.get(i).updateBalance(currentEntry == 0 ? 0 : entries.get(i - 1).getBalance());
        }

        // update the showing
        updateAllEntries();

    }

    public boolean save() {
        return Save.save(this, Phrases.PATH);
    }

    public void moveTopEntry(int amount) {
        int oldTopEntry = topEntry;
        topEntry += amount; // move the topEntry with the amount

        // adjust topEntry
        if (topEntry < 0) {
            topEntry = 0;
        }
        if (topEntry > entries.size() - window.getMaxContentElements()) {
            topEntry = entries.size() - window.getMaxContentElements();
        }

        if (oldTopEntry == topEntry) return;

        // update the GUI
        updateAllEntries();
    }

    public void updateAllEntries() {
        window.clearEntries();
        if (topEntry + window.getMaxContentElements() > entries.size()) {
            topEntry = entries.size() - window.getMaxContentElements();
            if (topEntry < 0) topEntry = 0;
        }
        for (int i = 0; i < window.getMaxContentElements(); i++) {
            if (topEntry + i >= entries.size()) {
                return;
            }
            window.addContentToTable(entries.get(topEntry + i));
        }
    }

    public void updateNumbers(Entry entry) {
        int current = entries.indexOf(entry);
        for (int i = current; i < entries.size(); i++) {
            entries.get(i).updateNumber(entries.get(i).getNumber() - 1);
        }
    }

    // GETTER && SETTER

    public ArrayList<Entry> getEntries() {
        return entries;
    }

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

