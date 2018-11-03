package lab1.data.frame;

import lab3.DoubleValue;
import lab3.FloatValue;
import lab3.IntegerValue;
import lab3.Value;
import lab4.Operation;

import java.util.ArrayList;
import java.util.List;

public class Column implements Cloneable{

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

    public Value getMax() {
        if(list.isEmpty()) {
            return null;
        }

        Value max = list.get(0);
        for (var value : list) {
            if(value.gte(max)) {
                max = value;
            }
        }
        return max;
    }

    public Value getMin() {
        if(list.isEmpty()) {
            return null;
        }

        Value min = list.get(0);
        for (var value : list) {
            if(value.lte(min)) {
                min = value;
            }
        }
        return min;
    }

    public Value getMean() {
        if(list.isEmpty()) {
            return null;
        }

        Value sum = getSum();
        return sum.div(new IntegerValue(list.size()));
    }

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

    public Value getStd() {
        if(list.isEmpty()) {
            return null;
        }

        Value var = getVar();

        if(clazz == IntegerValue.class) {
            return new IntegerValue((int)Math.sqrt(Double.parseDouble(var.toString())));
        } else if (clazz == FloatValue.class) {
            return new FloatValue((float) Math.sqrt(Double.parseDouble(var.toString())));
        } else if (clazz == DoubleValue.class) {
            return new DoubleValue(Math.sqrt((double)var.getValue()));
        }

        throw new IllegalArgumentException("Bad Column type: " + clazz);
    }

    public Value getVar() {
        Value mean = getMean();

        if(clazz == IntegerValue.class) {
            IntegerValue output = new IntegerValue(0);
            IntegerValue powVal = new IntegerValue(2);
            for (var value: list) {
                output = (IntegerValue) output.add(value.sub(mean).pow(powVal));
            }
            return output.div((new IntegerValue(list.size())));
        } else if (clazz == DoubleValue.class) {
            DoubleValue output = new DoubleValue(0.0);
            DoubleValue powVal = new DoubleValue(2.0);
            for (var value: list) {
                output = (DoubleValue) output.add(value.sub(mean).pow(powVal));
            }
            return output.div((new DoubleValue(list.size())));
        } else if(clazz == FloatValue.class) {
            FloatValue output = new FloatValue((float)0.0);
            FloatValue powVal = new FloatValue((float)2.0);
            for (var value: list) {
                output = (FloatValue) output.add(value.sub(mean).pow(powVal));
            }
            return output.div((new FloatValue(list.size())));
        }

        throw new IllegalArgumentException("Bad Column type " + clazz);
    }
}