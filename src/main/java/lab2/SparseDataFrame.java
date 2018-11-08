package lab2;

import lab1.data.frame.Column;
import lab1.data.frame.DataFrame;
import lab3.Value;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SparseDataFrame extends DataFrame {

    private List<SparseColumn> sparseColumnList;
    private Value argumentToHide;
    private int size;

    /**
     * Constructs DataFrame with empty Columns
     *
     * @param names   names of Columns
     * @param classes types for Columns to hold
     */
    public SparseDataFrame(String[] names, Class<? extends Value>[] classes, Value argumentToHide) {
        super(names, classes);
        this.sparseColumnList = new ArrayList<>();
        this.argumentToHide = argumentToHide;
        this.size = 0;
        String[] uniqueNames = getColumnNames();
        Class[] columnClass = super.getClasses();

        for (int i = 0; i < uniqueNames.length; i++) {
            sparseColumnList.add(new SparseColumn(uniqueNames[i], columnClass[i]));
        }
    }

    /**
     * Create Empty SparseDataFrame
     */
    public SparseDataFrame() {
        super();
        sparseColumnList = new ArrayList<>();
        size = 0;
    }

    /**
     * Constructor for SparseDataFrame, read values from CSV file
     *
     * @param file    csv file to read
     * @param classes type for column to hold
     * @throws IOException
     */
    public SparseDataFrame(String file, Class<? extends Value>[] classes, Value argumentToHide)
            throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        FileInputStream fstream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        this.argumentToHide = argumentToHide;

        String[] columnNames = br.readLine().split(",");
        sparseColumnList = new ArrayList<>();
        for (int i = 0; i < classes.length; i++) {
            sparseColumnList.add(new SparseColumn(columnNames[i], classes[i]));
        }

        String strLine;
        Value[] values = new Value[sparseColumnList.size()];
        List<Constructor<? extends Value>> constructors = new ArrayList<>(classes.length);
        for (int i = 0; i < classes.length; i++) {
            constructors.add(classes[i].getConstructor(String.class));
        }

        while ((strLine = br.readLine()) != null) {
            String[] str = strLine.split(",");
            for (int i = 0; i < str.length; i++) {
                values[i] = constructors.get(i).newInstance(str[i]);
            }
            addRow(values.clone());
        }

        br.close();
    }

    /**
     * Add Row to SparseDataFrame, true if any element
     * is stored in memory
     *
     * @param values Objects to add to DataFrame
     * @return true if any element isn't argumentToHide
     */
    @Override
    public void addRow(Value... values) {
        if (values.length != sparseColumnList.size()) {
            throw new IllegalArgumentException();
        }

        boolean toAdd = true;

        for (int i = 0; i < values.length; i++) {
            if (!sparseColumnList.get(i).getClazz().isInstance(values[i])) {
                toAdd = false;
                break;
            }
        }

        if (toAdd) {
            int i = 0;
            for (var sparseColumn : sparseColumnList) {
                if (!values[i].equals(argumentToHide)) {
                    sparseColumn.addElement(values[i], size);
                }
                i++;
            }
            size++;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Constructor which convert DataFrame
     * to SparseDataFrame
     *
     * @param dataFrame      DataFrame to convert
     * @param argumentToHide argument to hide
     */
    public SparseDataFrame(DataFrame dataFrame, Value argumentToHide) {
        super(dataFrame.getColumnNames(), dataFrame.getClasses());
        this.argumentToHide = argumentToHide;
        this.size = dataFrame.size();
        String[] names = getColumnNames();
        Column[] columns;
        sparseColumnList = new ArrayList<>(names.length);

        columns = Arrays.stream(names)
                .map(dataFrame::getColumn)
                .toArray(Column[]::new);

        for (Column c : columns) {
            SparseColumn sparseColumn = new SparseColumn(c.getName(), c.getClazz());

            for (int i = 0; i < size; i++) {
                if (!c.getElement(i).equals(argumentToHide)) {
                    sparseColumn.addElement(c.getElement(i), i);
                }
            }
            sparseColumnList.add(sparseColumn);
        }
    }

    /**
     * Return human readable form of SparseDataFrame
     *
     * @return string representation of SparseDataFrame
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("SparseDataFrame\n");
        for (var column : sparseColumnList) {
            int k = 0;
            stringBuilder.append(column.getName()).append('\n');

            for (int i = 0; i < size; i++) {
                if (k < column.size() && column.getCOOElement(k).getIndex() == i) {
                    stringBuilder.append(column.getCOOElement(k++).getValue()).append(", ");
                } else {
                    stringBuilder.append(argumentToHide).append(", ");
                }
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }


    /**
     * return size of rows with empty elements
     *
     * @return real size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Return Column for Data Frame with specified name
     *
     * @param name Column name to retrieve
     * @return Column
     */
    @Override
    public Column getColumn(String name) {
        Column column = null;
        int j = 0;

        for (var sparseColumn : sparseColumnList) {
            if (sparseColumn.getName().equals(name)) {
                column = new Column(name, sparseColumn.getClazz());

                for (int i = 0; i < size; i++) {
                    if ((j < sparseColumn.size()) && (sparseColumn.getCOOElement(j).getIndex() == i)) {
                        column.addElement(sparseColumn.getCOOElement(j++).getValue());
                    } else {
                        column.addElement(argumentToHide);
                    }
                }
            }
        }
        return column;
    }

    /**
     * Return SparseColumn with specified name
     *
     * @param name Column name to retrieve
     * @return SparseColumn
     */
    public SparseColumn getSparseColumn(String name) {
        return sparseColumnList.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .map(SparseColumn::clone)
                .orElse(null);
    }

    /**
     * Return new SparseDataFrame with specified columns
     *
     * @param cols names of Columns to copy
     * @param copy true - deep copy or false to shallow copy
     * @return SparseDataFrame with columns given as parameter
     */
    @Override
    public SparseDataFrame get(String[] cols, boolean copy) {
        SparseDataFrame sparseDataFrame = new SparseDataFrame();
        sparseDataFrame.size = size;
        sparseDataFrame.argumentToHide = argumentToHide;
        sparseDataFrame.sparseColumnList = new ArrayList<>(cols.length);

        for (int i = 0; i < cols.length; i++) {
            for (var column : sparseColumnList) {
                if (column.getName().equals(cols[i])) {
                    sparseDataFrame.sparseColumnList.add(copy ? column.clone() : column);
                    break;
                }
            }
        }
        return sparseDataFrame;
    }

    /**
     * Return SparseDataFrame with specified row
     *
     * @param i index of Row to copy
     * @return SparseDataFrame with one row
     */
    @Override
    public SparseDataFrame iloc(int i) {
        SparseDataFrame sparseDataFrame = new SparseDataFrame(getColumnNames(), getClasses(), argumentToHide);

        if (i < 0 || i >= size) {
            return sparseDataFrame;
        }

        Value[] values = new Value[sparseColumnList.size()];
        int k = 0;
        for (var sc : sparseColumnList) {
            boolean toAdd = true;
            for (int j = 0; j < sc.size(); j++) {
                if (sc.getCOOElement(j).getIndex() == i) {
                    values[k] = sc.getCOOElement(j).getValue();
                    toAdd = false;
                    break;
                }
            }
            if (toAdd) {
                values[k] = argumentToHide;
            }
            k++;
        }
        sparseDataFrame.addRow(values);
        return sparseDataFrame;
    }

    /**
     * Return new SparseDataFrame with rows between parameters form,to
     *
     * @param from from which index to copy
     * @param to   to which index to copy
     * @return SparseDataFrame with specified columns
     */
    @Override
    public SparseDataFrame iloc(int from, int to) {
        SparseDataFrame sparseDataFrame = new SparseDataFrame(getColumnNames(), super.getClasses(), argumentToHide);
        if (from < 0 || to < 0 || from >= to || to > size) {
            return sparseDataFrame;
        }

        sparseDataFrame.sparseColumnList.clear();
        for (var column : sparseColumnList) {
            SparseColumn sparseColumn = new SparseColumn(column.getName(), column.getClazz());
            for (int i = 0; i < column.size() && i < to; i++) {
                if ((column.getCOOElement(i).getIndex() >= from) && (column.getCOOElement(i).getIndex() < to)) {
                    sparseColumn.addElement(column.getCOOElement(i));
                }
            }
            sparseDataFrame.sparseColumnList.add(sparseColumn);
        }
        sparseDataFrame.size = to;
        return sparseDataFrame;
    }

    /**
     * Return Converted SparseDataFrame
     * to DataFrame
     *
     * @return DataFrame
     */
    public DataFrame toDense() {
        DataFrame dataFrame = new DataFrame(getColumnNames(), getClasses());
        Value[] values = new Value[sparseColumnList.size()];
        int[] indexes = new int[sparseColumnList.size()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = 0;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < sparseColumnList.size(); j++) {
                if ((indexes[j] < sparseColumnList.get(j).size())
                        && (sparseColumnList.get(j).getCOOElement(indexes[j]).getIndex() == i)) {
                    values[j] = sparseColumnList.get(j).getCOOElement(indexes[j]++).getValue();
                } else {
                    values[j] = argumentToHide;
                }
            }
            dataFrame.addRow(values.clone());
        }
        return dataFrame;
    }

    /**
     * Return array of Column types
     *
     * @return array of strings
     */
    @Override
    public Class<? extends Value>[] getClasses() {
        Class[] classes = new Class[sparseColumnList.size()];
        Arrays.setAll(classes, i -> sparseColumnList.get(i).getClazz());
        return classes;
    }

}
