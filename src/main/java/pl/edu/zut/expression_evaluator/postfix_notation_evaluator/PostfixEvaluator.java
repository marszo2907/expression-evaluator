package pl.edu.zut.expression_evaluator.postfix_notation_evaluator;

import static java.math.RoundingMode.HALF_UP;
import static pl.edu.zut.expression_evaluator.Operator.*;

import pl.edu.zut.expression_evaluator.Operator;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

public class PostfixEvaluator {
    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Exprssion contains too many operands.";
    private static final Pattern OPERATOR_REGEX;
    private static final String UNSUPPORTED_OPERATION_MESSAGE = "%s is not supported.";

    private static PostfixEvaluator instance;

    static {
        StringBuilder operatorRegexBuilder = new StringBuilder();
        operatorRegexBuilder.append("^[");
        for (var operator : Operator.values()) {
            operatorRegexBuilder.append(operator.getSign());
        }
        operatorRegexBuilder.append("]$");
        OPERATOR_REGEX = Pattern.compile(operatorRegexBuilder.toString());
    }

    public static PostfixEvaluator getInstance() {
        if (null == instance) {
            instance = new PostfixEvaluator();
        }
        return instance;
    }

    public BigDecimal evaluate(List<String> tokens) {
        Deque<BigDecimal> operandStack = new ArrayDeque<>();

        for (var token : tokens) {
            if (OPERATOR_REGEX.matcher(token).matches()) {
                BigDecimal rightOperand = operandStack.remove();
                BigDecimal leftOperand = operandStack.remove();
                Operator operator = Operator.valueOfSign(token);
                operandStack.offerFirst(performCalculation(leftOperand, rightOperand, operator));
            } else {
                operandStack.offerFirst(new BigDecimal(token));
            }
        }
        if (1 != operandStack.size()) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
        }

        return operandStack.remove();
    }

    private PostfixEvaluator() {}

    private BigDecimal performCalculation(BigDecimal leftOperand, BigDecimal rightOperand, Operator operator) {
        BigDecimal value;

        switch (operator) {
            case MULTIPLY -> value = leftOperand.multiply(rightOperand);
            case DIVIDE -> {
                try {
                    value = leftOperand.divide(rightOperand);
                } catch (ArithmeticException e) {
                    value = leftOperand.divide(rightOperand, 10, HALF_UP);
                }
            }
            case ADD -> value = leftOperand.add(rightOperand);
            case SUBTRACT -> value = leftOperand.subtract(rightOperand);
            default -> throw new UnsupportedOperationException(String.format(UNSUPPORTED_OPERATION_MESSAGE, operator.getSign()));
        };

        return value;
    }
}
