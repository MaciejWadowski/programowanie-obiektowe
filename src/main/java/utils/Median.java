package utils;

import lab1.data.frame.DataFrame;
import exceptions.ValueOperationException;

public class Median implements Applyable {
    @Override
    public DataFrame apply(DataFrame dataFrame) {
        DataFrame outputDataFrame = new DataFrame(dataFrame.getColumnNames(), dataFrame.getClasses());

        try {
            outputDataFrame.addRow((dataFrame.size() % 2 == 0)
                                    ? dataFrame.getRow(dataFrame.size() / 2)
                                    : dataFrame.getRow(dataFrame.size() / 2 + 1));
        } catch (ValueOperationException e) {
            e.printStackTrace();
        }

        return outputDataFrame;
    }
}
