package lab4;

import lab1.data.frame.DataFrame;

public class Median implements Applyable {
    @Override
    public DataFrame apply(DataFrame dataFrame) {
        DataFrame outputDataFrame = new DataFrame(dataFrame.getColumnNames(), dataFrame.getClasses());

        outputDataFrame.addRow((dataFrame.size() % 2 == 0)
                                ? dataFrame.getRow(dataFrame.size() / 2)
                                : dataFrame.getRow(dataFrame.size() / 2 + 1));

        return outputDataFrame;
    }
}
