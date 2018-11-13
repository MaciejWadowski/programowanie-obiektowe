package lab1.data.frame;

import lab3.IntegerValue;
import lab3.Value;
import lab4.Operation;
import lab5.InvalidColumnSizeException;
import lab5.ValueOperationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            throw new ValueOperationException("Incompatible types: " + element + " class: " + element.getClass() + " and column class: " + clazz);
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

    public Value calculate(Operation operation) {
        switch (operation) {
            case MAX:
                return getMax();
            case MIN:
                return getMin();
            case SUM:
                return getSum();
            case MEAN:
                return getMean();
            case STD:
                return getStd();
            case VAR:
                return getVar();

        }
        return new IntegerValue(0);
    }

    /**
     * Return maximum value from Column
     *
     * @return maixmum value
     */
    public Value getMin() {
        if (list.isEmpty()) {
            return null;
        }

        Value max = list.get(0);
        for (var value : list) {
            if (value.gte(max)) {
                max = value;
            }
        }
        return max;
    }

    /**
     * Return minimum value from Column
     *
     * @return minimum value
     */
    public Value getMax() {
        if (list.isEmpty()) {
            return null;
        }

        Value min = list.get(0);
        for (var value : list) {
            if (value.lte(min)) {
                min = value;
            }
        }
        return min;
    }

    /**
     * Return mean from all Values stored in Column
     * for DateTimeValue and StringValue IllegalArgumentException is thrown
     *
     * @return mean from column values
     */
    public Value getMean() {
        if (list.isEmpty()) {
            return null;
        }

        Value sum = getSum();
        return sum.div(new IntegerValue(list.size()));
    }

    /**
     * Return sum from all Values stored in Column
     * for DateTimeValue and StringValue IllegalArgumentException is thrown
     *
     * @return mean from column values
     */
    public Value getSum() {
        if (list.isEmpty()) {
            return null;
        }

        Value firstValue = list.get(0).clone();
        Value sum = list.get(0);
        for (var value : list) {
            sum = sum.add(value);
        }
        return sum.sub(firstValue);
    }

    /**
     * Return sum from all Values stored in Column
     * for DateTimeValue and StringValue IllegalArgumentException is thrown
     *
     * @return mean from column values
     */
    public Value getStd() {
        try {
            Constructor<? extends Value> constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(Double.toString(Math.sqrt(Double.parseDouble(getVar().toString()))));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Bad Column type: " + clazz);
    }

    /**
     * Return sum from all Values stored in Column
     * for DateTimeValue and StringValue IllegalArgumentException is thrown
     *
     * @return mean from column values
     */
    public Value getVar() {
        Value mean = getMean();

        try {
            Constructor<? extends Value> constructor = clazz.getConstructor(String.class);
            Value output = constructor.newInstance("0");
            Value powVal = constructor.newInstance("2");
            for (var value : list) {
                output = output.add(value.sub(mean).pow(powVal));
            }
            return output.div(new IntegerValue(list.size()));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("Bad Column type " + clazz);
    }

    /**
     * Add value parameter to all Values stored in Column
     *
     * @param value value to add
     */
    public void addAll(Value value) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).add(value));
        }
    }

    /**
     * Multiply all Values stored in Column with value parameter
     *
     * @param value value to multiply by
     */
    public void mulAll(Value value) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).mul(value));
        }
    }

    /**
     * Divide all Values stored in Column with value parameter
     *
     * @param value value to divide by
     */
    public void divAll(Value value) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).div(value));
        }
    }

    /**
     * multiply values in column with values in other column
     *
     * @param column column to multiply by
     * @throws InvalidColumnSizeException if columns size doesn't match
     */
    public void mul(Column column) throws InvalidColumnSizeException {
        if (list.size() != column.list.size()) {
            throw new InvalidColumnSizeException("Columns size doesn't match : "
                    + name + " size: " + list.size()
                    + " and  " + column.name + " size: "
                    + column.list.size());
        }

        for (int i = 0; i < list.size(); i++) {
            list.set(i, column.list.get(i).mul(list.get(i)));
        }
    }

    /**
     * Add values in column with values in other column
     *
     * @param column column to add
     * @throws InvalidColumnSizeException if columns size doesn't match
     */
    public void add(Column column) throws InvalidColumnSizeException {
        if (list.size() != column.list.size()) {
            throw new InvalidColumnSizeException("Columns size doesn't match : "
                    + name + " size: " + list.size()
                    + " and  " + column.name + " size: "
                    + column.list.size());
        }

        for (int i = 0; i < list.size(); i++) {
            list.set(i, column.list.get(i).add(list.get(i)));
        }
    }

    /**
     * Divide values in column with values in other column
     *
     * @param column column to divide by
     * @throws InvalidColumnSizeException if columns size doesn't match
     */
    public void div(Column column) throws InvalidColumnSizeException {
        if (list.size() != column.list.size()) {
            throw new InvalidColumnSizeException("Columns size doesn't match : "
                    + name + " size: " + list.size()
                    + " and  " + column.name + " size: "
                    + column.list.size());
        }

        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).div(column.list.get(i)));
        }
    }

    /**
     * Generated by Intellij
     *
     * @param o object to compare
     * @return if objects are equals or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Column column = (Column) o;
        return Objects.equals(name, column.name) &&
                Objects.equals(clazz, column.clazz) &&
                Objects.equals(list, column.list);
    }

    /**
     * Generated by Intellij
     *
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, clazz, list);
    }
}