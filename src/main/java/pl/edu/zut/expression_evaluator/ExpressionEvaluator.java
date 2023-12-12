package pl.edu.zut.expression_evaluator;

import java.math.BigDecimal;
import java.util.List;
import pl.edu.zut.expression_evaluator.postfix_notation_evaluator.PostfixEvaluator;

public class ExpressionEvaluator {
    private final InfixToPostfixConverter infixToPostfixConverter;
    private final PostfixEvaluator postfixEvaluator;

    private static ExpressionEvaluator instance;

    public static ExpressionEvaluator getInstance() {
        if (null == instance) {
            instance = new ExpressionEvaluator();
        }
        return instance;
    }

    public BigDecimal evaluate(String infixExpression) {
        List<String> postfixTokens = infixToPostfixConverter.getPostfixTokens(infixExpression);
        return postfixEvaluator.evaluate(postfixTokens);
    }

    private ExpressionEvaluator() {
        this.infixToPostfixConverter = InfixToPostfixConverter.getInstance();
        this.postfixEvaluator = PostfixEvaluator.getInstance();
    }
}
