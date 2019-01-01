package values;

import java.time.LocalDate;

public class DateTimeValue extends Value {
    private LocalDate date;

    public DateTimeValue(LocalDate date) {
        this.date = date;
    }

    public DateTimeValue(String string) {
        date = LocalDate.parse(string);
    }

    public DateTimeValue() {
    }

    @Override
    public String toString() {
        if (date == null) {
            return "null";
        }
        return date.toString();
    }

    @Override
    public Value add(Value value) {
        if (value instanceof DateTimeValue) {
            LocalDate valueDate = (LocalDate) value.getValue();
            return new DateTimeValue(date.plusYears(valueDate.getYear()).plusDays(valueDate.getDayOfMonth()).plusMonths(valueDate.getMonthValue()));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof DateTimeValue) {
            LocalDate valueDate = (LocalDate) value.getValue();
            return new DateTimeValue(date.minusYears(valueDate.getYear()).minusDays(valueDate.getDayOfMonth()).minusMonths(valueDate.getMonthValue()));
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
            return this.date.compareTo((LocalDate) value.getValue()) == 0;
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof DateTimeValue) {
            return this.date.compareTo((LocalDate) value.getValue()) < 0;
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof DateTimeValue) {
            return this.date.compareTo((LocalDate) value.getValue()) > 0;
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof DateTimeValue) {
            return this.date.compareTo((LocalDate) value.getValue()) != 0;
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DateTimeValue) {
            DateTimeValue dateTimeValue = (DateTimeValue) other;
            return dateTimeValue.date.equals(date);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.date.hashCode();
    }

    @Override
    public Value clone() {
        return new DateTimeValue(date);
    }

    @Override
    public Value create(String s) {
        return new DateTimeValue(s);
    }

    @Override
    public LocalDate getValue() {
        return this.date;
    }
}
