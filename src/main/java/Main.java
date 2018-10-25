import lab1.data.frame.DataFrame;
import lab2.SparseDataFrame;
import lab3.DoubleValue;
import lab3.IntegerValue;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DataFrame dataFrame = new DataFrame(new String[]{"col1", "col2"}, new Class[]{IntegerValue.class, IntegerValue.class});
        dataFrame.addRow(new IntegerValue(1), new IntegerValue(2));
        dataFrame.addRow(new IntegerValue(3), new IntegerValue(4));
        dataFrame.addRow(new IntegerValue(12), new IntegerValue(212));
        dataFrame.addRow(new IntegerValue(12), new IntegerValue(2123));

        System.out.println(dataFrame.toString());
        System.out.println(dataFrame.iloc(1));
        System.out.println(dataFrame.iloc(0, 2));
        System.out.println(dataFrame.getColumn("col1"));
        System.out.println();
        System.out.println(dataFrame.get(new String[]{"col1", "col2"}, true));

        SparseDataFrame sparseDataFrame = new SparseDataFrame(new String[]{"col1", "col2"},
                new Class[]{DoubleValue.class, DoubleValue.class}, new DoubleValue(0.0));
        sparseDataFrame.addRow(new DoubleValue(2.5), new DoubleValue(2.2));
        sparseDataFrame.addRow(new DoubleValue(0.0), new DoubleValue(0.0));
        sparseDataFrame.addRow(new DoubleValue(2.5), new DoubleValue(0.0));
        sparseDataFrame.addRow(new DoubleValue(2.215), new DoubleValue(212312.2));
        sparseDataFrame.addRow(new DoubleValue(2.5), new DoubleValue(2.2123));
        sparseDataFrame.addRow(new DoubleValue(-333.3), new DoubleValue(0.0));
        System.out.println(sparseDataFrame);
        System.out.println(sparseDataFrame.iloc(1));
        System.out.println(sparseDataFrame.iloc(1, 4));
        System.out.println(sparseDataFrame.get(new String[]{"col1", "col2"}, true));
        System.out.println(sparseDataFrame.getColumn("col1"));
        System.out.println(new SparseDataFrame(dataFrame, new IntegerValue(12)));

        DataFrame dataFrame1 = new DataFrame("src/main/resources/sparse.csv", new Class[]{DoubleValue.class, DoubleValue.class, DoubleValue.class});
        System.out.println(dataFrame1.iloc(100));
    }
}
