package lab2;

import lab1.data.frame.Column;
import lab1.data.frame.DataFrame;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SparseDataFrame extends DataFrame {

    private List<SparseColumn> sparseColumnList;
    private Object argumentToHide;
    private int size;

    /**
     * Constructs DataFrame with empty Columns
     * @param names names of Columns
     * @param types types for Columns to hold
     */
    public SparseDataFrame(String[] names, String[] types, Object argumentToHide) {
        super(names, types);
        sparseColumnList = new ArrayList<>();
        String[] uniqueNames = getColumnNames();
        String[] stringTypes = super.getTypes();
        for (int i = 0; i < uniqueNames.length; i++) {
            try {
                sparseColumnList.add(new SparseColumn(uniqueNames[i], stringTypes[i]));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.argumentToHide = argumentToHide;
        size = 0;
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
     * @param file csv file to read
     * @param types type for column to hold
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public SparseDataFrame(String file, String[] types, Object argumentToHide) throws IOException, ClassNotFoundException {
        FileInputStream fstream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        this.argumentToHide = argumentToHide;

        String[] columnNames = br.readLine().split(",");
        sparseColumnList = new ArrayList<>();
        for (int i = 0; i < types.length ; i++) {
            sparseColumnList.add(new SparseColumn(columnNames[i], types[i]));
        }

        String[] columnType = getTypes();
        String strLine;
        Object[] objects = new Object[sparseColumnList.size()];
        while ((strLine = br.readLine()) != null)   {
            String[] str = strLine.split(",");
            for (int i = 0; i < str.length; i++) {
                if(!columnType[i].equals("java.lang.String")) {
                    objects[i] = castSwitcher(Double.parseDouble(str[i]), columnType[i]);
                } else {
                    objects[i] = str[i];
                }
            }
            addRow(objects.clone());
        }

        br.close();
    }

    /**
     * Add Row to SparseDataFrame, true if any element
     * is stored in memory
     * @param objects Objects to add to DataFrame
     * @return true if any element isn't argumentToHide
     */
    @Override
    public boolean addRow(Object... objects) {
        if(objects.length != sparseColumnList.size()) {
            return false;
        }

        boolean toAdd = true;

        for (int i = 0; i < objects.length; i++) {
            if(!sparseColumnList.get(i).getType().isInstance(objects[i])) {
                toAdd = false;
                System.out.println(sparseColumnList.get(i).getType() + " and  "  + objects[i].getClass());
                break;
            }
        }

        if(toAdd) {
            int i = 0;
            for(var sparseColumn: sparseColumnList) {
                if(!objects[i].equals(argumentToHide)) {
                    sparseColumn.addElement(objects[i], size);
                }
                i++;
            }
            size++;
            return true;
        }

        return false;
    }

    /**
     * Constructor which convert DataFrame
     * to SparseDataFrame
     * @param dataFrame DataFrame to convert
     * @param argumentToHide argument to hide
     */
    public SparseDataFrame(DataFrame dataFrame, Object argumentToHide) {
        super(dataFrame.getColumnNames(), dataFrame.getTypes());
        this.argumentToHide = argumentToHide;
        this.size = dataFrame.size();
        String[] names = getColumnNames();
        Column[] columns = new Column[names.length];
        sparseColumnList = new ArrayList<>(names.length);

        for (int i = 0; i < names.length; i++) {
            columns[i] = dataFrame.getColumn(names[i]);
        }

        try {
            for (Column c : columns) {
                SparseColumn sparseColumn = new SparseColumn(c.getName(), c.getType().toString().replace("class ",""));
                for (int i = 0; i < size; i++) {
                    if (!c.getElement(i).equals(argumentToHide)) {
                        sparseColumn.addElement(c.getElement(i), i);
                    }
                }
                sparseColumnList.add(sparseColumn);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return human readable form of SparseDataFrame
     * @return string representation of SparseDataFrame
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("SparseDataFrame\n");
        for (var sc: sparseColumnList) {
            int k = 0;
            stringBuilder.append(sc.getName()).append('\n');

            for (int i = 0; i < size; i++) {
                if(k < sc.size() && sc.getElement(k).getIndex() == i) {
                    stringBuilder.append(sc.getElement(k++).getObject()).append(", ");
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
     * @return real size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Return Column for Data Frame with specified name
     * @param name Column name to retrieve
     * @return Column
     */
    @Override
    public Column getColumn(String name) {
        Column column = null;
        int j = 0;

        for (var sc: sparseColumnList) {
            if(sc.getName().equals(name)) {
                try {
                    column = new Column(name, sc.getType().toString().replace("class ", ""));

                    for (int i = 0; i < size; i++) {
                        if((j < sc.size()) && (sc.getElement(j).getIndex() == i)) {
                            column.addElement(sc.getElement(j++).getObject());
                        } else {
                            column.addElement(argumentToHide);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return column;
    }

    /**
     * Return SparseColumn with specified name
     * @param name Column name to retrieve
     * @return SparseColumn
     */
    public SparseColumn getSparseColumn(String name) {
        for (var sc: sparseColumnList) {
            if(sc.getName().equals(name)) {
                return sc.clone();
            }
        }
        return null;
    }

    /**
     * Return new SparseDataFrame with specified columns
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
            for (var sc : sparseColumnList) {
                if(sc.getName().equals(cols[i])) {
                    sparseDataFrame.sparseColumnList.add(copy ? sc.clone() : sc);
                    break;
                }
            }
        }
        return sparseDataFrame;
    }

    /**
     * Return SparseDataFrame with specified row
     * @param i index of Row to copy
     * @return SparseDataFrame with one row
     */
    @Override
    public SparseDataFrame iloc(int i) {
        SparseDataFrame sparseDataFrame = new SparseDataFrame(getColumnNames(),getTypes(),argumentToHide);

        if(i < 0 || i >= size) {
            return sparseDataFrame;
        }

        Object[] objects = new Object[sparseColumnList.size()];
        int k = 0;
        for (var sc:sparseColumnList) {
            boolean toAdd = true;
            for (int j = 0; j < sc.size(); j++) {
                if(sc.getElement(j).getIndex() == i) {
                    objects[k] = sc.getElement(j).getObject();
                    toAdd = false;
                    break;
                }
            }
            if(toAdd) {
                objects[k] = argumentToHide;
            }
            k++;
        }
        sparseDataFrame.addRow(objects);
        return sparseDataFrame;
    }

    /**
     * Return new SparseDataFrame with rows between parameters form,to
     * @param from from which index to copy
     * @param to to which index to copy
     * @return SparseDataFrame with specified columns
     */
    @Override
    public SparseDataFrame iloc(int from, int to) {
        SparseDataFrame sparseDataFrame = new SparseDataFrame(getColumnNames(), getTypes(), argumentToHide);
        if(from < 0 || to < 0  || from >= to || to > size) {
            return sparseDataFrame;
        }

        sparseDataFrame.sparseColumnList.clear();
        for (var sc: sparseColumnList) {
            try {
                SparseColumn sparseColumn = new SparseColumn(sc.getName(), sc.getType().toString().replace("class ",""));
                for (int  i = 0;i < sc.size() && i < to; i++) {
                    if((sc.getElement(i).getIndex() >= from) && (sc.getElement(i).getIndex() < to)) {
                        sparseColumn.addElement(sc.getElement(i));
                    }
                }
                sparseDataFrame.sparseColumnList.add(sparseColumn);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        sparseDataFrame.size = to;
        return sparseDataFrame;
    }

    /**
     * Return Converted SparseDataFrame
     * to DataFrame
     * @return DataFrame
     */
    public DataFrame toDense() {
        DataFrame dataFrame = new DataFrame(getColumnNames(), getTypes());
        Object[] objects = new Object[sparseColumnList.size()];
        int[] indexes = new int[sparseColumnList.size()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = 0;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < sparseColumnList.size(); j++) {
                if((indexes[j] < sparseColumnList.get(j).size())
                        && (sparseColumnList.get(j).getElement(indexes[j]).getIndex() == i)) {
                    objects[j] = sparseColumnList.get(j).getElement(indexes[j]++).getObject();
                } else {
                    objects[j] = argumentToHide;
                }
            }
            dataFrame.addRow(objects.clone());
        }
        return dataFrame;
    }

    /**
     * Return array of Column types
     * @return array of strings
     */
    @Override
    public String[] getTypes() {
        String[] str = new String[sparseColumnList.size()];
        for (int i = 0; i < str.length ; i++) {
            str[i] = sparseColumnList.get(i).getType().toString();
            str[i] = str[i].replace("class ", "");
        }
        return str;
    }
}
