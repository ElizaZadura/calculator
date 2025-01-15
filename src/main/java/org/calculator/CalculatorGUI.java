package org.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CalculatorGUI {
    private final JTextField textField; // Text field to display input and results

    // Creates a graphical calculator application with buttons for digits, operators, and clear functionality.
    public CalculatorGUI() {
        // Set the native look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main frame for the calculator GUI
        JFrame frame = new JFrame("Calculator");
        textField = new JTextField();
        textField.setEditable(false); // Make the text field read-only
        textField.setFont(new Font("Arial", Font.BOLD, 24)); // Set font for text field
        textField.setHorizontalAlignment(JTextField.RIGHT); // Align text to the right

        // Set up the frame
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Add text field to the frame
        frame.add(textField, BorderLayout.NORTH);

        // Create buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 10, 10)); // Add gaps between buttons
        panel.setBackground(Color.LIGHT_GRAY); // Set background color for the panel

        // Declare and initialize the List of button labels
        List<String> buttonLabels = List.of(
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        );

        // Create buttons and add action listeners
        buttonLabels.forEach(label -> {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 18)); // Set font for buttons
            button.setBackground(Color.WHITE); // Set button background color
            button.setFocusPainted(false); // Remove focus border
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        });

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
                textField.setText(command);
                boolean isNumeric = textField.getText().chars().allMatch(Character::isDigit);

                double firstOperand = 0;
                double secondOperand = 0;

                if (isNumeric && operator == null) {
                    try {
                        firstOperand = Double.parseDouble(textField.getText());
                    } catch (NumberFormatException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println("First Operand: " + firstOperand);
                } else {
                    try {
                        secondOperand = Double.parseDouble(textField.getText());
                        System.out.println("Second Operand: " + secondOperand);
                        result = calculate(firstOperand, secondOperand, operator);
                    } catch (NumberFormatException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println("Result: " + result);
                    textField.setText(String.valueOf(result));
                }

            } else if (command.equals("C")) {
                // Clear the text field and reset operator and result
                textField.setText("");
                operator = null;
                result = 0;
                System.out.println("All values cleared");
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
                    double firstOperand = Double.parseDouble(textField.getText());  // Store the first operand
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
            return switch (operator) {
                case "+" -> calculator.add(firstOperand, secondOperand);
                case "-" -> calculator.subtract(firstOperand, secondOperand);
                case "*" -> calculator.multiply(firstOperand, secondOperand);
                case "/" -> calculator.divide(firstOperand, secondOperand);
                default -> throw new IllegalArgumentException("Invalid operator: " + operator);
            };
        }
    }

    public static void main(String[] args) {
        new CalculatorGUI(); // Start the calculator GUI
    }
}
