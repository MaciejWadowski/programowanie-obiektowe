package values;

public class IntegerValue extends Value {

    private Integer value;

    public IntegerValue(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public IntegerValue(String string) {
        if (string.contains(".")) {
            value = Integer.valueOf(string.substring(0, string.indexOf(".")));
        }
        value = Integer.valueOf(string);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public Value add(Value value) {
        if (value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return new IntegerValue(this.value + val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return new IntegerValue(this.value - val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value mul(Value value) {
        if (value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return new IntegerValue(this.value * val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value div(Value value) {
        if (value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            if (val == 0) {
                throw new IllegalArgumentException();
            }
            return new IntegerValue(this.value / val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value pow(Value value) {
        if (value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            Integer output = 1;
            for (int i = 0; i < val; i++) {
                output *= this.value;
            }
            return new IntegerValue(output);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return val.equals(this.value);
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return val < this.value;
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return val > this.value;
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return !val.equals(this.value);
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IntegerValue) {
            IntegerValue integerValue = (IntegerValue) other;
            return integerValue.value.equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value clone() {
        return new IntegerValue(value);
    }

    @Override
    public Value create(String s) {
        return new IntegerValue(Integer.parseInt(s));
    }
}
