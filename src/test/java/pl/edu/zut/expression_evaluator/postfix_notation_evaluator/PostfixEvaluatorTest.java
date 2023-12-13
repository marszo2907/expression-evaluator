package pl.edu.zut.expression_evaluator.postfix_notation_evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.ParameterizedTest;

public class PostfixEvaluatorTest {
    private static PostfixEvaluator postfixEvaluator;

    @BeforeAll
    static void init() {
        postfixEvaluator = PostfixEvaluator.getInstance();
    }

    @ParameterizedTest
    @MethodSource("validTokensProvider")
    public void testValid(List<String> tokens, String expectedResultStr) {
        BigDecimal expectedResult = new BigDecimal(expectedResultStr);
        BigDecimal result = postfixEvaluator.evaluate(tokens);
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("divisionByZeroTokensProvider")
    public void testDivisionByZero(List<String> tokens) {
        assertThrows(ArithmeticException.class, () -> postfixEvaluator.evaluate(tokens));
    }

    @ParameterizedTest
    @MethodSource("invalidTokensProvider")
    public void testInvalid(List<String> tokens) {
        assertThrows(IllegalArgumentException.class, () -> postfixEvaluator.evaluate(tokens));
    }

    private static Stream<Arguments> validTokensProvider() {
        return Stream.of(
            Arguments.of(
                List.of("3", "4", "+"),
                "7"
            ),
            Arguments.of(
                List.of("3", "4", "-", "5", "+"),
                "4"
            ),
            Arguments.of(
                List.of("3", "4", "*", "5", "6", "*", "+"),
                "42"
            ),
            Arguments.of(
                List.of("2", "3", "1", "*", "+", "9", "-"),
                "-4"
            ),
            Arguments.of(
                List.of("100", "200", "+", "2", "/", "5", "*", "7", "+"),
                "757"
            ),
            Arguments.of(
                List.of("1", "2", "/"),
                "0.5"
            ),
            Arguments.of(
                List.of("1", "-2", "/"),
                "-0.5"
            ),
            Arguments.of(
                List.of("-1", "2", "/"),
                "-0.5"
            ),
            Arguments.of(
                List.of("-1", "-2", "/"),
                "0.5"
            ),
            Arguments.of(
                List.of("1", "3", "/"),
                "0.3333333"
            ),
            Arguments.of(
                List.of("2", "1", "3", "/", "*"),
                "0.6666667"
            )
        );
    }

    private static Stream<List<String>> divisionByZeroTokensProvider() {
        return Stream.of(
            List.of("0", "0", "/"),
            List.of("1", "0", "/"),
            List.of("1", "1", "1", "-", "/"),
            List.of("1", "1", "0", "*", "/")
        );
    }

    private static Stream<List<String>> invalidTokensProvider() {
        return Stream.of(
            List.of("1", "2", "*", "3"),
            List.of("1", "2", "/", "3"),
            List.of("1", "2", "+", "3"),
            List.of("1", "2", "-", "3")
        );
    }
}
