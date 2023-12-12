package pl.edu.zut.expression_evaluator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

public class InfixToPostfixConverter {
    private static final String ILLEGAL_ARGUMENT_MSG = "Exprssion contains an unsupported symbol: %s";
    private static final Pattern NUMBER_REGEX = Pattern.compile("^[0-9.+-]$");
    private static final Pattern OPERATOR_REGEX;

    private static InfixToPostfixConverter instance;

    static {
        StringBuilder operatorRegexBuilder = new StringBuilder();
        operatorRegexBuilder.append("^[");
        for (var operator : Operator.values()) {
            operatorRegexBuilder.append(operator.getSign());
        }
        operatorRegexBuilder.append("]$");
        OPERATOR_REGEX = Pattern.compile(operatorRegexBuilder.toString());
    }

    public static InfixToPostfixConverter getInstance() {
        if (null == instance) {
            instance = new InfixToPostfixConverter();
        }
        return instance;
    }

    public List<String> getPostfixTokens(String infixExpression) {
        boolean isInNumber = false;
        StringBuilder numberBuilder = new StringBuilder();
        Deque<Operator> operatorStack = new ArrayDeque<>();
        List<String> postfixTokens = new ArrayList<>();
        String previousSymbol = null;

        for (var symbol : infixExpression.replaceAll("\\s", "").split("")) {
            if (OPERATOR_REGEX.matcher(symbol).find() && !(null == previousSymbol || OPERATOR_REGEX.matcher(previousSymbol).find())) {
                if (isInNumber) {
                    isInNumber = false;
                    postfixTokens.add(numberBuilder.toString());
                    numberBuilder.setLength(0);
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
            } else if (NUMBER_REGEX.matcher(symbol).find()) {
                isInNumber = true;
                numberBuilder.append(symbol);
            } else {
                throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENT_MSG, symbol));
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

    private InfixToPostfixConverter() {}
}
