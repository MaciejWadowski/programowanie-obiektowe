package lab3;

public class IntegerValue extends Value {

    private Integer value;

    public IntegerValue(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public Value add(Value value) {
        if(value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return new IntegerValue(this.value + val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value sub(Value value) {
        if(value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return new IntegerValue(this.value - val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value mul(Value value) {
        if(value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return new IntegerValue(this.value * val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value div(Value value) {
        if(value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            if(val == 0) {
                throw new IllegalArgumentException();
            }
            return new IntegerValue(this.value / val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value pow(Value value) {
        if(value instanceof IntegerValue) {
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
        if(value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return val.equals(this.value);
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if(value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return val  < this.value;
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if(value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return val > this.value;
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if(value instanceof IntegerValue) {
            Integer val = (Integer) value.getValue();
            return val != this.value;
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return value.equals(other);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value create(String s) {
        return new IntegerValue(Integer.parseInt(s));
    }
}
