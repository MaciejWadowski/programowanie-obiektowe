package lab2;

import lab1.data.frame.DataFrame;

import java.util.ArrayList;
import java.util.List;

public class SparseDataFrame extends DataFrame {
    /**
     * Constructs DataFrame with empty Columns
     *
     * @param names names of Columns
     * @param types types for Columns to hold
     */

    private List<COOValue> columns;
    private Object argumentToHide;

    public SparseDataFrame(String[] names, String[] types, Object argumentToHide) throws Exception {
        super(names, types);
        String type = types[0];
        for (String s: types) {
            if (!type.equals(s)) {
                throw new Exception();
            }
        }
        columns = new ArrayList<>();
        this.argumentToHide = argumentToHide;
    }

    public boolean addRow(Object... objects) {
        super.addRow(objects);

        boolean toAdd = true;
        for (Object o: objects) {
            if(!o.equals(argumentToHide)) {
                columns.add(new COOValue(o, size() - 1));
                return true;
            }
        }
        return false;
    }

    public DataFrame toDense() {
        DataFrame output = new DataFrame(getColumnNames(), getTypes());

        int columnCount = getColumnNames().length;
        for (int i = 0; i < columns.size() ; i++) {
            Object[] objects = new Object[columnCount];
            for (int j = 0; j < columnCount; j++) {
                objects[j] = columns.get(i).getValue();
            }
            output.addRow(objects);
        }
        return output;
    }

}
