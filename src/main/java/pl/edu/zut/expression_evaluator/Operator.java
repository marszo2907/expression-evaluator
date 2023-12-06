package pl.edu.zut.expression_evaluator;

public enum Operator {
    MULTIPLY("*", 1),
    DIVIDE("/", 1),
    PLUS("+", 0),
    MINUS("-", 0);

    private final String sign;
    private final int precedence;

    private Operator(String sign, int precedence) {
        this.sign = sign;
        this.precedence = precedence;
    }
}
