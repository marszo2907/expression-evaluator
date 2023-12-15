package pl.edu.zut.expression_evaluator;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class InfixToPostfixConverterTest {
    private static InfixToPostfixConverter infixToPostfixConverter;

    @BeforeAll
    private static void init() {
        infixToPostfixConverter = InfixToPostfixConverter.getInstance();
    }

    private static Stream<Arguments> validExpressionsProvider() {
        return Stream.of(
            Arguments.of(
                "0 + 2.5 * 10 - 13.101",
                List.of("0", "2.5", "10", "*", "+", "13.101", "-")
            ),
            Arguments.of(
                "0+2.5*10-13.101",
                List.of("0", "2.5", "10", "*", "+", "13.101", "-")
            ),
            Arguments.of(
                "0 + 2.5*10 -13.101",
                List.of("0", "2.5", "10", "*", "+", "13.101", "-")
            ),
            Arguments.of(
                "-2 * 3.25 / -1.234",
                List.of("-2", "3.25", "*", "-1.234", "/")
            ),
            Arguments.of(
                "-2*3.25/-1.234",
                List.of("-2", "3.25", "*", "-1.234", "/")
            ),
            Arguments.of(
                "-2 *3.25/- 1.234",
                List.of("-2", "3.25", "*", "-1.234", "/")
            )
        );
    }

    private static Stream<Arguments> validExpressionsWithParenthesesProvider() {
        return Stream.of(
            Arguments.of(
                "0 + 2.5 * (10 - 13.101)",
                List.of("0", "2.5", "10", "13.101", "-" , "*", "+")
            ),
            Arguments.of(
                "0+2.5*(10-13.101)",
                List.of("0", "2.5", "10", "13.101", "-" , "*", "+")
            ),
            Arguments.of(
                "(-2 * 3.25) / -1.234",
                List.of("-2", "3.25", "*", "-1.234", "/")
            ),
            Arguments.of(
                "(-2*3.25)/-1.234",
                List.of("-2", "3.25", "*", "-1.234", "/")
            ),
            Arguments.of(
                "(0 + 2.5) * (10 - 13.101)",
                List.of("0", "2.5", "+", "10", "13.101", "-" , "*")
            ),
            Arguments.of(
                "(0+2.5)*(10-13.101)",
                List.of("0", "2.5", "+", "10", "13.101", "-" , "*")
            ),
            Arguments.of(
                "((0 + 2.5) * (10 - 13.101))",
                List.of("0", "2.5", "+", "10", "13.101", "-" , "*")
            ),
            Arguments.of(
                "((0+2.5)*(10-13.101))",
                List.of("0", "2.5", "+", "10", "13.101", "-" , "*")
            ),
            Arguments.of(
                "(0 + 2.5) * (3 / (10 - 13.101))",
                List.of("0", "2.5", "+", "3", "10", "13.101", "-" , "/", "*")
            ),
            Arguments.of(
                "(0+2.5)*(3/(10-13.101))",
                List.of("0", "2.5", "+", "3", "10", "13.101", "-" , "/", "*")
            )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0 + 2.5 * (10 - 13.101))",
        "0 + 2.5 * )(10 - 13.101)",
        "0 + 2.5 * ((10 - 13.101)",
        "0 + 2.5 * (10 - 13.101)(",
        "(-2 * 3.25) / -1.234)",
        "(-2 * 3.25)) / -1.234",
        ")(-2 * 3.25) / -1.234",
        "0 + 2.5 (10 - 13.101)",
    })
    void testInvalidParentheses(String infixExpression) {
        assertThrows(IllegalArgumentException.class, () -> infixToPostfixConverter.getPostfixTokens(infixExpression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0 + 2.5 * 10 - 13.1O1",
        "0+2.5*10-13O1",
        "0 + 2.5*10 -13.1O1",
        "-2 * 3.2A / -1.234",
        "-2*3.2A5/-1.234",
        "-2 *3.2A5/- 1.234",
        "-2 * / 3.25 / -1.234",
        "-2*/3.25/-1.234",
        "-2* /3.25/- 1.234",
        "-a * 2",
        "-a*2",
        "- a*2"
    })
    void testInvalidSymbols(String infixExpression) {
        assertThrows(IllegalArgumentException.class, () -> infixToPostfixConverter.getPostfixTokens(infixExpression));
    }

    @ParameterizedTest
    @MethodSource("validExpressionsProvider")
    void testValid(String infixExpression, List<String> expectedResult) {
        List<String> postfixTokens = infixToPostfixConverter.getPostfixTokens(infixExpression);
        assertIterableEquals(expectedResult, postfixTokens);
    }

    @ParameterizedTest
    @MethodSource("validExpressionsWithParenthesesProvider")
    void testValidWithParentheses(String infixExpression, List<String> expectedResult) {
        List<String> postfixTokens = infixToPostfixConverter.getPostfixTokens(infixExpression);
        assertIterableEquals(expectedResult, postfixTokens);
    }
}
