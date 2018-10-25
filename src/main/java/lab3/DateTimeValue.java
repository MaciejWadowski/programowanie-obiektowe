package lab3;

import java.util.Date;

@SuppressWarnings("deprecation")
public class DateTimeValue extends Value {
    private Date date;

    public DateTimeValue(Date date) {
        this.date = date;
    }

    public DateTimeValue(String string) {
        date = new Date(string);
    }

    @Override
    public String toString() {
        return date.toString();
    }

    @Override
    public Value add(Value value) {
        if (value instanceof DateTimeValue) {
            Date valueDate = (Date) value.getValue();
            return new DateTimeValue(new Date(this.date.getYear(),
                    this.date.getMonth(),
                    this.date.getDay(),
                    this.date.getHours() + valueDate.getHours(),
                    this.date.getMinutes() + valueDate.getMinutes()));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof DateTimeValue) {
            Date valueDate = (Date) value.getValue();
            return new DateTimeValue(new Date(this.date.getYear(),
                    this.date.getMonth(),
                    this.date.getDay(),
                    this.date.getHours() - valueDate.getHours(),
                    this.date.getMinutes() - valueDate.getMinutes()));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value mul(Value value) {
        return new DateTimeValue(this.date);
    }

    @Override
    public Value div(Value value) {
        return new DateTimeValue(this.date);
    }

    @Override
    public Value pow(Value value) {
        return new DateTimeValue(this.date);
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof DateTimeValue) {
            return this.date.compareTo((Date) value.getValue()) == 0;
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof DateTimeValue) {
            return this.date.compareTo((Date) value.getValue()) < 0;
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof DateTimeValue) {
            return this.date.compareTo((Date) value.getValue()) > 0;
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof DateTimeValue) {
            return this.date.compareTo((Date) value.getValue()) != 0;
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return this.date.hashCode();
    }

    @Override
    public Value create(String s) {
        return new DateTimeValue(new Date(Date.parse(s)));
    }

    @Override
    public Date getValue() {
        return this.date;
    }
}
