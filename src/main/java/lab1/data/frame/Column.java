package lab1.data.frame;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Column implements Cloneable {

    private String name;
    private Class<?> type;
    private List<Object> list;

    /**
     * Constructor for Column class,
     * if parameter name is not a primitive type, user have to pass Class name with
     * its full  path otherwise a ClassNotFoundException is thrown
     * @param name Column name
     * @param type Column type
     * @throws ClassNotFoundException
     */
    public Column(String name, String type) throws ClassNotFoundException {
        list = new ArrayList<>();
        this.name = name;
        System.out.println(fix(type));
        this.type = Class.forName(fix(type));
    }

    /**
     * Inspect if element is instance of Column Class
     * @param element Object to inspect
     * @return boolean true if it is instance of Column class Object
     */
    public boolean isValid(Object element) {
        return type.isInstance(element);
    }

    /**
     * Insert the Object element to the Column
     * @param element Object to insert
     * @return true if element insertion was successful
     */
    public boolean addElement(Object element) {
        if(isValid(element)) {
            list.add(element);
            return true;
        }
        return false;
    }

    /**
     * Return the name of Column
     * @return name of the Column
     */
    public String getName() {
        return name;
    }

    /**
     * Return class type which Column is holding
     * @return Column Class
     */
    public Class getType() {
        return type;
    }

    /**
     * Return the Object element at specified index
     * @param index element position
     * @return Object at specified index
     */
    public Object elementAtIndex(int index) {
        return list.get(index);
    }

    /**
     * Return the current size of this Column
     * @return int
     */
    public int size() {
        return list.size();
    }

    /**
     * Return a String representation of this Column
     * @return String representation of this Column
     */
    @Override
    public String toString() {
        return "Column " +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                "\n list=" + list;
    }

    private String fix(String type) {
        String lang = "java.lang.";
        switch(type) {
            case "int":
            case "Integer":
                return lang + "Integer";
            case "char":
            case "Character":
                return lang + "Character";
            case "double":
            case "short":
            case "long":
            case "byte":
            case "float":
            case "boolean":
                type = lang + (char)((int)type.charAt(0) - 32) + type.substring(1);
                return type;
            case "Double":
            case "Short":
            case "Long":
            case "Byte":
            case "Float":
            case "Boolean":
            case "String":
                return lang + type;
            default:
                return type;
        }
    }

    /**
     * Returns a clone of this Column
     * @return new cloned Column
     */
    @Override
    public Column clone() {
        Column column = null;
        try {
            column = new Column(name, type.toString().replace("class ", ""));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method method = null;

        if(list.isEmpty()) {
            return column;
        }
        try {
            method = list.get(0).getClass().getMethod("clone");
        } catch (NoSuchMethodException e) {
            System.out.println("Class: " + type + " doesn't have declared clone method");
        }

        for (Object o: list) {
            try {
                column.addElement(method == null ? o : method.invoke(o));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return column;
    }

    public Object getElement(int index) {
        return list.get(index);
    }
}