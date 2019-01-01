package lab2;

import values.Value;

public class COOValue extends Value {

    private Value value;
    private int index;

    public COOValue(Value value, int index) {
        this.value = value;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Value add(Value value) {
        if (value instanceof COOValue) {
            return this.value.add(((COOValue) value).getValue());
        }
        return this.value.add(value);
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof COOValue) {
            return this.value.sub(((COOValue) value).getValue());
        }
        return this.value.sub(value);
    }

    @Override
    public Value mul(Value value) {
        if (value instanceof COOValue) {
            return this.value.mul(((COOValue) value).getValue());
        }
        return this.value.mul(value);
    }

    @Override
    public Value div(Value value) {
        if (value instanceof COOValue) {
            return this.value.div(((COOValue) value).getValue());
        }
        return this.value.div(value);
    }

    @Override
    public Value pow(Value value) {
        if (value instanceof COOValue) {
            return this.value.pow(((COOValue) value).getValue());
        }
        return this.value.pow(value);
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof COOValue) {
            return this.value.eq(((COOValue) value).getValue());
        }
        return this.value.eq(value);
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof COOValue) {
            return this.value.lte(((COOValue) value).getValue());
        }
        return this.value.lte(value);
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof COOValue) {
            return this.value.gte(((COOValue) value).getValue());
        }
        return this.value.gte(value);
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof COOValue) {
            return this.value.neq(((COOValue) value).getValue());
        }
        return this.value.neq(value);
    }

    @Override
    public boolean equals(Object other) {
        if (value instanceof COOValue) {
            return this.value.equals(((COOValue) value).getValue()) && this.index == ((COOValue) value).getIndex();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public Value clone() {
        return new COOValue(value.clone(), index);
    }

    @Override
    public Value create(String s) {
        return this.value.create(s);
    }
}
