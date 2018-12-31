package benchmark;

import lab1.data.frame.DataFrame;
import lab3.DateTimeValue;
import lab3.DoubleValue;
import lab3.StringValue;
import lab5.ValueOperationException;
import org.openjdk.jmh.annotations.*;
import threads.ConcurrentDataFrame;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class BenchmarkCreator {

    private DataFrame dataFrame;

    private ConcurrentDataFrame concurrentDataFrame;

    @Setup
    public void setup() throws Exception {
        dataFrame
                = new DataFrame("/home/maciej/IdeaProjects/ProgramowanieObiektowe/src/main/resources/csv/groupby.csv",
                new Class[]{StringValue.class, DateTimeValue.class, DoubleValue.class, DoubleValue.class});
        concurrentDataFrame
                = new ConcurrentDataFrame("/home/maciej/IdeaProjects/ProgramowanieObiektowe/src/main/resources/csv/groupby.csv",
                new Class[]{StringValue.class, DateTimeValue.class, DoubleValue.class, DoubleValue.class});
    }


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1, time = 32, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 2, time = 300, timeUnit = TimeUnit.MILLISECONDS)
    @Timeout(time = 1)
    public void normalGroupBy() throws ValueOperationException {
        DataFrame dataFrameResult = dataFrame.groupBy("id").mean();
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1, time = 32, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 2, time = 300, timeUnit = TimeUnit.MILLISECONDS)
    @Timeout(time = 1)
    public void concurrentGroupBy() throws ValueOperationException {
        DataFrame dataFrameResult = concurrentDataFrame.groupBy("id").mean();
    }
}
