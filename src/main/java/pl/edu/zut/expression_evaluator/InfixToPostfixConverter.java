package pl.edu.zut.expression_evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Deque;

public class InfixToPostfixConverter {
    private static final String NUMBER_REGEX = "[0-9.+-]";
    private static String OPERATOR_REGEX;

    static {
        StringBuilder operatorRegexBuilder = new StringBuilder();
        operatorRegexBuilder.append("^[");
        for (var operator : Operator.values()) {
            operatorRegexBuilder.append(operator.getSign());
        }
        operatorRegexBuilder.append("]$");
        OPERATOR_REGEX = operatorRegexBuilder.toString();
    }

    private static InfixToPostfixConverter instance;

    public static InfixToPostfixConverter getInstance() {
        if (null == instance) {
            instance = new InfixToPostfixConverter();
        }
        return instance;
    }

    public List<String> getPostfixTokens(String infixExpression) {
        StringBuilder numberBuilder = new StringBuilder();
        List<String> postfixTokens = new ArrayList<>();
        Deque<Operator> operatorStack = new ArrayDeque<>();
        String previousSymbol = null;

        boolean isInNumber = false;

        for (var symbol : infixExpression.replaceAll("\\s", "").split("")) {
            if (symbol.matches(OPERATOR_REGEX) && !(null == previousSymbol || previousSymbol.matches(OPERATOR_REGEX))) {
                if (isInNumber) {
                    isInNumber = false;
                    postfixTokens.add(numberBuilder.toString());
                    numberBuilder = new StringBuilder();
                }
                Operator operator = Operator.valueOfSign(symbol);
                if (operatorStack.isEmpty() || operator.getPrecedence() > operatorStack.peekLast().getPrecedence()) {
                    operatorStack.offerLast(operator);
                } else {
                    while (!operatorStack.isEmpty() && operator.getPrecedence() <= operatorStack.peekLast().getPrecedence()) {
                        postfixTokens.add(operatorStack.pollLast().getSign());
                    }
                    operatorStack.offerLast(operator);
                }
            } else if (symbol.matches(NUMBER_REGEX)) {
                isInNumber = true;
                numberBuilder.append(symbol);
            } else {
                throw new IllegalArgumentException();
            }
            previousSymbol = symbol;
        }

        if (isInNumber) {
            postfixTokens.add(numberBuilder.toString());
        }
        while (!operatorStack.isEmpty()) {
            postfixTokens.add(operatorStack.pollLast().getSign());
        }

        return postfixTokens;
    }
}
