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
    private String tableName;
    private String userName;
    private String userPassword;
    private String url;

    private static boolean EXIST = false;

    /**
     * DataFrameDB constructor with parameters
     *
     * @param tableName - name of your table
     * @param url       - url to your database
     * @param userName  - user login to database
     * @param password  - user password to database
     */

    public DataFrameDB(String tableName, String[] names, Class<? extends Value>[] clazz, String url, String userName, String password) {
        super(names, clazz);
        this.tableName = tableName;
        this.url = url;
        this.userName = userName;
        this.userPassword = password;

    }

    /**
     * Method which connect DataFrameDB with database
     */
    public void connect() {
        if (connection == null) {
            for (int i = 0; i < 3; i++) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
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

    /**
     * Create a table in database, to hold DataFrameDB values
     *
     * @throws SQLException thows when error occurs, for example table name which exist already
     */
    public void createTable() throws SQLException {
        connect();
        statement = connection.createStatement();
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE " + tableName + " (");
        String[] columnNames = getColumnNames();
        Class[] columnClasses = getClasses();
        for (int i = 0; i < columnNames.length; i++) {
            stringBuilder.append(columnNames[i]);
            stringBuilder.append(" ");
            stringBuilder.append(sqlType(columnClasses[i]));
            stringBuilder.append(" NOT NULL");
            stringBuilder.append((i == columnNames.length - 1) ? ")" : ", ");
        }
        System.out.println(stringBuilder.toString());
        statement.executeUpdate(stringBuilder.toString());
        freeResources();
    }

    /**
     * return string for mysql statement, need when creating a table
     *
     * @param clazz Value subclass
     * @return mysql Value subclass representation
     */

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

    /**
     * Constructor for DataFrameDB
     *
     * @param tableName name for table in mysql database
     * @param file      csv file to read data from
     * @param classes   CLASSES in correct order for values in file
     * @param url       database url
     * @param userName  database user name
     * @param password  database user password
     * @throws Exception throws when file, user name, url, user password, CLASSES are invalid
     */
    public DataFrameDB(String tableName, String file, Class<? extends Value>[] classes, String url, String userName, String password) throws Exception {
        super(new BufferedReader(new FileReader(file)).readLine().split(","), classes);
        this.tableName = tableName;
        this.url = url;
        this.userName = userName;
        this.userPassword = password;
        connect();
        createTable();
        statement = connection.createStatement();
        String update = " LOAD DATA LOCAL INFILE '" + file +
                "' INTO TABLE " + tableName +
                " FIELDS TERMINATED BY \',\' " +
                " LINES TERMINATED BY \'\\n\'";
        statement.executeUpdate(update);
        statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + tableName + " WHERE id=\"id\"");
        freeResources();
    }

    /**
     * Static method which return result from  mysql statement
     *
     * @param dataFrameDB dataFrameDB where statement will be executed
     * @param expression  statement to execute
     * @return DataFrame with expression results
     * @throws Exception when statement is invalid
     */
    public static DataFrame select(DataFrameDB dataFrameDB, String expression) throws Exception {
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
        } catch (SQLException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | ValueOperationException | InstantiationException e) {
            throw e;
        } finally {
            dataFrameDB.freeResources();
        }

        return dataFrame;
    }

    /**
     * Add rows to DataFrameDB mysql database
     *
     * @param values string representation of Values to insert into database
     * @throws SQLException
     */
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
        statement.executeUpdate("INSERT INTO " + tableName + " VALUES (" + stringBuilder.toString());
    }

    /**
     * Add rows to DataFrameDB mysql database
     *
     * @param values values to insert into database
     */

    @Override
    public void addRow(Value... values) {
        try {
            addRow(Stream.of(values).map(Value::toString).toArray(String[]::new));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * return number of records in database
     *
     * @return size of DataFrameDB
     */
    @Override
    public int size() {
        int size = 0;
        try {
            connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT count(*) as size FROM " + tableName);
            size = resultSet.getInt("size");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * Conver DataFrameDB to string
     *
     * @return string representation of DataFrameDB
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + tableName);
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

    /**
     * Groups DataFrameDB by specified column names
     *
     * @param columnNames column names which groups dataframe
     * @return inner class, to calculate  operations on grouped DataFrame
     */
    @Override
    public DataFrameGroupBy groupBy(String... columnNames) {
        return new DataFrameGroupBy(null, columnNames);
    }


    public class DataFrameGroupBy extends DataFrame.DataFrameGroupBy {

        private ArrayList<String> columnNames;
        private ArrayList<String> allNames;

        /**
         * constructor for DataFrameGroupBy
         *
         * @param map      mapped values, used only to match outer class constructor
         * @param colNames column names which dataframe is grouped by
         */
        public DataFrameGroupBy(HashMap<List<Value>, DataFrame> map, String[] colNames) {
            super(map, colNames);
            columnNames = new ArrayList<>(List.of(colNames));
            allNames = new ArrayList<>(List.of(getColumnNames()));
        }

        /**
         * Used to create and execute sql statement, then convert result into DataFrame
         *
         * @param expression expression returned from one of min, max etc methods
         * @param toDrop     to drop tables with columns CLASSES, where operations on values are impossible
         * @return result as DataFrame
         */
        private DataFrame operation(String expression, boolean toDrop) {
            DataFrame dataFrame = getDataFrameGroupByTemplate(toDrop);

            try {
                connect();
                statement = connection.createStatement();
                Class[] classes = getClasses();
                StringBuilder stringBuilder = new StringBuilder("SELECT ");
                for (int i = 0; i < allNames.size(); i++) {
                    boolean queried = false;

                    if (!toDrop && !columnNames.contains(allNames.get(i))) {
                        stringBuilder.append(expression).append(allNames.get(i)).append(")");
                        queried = true;
                    } else if (columnNames.contains(allNames.get(i))) {
                        stringBuilder.append(allNames.get(i));
                        queried = true;
                    } else if (toDrop && !columnNames.contains(allNames.get(i)) && !(classes[i] == StringValue.class ||
                            classes[i] == DateTimeValue.class)) {
                        stringBuilder.append(expression).append(allNames.get(i)).append(")");
                        queried = true;
                    }

                    if (queried) {
                        stringBuilder.append((i == allNames.size() - 1) ? " " : ", ");
                    }
                }

                if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
                    stringBuilder.setCharAt(stringBuilder.length() - 1, ' ');
                }

                stringBuilder.append("FROM ").append(tableName).append(" GROUP BY ");

                for (int i = 0; i < columnNames.size(); i++) {
                    stringBuilder.append(columnNames.get(i));
                    stringBuilder.append((i == columnNames.size() - 1) ? ";" : ", ");
                }

                System.out.println(stringBuilder.toString());
                resultSet = statement.executeQuery(stringBuilder.toString());

                Value[] values = new Value[dataFrame.getClasses().length];
                List<Constructor<? extends Value>> constructors = new ArrayList<>();
                for (var clazz : dataFrame.getClasses()) {
                    Constructor<? extends Value> constructor = clazz.getConstructor(String.class);
                    constructors.add(constructor);
                }

                while (resultSet.next()) {
                    for (int i = 1; i <= dataFrame.getClasses().length; i++) {
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