import data.base.DataFrameDB;
import lab1.data.frame.DataFrame;
import values.DateTimeValue;
import values.DoubleValue;
import values.StringValue;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        //   org.openjdk.jmh.Main.main(args);
        DataFrameDB dataFrameDB = new DataFrameDB("test",
                "/home/maciej/IdeaProjects/ProgramowanieObiektowe/src/main/resources/csv/groupby.csv",
                new Class[]{StringValue.class, DateTimeValue.class, DoubleValue.class, DoubleValue.class},
                "jdbc:mysql://mysql.agh.edu.pl/mwadowsk",
                "mwadowsk",
                "oJG5kEujkudNMySv");
        DataFrame dataFrame = dataFrameDB.groupBy(new String[]{"id"}).max();
        DataFrame dataFrame1 = dataFrameDB.groupBy(new String[]{"id"}).sum();
        File file = dataFrame.convertToCSV("outputmax.csv");
        File file2 = dataFrame1.convertToCSV("outputsum.csv");
    }
}
