package threads;

import lab1.data.frame.Column;
import lab1.data.frame.DataFrame;
import values.DateTimeValue;
import values.StringValue;
import values.Value;
import utils.Applyable;
import utils.Operation;
import exceptions.InvalidColumnSizeException;
import exceptions.ValueOperationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ConcurrentDataFrame extends DataFrame {

    private int maximumThreadsConcurrently = 5;

    public ConcurrentDataFrame(String[] names, Class<? extends Value>[] clazz) {
        super(names, clazz);
    }

    public ConcurrentDataFrame() {
    }

    public ConcurrentDataFrame(String file, Class<? extends Value>[] classes) throws Exception {
        super(file, classes);
    }

    public ConcurrentDataFrame(List<Column> columns) {
        super(columns);
    }

    public void setMaximumThreadsConcurrently(int maximumThreadsConcurrently) {
        if (maximumThreadsConcurrently > 1) {
            this.maximumThreadsConcurrently = maximumThreadsConcurrently;
        }
    }

    @Override
    public DataFrameGroupBy groupBy(String... colname) throws ValueOperationException {
        HashMap<List<Value>, DataFrame> map = new HashMap<>(colname.length);
        List<Column> columns = Arrays.stream(colname)
                .map(this::getColumn)
                .collect(Collectors.toList());

        for (int i = 0; i < size(); i++) {
            List<Value> values = new ArrayList<>(columns.size());

            for (var column : columns) {
                values.add(column.getElement(i));
            }

            if (!map.containsKey(values)) {
                map.put(values, iloc(i));
            } else {
                map.get(values).addRow(getRow(i));
            }
        }
        return new ConcurrentDataFrameGroupBy(map, colname);
    }

    public class ConcurrentDataFrameGroupBy extends DataFrame.DataFrameGroupBy {

        /**
         * Constructor for inner DataFrame class
         *
         * @param map      organized map with small DataFrames
         * @param colNames column names, which outer DataFrame is grouped by
         */
        public ConcurrentDataFrameGroupBy(HashMap<List<Value>, DataFrame> map, String[] colNames) {
            super(map, colNames);
        }

        @Override
        protected DataFrame operation(Operation operation, boolean toDrop) throws ValueOperationException {
            DataFrame dataFrame = getDataFrameGroupByTemplate(toDrop);

            ExecutorService executorService = Executors.newFixedThreadPool(maximumThreadsConcurrently);
            List<Callable<List<Value>>> callables = new ArrayList<>();
            for (var keys : map.keySet()) {
                DataFrame dataFrameWithIdValues = map.get(keys);
                callables.add(() -> {
                    List<Value> toAdd = new ArrayList<>();
                    String[] columnNames = dataFrameWithIdValues.getColumnNames();
                    for (var columnName : columnNames) {
                        Column column = dataFrameWithIdValues.getColumn(columnName);
                        if (!colNames.contains(columnName)) {
                            if (toDrop && !(column.getClazz().equals(DateTimeValue.class)
                                    || column.getClazz().equals(StringValue.class))) {
                                toAdd.add(column.calculate(operation));
                            } else if (!toDrop) {
                                toAdd.add(column.calculate(operation));
                            }
                        } else {
                            toAdd.add(column.getElement(0));
                        }
                    }
                    return toAdd;
                });
            }

            List<List<Value>> aggregateDataFrameValues = new ArrayList<>();
            List<Future<List<Value>>> futureValues;
            try {
                futureValues = executorService.invokeAll(callables);
                for (var value : futureValues) {
                    aggregateDataFrameValues.add(value.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            executorService.shutdown();

            for (var result : aggregateDataFrameValues) {
                dataFrame.addRow(result);
            }
            return dataFrame;
        }

        @Override
        public DataFrame max() {
            try {
                return operation(Operation.MAX, false);
            } catch (ValueOperationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame min() {
            try {
                return operation(Operation.MIN, false);
            } catch (ValueOperationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame mean() {
            try {
                return operation(Operation.MEAN, true);
            } catch (ValueOperationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame std() {
            try {
                return operation(Operation.STD, true);
            } catch (ValueOperationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame sum() {
            try {
                return operation(Operation.SUM, true);
            } catch (ValueOperationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame var() {
            try {
                return operation(Operation.VAR, true);
            } catch (ValueOperationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame apply(Applyable applyable) throws ValueOperationException {
            List<DataFrame> dataFrames = map.keySet().parallelStream()
                    .map(e -> applyable.apply(map.get(e)))
                    .collect(Collectors.toList());

            if (!dataFrames.isEmpty()) {
                DataFrame outputDataFrame = new DataFrame(dataFrames.get(0).getColumnNames(), dataFrames.get(0).getClasses());
                //because all DataFrames with outputs have only one row
                for (DataFrame dataFrame : dataFrames) {
                    Value[] row = dataFrame.getRow(0);
                    outputDataFrame.addRow(row);
                }

                return outputDataFrame;
            }
            throw new ValueOperationException("List of DataFrames is empty");
        }
    }

    @Override
    public void add(Value value) {
        columns.parallelStream().forEach(col -> col.addAll(value));
    }

    @Override
    public void div(Value value) {
        columns.parallelStream().forEach(col -> col.divAll(value));
    }

    @Override
    public void mul(Value value) {
        columns.parallelStream().forEach(col -> col.mulAll(value));
    }

    @Override
    public void add(Column column) {
        columns.parallelStream().forEach(col -> {
            try {
                col.add(column);
            } catch (InvalidColumnSizeException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void mul(Column column) {
        columns.parallelStream().forEach(col -> {
            try {
                col.mul(column);
            } catch (InvalidColumnSizeException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void div(Column column) {
        columns.parallelStream().forEach(col -> {
            try {
                col.div(column);
            } catch (InvalidColumnSizeException e) {
                e.printStackTrace();
            }
        });
    }
}
