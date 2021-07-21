package main;

import window.Window;
import Money.Entry;

import java.util.ArrayList;

public class Money {

    private final Window window;

    private ArrayList<String> list_receiverBy;
    private ArrayList<String> list_categories;
    private ArrayList<String> list_purpose;

    public Money() {

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
    }

    public void neu() {
        // TODO neu methode
    }

    public void enter() {
        // TODO enter methode
    }

    public void edit(Entry entry) {
        // TODO edit methode
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

