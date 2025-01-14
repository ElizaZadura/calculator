package org.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGUI {
    private JTextField textField;
    private JFrame frame;

    public CalculatorGUI() {

        frame = new JFrame("Calculator");
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
        frame.setVisible(true); // Set visibility after adding components
    }

    private class ButtonClickListener implements ActionListener {
        private Calculator calculator;
        private String operator;
        private double firstOperand;

        private ButtonClickListener() {
            calculator = new Calculator();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.charAt(0) >= '0' && command.charAt(0) <= '9') {
                // If the button pressed is a number, append it to the text field
                textField.setText(textField.getText() + command);
            } else if (command.equals("C")) {
                // Clear the text field and reset operator and first operand
                textField.setText("");
                operator = null; // Reset operator
                firstOperand = 0; // Reset first operand
            } else if (command.equals("=")) {
                // Perform the calculation
                try {
                    double secondOperand = Double.parseDouble(textField.getText());
                    double result = 0;

                    // Check if an operator was selected
                    if (operator != null) {
                        switch (operator) {
                            case "+":
                                result = calculator.add(firstOperand, secondOperand);
                                break;
                            case "-":
                                result = calculator.subtract(firstOperand, secondOperand);
                                break;
                            case "*":
                                result = calculator.multiply(firstOperand, secondOperand);
                                break;
                            case "/":
                                result = calculator.divide(firstOperand, secondOperand);
                                break;
                        }
                        textField.setText(String.valueOf(result));
                    } else {
                        // Display an error message if no operator was selected
                        textField.setText("Error: No operator selected");
                    }
                } catch (NumberFormatException ex) {
                    textField.setText("Error: Invalid input");
                } catch (ArithmeticException ex) {
                    textField.setText("Error: " + ex.getMessage());
                }
            } else {
                // Store the operator and the first operand for the calculation
                operator = command; // Set the operator
                try {
                    firstOperand = Double.parseDouble(textField.getText());
                    textField.setText(""); // Clear text field for next input
                } catch (NumberFormatException ex) {
                    textField.setText("Error: Invalid input");
                }
            }
        }
    }

    public static void main(String[] args) {
        new CalculatorGUI();
    }
}