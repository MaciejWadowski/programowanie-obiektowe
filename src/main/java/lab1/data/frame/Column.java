package lab1.data.frame;

import lab3.Value;

import java.util.ArrayList;
import java.util.List;

public class Column implements Cloneable {

    private String name;
    private Class<? extends Value> clazz;
    private List<Value> list;

    /**
     * Constructor for Column class,
     * if parameter name is not a primitive clazz, user have to pass Class name with
     * its full  path otherwise a ClassNotFoundException is thrown
     *
     * @param name  Column name
     * @param clazz Column class
     */
    public Column(String name, Class<? extends Value> clazz) {
        list = new ArrayList<>();
        this.name = name;
        this.clazz = clazz;
    }


    /**
     * Insert the Object element to the Column
     *
     * @param element Object to insert
     */
    public void addElement(Value element) {
        if (clazz.isInstance(element)) {
            list.add(element);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Return the name of Column
     *
     * @return name of the Column
     */
    public String getName() {
        return name;
    }

    /**
     * Return class  which Column is holding
     *
     * @return Column Class
     */
    public Class<? extends Value> getClazz() {
        return clazz;
    }

    /**
     * Return the Value element at specified index
     *
     * @param index element position
     * @return Value at specified index
     */
    public Value getElement(int index) {
        return list.get(index);
    }

    /**
     * Return the current size of this Column
     *
     * @return int
     */
    public int size() {
        return list.size();
    }

    /**
     * Return a String representation of this Column
     *
     * @return String representation of this Column
     */
    @Override
    public String toString() {
        return "Column " +
                "name='" + name + '\'' +
                ", clazz='" + clazz + '\'' +
                "\n list=" + list;
    }

    /**
     * Returns a clone of this Column
     *
     * @return new cloned Column
     */
    @Override
    public Column clone() {
        Column column = new Column(name, clazz);
        column.list = new ArrayList<>(list);
        return column;
    }
}