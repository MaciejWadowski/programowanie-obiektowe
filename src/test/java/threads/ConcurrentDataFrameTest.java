package threads;

import lab1.data.frame.Column;
import values.DateTimeValue;
import values.IntegerValue;
import values.StringValue;
import exceptions.ValueOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentDataFrameTest {


    private ConcurrentDataFrame dataFrame;
    private ConcurrentDataFrame dataFrame1;
    private ConcurrentDataFrame dataFrame2;

    @BeforeEach
    void setUp() throws ValueOperationException {
        dataFrame = new ConcurrentDataFrame(new String[]{"kol1", "kol2"}, new Class[]{StringValue.class, IntegerValue.class});
        dataFrame1 = new ConcurrentDataFrame(new String[]{"kol1", "kol2"}, new Class[]{IntegerValue.class, IntegerValue.class});
        dataFrame2 = new ConcurrentDataFrame(new String[]{"kol1", "kol2"}, new Class[]{IntegerValue.class, IntegerValue.class});
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
        assertThrows(ValueOperationException.class, () -> dataFrame.addRow());
    }

    @Test
    void shouldNotThrowException() {
        assertDoesNotThrow(() -> dataFrame.addRow(new StringValue("should add"), new IntegerValue(1)));
    }

    @Test
    void shouldNotAddColumnWithSameName() {
        //given
        ConcurrentDataFrame dataFrame = new ConcurrentDataFrame(new String[]{"kol1", "kol1", "kol1"}, new Class[]{IntegerValue.class, IntegerValue.class, IntegerValue.class});

        //when
        String[] names = dataFrame.getColumnNames();

        //then
        assertArrayEquals(new String[]{"kol1"}, names);
    }

    @Test
    void shouldAddValueToAllData() throws ValueOperationException {
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
    void shouldDivAllValues() throws ValueOperationException {
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
    void shouldMultiplyByWholeColumn() throws ValueOperationException {
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
    void shouldDivideByWholeColumn() throws ValueOperationException {
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
    void shouldAddWholeColumn() throws ValueOperationException {
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