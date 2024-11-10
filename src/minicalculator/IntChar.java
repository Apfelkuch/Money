package minicalculator;

public class IntChar {

    public int integer;
    public Character character;

    public IntChar(int integer, char character) {
        this.integer = integer;
        this.character = character;
    }

    @Override
    public String toString() {
        return integer + ":" + character;
    }

}
