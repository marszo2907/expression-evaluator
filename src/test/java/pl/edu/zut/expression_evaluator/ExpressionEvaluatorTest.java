package pl.edu.zut.expression_evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ExpressionEvaluatorTest {
    private static ExpressionEvaluator expressionEvaluator;

    @BeforeAll
    private static void init() {
        expressionEvaluator = ExpressionEvaluator.getInstance();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0/0",
        "1/0",
        "-1/0",
    })
    void testDivisionByZero(String infixExpression) {
        assertThrows(ArithmeticException.class, () -> expressionEvaluator.evaluate(infixExpression));
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
        assertThrows(IllegalArgumentException.class, () -> expressionEvaluator.evaluate(infixExpression));
    }

    @ParameterizedTest
    @CsvSource({
        "3 + 4, 7",
        "3+4, 7",
        "3+ 4, 7",
        "3 - 4 + 5, 4",
        "3-4+5, 4",
        "3 -4+ 5, 4",
        "3 * 4 + 5 * 6, 42",
        "3*4+5*6, 42",
        "3* 4+5* 6, 42",
        "2 + 3 * 1 - 9, -4",
        "2+3*1-9, -4",
        "2+3*1-9, -4",
        "2 +3* 1-9, -4",
        "1 / 2, 0.5",
        "1/2, 0.5",
        "1 /2, 0.5",
        "1 / -2, -0.5",
        "1/-2, -0.5",
        "1/ -2, -0.5",
        "-1 / 2, -0.5",
        "-1/2, -0.5",
        "-1 /2, -0.5",
        "-1 / -2, 0.5",
        "-1/-2, 0.5",
        "-1/ -2, 0.5",
        "1 / 3, 0.3333333333333333333333333333333333",
        "1/3, 0.3333333333333333333333333333333333",
        "1 /3, 0.3333333333333333333333333333333333",
        "2 * 1 / 3, 0.6666666666666666666666666666666667",
        "2*1/3, 0.6666666666666666666666666666666667",
        "2* 1 /3, 0.6666666666666666666666666666666667",
    })
    public void testValid(String infixExpression, String expectedResultStr) {
        BigDecimal expectedResult = new BigDecimal(expectedResultStr);
        BigDecimal result = expressionEvaluator.evaluate(infixExpression);
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @CsvSource({
        "0 + 2.5 * (10 - 13.101), -7.7525",
        "0+2.5*(10-13.101), -7.7525",
        "(-2 * 3.25) / -1.234, 5.267423014586709886547811993517018",
        "(-2*3.25)/-1.234, 5.267423014586709886547811993517018",
        "(0 + 2.5) * (10 - 13.101), -7.7525",
        "(0+2.5)*(10-13.101), -7.7525",
        "(0 + 2.5) * (3 / (10 - 13.101)), -2.418574653337633021605933569816188",
        "(0+2.5)*(3/(10-13.101)), -2.418574653337633021605933569816188",
    })
    void testValidWithParentheses(String infixExpression, String expectedResultStr) {
        BigDecimal expectedResult = new BigDecimal(expectedResultStr);
        BigDecimal result = expressionEvaluator.evaluate(infixExpression);
        assertEquals(expectedResult, result);
    }
}
