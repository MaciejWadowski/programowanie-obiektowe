package lab2;

public class COOValue {
    private Object value;
    private int index;

    public COOValue(Object value, int index) {
        this.value = value;
        this.index = index;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
