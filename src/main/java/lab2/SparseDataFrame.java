package lab2;

import lab1.data.frame.DataFrame;

import java.util.ArrayList;
import java.util.List;

public class SparseDataFrame extends DataFrame {

    private List<COOValue> cooValueList;
    private Object argumentToHide;

    /**
     * Constructor for SparseDataFrame, types in String[] must be same type
     * otherwise throws Exception
     * @param names column names
     * @param types type of Object to hold
     * @param argumentToHide argument to hide in SparseDataFrame
     * @throws Exception
     */
    public SparseDataFrame(String[] names, String[] types, Object argumentToHide) throws Exception {
        super(names, types);
        String type = types[0];
        for (String s: types) {
            if (!type.equals(s)) {
                throw new Exception();
            }
        }

        cooValueList = new ArrayList<>();
        this.argumentToHide = argumentToHide;
    }

    /**
     * Invoke super Class addRow and if
     * doesn't have a row with argument to Hide,
     * store it in COOValue
     * @param objects Objects to add to DataFrame
     * @return true if COOValue was added successfully
     */
    @Override
    public boolean addRow(Object... objects) {
        boolean toAdd = super.addRow(objects);

        if(!toAdd) {
            return false;
        }
        toAdd = false;
        for (Object o: objects) {
            if(!o.equals(argumentToHide)) {
                toAdd = true;
                break;
            }
        }

        if(toAdd) {
            cooValueList.add(new COOValue(objects, super.size() - 1));
        }
        return false;
    }

    /**
     * Return DataFrame, with COOValues in SparseDataFrame
     * @return DataFrame without "empty" records
     */
    public DataFrame toDense() {
        DataFrame dataFrame = new DataFrame(getColumnNames(), getTypes());

        for(COOValue cov: cooValueList) {
            dataFrame.addRow(cov.getRecord());
        }
        return dataFrame;
    }

    /**
     * returns size of rows not filled with argumentToHide object
     * @return size of SparseDataFrame
     */
    @Override
    public int size() {
        return cooValueList.size();
    }

    /**
     * Returns DataFrame with specified row from SparseDataFrame
     * @param i index of Row to copy
     * @return DataFrame with one row
     */
    @Override
    public DataFrame iloc(int i) {
        DataFrame dataFrame = new DataFrame(getColumnNames(), getTypes());

        if(i >= 0 && i < size()) {
            dataFrame.addRow(cooValueList.get(i).getRecord());
        }
        return dataFrame;
    }

    /**
     * Returns DataFrame with specified range of rows in SparseDataFrame
     * @param from from which index to copy
     * @param to to which index to copy
     * @return DataFrame with rows between parameters
     */
    @Override
    public DataFrame iloc(int from, int to) {
        DataFrame dataFrame = new DataFrame(getColumnNames(), getTypes());

        if((from < 0) && (to > size()) && (from > to)) {
            return dataFrame;
        }

        for (int i = from; i < to ; i++) {
            dataFrame.addRow(cooValueList.get(i).getRecord());
        }
        return dataFrame;
    }

    /**
     * Returns Sparse DataFrame with Specified Column Names
     * @param names Column name to copy
     * @param copy true - deep copy or false to shallow copy
     * @return SparseDataFrame with specified columns
     */
    @Override
    public SparseDataFrame get(String[] names, boolean copy) {
        SparseDataFrame sparseDataFrame = null;
        String[] strings = getColumnNames();
        String[] types = new String[names.length];
        String[] superTypes = getTypes();

        for (int i = 0; i < types.length; i++) {
            types[i] = superTypes[i];
        }
        try {
            sparseDataFrame = new SparseDataFrame(names, types, argumentToHide);
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < names.length; i++) {
                for (int j = 0; j < strings.length; j++) {
                    if(names[i].equals(strings[j])) {
                        indexes.add(j);
                        break;
                    }
                }
            }

            Object[] objects = new Object[indexes.size()];
            for (COOValue cov: cooValueList) {
                for (int i = 0; i < names.length; i++) {
                    objects[i] = cov.getRecord()[indexes.get(i)];
                }
                sparseDataFrame.cooValueList.add(new COOValue(objects, cov.getIndex()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sparseDataFrame;
    }

    /**
     * Human readable form of SparseDataFrame
     * @return SparseDataFrame as String
     */
    @Override
    public String toString() {
        return "SparseDataFrame " +
                "cooValueList=\n{" + cooValueList +
                '}';
    }
}
