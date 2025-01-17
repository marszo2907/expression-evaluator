package pl.edu.zut.expression_evaluator;

public enum Operator {
    LEFT_PARENTHESIS(Integer.MAX_VALUE, '('),
    RIGHT_PARENTHESIS(Integer.MAX_VALUE, ')'),
    MULTIPLICATION(1, '*'),
    DIVISION(1, '/'),
    ADDITION(0, '+'),
    SUBTRACTION(0, '-');

    private static final String OPERATOR_NOT_SUPPORTED_MSG = "Operator %c is not supported.";

    private final int precedence;
    private final char sign;

    public static Operator valueOf(char sign) {
        for (var operator : Operator.values()) {
            if (operator.getSign() == sign) {
                return operator;
            }
        }
        throw new IllegalArgumentException(String.format(OPERATOR_NOT_SUPPORTED_MSG, sign));
    }

    public int getPrecedence() {
        return precedence;
    }

    public char getSign() {
        return sign;
    }

    private Operator(int precedence, char sign) {
        this.precedence = precedence;
        this.sign = sign;
    }
}
