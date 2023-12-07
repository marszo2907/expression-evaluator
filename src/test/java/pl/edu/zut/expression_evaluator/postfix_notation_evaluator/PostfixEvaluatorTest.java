package pl.edu.zut.expression_evaluator.postfix_notation_evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.ParameterizedTest;
import pl.edu.zut.expression_evaluator.postfix_notation_evaluator.PostfixEvaluator;

public class PostfixEvaluatorTest {
    private static PostfixEvaluator postfixEvaluator;

    @BeforeAll
    static void init() {
        postfixEvaluator = PostfixEvaluator.getInstance();
    }

    @ParameterizedTest
    @MethodSource("validAdditionTokensProvider")
    public void testValidAddtiton(List<String> tokens, String expectedResultStr) {
        BigDecimal expectedResult = new BigDecimal(expectedResultStr);

        BigDecimal result = postfixEvaluator.evaluate(tokens);

        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> validAdditionTokensProvider() {
        return Stream.of(
            Arguments.of(
                List.of("0", "0", "+"),
                "0"
            ),
            Arguments.of(
                List.of("0", "1", "+"),
                "1"
            ),
            Arguments.of(
                List.of("1", "0", "+"),
                "1"
            ),
            Arguments.of(
                List.of("1", "2", "+"),
                "3"
            ),
            Arguments.of(
                List.of("2", "1", "+"),
                "3"
            ),
            Arguments.of(
                List.of("-1", "2", "+"),
                "1"
            ),
            Arguments.of(
                List.of("2", "-1", "+"),
                "1"
            ),
            Arguments.of(
                List.of("-1", "-2", "+"),
                "-3"
            ),
            Arguments.of(
                List.of("-2", "-1", "+"),
                "-3"
            ),
            Arguments.of(
                List.of("0", "0", "+", "1", "+"),
                "1"
            ),
            Arguments.of(
                List.of("0", "1", "+", "2", "+"),
                "3"
            ),
            Arguments.of(
                List.of("1", "0", "+", "2", "+"),
                "3"
            ),
            Arguments.of(
                List.of("1", "2", "+", "3", "+"),
                "6"
            ),
            Arguments.of(
                List.of("2", "1", "+", "3", "+"),
                "6"
            ),
            Arguments.of(
                List.of("-1", "2", "+", "-3", "+"),
                "-2"
            ),
            Arguments.of(
                List.of("2", "-1", "+", "-3", "+"),
                "-2"
            ),
            Arguments.of(
                List.of("-1", "-2", "+", "-3", "+"),
                "-6"
            ),
            Arguments.of(
                List.of("-2", "-1", "+", "-3", "+"),
                "-6"
            )
        );
    }
}
