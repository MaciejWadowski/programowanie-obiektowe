package lab2;

import lab1.data.frame.Column;

import java.util.ArrayList;
import java.util.List;

public class SparseColumn extends Column {

    private List<COOValue> cooValues;

    /**
     * Constructor for Column class,
     * if parameter name is not a primitive type, user have to pass Class name with
     * its full  path otherwise a ClassNotFoundException is thrown
     *
     * @param name Column name
     * @param type Column type
     * @throws ClassNotFoundException
     */
    public SparseColumn(String name, String type) throws ClassNotFoundException {
        super(name, type);
        cooValues = new ArrayList<>();
    }

    /**
     * Return COOValue object from column
     * at specified index
     * @param index element position
     * @return COOValue object
     */
    @Override
    public COOValue getElement(int index) {
        return cooValues.get(index);
    }

    /**
     * Return how much Column elements store
     * in memory
     * @return size of this column
     */
    @Override
    public int size() {
        return cooValues.size();
    }

    /**
     * Human readable form of SparseColumn
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
        SparseColumn sparseColumn = null;
        try {
            sparseColumn = new SparseColumn(getName(), getType().toString().replace("class ",""));
            sparseColumn.cooValues = new ArrayList<>(cooValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sparseColumn;
    }

    public void addElement(COOValue element) {
        cooValues.add(element);
    }

    public void addElement(Object object, int index) {
        cooValues.add(new COOValue(object,index));
    }
}
