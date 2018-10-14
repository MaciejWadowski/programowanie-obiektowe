import lab1.data.frame.DataFrame;
import lab2.SparseDataFrame;

public class Main {
    public static void main(String[] args) {
        try {
            SparseDataFrame sparseDataFrame = new SparseDataFrame(new String[]{"kol1", "kol2", "kol3"}, new String[]{"int","int","int"}, 0);
            sparseDataFrame.addRow(12,12,14);
            sparseDataFrame.addRow(0,0,0);
            sparseDataFrame.addRow(0,0,0);
            sparseDataFrame.addRow(12,12,14);
            sparseDataFrame.addRow(11,112,1411);
            sparseDataFrame.addRow(0,0,0);
            sparseDataFrame.addRow(12,12,14);
            DataFrame dataFrame = sparseDataFrame.toDense();
            System.out.println(dataFrame);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
