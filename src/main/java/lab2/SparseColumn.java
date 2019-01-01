package lab2;

import lab1.data.frame.Column;
import values.Value;

import java.util.ArrayList;
import java.util.List;

public class SparseColumn extends Column {

    private List<COOValue> cooValues;

    /**
     * Constructor for Column class,
     * if parameter name is not a primitive type, user have to pass Class name with
     * its full  path otherwise a ClassNotFoundException is thrown
     *
     * @param name  Column name
     * @param clazz Column type
     */
    public SparseColumn(String name, Class<? extends Value> clazz) {
        super(name, clazz);
        cooValues = new ArrayList<>();
    }

    /**
     * Return COOValue object from column
     * at specified index
     *
     * @param index element position
     * @return COOValue object
     */

    public COOValue getCOOElement(int index) {
        return cooValues.get(index);
    }

    /**
     * Return how much Column elements store
     * in memory
     *
     * @return size of this column
     */
    @Override
    public int size() {
        return cooValues.size();
    }

    /**
     * Human readable form of SparseColumn
     *
     * @return String representation of SparseColumn
     */
    @Override
    public String toString() {
        return "SparseColumn{" +
                "name=" + getName() +
                " cooValues=" + cooValues +
                "}\n";
    }

    @Override
    public SparseColumn clone() {
        SparseColumn sparseColumn = new SparseColumn(getName(), getClazz());
        sparseColumn.cooValues = new ArrayList<>(cooValues);
        return sparseColumn;
    }

    public void addElement(COOValue element) {
        cooValues.add(element);
    }

    public void addElement(Value value, int index) {
        cooValues.add(new COOValue(value, index));
    }
}
