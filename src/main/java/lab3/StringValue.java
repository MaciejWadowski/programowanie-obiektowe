package lab3;

public class StringValue extends Value {

    private String value;

    public StringValue(String value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Value add(Value value) {
        return new StringValue(this.value + value.toString());
    }

    @Override
    public Value sub(Value value) {
        return new StringValue(this.value.replace(value.toString(), ""));
    }

    @Override
    public Value mul(Value value) {
        if (value instanceof IntegerValue) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < ((IntegerValue) value).getValue(); i++) {
                stringBuilder.append(this.value);
            }
            return new StringValue(stringBuilder.toString());
        }
        return new StringValue(this.value);
    }

    @Override
    public Value div(Value value) {
        return new StringValue(this.value);
    }

    @Override
    public Value pow(Value value) {
        if (value instanceof IntegerValue) {
            return mul(value);
        }
        return new StringValue(this.value);
    }

    @Override
    public boolean eq(Value value) {
        return this.value.equals(value.toString());
    }

    @Override
    public boolean lte(Value value) {
        return this.value.compareTo(value.toString()) < 0;
    }

    @Override
    public boolean gte(Value value) {
        return this.value.compareTo(value.toString()) > 0;
    }

    @Override
    public boolean neq(Value value) {
        return !this.value.equals(value.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof StringValue) {
            return this.value.equals(other.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value clone() {
        return new StringValue(value);
    }

    @Override
    public Value create(String s) {
        return new StringValue(s);
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
