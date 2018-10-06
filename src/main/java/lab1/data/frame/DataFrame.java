package lab1.data.frame;

import java.util.ArrayList;
import java.util.List;


public class DataFrame {

    private List<Column> columns;

    public DataFrame(String[] names, String[] types) {

        columns = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {

            if((names.length <= i)) {
                break;
            }

            if(isUnique(names[i])) {
                columns.add(new Column(names[i], types[i]));
            }
        }
    }

    private DataFrame() {
        columns = new ArrayList<>();
    }

    private boolean isUnique(String name) {
        for(Column c : columns) {
            if(c.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public boolean addRow(Object... o) {
        if(columns.size() != o.length) {
            return false;
        }

        for (int i = 0; i < columns.size() ; i++) {
            if(!columns.get(i).isValid(o[i])){
                return false;
            }
        }
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).addElement(o[i]);
        }
        return true;
    }

    public int size() {
        return columns.isEmpty() ? 0 : columns.get(0).size();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for(Column c : columns) {
            out.append(c.toString()).append("\n");
        }
        return out.toString();
    }

    public Column getColumn(String name) {
        for(Column c : columns) {
            if(c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }

    public DataFrame get(String[] cols, boolean copy) {

        DataFrame output = new DataFrame();

        for (String s: cols) {
            for (Column c: columns) {
                if(s.equals(c.getName())) {
                    output.columns.add(copy ? c : c.clone());
                    break;
                }
            }
        }
        return output;
    }

}