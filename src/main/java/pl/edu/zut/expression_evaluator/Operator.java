package pl.edu.zut.expression_evaluator;

public enum Operator {
    MULTIPLY("*", 1),
    DIVIDE("/", 1),
    ADD("+", 0),
    SUBTRACT("-", 0);

    private final String sign;
    private final int precedence;

    public static Operator valueOfSign(String sign) {
        for (var operator : Operator.values()) {
            if (operator.getSign().equals(sign)) {
                return operator;
            }
        }
        throw new IllegalArgumentException();
    }

    public String getSign() {
        return sign;
    }

    public int getPrecedence() {
        return precedence;
    }

    private Operator(String sign, int precedence) {
        this.sign = sign;
        this.precedence = precedence;
    }
}
