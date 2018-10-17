package lab2;

import java.util.Arrays;

public class COOValue {
    private Object[] value;
    private int index;

    /**
     * Constructor for COOValue object
     * @param value Objects of DataFrame row to contain
     * @param index index in DataFrame
     */
    public COOValue(Object[] value, int index) {
        this.value = value;
        this.index = index;
    }

    /**
     * returns Objects
     * @return Array of Objects
     */
    public Object[] getRecord() {
        return value;
    }

    /**
     * Returns index in DataFrame
     * @return index in DataFrame
     */
    public int getIndex() {
        return index;
    }

    /**
     * Return human readable form
     * @return COOValue in string representation
     */
    @Override
    public String toString() {
        return  "{values=" + Arrays.toString(value) +
                ", index=" + index +
                "}\n";
    }
}
