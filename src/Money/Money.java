package Money;

import Phrases.Phrases;
import Storage.Save;
import Storage.Load;
import utilitis.Options;
import utilitis.StringInteger;
import window.Window;
import window.startingWindow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Money {

    private final Window window;

    private final ArrayList<Entry> entries;
    private int currentEntry;
    private int topEntry = 0;

    private final ArrayList<String> list_receiverBy;
    private final ArrayList<StringInteger> pre_list_receiverBy;
    private final ArrayList<String> list_categories;
    private final ArrayList<StringInteger> pre_list_categories;
    private final ArrayList<String> list_purpose;
    private final ArrayList<StringInteger> pre_list_purpose;

    public Money() {

        startingWindow startingWindow = new startingWindow();

        new Phrases();

        window = new Window("Money", this);

        pre_list_receiverBy = new ArrayList<>();
        pre_list_categories = new ArrayList<>();
        pre_list_purpose = new ArrayList<>();

        // loading

        entries = new ArrayList<>();
        list_receiverBy = new ArrayList<>();
        list_categories = new ArrayList<>();
        list_purpose = new ArrayList<>();

        boolean loading = Load.load(this, Phrases.PATH, window.getMaxContentElements());
        if (loading) {
            System.out.println("Entries loaded");
        } else {
            System.out.println("No Entries loaded");
        }

//        // testing entries
//        entries.add(new Entry(Options.INCOME, 1, LocalDate.of(2021, 7, 10), "receiver1", "category1", "purpose1", 0f, 1f, 1f, this));
//        entries.add(new Entry(Options.INCOME, 2, LocalDate.of(2021, 7, 11), "receiver1", "category2", "purpose2", 0f, 2f, 3f, this));
//        entries.add(new Entry(Options.INCOME, 3, LocalDate.of(2021, 7, 12), "receiver1", "category3", "purpose3", 0f, 3f, 6f, this));
//        entries.add(new Entry(Options.SPENDING, 4, LocalDate.of(2021, 7, 13), "receiver1", "category4", "purpose4", 1f, 0f, 5f, this));
//        entries.add(new Entry(Options.SPENDING, 5, LocalDate.of(2021, 7, 14), "receiver1", "category5", "purpose5", 2f, 0f, 3f, this));
//        entries.add(new Entry(Options.INCOME, 6, LocalDate.of(2021, 7, 15), "receiver1", "category6", "purpose6", 0f, 1f, 4f, this));
//        entries.add(new Entry(Options.INCOME, 7, LocalDate.of(2021, 7, 16), "receiver1", "category7", "purpose7", 0f, 2f, 6f, this));
//        entries.add(new Entry(Options.INCOME, 8, LocalDate.of(2021, 7, 17), "receiver1", "category8", "purpose8", 0f, 3f, 9f, this));
//        entries.add(new Entry(Options.INCOME, 9, LocalDate.of(2021, 7, 17), "receiver1", "category9", "purpose9", 0f, 0f, 9f, this));
//
//        topEntry = 1;
//
//        for (Entry e : entries) {
//            window.addContentToTable(e);
//        }

//        // testing list
//        list_receiverBy = new ArrayList<>();
//        list_receiverBy.add("and");
//        list_receiverBy.add("you");
//        list_receiverBy.add("are");
//        list_receiverBy.add("lucky");
//        list_receiverBy.add("happy");
//        window.setInputReceiver_by(list_receiverBy.toArray(new String[0]));
//        list_categories = new ArrayList<>(list_receiverBy);
//        window.setInputCategory(list_categories.toArray(new String[0]));
//        list_purpose = new ArrayList<>(list_receiverBy);
//        window.setInputPurpose(list_purpose.toArray(new String[0]));
//
//        // fill the lists with all elements which are more then Phrases.LIST_JUMP_VALUE times used.
//        for (int i = 0; i < pre_list_receiverBy.size(); i++) {
//            if (pre_list_receiverBy.get(i).getInteger() >= Phrases.LIST_JUMP_VALUE) {
//                list_receiverBy.add(pre_list_receiverBy.get(i).getString());
//            }
//        }
//        window.setInputReceiver_by(list_receiverBy.toArray(new String[0]));
//        for (int i = 0; i < pre_list_categories.size(); i++) {
//            if (pre_list_categories.get(i).getInteger() >= Phrases.LIST_JUMP_VALUE) {
//                list_categories.add(pre_list_categories.get(i).getString());
//            }
//        }
//        window.setInputCategory(list_categories.toArray(new String[0]));
//        for (int i = 0; i < pre_list_purpose.size(); i++) {
//            if (pre_list_purpose.get(i).getInteger() >= Phrases.LIST_JUMP_VALUE) {
//                list_purpose.add(pre_list_purpose.get(i).getString());
//            }
//        }
//        window.setInputPurpose(list_purpose.toArray(new String[0]));


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

        // add to the comboBoxLists
        this.addToPreListReceiverBy(receiverBy);
        if (!(receiverBy == null || receiverBy.isBlank())) {
            if (!list_receiverBy.contains(receiverBy)) {
                list_receiverBy.add(receiverBy);
                window.setInputReceiver_by(list_receiverBy.toArray(new String[0]));
            }
        }
        this.addToPreListCategories(category);
        if (!(category == null || category.isBlank())) {
            if (!list_categories.contains(category)) {
                list_categories.add(category);
                window.setInputCategory(list_categories.toArray(new String[0]));
            }
        }
        this.addToPreListPurpose(purpose);
        if (!(purpose == null || purpose.isBlank())) {
            if (!list_purpose.contains(purpose)) {
                list_purpose.add(purpose);
                window.setInputPurpose(list_purpose.toArray(new String[0]));
            }
        }
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

    public void addToPreListReceiverBy(String content) {
        if (content == null || content.isBlank()) return;
        if (list_receiverBy.contains(content)) return;
        boolean finish = false;
        int i = 0;
        int size = pre_list_receiverBy.size();
        while (!finish && i < size) {
            if (pre_list_receiverBy.get(i).getString().equals(content)) {
                pre_list_receiverBy.get(i).addInteger(1); // increase the uses of the content

                // add the content to the main list if the Phrases.LIST_JUMP_VALUE is passed and it is ot already their
                if (!list_receiverBy.contains(content)) {
                    if (pre_list_receiverBy.get(i).getInteger() >= Phrases.LIST_JUMP_VALUE) {
                        list_receiverBy.add(content);
                        window.setInputReceiver_by(list_receiverBy.toArray(new String[0]));
                    }
                }

                finish = true;
            }
            i++;
        }
        if (!finish) { // if the content is not in the pre_list the content is added to the pre_list
            pre_list_receiverBy.add(new StringInteger(content, 1));
        }
        System.out.println(Arrays.toString(list_receiverBy.toArray()));
    }

    public void addToPreListCategories(String content) {
        if (content == null || content.isBlank()) return;
        if (list_categories.contains(content)) return;
        boolean finish = false;
        int i = 0;
        int size = pre_list_categories.size();
        while (!finish && i < size) {
            if (pre_list_categories.get(i).getString().equals(content)) {
                pre_list_categories.get(i).addInteger(1); // increase the uses of the content

                // add the content to the main list if the Phrases.LIST_JUMP_VALUE is passed
                if (!list_categories.contains(content)) {
                    if (pre_list_categories.get(i).getInteger() >= Phrases.LIST_JUMP_VALUE) {
                        list_categories.add(content);
                        window.setInputCategory(list_categories.toArray(new String[0]));
                    }
                }

                finish = true;
            }
            i++;
        }
        if (!finish) { // if the content is not in the pre_list the content is added to the pre_list
            pre_list_categories.add(new StringInteger(content, 1));
        }
    }

    public void addToPreListPurpose(String content) {
        if (content == null || content.isBlank()) return;
        if (list_purpose.contains(content)) return;
        boolean finish = false;
        int i = 0;
        int size = pre_list_purpose.size();
        while (!finish && i < size) {
            if (pre_list_purpose.get(i).getString().equals(content)) {
                pre_list_purpose.get(i).addInteger(1); // increase the uses of the content

                // add the content to the main list if the Phrases.LIST_JUMP_VALUE is passed
                if (!list_purpose.contains(content)) {
                    if (pre_list_purpose.get(i).getInteger() >= Phrases.LIST_JUMP_VALUE) {
                        list_purpose.add(content);
                        window.setInputPurpose(list_purpose.toArray(new String[0]));
                    }
                }

                finish = true;
            }
            i++;
        }
        if (!finish) { // if the content is not in the pre_list the content is added to the pre_list
            pre_list_purpose.add(new StringInteger(content, 1));
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

