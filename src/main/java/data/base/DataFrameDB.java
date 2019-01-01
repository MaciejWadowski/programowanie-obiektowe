package data.base;

import lab1.data.frame.DataFrame;
import values.*;
import utils.Applyable;
import exceptions.ValueOperationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

public class DataFrameDB extends DataFrame {

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private String dataBaseName;
    private String userName;
    private String userPassword;
    private String url;

    public DataFrameDB(String dataBaseName, String[] names, Class<? extends Value>[] clazz, String url, String userName, String password) {
        super(names, clazz);
        this.dataBaseName = dataBaseName;
        this.url = url;
        this.userName = userName;
        this.userPassword = password;

    }

    public void connect() {
        if (connection == null) {
            for (int i = 0; i < 3; i++) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                    //"jdbc:mysql://mysql.agh.edu.pl/mwadowsk"
                    // mwadowsk
                    // oJG5kEujkudNMySv
                    connection = DriverManager.getConnection(url, userName, userPassword);
                    break;
                } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createTable() throws SQLException {
        connect();
        statement = connection.createStatement();
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE " + dataBaseName + " (");
        String[] columnNames = getColumnNames();
        Class[] columnClasses = getClasses();
        for (int i = 0; i < columnNames.length; i++) {
            stringBuilder.append(columnNames[i]);
            stringBuilder.append(" ");
            stringBuilder.append(sqlType(columnClasses[i]));
            stringBuilder.append(" NOT NULL");
            stringBuilder.append((i == columnNames.length - 1) ? ")" : ", ");
        }
        statement.executeUpdate(stringBuilder.toString());
        freeResources();
    }

    private String sqlType(Class<? extends Value> clazz) {
        if (clazz == IntegerValue.class) {
            return "INT";
        } else if (clazz == DoubleValue.class) {
            return "DOUBLE";
        } else if (clazz == DateTimeValue.class) {
            return "DATE";
        } else if (clazz == FloatValue.class) {
            return "FLOAT";
        } else if (clazz == StringValue.class) {
            return "VARCHAR(64)";
        }
        return null;
    }

    public DataFrameDB(String dataBaseName, String file, Class<? extends Value>[] classes, String url, String userName, String password) throws Exception {
        super(new BufferedReader(new FileReader(file)).readLine().split(","), classes);
        this.dataBaseName = dataBaseName;
        this.url = url;
        this.userName = userName;
        this.userPassword = password;
        connect();
        createTable();
        statement = connection.createStatement();
        String update = " LOAD DATA LOCAL INFILE '" + file +
                "' INTO TABLE " + dataBaseName +
                " FIELDS TERMINATED BY \',\' " +
                " LINES TERMINATED BY \'\\n\'";
        statement.executeUpdate(update);
        statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + dataBaseName + " WHERE id=\"id\"");
        freeResources();
    }

    public static DataFrame select(DataFrameDB dataFrameDB, String expression) {
        DataFrame dataFrame = null;
        dataFrameDB.connect();
        try {
            dataFrameDB.statement = dataFrameDB.connection.createStatement();
            dataFrameDB.resultSet = dataFrameDB.statement.executeQuery(expression);

            ResultSetMetaData resultSetMetaData = dataFrameDB.resultSet.getMetaData();
            List<String> names = List.of(dataFrameDB.getColumnNames());
            List<Class<? extends Value>> classes = List.of(dataFrameDB.getClasses());
            String[] columnNames = new String[resultSetMetaData.getColumnCount()];
            Class[] columnClasses = new Class[resultSetMetaData.getColumnCount()];

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                columnNames[i - 1] = resultSetMetaData.getColumnName(i);
                columnClasses[i - 1] = classes.get(names.indexOf(columnNames[i - 1]));
            }

            dataFrame = new DataFrame(columnNames, columnClasses);

            Value[] values = new Value[resultSetMetaData.getColumnCount()];

            while (dataFrameDB.resultSet.next()) {
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    Constructor<? extends Value> constructor = classes.get(names.indexOf(resultSetMetaData.getColumnName(i))).getConstructor(String.class);
                    values[i - 1] = constructor.newInstance(dataFrameDB.resultSet.getString(i));
                }
                dataFrame.addRow(values.clone());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ValueOperationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            dataFrameDB.freeResources();
        }

        return dataFrame;
    }

    public void addRow(String[] values) throws SQLException {
        connect();
        statement = connection.createStatement();
        StringBuilder stringBuilder = new StringBuilder();
        Class[] columnClasses = getClasses();
        for (int i = 0; i < values.length; i++) {
            if (columnClasses[i] == DateTimeValue.class || columnClasses[i] == StringValue.class) {
                stringBuilder.append('\'');
            }
            stringBuilder.append(values[i]);
            if (columnClasses[i] == DateTimeValue.class || columnClasses[i] == StringValue.class) {
                stringBuilder.append('\'');
            }
            stringBuilder.append((i == values.length - 1) ? ")" : ", ");
        }
        statement.executeUpdate("INSERT INTO " + dataBaseName + " VALUES (" + stringBuilder.toString());
    }

    @Override
    public void addRow(Value... values) {
        try {
            addRow(Stream.of(values).map(Value::toString).toArray(String[]::new));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        int size = 0;
        try {
            connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT count(*) as size FROM " + dataBaseName);
            size = resultSet.getInt("size");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + dataBaseName);
            while (resultSet.next()) {
                for (int i = 1; i <= getClasses().length; i++) {
                    stringBuilder.append(resultSet.getString(i)).append(" | ");
                }
                stringBuilder.append('\n');
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            freeResources();
        }
        return stringBuilder.toString();
    }

    @Override
    public DataFrameGroupBy groupBy(String[] columnNames) {
        return new DataFrameGroupBy(null, columnNames);
    }

    public class DataFrameGroupBy extends DataFrame.DataFrameGroupBy {

        private ArrayList<String> columnNames;
        private ArrayList<String> allNames;

        public DataFrameGroupBy(HashMap<List<Value>, DataFrame> map, String[] colNames) {
            super(map, colNames);
            columnNames = new ArrayList<>(List.of(colNames));
            allNames = new ArrayList<>(List.of(getColumnNames()));
        }

        private DataFrame operation(String expression, boolean toDrop) {
            DataFrame dataFrame;

            if (toDrop) {
                List<Class<? extends Value>> classList = new ArrayList<>(List.of(getClasses()));
                ArrayList<String> nameList = new ArrayList<>(List.of(getColumnNames()));
                List<Integer> namesToRemove = new ArrayList<>();

                for (int i = 0; i < classList.size(); i++) {
                    if ((classList.get(i).equals(StringValue.class) || classList.get(i).equals(DateTimeValue.class)) && !colNames.contains(nameList.get(i))) {
                        namesToRemove.add(i);
                    }
                }

                for (int i = namesToRemove.size() - 1; i >= 0; i--) {
                    nameList.remove((int) namesToRemove.get(i));
                    classList.remove((int) namesToRemove.get(i));
                }

                String[] names = nameList.toArray(String[]::new);
                Class[] classes = classList.toArray(Class[]::new);

                dataFrame = new DataFrame(names, classes);
            } else {
                dataFrame = new DataFrame(getColumnNames(), getClasses());
            }

            try {
                connect();
                statement = connection.createStatement();
                Class[] classes = getClasses();
                StringBuilder stringBuilder = new StringBuilder("SELECT ");
                for (int i = 0; i < allNames.size(); i++) {
                    boolean queried = false;

                    if (!columnNames.contains(allNames.get(i))
                            && classes[i] != DateTimeValue.class
                            && classes [i] != StringValue.class) {
                        stringBuilder.append(expression).append(allNames.get(i)).append(")");
                        queried = true;
                    } else if((!toDrop && (classes[i] == DateTimeValue.class || classes[i] == StringValue.class)) ||
                            columnNames.contains(allNames.get(i))) {
                        stringBuilder.append(allNames.get(i));
                        queried = true;
                    }

                    if(queried) {
                        stringBuilder.append((i == allNames.size() - 1) ? " " : ", ");
                    }
                }

                if(stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
                    stringBuilder.setCharAt(stringBuilder.length() - 1, ' ');
                }

                stringBuilder.append("FROM ").append(dataBaseName).append(" GROUP BY ");

                for (int i = 0; i < columnNames.size(); i++) {
                    stringBuilder.append(columnNames.get(i));
                    stringBuilder.append((i == columnNames.size() - 1) ? ";" : ", ");
                }

                System.out.println(stringBuilder.toString());
                resultSet = statement.executeQuery(stringBuilder.toString());

                Value[] values = new Value[getClasses().length];
                List<Constructor<? extends Value>> constructors = new ArrayList<>();
                for (var clazz : getClasses()) {
                    Constructor<? extends Value> constructor = clazz.getConstructor(String.class);
                    constructors.add(constructor);
                }

                while (resultSet.next()) {
                    for (int i = 1; i <= getClasses().length; i++) {
                        values[i - 1] = constructors.get(i - 1).newInstance(resultSet.getString(i));
                    }
                    dataFrame.addRow(values.clone());
                }
            } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | ValueOperationException e) {
                e.printStackTrace();
            } finally {
                freeResources();
            }
            return dataFrame;
        }

        @Override
        public DataFrame max() {
            return operation("max(", false);
        }

        @Override
        public DataFrame min() {
            return operation("min(", false);
        }

        @Override
        public DataFrame mean() {
            return operation("avg(", true);
        }

        @Override
        public DataFrame std() {
            return operation("std(", true);
        }

        @Override
        public DataFrame sum() {
            return operation("sum(", true);
        }

        @Override
        public DataFrame var() {
            return operation("variance(", true);
        }

        @Override
        public DataFrame apply(Applyable applyable) throws ValueOperationException {
            return super.apply(applyable);
        }
    }

    private void freeResources() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
            } // ignore
            resultSet = null;
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
            } // ignore

            statement = null;
        }
    }
}