package pl.edu.zut.expression_evaluator;

import java.util.Scanner;

public class ExpressionEvaluatorTestRun {
    private final static String QUIT = "Q";
    private final static String SUPPORTED_OPERATIONS;

    static {
        boolean printLeadingComma = false;
        StringBuilder supportedOperationsBuilder = new StringBuilder();
        for (var operator : Operator.values()) {
            if (printLeadingComma) {
                supportedOperationsBuilder.append(", ");
            } else {
                printLeadingComma = true;
            }
            supportedOperationsBuilder
                    .append(operator.toString())
                    .append(" [")
                    .append(operator.getSign())
                    .append("]");
        }
        SUPPORTED_OPERATIONS = supportedOperationsBuilder.toString();
    }

    public static void main(String[] args) {
        ExpressionEvaluator expressionEvaluator = ExpressionEvaluator.getInstance();
        String input;
        Scanner scanner = new Scanner(System.in);

        System.out.printf("""
            Welcome to the expression evaluator test run!"
            Supported operations: %s
            Note: the evaluator ignores whitespace characters (e.g. '1 + 2 3' is going to be evaluated as '1+23')
            """, SUPPORTED_OPERATIONS);
        System.out.printf("Enter an expression (or %s to quit): ", QUIT);
        input = scanner.nextLine();
        while (!QUIT.equalsIgnoreCase(input)) {
            try {
                System.out.println(expressionEvaluator.evaluate(input));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.printf("Enter an expression (or %s to quit): ", QUIT);
            input = scanner.nextLine();
        }
        
        System.out.println("Goodbye!");
    }
}
