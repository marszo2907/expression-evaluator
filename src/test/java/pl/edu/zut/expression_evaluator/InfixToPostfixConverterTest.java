package pl.edu.zut.expression_evaluator;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.edu.zut.expression_evaluator.InfixToPostfixConverter;

public class InfixToPostfixConverterTest {
    private static InfixToPostfixConverter infixToPostfixConverter;

    @BeforeAll
    static void init() {
        infixToPostfixConverter = InfixToPostfixConverter.getInstance();
    }

    @ParameterizedTest
    @MethodSource("validExpressionsProvider")
    void testValid(String infixExpression, List<String> expectedResult) {
        List<String> postfixTokens = infixToPostfixConverter.getPostfixTokens(infixExpression);
        assertIterableEquals(expectedResult, postfixTokens);

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
}
