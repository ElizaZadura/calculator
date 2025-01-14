package org.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGUI {
    private JTextField textField; // Text field to display input and results
    private JFrame frame; // Main frame for the calculator GUI

    public CalculatorGUI() {
        frame = new JFrame("Calculator");
        textField = new JTextField();

        // Set up the frame
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Add text field to the frame
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

        // Create buttons and add action listeners
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        // Add button panel to the frame
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true); // Set visibility after adding components
    }

    private class ButtonClickListener implements ActionListener {
        private Calculator calculator = new Calculator();
        private String operator;
        private double firstOperand;
        private StringBuilder expressionBuilder = new StringBuilder(); // To store the current expression

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.charAt(0) >= '0' && command.charAt(0) <= '9') {
                // If the button pressed is a number, append it to the text field and expression
                textField.setText(textField.getText() + command);
                expressionBuilder.append(command); // Update the expression
            } else if (command.equals("C")) {
                // Clear the text field and reset operator and first operand
                textField.setText("");
                operator = null; // Reset operator
                firstOperand = 0; // Reset first operand
                expressionBuilder.setLength(0); // Clear the expression
            } else if (command.equals("=")) {
                // Perform the calculation
                try {
                    double secondOperand = Double.parseDouble(textField.getText());
                    double result = 0;

                    if (operator != null) { // Ensure operator is not null
                        result = switch (operator) {
                            case "+" -> calculator.add(firstOperand, secondOperand);
                            case "-" -> calculator.subtract(firstOperand, secondOperand);
                            case "*" -> calculator.multiply(firstOperand, secondOperand);
                            case "/" -> calculator.divide(firstOperand, secondOperand);
                            default -> result;
                        };
                        textField.setText(String.valueOf(result));
                        expressionBuilder.append(" = ").append(result); // Append result to expression
                    } else {
                        textField.setText("Error: No operator selected");
                    }
                } catch (NumberFormatException ex) {
                    textField.setText("Error: Invalid input");
                } catch (ArithmeticException ex) {
                    textField.setText("Error: " + ex.getMessage());
                }
            } else {
                // Store the operator and the first operand for the calculation
                if (operator != null) {
                    expressionBuilder.append(" ").append(operator).append(" "); // Append operator to expression
                }
                operator = command; // Set the operator
                try {
                    firstOperand = Double.parseDouble(textField.getText());
                    textField.setText(""); // Clear text field for next input
                    expressionBuilder.append(operator); // Update the expression with the operator
                } catch (NumberFormatException ex) {
                    textField.setText("Error: Invalid input");
                }
            }

            // Update the text field to show the current expression
            textField.setText(expressionBuilder.toString());
        }
    }

    public static void main(String[] args) {
        new CalculatorGUI(); // Start the calculator GUI
    }
}
