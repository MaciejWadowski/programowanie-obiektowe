package benchmark;

import lab1.data.frame.DataFrame;
import values.DateTimeValue;
import values.DoubleValue;
import values.StringValue;
import exceptions.ValueOperationException;
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
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Timeout(time = 1)
    public void normalGroupBy() throws ValueOperationException {
        DataFrame dataFrameResult = dataFrame.groupBy("id").mean();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Timeout(time = 1)
    public void concurrentGroupBy5() throws ValueOperationException {
        concurrentDataFrame.setMaximumThreadsConcurrently(5);
        DataFrame dataFrameResult = concurrentDataFrame.groupBy("id").mean();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Timeout(time = 1)
    public void concurrentGroupBy6() throws ValueOperationException {
        concurrentDataFrame.setMaximumThreadsConcurrently(6);
        DataFrame dataFrameResult = concurrentDataFrame.groupBy("id").mean();
    }


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Timeout(time = 1)
    public void concurrentGroupBy7() throws ValueOperationException {
        concurrentDataFrame.setMaximumThreadsConcurrently(7);
        DataFrame dataFrameResult = concurrentDataFrame.groupBy("id").mean();
    }


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Timeout(time = 1)
    public void concurrentGroupBy8() throws ValueOperationException {
        concurrentDataFrame.setMaximumThreadsConcurrently(8);
        DataFrame dataFrameResult = concurrentDataFrame.groupBy("id").mean();
    }


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Timeout(time = 1)
    public void concurrentGroupBy4() throws ValueOperationException {
        concurrentDataFrame.setMaximumThreadsConcurrently(4);
        DataFrame dataFrameResult = concurrentDataFrame.groupBy("id").mean();
    }


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Timeout(time = 1)
    public void concurrentGroupBy3() throws ValueOperationException {
        concurrentDataFrame.setMaximumThreadsConcurrently(3);
        DataFrame dataFrameResult = concurrentDataFrame.groupBy("id").mean();
    }


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Timeout(time = 1)
    public void concurrentGroupBy2() throws ValueOperationException {
        concurrentDataFrame.setMaximumThreadsConcurrently(2);
        DataFrame dataFrameResult = concurrentDataFrame.groupBy("id").mean();
    }

}
