package pl.edu.zut.expression_evaluator.postfix_notation_evaluator;

import static pl.edu.zut.expression_evaluator.Operator.*;

import pl.edu.zut.expression_evaluator.Operator;

import java.math.BigDecimal;
import java.util.List;

public class PostfixEvaluator {
    private static PostfixEvaluator instance;

    public static PostfixEvaluator getInstance() {
        if (null == instance) {
            instance = new PostfixEvaluator();
        }
        return instance;
    }

    public BigDecimal evaluate(List<String> postfixTokens) {
        return null;
    }

    private PostfixEvaluator() {}

    private BigDecimal performCalculation(BigDecimal leftOperand, BigDecimal rightOperand, Operator operator) {
        BigDecimal result = switch (operator) {
            case MULTIPLY -> leftOperand.multiply(rightOperand);
            case DIVIDE -> leftOperand.divide(rightOperand);
            case ADD -> leftOperand.add(rightOperand);
            case SUBTRACT -> leftOperand.subtract(rightOperand);
            default -> throw new IllegalArgumentException();
        };
        return result;
    }
}
