package lab1.data.frame;

import lab3.DateTimeValue;
import lab3.DoubleValue;
import lab3.IntegerValue;
import lab3.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataFrameTest {

    private DataFrame dataFrame;

     @BeforeEach
     void setUp() {
         dataFrame = new DataFrame(new String[] {"kol1", "kol2"}, new Class[]{StringValue.class, IntegerValue.class});
         for (int i = 0; i <= 10; i++) {
             dataFrame.addRow(new StringValue(i + "" + "a"), new IntegerValue(i));
         }
     }

    @Test
    void shouldThrowExceptionForInvalidType() {
        assertThrows(IllegalArgumentException.class,() -> dataFrame.addRow(new DateTimeValue("1992-11-12"), new IntegerValue(1)));
    }

    @Test
    void shoulThrowExceptionForInvalidNumberOfArgumets() {
        assertThrows(IndexOutOfBoundsException.class, () -> dataFrame.addRow());
    }

    @Test
    void shouldNotThrowException() {
         assertDoesNotThrow(() -> dataFrame.addRow(new StringValue("should add"), new IntegerValue(1)));
    }

    @Test
    void shouldReturnDataFraneSpecifiedRow() {
         DataFrame dataFrame = this.dataFrame.iloc(1);
         assertEquals(dataFrame.toString(), "kol1\t\tkol2\t\t\n" +
                 "1a\t1\t\n");
    }

    @Test
    void shouldReturnDataFrameWithSpecifiedRows() {
        DataFrame dataFrame = this.dataFrame.iloc(2,3);
        assertEquals(dataFrame.toString(), "kol1\t\tkol2\t\t\n" +
                "2a\t2\t\n" +
                "3a\t3\t\n");
    }
}