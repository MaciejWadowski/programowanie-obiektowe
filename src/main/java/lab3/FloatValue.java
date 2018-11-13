package lab3;

public class FloatValue extends Value {

    private Float value;

    public FloatValue(float value) {
        this.value = value;
    }

    public FloatValue(String string) {
        value = Float.valueOf(string);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Value add(Value value) {
        if (value instanceof FloatValue) {
            Float val = (Float) value.getValue();
            return new FloatValue(this.value + val);
        }
        throw new IllegalArgumentException("");
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof FloatValue) {
            Float val = (Float) value.getValue();
            return new FloatValue(this.value - val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value mul(Value value) {
        if (value instanceof FloatValue) {
            Float val = (Float) value.getValue();
            return new FloatValue(this.value * val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value div(Value value) {
        if (value instanceof FloatValue) {
            Float val = (Float) value.getValue();
            if (val.equals(0.0)) {
                throw new IllegalArgumentException();
            }
            return new FloatValue(this.value / val);
        } else if (value instanceof IntegerValue) {
            int val = (int) value.getValue();
            return new FloatValue(this.value / val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Value pow(Value value) {
        if (value instanceof FloatValue) {
            float val = (float) value.getValue();
            double valDouble = val;
            double thisDouble = (double) this.value;
            return new FloatValue((float) Math.pow(thisDouble, valDouble));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof FloatValue) {
            Float val = (Float) value.getValue();
            return this.value.equals(val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof FloatValue) {
            Float val = (Float) value.getValue();
            return this.value < val;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof FloatValue) {
            Float val = (Float) value.getValue();
            return this.value > val;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof FloatValue) {
            Float val = (Float) value.getValue();
            return !this.value.equals(val);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FloatValue) {
            FloatValue floatValue = (FloatValue) other;
            return floatValue.value.equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value clone() {
        return new FloatValue(value);
    }

    @Override
    public Value create(String s) {
        return new FloatValue(Float.parseFloat(s));
    }

    @Override
    public Float getValue() {
        return value;
    }
}
