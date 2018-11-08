import lab1.data.frame.Column;
import lab3.IntegerValue;

public class Main {
    public static void main(String[] args) {
//        DataFrame dataFrame = new DataFrame("/home/maciej/IdeaProjects/ProgramowanieObiektowe/src/main/resources/groupby.csv",
//                                    new Class[]{StringValue.class, DateTimeValue.class, FloatValue.class, FloatValue.class});
//
//        System.out.println(dataFrame.groupBy("id").apply(new Median()));

        Column column = new Column("column", IntegerValue.class);
        for (int i = 0; i <= 10; i++) {
            column.addElement(new IntegerValue(i));
        }
        System.out.println(column);
        System.out.println(column.getMax());
        System.out.println(column.getMin());

    }
}
