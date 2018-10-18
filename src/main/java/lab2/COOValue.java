package lab2;

import java.util.Arrays;

public class COOValue {

    private Object object;
    private int index;

    public COOValue(Object object, int index) {
        this.object = object;
        this.index = index;
    }

    public Object getObject() {
        return object;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
