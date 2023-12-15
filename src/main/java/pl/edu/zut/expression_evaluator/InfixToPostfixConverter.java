package pl.edu.zut.expression_evaluator;

import static pl.edu.zut.expression_evaluator.Operator.LEFT_PARENTHESIS;
import static pl.edu.zut.expression_evaluator.Operator.RIGHT_PARENTHESIS;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class InfixToPostfixConverter {
    private static final String ILLEGAL_ARGUMENT_MSG = "Exprssion contains an unsupported symbol or a combination of symbols: %s.";
    private static final String INVALID_PARENTHESIS_MSG = "Expression contains a mismatched %c parenthesis.";
    private static final String NUMBERS = "0123456789.+-";
    private static final String OPERATORS;
    private static final String PARENTHESES;

    private static InfixToPostfixConverter instance;

    static {
        StringBuilder operatorBuilder = new StringBuilder();
        StringBuilder parenthesesBuilder = new StringBuilder();
        for (var operator : Operator.values()) {
            if (LEFT_PARENTHESIS.equals(operator) || RIGHT_PARENTHESIS.equals(operator)) {
                parenthesesBuilder.append(operator.getSign());
            } else {
                operatorBuilder.append(operator.getSign());
            }
        }
        OPERATORS = operatorBuilder.toString();
        PARENTHESES = parenthesesBuilder.toString();
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
        char previousSymbol = Character.MIN_VALUE;

        for (var symbol : infixExpression.replaceAll("\\s", "").toCharArray()) {
            if (-1 != PARENTHESES.indexOf(symbol)) {
                Operator parenthesis = Operator.valueOf(symbol);
                if (LEFT_PARENTHESIS.equals(parenthesis)) {
                    if (isInNumber) {
                        String illegalSequence = Character.toString(previousSymbol) + symbol;
                        throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENT_MSG, illegalSequence));
                    }
                    operatorStack.offerLast(parenthesis);
                } else {
                    if (isInNumber) {
                        isInNumber = false;
                        postfixTokens.add(numberBuilder.toString());
                        numberBuilder.setLength(0);
                    }
                    Operator operator;
                    while (!LEFT_PARENTHESIS.equals(operator = operatorStack.pollLast())) {
                        if (null == operator) {
                            throw new IllegalArgumentException(String.format(INVALID_PARENTHESIS_MSG, "right"));
                        }
                        postfixTokens.add(Character.toString(operator.getSign()));
                    }
                }
            } else if ((-1 != OPERATORS.indexOf(symbol))
                        && !(Character.MIN_VALUE == previousSymbol
                            || LEFT_PARENTHESIS.getSign() == previousSymbol
                            || -1 != OPERATORS.indexOf(previousSymbol))) {
                if (isInNumber) {
                    isInNumber = false;
                    postfixTokens.add(numberBuilder.toString());
                    numberBuilder.setLength(0);
                }
                Operator operator = Operator.valueOf(symbol);
                Operator peekedOperator = operatorStack.peekLast();
                while (null != peekedOperator
                    && !LEFT_PARENTHESIS.equals(peekedOperator)
                    && operator.getPrecedence() <= peekedOperator.getPrecedence()) {
                    postfixTokens.add(Character.toString(operatorStack.pollLast().getSign()));
                    peekedOperator = operatorStack.peekLast();
                }
                operatorStack.offerLast(operator);
            } else if (-1 != NUMBERS.indexOf(symbol)) {
                isInNumber = true;
                numberBuilder.append(symbol);
            } else {
                String illegalSequence = (Character.MIN_VALUE == previousSymbol ? "" : Character.toString(previousSymbol)) + symbol;
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
            postfixTokens.add(Character.toString(operator.getSign()));
        }

        return postfixTokens;
    }

    private InfixToPostfixConverter() {}
}
