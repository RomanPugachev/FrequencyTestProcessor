package org.example.frequencytestsprocessor.datamodel.formula;

import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.example.frequencytestsprocessor.datamodel.formula.AnalyticalFormula.Token.Type.S;

// TODO: implement this type of formula
public class AnalyticalFormula extends Formula{

    List<AnalyticalFormula.Token> tokensList;

    private List<AnalyticalFormula.Token> rpnTokens;

    public AnalyticalFormula() {
        super("1", "This is test analytical formula", FormulaType.ANALYTICAL);
    }

    @Override
    public void setFormulaString(String formulaString) {
        this.formulaString = formulaString;
        updateInformation();
    }

    private void updateInformation() {
        tokensList = AnalyticalFormula.Lexer.tokenize(formulaString);
        rpnTokens = AnalyticalFormula.PolishParser.parseToRPN(tokensList);
    }

    public boolean validate(String formulaString) {
        // TODO: Implement validation logic

        return true;
    }

    public RepresentableDataset getDataset(Long runNumber) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: debug and  lexer
    public static class Lexer {
        public static List<AnalyticalFormula.Token> tokenize(String input) {
            List<AnalyticalFormula.Token> tokens = new ArrayList<>();
            StringBuilder buffer = new StringBuilder();

            for (int i = 0; i < input.length(); i++) {
                char ch = input.charAt(i);

                if (Character.isWhitespace(ch)) continue;

                if (Character.isDigit(ch) || ch == '.') {
                    buffer.append(ch);
                    while (i + 1 < input.length() && (Character.isDigit(input.charAt(i + 1)) || input.charAt(i + 1) == '.')) {
                        buffer.append(input.charAt(++i));
                    }
                    tokens.add(new AnalyticalFormula.Token(AnalyticalFormula.Token.Type.NUMBER, buffer.toString()));
                    buffer.setLength(0);
                } else if (ch == '(' || ch == ')') {
                    tokens.add(new AnalyticalFormula.Token(AnalyticalFormula.Token.Type.PARENTHESIS, String.valueOf(ch)));
                } else if ("+-*/".indexOf(ch) >= 0) {
                    tokens.add(new AnalyticalFormula.Token(AnalyticalFormula.Token.Type.OPERATOR, String.valueOf(ch)));
                } else if (Character.isLetter(ch)) {
                    buffer.append(ch);
                    while (i + 1 < input.length() && Character.isLetterOrDigit(input.charAt(i + 1))) {
                        buffer.append(input.charAt(++i));
                    }
                    String word = buffer.toString();
                    if ("integrate".equalsIgnoreCase(word) || "differentiate".equalsIgnoreCase(word)) {
                        tokens.add(new AnalyticalFormula.Token(AnalyticalFormula.Token.Type.FUNCTION, word));
                    } else {
                        tokens.add(new AnalyticalFormula.Token(AnalyticalFormula.Token.Type.IDENTIFIER, word));
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
            NUMBER, S, OPERATOR, FUNCTION, PARENTHESIS
        }

        private AnalyticalFormula.Token.Type type;
        private String value;

        public Token(AnalyticalFormula.Token.Type type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    public static class PolishParser {

        public static List<AnalyticalFormula.Token> parseToRPN(List<AnalyticalFormula.Token> tokens) {
            List<AnalyticalFormula.Token> output = new ArrayList<>();
            Stack<AnalyticalFormula.Token> operators = new Stack<>();

            for (AnalyticalFormula.Token token : tokens) {
                switch (token.getType()) {
                    case NUMBER:
                    case S:
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
                            if (!operators.isEmpty() && operators.peek().getType() == AnalyticalFormula.Token.Type.FUNCTION) {
                                output.add(operators.pop());
                            }
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected token type: " + token.getType());
                }
            }

            while (!operators.isEmpty()) {
                AnalyticalFormula.Token op = operators.pop();
                if ("(".equals(op.getValue()) || ")".equals(op.getValue())) {
                    throw new IllegalArgumentException("Mismatched parentheses.");
                }
                output.add(op);
            }

            return output;
        }

        private static int precedence(AnalyticalFormula.Token token) {
            if (token.getType() != AnalyticalFormula.Token.Type.OPERATOR) return 0;
            switch (token.getValue()) {
                case "+":
                case "-":
                    return 1;
                case "*":
                case "/":
                    return 2;
                case "^":
                    return 3;
                default:
                    return 0;
            }
        }


    }
}
