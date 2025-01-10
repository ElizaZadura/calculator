package org.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGUI {
    private final JTextField textField;

    public CalculatorGUI() {
        JFrame frame = new JFrame("Calculator");
        textField = new JTextField();

        // Set up the frame
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Add text field
        frame.add(textField, BorderLayout.NORTH);

        // Create buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4));

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new CalculatorGUI();
    }

    private class ButtonClickListener implements ActionListener {
        private final Calculator calculator = new Calculator();
        private String operator;
        private double result;

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.charAt(0) >= '0' && command.charAt(0) <= '9') {
                // If the button pressed is a number, append it to the text field
                textField.setText(textField.getText() + command);
            } else if (command.equals("C")) {
                // Clear the text field and reset operator and result
                textField.setText("");
                operator = null;
                result = 0;
            } else if (command.equals("=")) {
                // Perform the final calculation
                try {
                    double operand = Double.parseDouble(textField.getText());
                    result = calculate(result, operand, operator);
                    textField.setText(String.valueOf(result));
                } catch (NumberFormatException ex) {
                    textField.setText("Error: Invalid input");
                } catch (ArithmeticException ex) {
                    textField.setText("Error: " + ex.getMessage());
                }
            } else { // Handle operator buttons SEPARATELY
                try {
                    double operand = Double.parseDouble(textField.getText());
                    if (operator!=null) {
                        result = calculate(result, operand, operator);
                    } else {
                        result = operand;
                    }
                    operator = command;
                    textField.setText("");
                } catch (NumberFormatException ex) {
                    textField.setText("Error: Invalid input");
                }
            }
        }

        private double calculate(double firstOperand, double secondOperand, @org.jetbrains.annotations.NotNull String operator) {
            return switch (operator) {
                case "+" -> calculator.add(firstOperand, secondOperand);
                case "-" -> calculator.subtract(firstOperand, secondOperand);
                case "*" -> calculator.multiply(firstOperand, secondOperand);
                case "/" -> calculator.divide(firstOperand, secondOperand);
                default -> throw new IllegalArgumentException("Invalid operator: " + operator);
            };
        }
    }
}
