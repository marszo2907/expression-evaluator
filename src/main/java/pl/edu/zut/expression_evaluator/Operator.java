package pl.edu.zut.expression_evaluator;

public enum Operator {
    LEFT_PARENTHESIS(Integer.MAX_VALUE, "("),
    RIGHT_PARENTHESIS(Integer.MAX_VALUE, ")"),
    MULTIPLY(1, "*"),
    DIVIDE(1, "/"),
    ADD(0, "+"),
    SUBTRACT(0, "-");

    private static final String OPERATOR_NOT_SUPPORTED_MSG = "Operator %s is not supported.";

    private final int precedence;
    private final String sign;

    public static Operator valueOfSign(String sign) {
        for (var operator : Operator.values()) {
            if (operator.getSign().equals(sign)) {
                return operator;
            }
        }
        throw new IllegalArgumentException(String.format(OPERATOR_NOT_SUPPORTED_MSG, sign));
    }

    public int getPrecedence() {
        return precedence;
    }

    public String getSign() {
        return sign;
    }

    private Operator(int precedence, String sign) {
        this.precedence = precedence;
        this.sign = sign;
    }
}
