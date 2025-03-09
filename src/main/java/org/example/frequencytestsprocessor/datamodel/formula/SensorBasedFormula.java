package org.example.frequencytestsprocessor.datamodel.formula;

import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.Sensor;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.SensorProxyForTable;
import org.example.frequencytestsprocessor.datamodel.controlTheory.CalculatedFRF;
import org.example.frequencytestsprocessor.datamodel.controlTheory.FRF;

import java.util.*;

public class SensorBasedFormula extends Formula {

    List<Token> tokensList;

    private List<Token> rpnTokens;

    public SensorBasedFormula() {
        super("(F2-F1)/F0", "Add some comments here", FormulaType.SENSOR_BASED);
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

    public FRF calculate(Long runNumber, TableView<Sensor> chosenSensorsTable, Map<Long, Set<Map.Entry<String, FRF>>> calculatedFRFs) {
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
                    try {
                        String id = (String) token.getValue();
                        FRF frfToPush = null;
                        Sensor sensorById = chosenSensorsTable.getItems().stream().filter(sensor -> ((SensorProxyForTable) sensor).getId().equals(id)).findFirst().orElseGet(null);
                        if (sensorById != null) {
                            frfToPush = ((SensorProxyForTable) sensorById).getOriginalSensor().getData().get(runNumber);
                            if (frfToPush != null) { stack.push(frfToPush); continue; }
                        }
                        if (calculatedFRFs.containsKey(runNumber)) {
                            Set<Map.Entry<String, FRF>> FRFEntriesInRun = calculatedFRFs.get(runNumber);
                            frfToPush = FRFEntriesInRun.stream().
                                    filter(entry -> entry.getKey().equals(id)).findFirst().orElseThrow().getValue();
                        } else {
                            Sensor foundedSensor = chosenSensorsTable.getItems().stream()
                                    .filter(sensor -> ((SensorProxyForTable) sensor).getId().equals(id))
                                    .map(sensor -> ((SensorProxyForTable)sensor).getOriginalSensor()).findFirst().orElseGet(null);
                            if (foundedSensor != null) {
                                frfToPush = foundedSensor.getData().get(runNumber);
                            }
                        }
                        if (frfToPush != null) stack.push(frfToPush);
                        else throw new IllegalArgumentException("Formula depends on a FRF that has not been calculated. Formula/n------------" + formulaString
                                + "/nSensor id: " + id);
                    } catch (Exception e) {
                        throw new RuntimeException("Error extracting FRF by id.", e);
                    }
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
        return (FRF) stack.pop();
    }

    private Object applyOperator(String operator, Object a, Object b) {
        if (a instanceof Double && b instanceof Double) {
            return applyOperator(operator, (Double) a, (Double) b);
        } else if (a instanceof FRF && b instanceof Double) {
            return applyOperator(operator, (FRF) a, (Double) b);
        } else if (a instanceof Double && b instanceof FRF) {
            return applyOperator(operator, (Double) a, (FRF) b);
        } else if (a instanceof FRF && b instanceof FRF) {
            return applyOperator(operator, (FRF) a, (FRF) b);
        } else {
            throw new IllegalArgumentException("Unsupported operand types");
        }
    }

    private Object applyOperator(String operator, Double a, Double b) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }

    private Object applyOperator(String operator, FRF a, Double b) {
        return switch (operator) {
            case "+" -> CalculatedFRF.additionResult(a, b);
            case "-" -> CalculatedFRF.subtractionResult(a, b);
            case "*" -> CalculatedFRF.multiplicationResult(a, b);
            case "/" -> CalculatedFRF.divisionResult(a, b);
            case "^" -> CalculatedFRF.poweringResult(a, b);
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }

    private Object applyOperator(String operator, Double a, FRF b) {
        return switch (operator) {
            case "+" -> CalculatedFRF.additionResult(a, b);
            case "-" -> CalculatedFRF.subtractionResult(a, b);
            case "*" -> CalculatedFRF.multiplicationResult(a, b);
            case "/" -> CalculatedFRF.divisionResult(a, b);
            case "^" -> throw new UnsupportedOperationException("Powering is not supported for FRF objects.");
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }

    private Object applyOperator(String operator, FRF a, FRF b) {
        switch (operator) {
            case "+":
                return CalculatedFRF.additionResult(a, b);
            case "-":
                return CalculatedFRF.subtractionResult(a, b);
            case "*":
                return CalculatedFRF.multiplicationResult(a, b);
            case "/":
                return CalculatedFRF.divisionResult(a, b);
            case "^":
                throw new UnsupportedOperationException("Powering is not supported for FRF objects.");
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    private Object applyFunction(String function, Object operand) {
        if (operand instanceof FRF) {
            FRF frf = (FRF) operand;
            switch (function.toLowerCase()) {
                case "integrate":
                    return frf.integrate();
                case "differentiate":
                    return frf.differentiate();
            }
        }
        throw new IllegalArgumentException("Function " + function + " can only be applied to MyObject instances.");
    }

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
                } else if ("+-*/^".indexOf(ch) >= 0) {
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
                case "^":
                    return 3;
                default:
                    return 0;
            }
        }


    }
}