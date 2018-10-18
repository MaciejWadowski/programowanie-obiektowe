import lab2.SparseDataFrame;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SparseDataFrame dataFrame = new SparseDataFrame("src/main/resources/sparse.csv",new String[] {"double", "double", "double"}, 0.);
        System.out.println(dataFrame.iloc(2, 30));

    }
}
