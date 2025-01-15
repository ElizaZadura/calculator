package org.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGUI {
    private final JTextField textField; // Text field to display input and results
    private final JFrame frame; // Main frame for the calculator GUI

// Creates a graphical calculator application with buttons for digits, operators, and clear functionality.
    public CalculatorGUI() {
        frame = new JFrame("Calculator");
        textField = new JTextField();
        textField.setEditable(false); // Make the text field read-only

        // Set up the frame
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the frame on the screen

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
                System.out.println("Operator reset to null");
            } else if (command.equals("=")) {
                // Perform the final calculation
                try {
                    double operand = Double.parseDouble(textField.getText());
                    result = calculate(result, operand, operator);
                    textField.setText(String.valueOf(result));
                    System.out.println("Calculation performed with operator: " + operator);
                } catch (NumberFormatException ex) {
                    textField.setText("Error: Invalid input");
                } catch (ArithmeticException ex) {
                    textField.setText("Error: " + ex.getMessage());
                }
            } else {
                // If an operator is pressed, store the current number in the text field
                // as the first operand and store the operator
                try {
                    operator = e.getActionCommand().charAt(0) + "";
                    double operand = Double.parseDouble(textField.getText());
                    if (operator != null) {
                        result = calculate(result, operand, operator);
                        textField.setText(String.valueOf(result));
                        System.out.println("Calculation performed with operator: " + operator);
                    } else {
                        result = operand;
                    }
                    operator = command;
                    textField.setText("");
                    System.out.println("Operator set to: " + operator);
                } catch (NumberFormatException ex) {
                    textField.setText("Error: Invalid input");
                }
            }
        }

        private double calculate(double firstOperand, double secondOperand, String operator) {
            if (operator == null) {
                throw new IllegalArgumentException("Operator missing");
            }
            switch (operator) {
                case "+":
                    return calculator.add(firstOperand, secondOperand);
                case "-":
                    return calculator.subtract(firstOperand, secondOperand);
                case "*":
                    return calculator.multiply(firstOperand, secondOperand);
                case "/":
                    return calculator.divide(firstOperand, secondOperand);
                default:
                    throw new IllegalArgumentException("Invalid operator: " + operator);
            }
        }
    }



    public static void main(String[] args) {
        new CalculatorGUI(); // Start the calculator GUI
    }
}
