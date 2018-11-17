package lab1.data.frame;

import lab3.DoubleValue;
import lab3.IntegerValue;
import lab3.Value;
import lab5.InvalidColumnSizeException;
import lab5.ValueOperationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class ColumnTest {

    private Column column;

    @BeforeEach
    void setUp() {
        column = new Column("column", IntegerValue.class);
        IntStream.rangeClosed(0, 10).forEach(i -> {
            try {
                column.addElement(new IntegerValue(i));
            } catch (ValueOperationException e) {
                e.printStackTrace();
            }
        });
    }


    @Test
    void shouldNotAcceptDifferentType() {
        Assertions.assertThrows(ValueOperationException.class, () -> column.addElement(new DoubleValue(0.0)));
    }

    @Test
    void shouldAcceptColumnType() {
        Assertions.assertDoesNotThrow(() -> column.addElement(new IntegerValue(2)));
    }

    @Test
    void shouldCalculateMax() {
        //when
        Value value = column.getMax();

        //then
        Assertions.assertEquals(new IntegerValue(10), value);
    }

    @Test
    void shouldCalculateMin() {
        //when
        Value value = column.getMin();

        //then
        Assertions.assertEquals(new IntegerValue(0), value);
    }

    @Test
    void shouldCalculateMean() {
        //when
        Value value = column.getMean();

        //then
        Assertions.assertEquals(new IntegerValue(5), value);
    }

    @Test
    void shouldCalculateStd() throws ValueOperationException {
        //given
        Column column = new Column("doubleClass", DoubleValue.class);
        for (int i = 0; i <= 10; i++) {
            column.addElement(new DoubleValue((double) i));
        }

        //when
        Value value = column.getStd();

        //then
        Assertions.assertEquals(new DoubleValue(3.1622776601683795), value);
    }

    @Test
    void shouldCalculateVar() {
        //when
        Value value = column.getVar();

        //then
        Assertions.assertEquals(new IntegerValue(10), value);
    }

    @Test
    void shouldCloneProperly() {
        //given
        Column column1 = column.clone();

        //then
        Assertions.assertNotSame(column, column1);
    }

    @Test
    void shouldThrowInvalidColumnSizeExceptionWhenAdd() {
        //given
        Column column1 = new Column("something", IntegerValue.class);

        //then
        Assertions.assertThrows(InvalidColumnSizeException.class, () -> column1.add(column));
    }

    @Test
    void shouldThrowInvalidColumnSizeExceptionWhenMultiply() {
        //given
        Column column1 = new Column("something", IntegerValue.class);

        //then
        Assertions.assertThrows(InvalidColumnSizeException.class, () -> column1.mul(column));
    }
}