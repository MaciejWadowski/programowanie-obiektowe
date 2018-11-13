package lab1.data.frame;

import lab3.DateTimeValue;
import lab3.IntegerValue;
import lab3.StringValue;
import lab5.ValueOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataFrameTest {

    private DataFrame dataFrame;
    private DataFrame dataFrame1;
    private DataFrame dataFrame2;

    @BeforeEach
    void setUp() {
        dataFrame = new DataFrame(new String[]{"kol1", "kol2"}, new Class[]{StringValue.class, IntegerValue.class});
        dataFrame1 = new DataFrame(new String[]{"kol1", "kol2"}, new Class[]{IntegerValue.class, IntegerValue.class});
        dataFrame2 = new DataFrame(new String[]{"kol1", "kol2"}, new Class[]{IntegerValue.class, IntegerValue.class});
        for (int i = 0; i <= 10; i++) {
            dataFrame.addRow(new StringValue(i + "" + "a"), new IntegerValue(i));
        }
    }

    @Test
    void shouldThrowExceptionForInvalidType() {
        assertThrows(ValueOperationException.class, () -> dataFrame.addRow(new DateTimeValue("1992-11-12"), new IntegerValue(1)));
    }

    @Test
    void shouldNotAddRow() {
        assertFalse(dataFrame.addRow());
    }

    @Test
    void shouldNotThrowException() {
        assertDoesNotThrow(() -> dataFrame.addRow(new StringValue("should add"), new IntegerValue(1)));
    }

    @Test
    void shouldReturnDataFrameSpecifiedRow() {
        //when
        DataFrame dataFrame = this.dataFrame.iloc(1);
        //then
        assertEquals(dataFrame.toString(), "kol1\t\tkol2\t\t\n" +
                "1a\t1\t\n");
    }

    @Test
    void shouldReturnDataFrameWithSpecifiedRows() {
        //when
        DataFrame dataFrame = this.dataFrame.iloc(2, 3);
        //then
        assertEquals(dataFrame.toString(), "kol1\t\tkol2\t\t\n" +
                "2a\t2\t\n" +
                "3a\t3\t\n");
    }

    @Test
    void shouldNotAddColumnWithSameName() {
        //given
        DataFrame dataFrame = new DataFrame(new String[]{"kol1", "kol1", "kol1"}, new Class[]{IntegerValue.class, IntegerValue.class, IntegerValue.class});

        //when
        String[] names = dataFrame.getColumnNames();

        //then
        assertArrayEquals(new String[]{"kol1"}, names);
    }

    @Test
    void shouldAddValueToAllData() {
        //given
        for (int i = 0; i < 5; i++) {
            dataFrame1.addRow(new IntegerValue(i), new IntegerValue(i - 1));
            dataFrame2.addRow(new IntegerValue(i + 5), new IntegerValue(i + 4));
        }

        //when
        dataFrame1.add(new IntegerValue(5));

        //then
        assertEquals(dataFrame2, dataFrame1);
    }

    @Test
    void shouldDivAllValues() {
        //given
        for (int i = 0; i < 5; i++) {
            dataFrame1.addRow(new IntegerValue(i), new IntegerValue(i - 1));
            dataFrame2.addRow(new IntegerValue(i/2), new IntegerValue((i - 1)/2));
        }

        //when
        dataFrame1.div(new IntegerValue(2));

        //then
        assertEquals(dataFrame2, dataFrame1);
    }

    @Test
    void shouldMultiplyByWholeColumn() {
        //given
        Column column = new Column("column", IntegerValue.class);
        for (int i = 0; i < 5; i++) {
            dataFrame1.addRow(new IntegerValue(i), new IntegerValue(i - 1));
            column.addElement(new IntegerValue(i));
            dataFrame2.addRow(new IntegerValue(i * i), new IntegerValue((i - 1) * i));
        }

        //when
        dataFrame1.mul(column);

        //then
        assertEquals(dataFrame2, dataFrame1);
    }

    @Test
    void shouldDivideByWholeColumn() {
        //given
        Column column = new Column("column", IntegerValue.class);
        for (int i = 0; i < 5; i++) {
            dataFrame1.addRow(new IntegerValue(i), new IntegerValue(i - 1));
            column.addElement(new IntegerValue( i * 2 + 1));
            dataFrame2.addRow(new IntegerValue(i / (i *2 + 1)), new IntegerValue((i - 1) / (i * 2 + 1)));
        }

        //when
        dataFrame1.div(column);

        //then
        assertEquals(dataFrame2, dataFrame1);
    }

    @Test
    void shouldAddWholeColumn() {
        //given
        Column column = new Column("column", IntegerValue.class);
        for (int i = 0; i < 5; i++) {
            dataFrame1.addRow(new IntegerValue(i), new IntegerValue(i - 1));
            column.addElement(new IntegerValue(i));
            dataFrame2.addRow(new IntegerValue(i + i), new IntegerValue(i + i - 1));
        }

        //when
        dataFrame1.add(column);

        //then
        assertEquals(dataFrame2, dataFrame1);
    }
}