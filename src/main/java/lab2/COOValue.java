package lab2;

import lab3.Value;

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
        return this.value.add(value);
    }

    @Override
    public Value sub(Value value) {
        return this.value.sub(value);
    }

    @Override
    public Value mul(Value value) {
        return this.value.mul(value);
    }

    @Override
    public Value div(Value value) {
        return this.value.div(value);
    }

    @Override
    public Value pow(Value value) {
        return this.value.pow(value);
    }

    @Override
    public boolean eq(Value value) {
        return this.value.eq(value);
    }

    @Override
    public boolean lte(Value value) {
        return this.value.lte(value);
    }

    @Override
    public boolean gte(Value value) {
        return this.value.gte(value);
    }

    @Override
    public boolean neq(Value value) {
        return this.value.neq(value);
    }

    @Override
    public boolean equals(Object other) {
        return this.value.equals(other);
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
