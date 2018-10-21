package lab1.data.frame;

import lab3.IntegerValue;
import lab3.Value;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class DataFrame {

    private List<Column> columns;

    /**
     * Constructs DataFrame with empty Columns
     * @param names names of Columns
     * @param clazz types for Columns to hold
     */
    public DataFrame(String[] names, Class<? extends Value>[] clazz) {
        columns = new ArrayList<>();
        for (int i = 0; i < clazz.length; i++) {

            if(names.length <= i) {
                break;
            }

            if(isUnique(names[i])) {
                columns.add(new Column(names[i], clazz[i]));
            }
        }
    }

    /**
     * Create empty DataFrame
     */
    public DataFrame() {
        columns = new ArrayList<>();
    }

    /**
     * Constructor DataFrame wchic reads data from csv file,
     * if not double or integer types, arguments must be String(Char and Byte too)
     * @param file CSV file
     * @param clazz type
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public DataFrame(String file, Class<? extends Value>[] clazz) throws IOException, ClassNotFoundException {

        FileInputStream fstream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String[] columnNames = br.readLine().split(",");
        columns = new ArrayList<>();
        for (int i = 0; i < clazz.length ; i++) {
            columns.add(new Column(columnNames[i], clazz[i]));
        }

        Class<? extends Value>[] columnClass = getClasses();
        IntegerValue integerValue = new IntegerValue(11);
        String strLine;
        Value[] values = new Value[columns.size()];
        while ((strLine = br.readLine()) != null)   {
            String[] str = strLine.split(",");
            for (int i = 0; i < str.length; i++) {
                values[i] = integerValue.create(str[i]);
            }
            addRow(values.clone());
        }

        br.close();
    }

    /**
     * Method to cast double type to another
     * @param value double value from file
     * @param clazz class to cast
     * @return Object with proper class type
     */
    public Object castSwitcher(double value, String clazz) {
        switch(clazz) {
            case "java.lang.Integer":
                return (int) value;
            case "java.lang.Short":
                return (short) value;
            case "java.lang.Long":
                return (long) value;
            case "java.lang.Float":
                return (float) value;
            case "java.lang.Boolean":
                return value != 0;
            default:
                return value;
        }
    }

    /**
     * Constructor with ArrayList of columns as parameter
     * @param columns list of columns
     */
    public DataFrame(List<Column> columns) {
        this.columns = columns;
    }

    protected boolean isUnique(String name) {
        for(Column c : columns) {
            if(c.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add row to DataFrame
     * @param values Objects to add to DataFrame
     * @return true if amount of passed objects match to amount of Columns and Objects to each Column have same type
     */
    public boolean addRow(Value... values) {
        if(columns.size() != values.length) {
            return false;
        }

        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).addElement(values[i]);
        }
        return true;
    }

    /**
     * Returns actual amount of rows in DataFrame(every Column has the same amount of rows)
     * @return actual amount of rows
     */
    public int size() {
        return columns.isEmpty() ? 0 : columns.get(0).size();
    }

    /**
     * Returns String representation of DataFrame
     * @return a string representation of DataFrame
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for(Column c : columns) {
            out.append(c.toString()).append("\n");
        }
        return out.toString();
    }

    /**
     * Returns the Column with specified name
     * @param name Column name to get
     * @return Column with specified name
     */
    public Column getColumn(String name) {
        for(Column c : columns) {
            if(c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }

    /**
     * Returns Data Frame with specified Columns by their names
     * @param cols names of Columns to copy
     * @param copy true - deep copy or false to shallow copy
     * @return new DataFrame with Columns with specified name
     */
    public DataFrame get(String[] cols, boolean copy) {
        DataFrame output = new DataFrame();

        for (String s: cols) {
            for (Column c: columns) {
                if(s.equals(c.getName())) {
                    output.columns.add(copy ? c.clone() : c);
                    break;
                }
            }
        }
        return output;
    }

    /**
     * Return new DataFrame with one specified row by index
     * @param i index of Row to copy
     * @return new DataFrame with one row
     */
    public DataFrame iloc(int i) {
        DataFrame output = new DataFrame(getColumnNames(), getClasses());

        int k = 0;
        if(i >= 0 && i < size()) {
            for (Column c: output.columns) {
                c.addElement(columns.get(k++).getElement(i));
            }
        }
        return output;
    }

    /**
     * Return new DataFrame with range of rows in this DataFrame
     * @param from from which index to copy
     * @param to to which index to copy
     * @return new DataFrame with specified rows
     */
    public DataFrame iloc(int from, int to) {
        DataFrame output = new DataFrame();
        from = (from < 0) ? 0 : from;

        for (Column c: columns) {
            Column column = new Column(c.getName(), c.getClazz());
            for (int i = from; (i <= to) && (i < size()); i++) {
                column.addElement(c.getElement(i));
            }
            output.columns.add(column);
        }

        return output;
    }

    /**
     * Return array of Column names
     * @return array of strings
     */
    public String[] getColumnNames() {
        String[] str = new String[columns.size()];
        for (int i = 0; i < str.length; i++) {
            str[i] = columns.get(i).getName();
        }
        return str;
    }

    /**
     * Return array of Column types
     * @return array of strings
     */
    public Class<? extends Value>[] getClasses() {
        Class[] classes = new Class[columns.size()];
        for (int i = 0; i < classes.length ; i++) {
            classes[i] = columns.get(i).getClazz();
        }
        return classes;
    }
}