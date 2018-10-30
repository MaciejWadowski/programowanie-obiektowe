package lab4;

import lab1.data.frame.DataFrame;

public interface GroupBy {
    DataFrame max();
    DataFrame min();
    DataFrame mean();
    DataFrame std();
    DataFrame sum();
    DataFrame var();
    DataFrame apply(Applyable applyable);
}
