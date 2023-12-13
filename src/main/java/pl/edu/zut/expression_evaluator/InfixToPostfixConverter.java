package pl.edu.zut.expression_evaluator;

import static pl.edu.zut.expression_evaluator.Operator.LEFT_PARENTHESIS;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

public class InfixToPostfixConverter {
    private static final String ILLEGAL_ARGUMENT_MSG = "Exprssion contains an unsupported symbol or a combination of symbols: %s.";
    private static final String INVALID_PARENTHESIS_MSG = "Expression contains a mismatched %s parenthesis.";
    private static final Pattern NUMBER_REGEX = Pattern.compile("^[0-9.+-]$");
    private static final Pattern OPERATOR_REGEX;
    private static final Pattern PARENTHESES_REGEX;

    private static InfixToPostfixConverter instance;

    static {
        StringBuilder operatorRegexBuilder = new StringBuilder();
        StringBuilder parenthesesRegexBuilder = new StringBuilder();
        operatorRegexBuilder.append("^[");
        parenthesesRegexBuilder.append("^[");
        for (var operator : Operator.values()) {
            if (operator.toString().toUpperCase().contains("PARENTHESIS")) {
                parenthesesRegexBuilder.append(operator.getSign());
            } else {
                operatorRegexBuilder.append(operator.getSign());
            }
        }
        operatorRegexBuilder.append("]$");
        parenthesesRegexBuilder.append("]$");
        OPERATOR_REGEX = Pattern.compile(operatorRegexBuilder.toString());
        PARENTHESES_REGEX = Pattern.compile(parenthesesRegexBuilder.toString());
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
            if (PARENTHESES_REGEX.matcher(symbol).find()) {
                Operator parenthesis = Operator.valueOfSign(symbol);
                if (LEFT_PARENTHESIS.equals(parenthesis)) {
                    if (isInNumber) {
                        String illegalSequence = previousSymbol + symbol;
                        throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENT_MSG, illegalSequence));
                    }
                    operatorStack.offerLast(parenthesis);
                } else {
                    if (isInNumber) {
                        isInNumber = false;
                        postfixTokens.add(numberBuilder.toString());
                        numberBuilder.setLength(0);
                    }
                    Operator operator = operatorStack.pollLast();
                    while (!LEFT_PARENTHESIS.equals(operator)) {
                        if (null == operator) {
                            throw new IllegalArgumentException(String.format(INVALID_PARENTHESIS_MSG, "right"));
                        }
                        postfixTokens.add(operator.getSign());
                        operator = operatorStack.pollLast();
                    }
                }
            } else if (OPERATOR_REGEX.matcher(symbol).find()
                && !(null == previousSymbol || LEFT_PARENTHESIS.getSign().equals(previousSymbol) || OPERATOR_REGEX.matcher(previousSymbol).find())) {
                if (isInNumber) {
                    isInNumber = false;
                    postfixTokens.add(numberBuilder.toString());
                    numberBuilder.setLength(0);
                }
                Operator operator = Operator.valueOfSign(symbol);
                if (operatorStack.isEmpty() || operator.getPrecedence() > operatorStack.peekLast().getPrecedence()) {
                    operatorStack.offerLast(operator);
                } else {
                    Operator peekedOperator = operatorStack.peekLast();
                    while (null != peekedOperator && !LEFT_PARENTHESIS.equals(peekedOperator) && operator.getPrecedence() <= peekedOperator.getPrecedence()) {
                        postfixTokens.add(operatorStack.pollLast().getSign());
                        peekedOperator = operatorStack.peekLast();
                    }
                    operatorStack.offerLast(operator);
                }
            } else if (NUMBER_REGEX.matcher(symbol).find()) {
                isInNumber = true;
                numberBuilder.append(symbol);
            } else {
                String illegalSequence = (null == previousSymbol ? "" : previousSymbol) + symbol;
                throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENT_MSG, illegalSequence));
            }
            previousSymbol = symbol;
        }
        if (isInNumber) {
            postfixTokens.add(numberBuilder.toString());
        }

        while (!operatorStack.isEmpty()) {
            Operator operator = operatorStack.pollLast();
            if (LEFT_PARENTHESIS.equals(operator)) {
                throw new IllegalArgumentException(String.format(INVALID_PARENTHESIS_MSG, "left"));
            }
            postfixTokens.add(operator.getSign());
        }

        return postfixTokens;
    }

    private InfixToPostfixConverter() {}
}
