package Money;

import Phrases.Phrases;
import Storage.Load;
import Storage.Save;
import utilitis.Options;
import utilitis.StringInteger;
import window.ChoseFile;
import window.Window;
import window.startingWindow;

import java.time.LocalDate;
import java.util.ArrayList;

public class Money {

    private Window window;

    private final ArrayList<Entry> entries = new ArrayList<>();
    private int currentEntry;
    private int topEntry = 0;
    private int oldTopEntry;

    private final ArrayList<String> list_receiverBy = new ArrayList<>();
    private final ArrayList<StringInteger> pre_list_receiverBy = new ArrayList<>();
    private final ArrayList<String> list_categories = new ArrayList<>();
    private final ArrayList<StringInteger> pre_list_categories = new ArrayList<>();
    private final ArrayList<String> list_purpose = new ArrayList<>();
    private final ArrayList<StringInteger> pre_list_purpose = new ArrayList<>();

    private String path;
    private final ArrayList<String> paths;

    public Money() {

        paths = Load.loadPaths(Phrases.FILE_PATHS);

        startingWindow startingWindow = new startingWindow();
        startingWindow.setMaxProgressBar(4);

        path = ChoseFile.inputDialog(startingWindow, Phrases.choseFile, paths.toArray(new String[0]));

        if (path == null) {
            startingWindow.dispose();
            return;
        }

        startingWindow.addToProgressBar(1);

        new Phrases();

        startingWindow.addToProgressBar(1);

        window = new Window("Money", this);

        startingWindow.addToProgressBar(1);

        // loading

        boolean loading = Load.load(this, path, window.getMaxContentElements());
        if (loading) {
            System.out.println("Money.Money >> Entries loaded");
        } else {
            System.out.println("Money.Money >> No Entries loaded");
        }

        startingWindow.addToProgressBar(1);

//        // testing entries
//        int preload = 10000;
//        for (int i = 0; i < preload; i++) {
//            entries.add(new Entry(Options.INCOME, i + 1, LocalDate.of(2021, 7, 10), "receiver", "category", "purpose", 0f, 0f, 0f, this));
//        }
//        int n = entries.size();
//        entries.add(new Entry(Options.INCOME, n + 1, LocalDate.of(2021, 7, 10), "receiver1", "category1", "purpose1", 0f, 99f, 99f, this));
//
//        topEntry = preload + 1 - 7;
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
        tempEntry.setLocalDate(date);
        tempEntry.setReceiverBy(receiverBy.isBlank() ? " " : receiverBy);
        tempEntry.setCategory(category.isBlank() ? " " : category);
        tempEntry.setPurpose(purpose.isBlank() ? " " : purpose);
        tempEntry.setSpending(spending);
        tempEntry.setIncome(income);
        tempEntry.setBalance(balance);

        // update all following entries.
        for (int i = currentEntry; i < entries.size(); i++) {
            entries.get(i).updateBalance(currentEntry == 0 ? 0 : entries.get(i - 1).getBalance());
        }

        // update the showing
        updateAllEntries();

    }

    public boolean save() {
        if (!paths.contains(path)) {
            paths.add(path);
        }
        Save.saveFiles(Phrases.FILE_PATHS, paths.toArray(new String[0]));
        return Save.save(this, path);
    }

    public void moveTopEntry(int amount) {
        if (amount == 0) {
            return;
        }
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

    public void updateAfterEntryDelete(Entry entry) {
        int current = entries.indexOf(entry);
        boolean updateBalance = false;

        for (int i = current; i < entries.size(); i++) {
            // update the following entry numbers
            entries.get(i).setNumber(entries.get(i).getNumber() - 1);

            // update the following entry balances
            if (updateBalance) {
                entries.get(i).updateBalance(entries.get(i - 1).getBalance());
            }
            if (i == current + 1) {
                entries.get(i).updateBalance(entries.get(i - 2).getBalance());
                updateBalance = true;
            }
        }

        // remove the entry
        entries.remove(entry);

        // update the GUI
        updateAllEntries();
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

    public void clearPaths() {
        paths.clear();
    }

    public void saveOldTopEntry() {
        this.oldTopEntry = topEntry;
    }

    public void loadOldTopEntry() {
        this.topEntry = oldTopEntry;
    }

    // GETTER && SETTER

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

