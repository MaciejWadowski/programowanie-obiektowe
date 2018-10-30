package lab3;

public class DoubleValue extends Value {

    private Double value;

    public DoubleValue(double value) {
        super();
        this.value = value;
    }

    public DoubleValue(String string) {
        value = Double.valueOf(string);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Value add(Value value) {
        if (value instanceof DoubleValue) {
            return new DoubleValue(this.value + (double) value.getValue());
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof DoubleValue) {
            return new DoubleValue(this.value - (double) value.getValue());
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value mul(Value value) {
        if (value instanceof DoubleValue) {
            return new DoubleValue(this.value * (double) value.getValue());
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value div(Value value) {
        if (value instanceof DoubleValue) {
            double val = (double) value.getValue();
            if (val == 0.0) {
                throw new IllegalArgumentException();
            }
            return new DoubleValue(this.value / val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value pow(Value value) {
        if (value instanceof DoubleValue) {
            return new DoubleValue(Math.pow(this.value, (double) value.getValue()));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof DoubleValue) {
            this.value.equals(value.getValue());
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof DoubleValue) {
            return this.value.compareTo((Double) value.getValue()) < 0;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof DoubleValue) {
            return this.value.compareTo((Double) value.getValue()) > 0;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof DoubleValue) {
            this.value.equals(value.getValue());
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof DoubleValue) {
            DoubleValue doubleValue = (DoubleValue) other;
            return doubleValue.value.equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public Value clone() {
        return new DoubleValue(value);
    }

    @Override
    public Value create(String s) {
        return new DoubleValue(Double.parseDouble(s));
    }

    @Override
    public Double getValue() {
        return this.value;
    }
}
