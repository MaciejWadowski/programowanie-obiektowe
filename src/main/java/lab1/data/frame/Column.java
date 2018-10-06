package lab1.data.frame;

import java.util.ArrayList;
import java.util.List;

public class Column {

    private String name;
    private String type;
    private List<Object> list;

    public Column(String name, String type) {
        list = new ArrayList<>();
        this.name = name;
        this.type = fix(type);
    }

    public boolean isValid(Object element) {
        return element.getClass().toString().contains(type);
    }

    public void addElement(Object element) {
        if(isValid(element)) {
            list.add(element);
        }
    }

    public String getName() {
        return name;
    }

    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        return "Column " +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                "\n list=" + list;
    }

    private String fix(String type) {
        switch(type) {
            case "int":
                return "Integer";
            case "double":
                return "Double";
            case "byte":
                return "Byte";
            case "char":
                return "Character";
            case "boolean":
                return "Boolean";
            case "short":
                return "Short";
            case "float":
                return "Float";
            case "long":
                return "Long";
            default:
                return type;
        }
    }

    @Override
    public Column clone() {

        Column column = new Column(name, type);

        for (Object o: list) {
            column.addElement(o);
        }
        return column;
    }
}