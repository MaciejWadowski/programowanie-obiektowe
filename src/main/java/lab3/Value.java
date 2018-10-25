package lab3;

public abstract class Value {

    public abstract String toString();

    public abstract Value add(Value value);

    public abstract Value sub(Value value);

    public abstract Value mul(Value value);

    public abstract Value div(Value value);

    public abstract Value pow(Value value);

    public abstract boolean eq(Value value);

    public abstract boolean lte(Value value);

    public abstract boolean gte(Value value);

    public abstract boolean neq(Value value);

    public abstract boolean equals(Object other);

    public abstract int hashCode();

    public abstract Value create(String s);

    public static Value parse(String s, Class<? extends Value> clazz) {
        if (clazz == IntegerValue.class) {
            return new IntegerValue(s);
        } else if (clazz == StringValue.class) {
            return new StringValue(s);
        } else if (clazz == FloatValue.class) {
            return new FloatValue(s);
        } else if (clazz == DoubleValue.class) {
            return new DoubleValue(s);
        } else if (clazz == DateTimeValue.class) {
            return new DateTimeValue(s);
        }

        throw new IllegalArgumentException();
    }

    public abstract Object getValue();
}
