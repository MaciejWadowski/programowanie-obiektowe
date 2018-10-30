import lab1.data.frame.DataFrame;
import lab3.DateTimeValue;
import lab3.DoubleValue;
import lab3.StringValue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DataFrame dataFrame = new DataFrame("/home/maciej/IdeaProjects/ProgramowanieObiektowe/src/main/resources/groupby.csv",
                                    new Class[]{StringValue.class, DateTimeValue.class, DoubleValue.class, DoubleValue.class});

        System.out.println(dataFrame.groupBy("id").max());
        System.out.println(dataFrame.groupBy("id").min());
    }
}
