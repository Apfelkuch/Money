package utilitis;

public class StringInteger {

    private String string;
    private Integer integer;

    public StringInteger(String string, Integer integer) {
        this.string = string;
        this.integer = integer;
    }

    public void addInteger(int amount) {
        this.integer += amount;
    }

    // GETTER && SETTER

    public String getString() {
        return string;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

}



