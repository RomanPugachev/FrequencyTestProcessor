package org.example.frequencytestsprocessor.datamodel.formula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;
import org.example.frequencytestsprocessor.services.idManagement.IdManager;

import java.util.*;
import java.util.function.Predicate;

public class SensorBasedFormula extends Formula {

    List<Token> tokensList;

    private List<Token> rpnTokens;

    public SensorBasedFormula() {
        super("1", "This is test formula", FormulaType.SENSOR_BASED);
        updateInformation();
    }

    public void setFormulaString(String formulaString) {
        this.formulaString = formulaString;
        updateInformation();
    }

    private void updateInformation() {
        tokensList = Lexer.tokenize(formulaString);
        rpnTokens = PolishParser.parseToRPN(tokensList);
    }

    public boolean validate(String formulaString) {
        // Simple validation logic
        System.out.println("Validating formula: " + formulaString);
        try {
            Lexer.tokenize(formulaString); // Check for tokenization errors
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Validation failed: " + e.getMessage());
            return false;
        }
    }

    public Set<String> getDependentIds() {
        Set<String> dependentIds = new HashSet<>();
        tokensList.forEach(token -> {
            if (token.getType().equals(Token.Type.IDENTIFIER)) dependentIds.add((String) token.getValue());
        });
        return dependentIds;
    }

    public RepresentableDataset getDataset(Long runNumber) {
        throw new UnsupportedOperationException("Not implemented yet");
        // TODO: IMPLEMENT THIS METHOD

        //        return this.calculate(runNumber);
    }
    /*
    public RepresentableDataset calculate(Long runNumber) {
        if (rpnTokens == null) {
            throw new IllegalStateException("Formula has not been parsed to RPN.");
        }
        Stack<Object> stack = new Stack<>();
        for (Token token : rpnTokens) {
            switch (token.getType()) {
                case NUMBER:
                    stack.push(Double.parseDouble(token.getValue()));
                    break;
                case IDENTIFIER:
                    stack.push(new MyObject(Double.parseDouble(token.getValue()))); // Example casting logic
                    break;
                case OPERATOR:
                    if (stack.size() < 2) throw new IllegalArgumentException("Invalid formula.");
                    Object b = stack.pop();
                    Object a = stack.pop();
                    stack.push(applyOperator(token.getValue(), a, b));
                    break;
                case FUNCTION:
                    if (stack.isEmpty()) throw new IllegalArgumentException("Invalid formula.");
                    Object operand = stack.pop();
                    stack.push(applyFunction(token.getValue(), operand));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected token type: " + token.getType());
            }
        }

        if (stack.size() != 1) throw new IllegalArgumentException("Invalid formula.");
        return (RepresentableDataset) stack.pop(); // Example conversion to dataset
    }

    private Object applyOperator(String operator, Object a, Object b) {
        // Example logic: Adjust as per MyObject operations
        if (a instanceof MyObject && b instanceof MyObject) {
            double valueA = ((MyObject) a).getValue();
            double valueB = ((MyObject) b).getValue();
            switch (operator) {
                case "+":
                    return new MyObject(valueA + valueB);
                case "-":
                    return new MyObject(valueA - valueB);
                case "*":
                    return new MyObject(valueA * valueB);
                case "/":
                    return new MyObject(valueA / valueB);
            }
        }
        throw new IllegalArgumentException("Unsupported operator or operand types.");
    }

    private Object applyFunction(String function, Object operand) {
        if (operand instanceof MyObject) {
            MyObject obj = (MyObject) operand;
            switch (function.toLowerCase()) {
                case "integrate":
                    return obj.integrate();
                case "differentiate":
                    return obj.differentiate();
            }
        }
        throw new IllegalArgumentException("Function " + function + " can only be applied to MyObject instances.");
    }*/

    public static class Lexer {
        public static List<Token> tokenize(String input) {
            List<Token> tokens = new ArrayList<>();
            StringBuilder buffer = new StringBuilder();

            for (int i = 0; i < input.length(); i++) {
                char ch = input.charAt(i);

                if (Character.isWhitespace(ch)) continue;

                if (Character.isDigit(ch) || ch == '.') {
                    buffer.append(ch);
                    while (i + 1 < input.length() && (Character.isDigit(input.charAt(i + 1)) || input.charAt(i + 1) == '.')) {
                        buffer.append(input.charAt(++i));
                    }
                    tokens.add(new Token(Token.Type.NUMBER, buffer.toString()));
                    buffer.setLength(0);
                } else if (ch == '(' || ch == ')') {
                    tokens.add(new Token(Token.Type.PARENTHESIS, String.valueOf(ch)));
                } else if ("+-*/".indexOf(ch) >= 0) {
                    tokens.add(new Token(Token.Type.OPERATOR, String.valueOf(ch)));
                } else if (Character.isLetter(ch)) {
                    buffer.append(ch);
                    while (i + 1 < input.length() && Character.isLetterOrDigit(input.charAt(i + 1))) {
                        buffer.append(input.charAt(++i));
                    }
                    String word = buffer.toString();
                    if ("integrate".equalsIgnoreCase(word) || "differentiate".equalsIgnoreCase(word)) {
                        tokens.add(new Token(Token.Type.FUNCTION, word));
                    } else {
                        tokens.add(new Token(Token.Type.IDENTIFIER, word));
                    }
                    buffer.setLength(0);
                } else {
                    throw new IllegalArgumentException("Unexpected character: " + ch);
                }
            }

            return tokens;
        }
    }

    @Getter
    @Setter
    public static class Token {
        public enum Type {
            NUMBER, IDENTIFIER, OPERATOR, FUNCTION, PARENTHESIS
        }

        private Type type;
        private String value;

        public Token(Type type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    public static class PolishParser {

        public static List<Token> parseToRPN(List<Token> tokens) {
            List<Token> output = new ArrayList<>();
            Stack<Token> operators = new Stack<>();

            for (Token token : tokens) {
                switch (token.getType()) {
                    case NUMBER:
                    case IDENTIFIER:
                        output.add(token);
                        break;
                    case FUNCTION:
                        operators.push(token);
                        break;
                    case OPERATOR:
                        while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                            output.add(operators.pop());
                        }
                        operators.push(token);
                        break;
                    case PARENTHESIS:
                        if ("(".equals(token.getValue())) {
                            operators.push(token);
                        } else if (")".equals(token.getValue())) {
                            while (!operators.isEmpty() && !"(".equals(operators.peek().getValue())) {
                                output.add(operators.pop());
                            }
                            if (operators.isEmpty() || !"(".equals(operators.pop().getValue())) {
                                throw new IllegalArgumentException("Mismatched parentheses.");
                            }
                            if (!operators.isEmpty() && operators.peek().getType() == Token.Type.FUNCTION) {
                                output.add(operators.pop());
                            }
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected token type: " + token.getType());
                }
            }

            while (!operators.isEmpty()) {
                Token op = operators.pop();
                if ("(".equals(op.getValue()) || ")".equals(op.getValue())) {
                    throw new IllegalArgumentException("Mismatched parentheses.");
                }
                output.add(op);
            }

            return output;
        }

        private static int precedence(Token token) {
            if (token.getType() != Token.Type.OPERATOR) return 0;
            switch (token.getValue()) {
                case "+":
                case "-":
                    return 1;
                case "*":
                case "/":
                    return 2;
                default:
                    return 0;
            }
        }
    }
}
//    How to parse formula and perform calculations in Java, including taking into account brackets '(', ')', some function operators, such as 'INTEGRATE', which must accept one argument in brackets? Also it is important to include opportunity to parse identifiers as thing, which can also be calculated. For example, this formula:
//INTEGRATE(DIFFERENTIATE(F0) - 2) / (-2) must be parsed and performed validly. Suppose, that using 'F0' I mean some identifier, which represents my object with data, which allows to be summed, devided, ITEGRATEd etc. with similar object or number.
//Please, provide algorythm, which will allow me to perform calculations
