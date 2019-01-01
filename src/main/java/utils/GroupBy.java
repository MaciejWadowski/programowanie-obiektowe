package utils;

import lab1.data.frame.DataFrame;
import exceptions.ValueOperationException;

public interface GroupBy {
    DataFrame max() throws ValueOperationException;
    DataFrame min() throws ValueOperationException;
    DataFrame mean() throws ValueOperationException;
    DataFrame std() throws ValueOperationException;
    DataFrame sum() throws ValueOperationException;
    DataFrame var() throws ValueOperationException;
    DataFrame apply(Applyable applyable) throws ValueOperationException;
}
