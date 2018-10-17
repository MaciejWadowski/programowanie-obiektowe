package lab1.data.frame;

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
     * @param types types for Columns to hold
     */
    public DataFrame(String[] names, String[] types) {

        columns = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {

            if(names.length <= i) {
                break;
            }

            if(isUnique(names[i])) {
                try {
                    columns.add(new Column(names[i], types[i]));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
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
     * types are hol
     * @param file CSV file
     * @param types type
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public DataFrame(String file, String[] types) throws IOException, ClassNotFoundException {

        FileInputStream fstream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String[] columnNames = br.readLine().split(",");
        columns = new ArrayList<>();
        for (int i = 0; i < types.length ; i++) {
            columns.add(new Column(columnNames[i], types[i]));
        }

        String strLine;
        while ((strLine = br.readLine()) != null)   {
            String[] str = strLine.split(",");
            Object[] objects = new Object[str.length];
            for (int i = 0; i < str.length; i++) {
                objects[i] = Double.parseDouble(str[i]);
            }
            addRow(objects);
        }

        br.close();
    }

    /**
     * Constructor with ArrayList of columns as parameter
     * @param columns list of columns
     */
    public DataFrame(List<Column> columns) {
        this.columns = columns;
    }

    private boolean isUnique(String name) {
        for(Column c : columns) {
            if(c.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add row to DataFrame
     * @param objects Objects to add to DataFrame
     * @return true if amount of passed objects match to amount of Columns and Objects to each Column have same type
     */
    public boolean addRow(Object... objects) {
        if(columns.size() != objects.length) {
            return false;
        }

        for (int i = 0; i < columns.size() ; i++) {
            if(!columns.get(i).isValid(objects[i])){
                return false;
            }
        }
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).addElement(objects[i]);
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

        if(!copy) {
            output.columns = columns;
            return output;
        }

        for (String s: cols) {
            for (Column c: columns) {
                if(s.equals(c.getName())) {
                    output.columns.add(c.clone());
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
        DataFrame output = new DataFrame(getColumnNames(), getTypes());

        int k = 0;
        if(i >= 0 && i < size()) {
            for (Column c: output.columns) {
                c.addElement(columns.get(i).getElement(k++));
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
            try {
                Column column = new Column(c.getName(), c.getType().toString().replace("class ", ""));
                for (int i = from; (i <= to) && (i < size()); i++) {
                    column.addElement(c.getElement(i));
                }
                output.columns.add(column);
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
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
    public String[] getTypes() {
        String[] str = new String[columns.size()];
        for (int i = 0; i < str.length ; i++) {
            str[i] = columns.get(i).getType().toString();
            str[i] = str[i].replace("class ", "");
        }
        return str;
    }
}