import lab1.data.frame.DataFrame;
import lab3.DateTimeValue;
import lab3.FloatValue;
import lab3.StringValue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DataFrame dataFrame = new DataFrame("/home/maciej/IdeaProjects/ProgramowanieObiektowe/src/main/resources/groubymulti.csv",
                                    new Class[]{StringValue.class, DateTimeValue.class, FloatValue.class, FloatValue.class});

        System.out.println(dataFrame.groupBy("id","date").std());
    }
}
