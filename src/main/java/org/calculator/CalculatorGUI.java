package org.calculator;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CalculatorGUI {
    private final JTextField textField; // Text field and label to display operator, input, and results
    private final JLabel oLabel;
    private String operator;
    private double firstOperand;
    private double secondOperand;
    private double result;

    // Creates a graphical calculator application with buttons for digits, operators, and clear functionality.
    public CalculatorGUI() {
        // Set the native look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //should be replaced with a more robust error handling
            e.printStackTrace();
        }

        // Main frame for the calculator GUI
        JFrame frame = new JFrame("Calculator");
        textField = new JTextField();
        textField.setEditable(false); // Make the text field read-only
        textField.setFont(new Font("Arial", Font.BOLD, 24)); // Set font for text field
        textField.setHorizontalAlignment(JTextField.RIGHT); // Align text to the right

        oLabel = new JLabel("");

        //for displaying the current operator
        oLabel.setOpaque(true);

        // Set up the frame
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Add text field and label to the frame
        frame.add(textField, BorderLayout.NORTH);
        frame.add(oLabel, BorderLayout.SOUTH);

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
        private final Calculator calculator = new Calculator();
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            System.out.println("Button pressed: " + command);

/*            double result = 0;
            double firstOperand = 0;
            double secondOperand=0;*/

            if (command.charAt(0) >= '0' && command.charAt(0) <= '9') {
                // If the button pressed is a number, append it to the text field
                textField.setText(command);
                boolean isNumeric = textField.getText().chars().allMatch(Character::isDigit);
                //if the operator is set
                if (isNumeric && operator == null) {
                    try {
                        firstOperand = Double.parseDouble(textField.getText());
                    } catch (NumberFormatException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println("First Operand: " + firstOperand);
                } else  {
                    try {
                        secondOperand = Double.parseDouble(textField.getText());
                        System.out.println("Second Operand: " + secondOperand);
                    } catch (NumberFormatException ex) {
                        throw new RuntimeException(ex);
                    }

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
                    secondOperand = Double.parseDouble(textField.getText());
                    result = calculate(firstOperand, secondOperand, operator);
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
                    firstOperand = Double.parseDouble(textField.getText());  // Store the first operand
                    operator = command;
                    oLabel.setText(operator);
                    textField.setText("");
                    System.out.println("Operator set to: " + operator);
                } catch (NumberFormatException ex) {
                    textField.setText("Error: Invalid input");
                }
            }
            System.out.println("Resume execution...");
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
