import threads.ConcurrentDataFrame;
import values.DateTimeValue;
import values.DoubleValue;
import values.IntegerValue;
import values.StringValue;

public class Main {

    public static final String FILE = "/home/maciej/Desktop/large_groupby.csv";

    public static final Class[] CLASSES = { IntegerValue.class, DateTimeValue.class, StringValue.class, DoubleValue.class, DoubleValue.class};

    public static void main(String[] args) throws Exception {

        //DataFrame dataFrame = new DataFrame(FILE, CLASSES);
        ConcurrentDataFrame concurrentDataFrame = new ConcurrentDataFrame(FILE, CLASSES);

        long start = System.currentTimeMillis();
        concurrentDataFrame.groupBy("date").min();
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        concurrentDataFrame.groupBy("date").max();
        end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        concurrentDataFrame.groupBy("date").sum();
        end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        concurrentDataFrame.groupBy("date").mean();
        end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        concurrentDataFrame.groupBy("date").var();
        end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        concurrentDataFrame.groupBy("date").std();
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
