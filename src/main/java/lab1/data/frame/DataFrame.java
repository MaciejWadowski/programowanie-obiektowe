package lab1.data.frame;

import javax.xml.crypto.Data;
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
                    output.columns.add(copy ? c.clone() : c);
                    break;
                }
            }
        }
        return output;
    }

    public DataFrame iloc(int i) {
        DataFrame output = new DataFrame();

        for(Column c : columns) {
            Column column = new Column(c.getName(), c.getType());
            if(this.size() > i && i >= 0) {
                column.addElement(c.elementAtIndex(i));
            }
            output.columns.add(column);
        }

        return output;
    }

    public DataFrame iloc(int from, int to) {
        DataFrame output = new DataFrame();
        from = (from < 0) ? 0 : from;

        for (Column c: columns) {
            Column column = new Column(c.getName(), c.getType());
            for(int i = from; (i <= to) && (i < size()); i++) {
                column.addElement(c.elementAtIndex(i));
            }
            output.columns.add(column);
        }

        return output;
    }
}