package org.example.frequencytestsprocessor.datamodel.formula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.services.idManagement.IdManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SensorBasedFormula extends Formula {

    List<Token> tokensList;

    public SensorBasedFormula() {
        super("Some formula string", "This is test formula", FormulaType.SENSOR_BASED);
        updateInformation();
    }

    private void updateInformation() {
        tokensList = Tokenizer.tokenize(formulaString);
    }

    public boolean validate(String formulaString) {
        // TODO: Implement validation logic
        System.out.println("Validating formula: " + formulaString);
        System.out.println("For now I just return true");
        return true;
    }

    public Set<String> getDependentIds(){
        Set<String> dependentIds = new HashSet<>();
        tokensList.forEach(token -> {if (token.getType().equals(TokenType.IDENTIFIER)) dependentIds.add((String) token.getValue());});
        return dependentIds;
    }


    public static class Tokenizer {
        public static List<Token> tokenize(String expression) {
            List<Token> tokens = new ArrayList<>();
            expression = expression.replace(" ", "");
            StringBuilder numberBuffer = new StringBuilder();
            StringBuilder identifierBuffer = new StringBuilder();
            StringBuilder functionBuffer = new StringBuilder();
            String current;
            for (int i=0;i<expression.length();i++){
                current = expression.substring(i, i + 1);
                if (current.equals("(")) {
                    if (numberBuffer.length() > 0 || functionBuffer.length() > 0 || identifierBuffer.length() > 0) {
                        throw new IllegalArgumentException("Invalid expression (you should finish all identifiers and numbers editing before using opening bracket): " + expression);
                    }
                    tokens.add(Token.LEFT_BRACKET);
                    continue;
                }

                else if (current.equals(")")) {
                    if (functionBuffer.length() > 0) {
                        String currentFunctionOrIdentifier = functionBuffer.toString();
                        Token.AVAILABLE_FUNCTIONS.stream()
                                .filter(function -> function.equals(currentFunctionOrIdentifier.toUpperCase()))
                                .findFirst()
                                .ifPresent(function -> {
                                    tokens.add(function);
                                    functionBuffer.setLength(0);
                                    identifierBuffer.setLength(0);
                                    tokens.add(Token.RIGHT_BRACKET);
                                    continue;
                                });
                        tokens.add(new Token(TokenType.IDENTIFIER,currentFunctionOrIdentifier));
                        identifierBuffer.setLength(0);
                        functionBuffer.setLength(0);
                        tokens.add(Token.RIGHT_BRACKET);
                        continue;
                    } else if (numberBuffer.length() > 0) {
                        tokens.add(new Token(TokenType.NUMBER, numberBuffer.toString()));
                        numberBuffer.setLength(0);
                        tokens.add(Token.RIGHT_BRACKET);
                        continue;
                    }
                }

                else if (current.matches("[0-9]")) {
                    if (functionBuffer.length() > 0 || identifierBuffer.length() > 0) {
                        functionBuffer.append(current);
                        identifierBuffer.append(current);
                    } else {
                        numberBuffer.append(current);
                    }
                }

                else if (current.matches("[a-zA-Z]")) {
                    if (numberBuffer.length() > 0) throw new IllegalArgumentException("Invalid expression (number can't contain letters): " + expression);
                    identifierBuffer.append(current);
                    functionBuffer.append(current);
                    continue;
                }

                else if (Token.AVAILABLE_OPERATORS.stream().anyMatch(token -> token.getValue().equals(current))) {
//                    TODO: Implement operator handling
//                    if (numberBuffer.length() > 0) {
//                        tokens.add(new Token(TokenType.NUMBER, numberBuffer.toString()));
//                        numberBuffer.setLength(0);
//                    }
//                    if (functionBuffer.length() > 0) {
//                        tokens.add(new Token(TokenType.IDENTIFIER, functionBuffer.toString()));
//                        functionBuffer.setLength(0);
//                    }
//                    if (identifierBuffer.length() > 0) {
//                        tokens.add(new Token(TokenType.IDENTIFIER, identifierBuffer.toString()));
//                        identifierBuffer.setLength(0);
//                    }
//                    tokens.add(new Token(TokenType.OPERATOR, current));
                } else {
                    // Handle unknown characters
                }

                }
            return tokens;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Token {
        // BRACKET TOKENS
        public static final Token LEFT_BRACKET = new Token(TokenType.BRACKET_L, "(");
        public static final Token RIGHT_BRACKET = new Token(TokenType.BRACKET_R, ")");
        public static final List<Token> AVAILABLE_BRACKETS = List.of(LEFT_BRACKET, RIGHT_BRACKET);
        // FUNCTION TOKENS
        public static final Token INTEGRATE = new Token(TokenType.FUNCTION, "INTEGRATE");
        public static final Token DIFFERENTIATE = new Token(TokenType.FUNCTION, "DIFFERENTIATE");
        public static final List<Token> AVAILABLE_FUNCTIONS = List.of(INTEGRATE, DIFFERENTIATE);
        // OPERATOR TOKENS
        public static final Token PLUS = new Token(TokenType.OPERATOR, "+");
        public static final Token MINUS = new Token(TokenType.OPERATOR, "-");
        public static final Token MULTIPLY = new Token(TokenType.OPERATOR, "*");
        public static final Token DIVIDE = new Token(TokenType.OPERATOR, "/");
        public static final List<Token> AVAILABLE_OPERATORS = List.of(PLUS, MINUS, MULTIPLY, DIVIDE);


        private TokenType type;
        private Object value;
    }

    public enum TokenType {
        OPERATOR,
        IDENTIFIER,
        FUNCTION,
        NUMBER,
        BRACKET_L,
        BRACKET_R
    }
//    How to parse formula and perform calculations in Java, including taking into account brackets '(', ')', some function operators, such as 'INTEGRATE', which must accept one argument in brackets? Also it is important to include opportunity to parse identifiers as thing, which can also be calculated. For example, this formula:
//INTEGRATE(DIFFERENTIATE(F0) - 2) / (-2) must be parsed and performed validly. Suppose, that using 'F0' I mean some identifier, which represents my object with data, which allows to be summed, devided, ITEGRATEd etc. with similar object or number.
//Please, provide algorythm, which will allow me to perform calculations
}
