package lab2;

import lab1.data.frame.Column;
import lab1.data.frame.DataFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        String[] stringTypes = getTypes();
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

    public SparseDataFrame() {
        super();
        sparseColumnList = new ArrayList<>();
        size = 0;
    }

    public SparseDataFrame(String file, String[] types) throws IOException, ClassNotFoundException {
        super(file, types);
    }

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

    @Override
    public int size() {
        return size;
    }

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

    @Override
    public SparseDataFrame get(String[] cols, boolean copy) {
        SparseDataFrame sparseDataFrame = new SparseDataFrame();
        sparseDataFrame.size = size;
        sparseDataFrame.argumentToHide = argumentToHide;

        for (var sc: sparseColumnList) {
            sparseDataFrame.sparseColumnList.add(copy ? sc.clone() : sc);
        }
        return sparseDataFrame;
    }

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
}
