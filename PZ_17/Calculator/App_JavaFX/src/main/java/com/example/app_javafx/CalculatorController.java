package com.example.app_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CalculatorController {
    @FXML private TextField displayField;

    private double firstOperand = 0;
    private String operator = "";
    private boolean isStart = true;

    @FXML
    private void handleButton(javafx.event.ActionEvent event) {
        String value = ((javafx.scene.control.Button)
                event.getSource()).getText();

        if (isStart) {
            displayField.setText("");
            isStart = false;
        }

        if (value.matches("[0-9.]")) {
            displayField.setText(displayField.getText() + value);
        } else if (value.matches("[+\\-*/]")) {
            firstOperand = Double.parseDouble(displayField.getText());
            operator = value;
            isStart = true;
        }
    }

    @FXML
    private void handleEquals() {
        if (!operator.isEmpty()) {
            double secondOperand =
                    Double.parseDouble(displayField.getText());
            double result = calculate(firstOperand, secondOperand, operator);
            displayField.setText(String.valueOf(result));
            operator = "";
            isStart = true;
        }
    }

    @FXML
    private void handleClear() {
        displayField.setText("0");
        firstOperand = 0;
        operator = "";
        isStart = true;
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b != 0) return a / b;
                else {
                    displayField.setText("Ошибка: деление на 0");
                    return 0;
                }
            default: return 0;
        }
    }
}
