package pl.edu.zut.expression_evaluator.postfix_notation_evaluator;

import static pl.edu.zut.expression_evaluator.Operator.*;

import pl.edu.zut.expression_evaluator.Operator;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

public class PostfixEvaluator {
    private static PostfixEvaluator instance;

    public static PostfixEvaluator getInstance() {
        if (null == instance) {
            instance = new PostfixEvaluator();
        }
        return instance;
    }

    public BigDecimal evaluate(List<String> tokens) {
        Deque<BigDecimal> operandStack = new ArrayDeque<>();
        Pattern operatorPattern = Pattern.compile(getOperatorRegex());

        for (var token : tokens) {
            if (operatorPattern.matcher(token).matches()) {
                BigDecimal rightOperand = operandStack.remove();
                BigDecimal leftOperand = operandStack.remove();
                Operator operator = Operator.valueOfSign(token);
                operandStack.offer(performCalculation(leftOperand, rightOperand, operator));
            } else {
                operandStack.offer(new BigDecimal(token));
            }
        }

        return operandStack.remove();
    }

    private PostfixEvaluator() {}

    private BigDecimal performCalculation(BigDecimal leftOperand, BigDecimal rightOperand, Operator operator) {
        return switch (operator) {
            case MULTIPLY -> leftOperand.multiply(rightOperand);
            case DIVIDE -> leftOperand.divide(rightOperand);
            case ADD -> leftOperand.add(rightOperand);
            case SUBTRACT -> leftOperand.subtract(rightOperand);
            default -> throw new IllegalArgumentException();
        };
    }

    private String getOperatorRegex() {
        StringBuilder regexBuilder = new StringBuilder();
        regexBuilder.append("^[");
        for (var operator : Operator.values()) {
            regexBuilder.append(operator.getSign());
        }
        regexBuilder.append("]$");
        return regexBuilder.toString();
    }
}
