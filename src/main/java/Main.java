import lab1.data.frame.DataFrame;
import lab2.SparseDataFrame;

public class Main {
    public static void main(String[] args) {
        DataFrame dataFrame = new DataFrame(new String[]{"kol1","kol2"}, new String[]{"int","int"});
        dataFrame.addRow(1, 2);
        dataFrame.addRow(2,3);
        dataFrame.addRow(1,3);
        dataFrame.addRow(1,2);
        dataFrame.addRow(5,1);
        DataFrame df = dataFrame.iloc(1);
        System.out.println(df);
        System.out.println(dataFrame);
        SparseDataFrame sparseDataFrame = null;
        SparseDataFrame sparseDataFrame2 = null;

        try {
            sparseDataFrame = new SparseDataFrame(new String[]{"kol1","kol3","kol4","kol5"}, new String[] {"int", "int", "int", "int"}, 0);
            sparseDataFrame2 = new SparseDataFrame(new String[]{"s1","s2","s3"}, new String[]{"String","String","String"}, "XD");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sparseDataFrame.addRow(1,2,2,0);
        sparseDataFrame.addRow(2,3,2,1);
        sparseDataFrame.addRow(0,0,0,0);
        sparseDataFrame.addRow(2,0,12,3);
        sparseDataFrame.addRow(0,0,0,0);
        sparseDataFrame.addRow(2,555,222,11);


        System.out.println(sparseDataFrame);

        sparseDataFrame2.addRow("XD", "XD", "XD");
        sparseDataFrame2.addRow("X1D", "X2D", "X3D");
        sparseDataFrame2.addRow("X6D", "X5D", "X4D");
        sparseDataFrame2.addRow("X7D", "X8D", "X9D");
        sparseDataFrame2.addRow("XD", "XD", "XD");
        System.out.println(sparseDataFrame2);
        System.out.println(sparseDataFrame2.getColumn("s1"));
        System.out.println(sparseDataFrame.get(new String[]{"kol1","kol3"}, true));
        System.out.println(sparseDataFrame.iloc(1));
        System.out.println(sparseDataFrame.iloc(0,3));
        System.out.println(new SparseDataFrame(dataFrame, 1));
        System.out.println(sparseDataFrame2.toDense());
    }
}
