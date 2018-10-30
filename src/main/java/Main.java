import lab1.data.frame.DataFrame;
import lab2.SparseDataFrame;
import lab3.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DataFrame dataFrame = new DataFrame("/home/wadziuxxx/IdeaProjects/programowanie-obiektowe/src/main/resources/groupby.csv",
                                    new Class[]{StringValue.class, DateTimeValue.class, DoubleValue.class, DoubleValue.class});

        System.out.println(dataFrame.iloc(1,20));
    }
}
