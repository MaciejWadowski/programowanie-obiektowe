import lab1.data.frame.DataFrame;
import lab3.DateTimeValue;
import lab3.FloatValue;
import lab3.StringValue;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        DataFrame dataFrame = new DataFrame("/home/maciej/IdeaProjects/ProgramowanieObiektowe/src/main/resources/groupby.csv",
                                    new Class[]{StringValue.class, DateTimeValue.class, FloatValue.class, FloatValue.class});
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
        startTime = System.currentTimeMillis();
        System.out.println(dataFrame.groupBy("id").std());
        endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
    }
}
