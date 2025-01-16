package org.calculator;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Stack;

public class CalculatorGUI {
    private final JTextField textField; // Text field to display input and results
    private final StringBuilder expressionBuilder; // To store the full expression

    // Creates a graphical calculator application with buttons for digits, operators, and clear functionality.
    public CalculatorGUI() {
        expressionBuilder = new StringBuilder(); // Initialize the expression builder

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
        JPanel panel = getJPanel();

        // Add button panel to the frame
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true); // Set visibility after adding components
    }

    private @NotNull JPanel getJPanel() {
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
        return panel;
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.matches("[0-9]")) {
                // If the button pressed is a number, append it to the expression
                expressionBuilder.append(command);
                textField.setText(expressionBuilder.toString());
            } else if (command.matches("[+\\-*/]")) {
                // Append the operator to the expression
                if (expressionBuilder.length() > 0) {
                    expressionBuilder.append(" ").append(command).append(" ");
                    textField.setText(expressionBuilder.toString());
                }
            } else if (command.equals("=")) {
                // Evaluate the full expression
                try {
                    String result = evaluateExpression(expressionBuilder.toString());
                    textField.setText(result);
                    expressionBuilder.setLength(0); // Clear the expression builder
                    expressionBuilder.append(result); // Allow further calculations
                } catch (Exception ex) {
                    textField.setText("Error");
                }
            } else if (command.equals("C")) {
                // Clear the text field and the expression builder
                expressionBuilder.setLength(0);
                textField.setText("");
            }
        }

        private String evaluateExpression(String expression) {
            String[] tokens = expression.split(" ");
            // Why Stack: Operators like * and / have higher precedence than + and -. A stack allows you to temporarily store operators and resolve them in the correct order.
            Stack<Double> values = new Stack<>();
            Stack<String> operators = new Stack<>();

            for (String token : tokens) {
                if (token.matches("[0-9.]+")) {
                    values.push(Double.parseDouble(token));
                } else if (token.matches("[+\\-*/]")) {
                    while (!operators.isEmpty() && precedence(token) <= precedence(operators.peek())) {
                        double b = values.pop();
                        double a = values.pop();
                        String op = operators.pop();
                        values.push(applyOperator(a, b, op));
                    }
                    operators.push(token);
                }
            }

            while (!operators.isEmpty()) {
                double b = values.pop();
                double a = values.pop();
                String op = operators.pop();
                values.push(applyOperator(a, b, op));
            }

            return String.valueOf(values.pop());
        }

        private int precedence(String operator) {
            return switch (operator) {
                case "+", "-" -> 1;
                case "*", "/" -> 2;
                default -> -1;
            };
        }

        private double applyOperator(double a, double b, String operator) {
            return switch (operator) {
                case "+" -> a + b;
                case "-" -> a - b;
                case "*" -> a * b;
                case "/" -> {
                    if (b == 0) throw new ArithmeticException("Cannot divide by zero");
                    yield a / b;
                }
                default -> throw new IllegalArgumentException("Invalid operator");
            };
        }
    }

    public static void main(String[] args) {
        new CalculatorGUI(); // Start the calculator GUI
    }
}
