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

    private List<Token> tokensList;

    public SensorBasedFormula() {
        super("1", "Comment", FormulaType.SENSOR_BASED);
        updateInformation();
    }

    private void updateInformation() {
        tokensList = Tokenizer.tokenize(formulaString);
    }

    public void setFormulaString(String formulaString) {
        this.formulaString = formulaString;
        updateInformation();
    }

    public boolean validate(String formulaString) {
        // TODO: Implement validation logic
        System.out.println("Validating formula: " + formulaString);
        System.out.println("For now I just return true");
        return true;
    }

    public Set<String> getDependentIds(){
        Set<String> dependentIds = new HashSet<>();
        tokensList.forEach(token -> {if (token.getType().equals(Token.TokenType.IDENTIFIER)) dependentIds.add((String) token.getValue());});
        return dependentIds;
    }


    public static class Tokenizer {
        public static List<Token> tokenize(String expression) {
            List<Token> tokens = new ArrayList<>();
            expression = expression.replace(" ", "");
            StringBuilder buffer = new StringBuilder();
            for (int i=0;i<expression.length();i++) {
                char c = expression.charAt(i);
                if (Character.isDigit(c) || c == '.') {
                    buffer.append(c);
                    while (i + 1 < expression.length() && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '.')) {
                        buffer.append(expression.charAt(i + 1));
                        i++;
                    }
                    tokens.add(new Token(Token.TokenType.NUMBER, buffer.toString()));
                    buffer.setLength(0);
                } else if (c == '(' || c == ')') {
                    tokens.add(new Token(Token.TokenType.PARENTHESIS, String.valueOf(c)));
                } else if ("+-*/".indexOf(c) >= 0) {
                    tokens.add(new Token(Token.TokenType.OPERATOR, String.valueOf(c)));
                } else if (Character.isLetter(c)) {
                    buffer.append(c);
                    while (i + 1 < expression.length() && Character.isLetterOrDigit(expression.charAt(i + 1))) {
                        buffer.append(expression.charAt(++i));
                    }
                    String word = buffer.toString();
                    if ("integrate".equals(word) || "differentiate".equals(word)) {
                        tokens.add(new Token(Token.TokenType.FUNCTION, word));
                    } else {
                        tokens.add(new Token(Token.TokenType.IDENTIFIER, word));
                    }
                    buffer.setLength(0);
                } else {
                    throw new IllegalArgumentException("Invalid character: " + c);
                }
            }
            return tokens;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Token {
        public enum TokenType {
            OPERATOR,
            IDENTIFIER,
            FUNCTION,
            NUMBER,
            PARENTHESIS
        }

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
        private String value;
    }


//    How to parse formula and perform calculations in Java, including taking into account brackets '(', ')', some function operators, such as 'INTEGRATE', which must accept one argument in brackets? Also it is important to include opportunity to parse identifiers as thing, which can also be calculated. For example, this formula:
//INTEGRATE(DIFFERENTIATE(F0) - 2) / (-2) must be parsed and performed validly. Suppose, that using 'F0' I mean some identifier, which represents my object with data, which allows to be summed, devided, ITEGRATEd etc. with similar object or number.
//Please, provide algorythm, which will allow me to perform calculations
}
