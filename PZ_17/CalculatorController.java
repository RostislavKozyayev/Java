package com.example.app_javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CalculatorController {

    @FXML private TextField displayField;

    private double firstOperand = 0;
    private String operator = "";
    private boolean isStart = true;
    private boolean hasOperator = false;

    @FXML
    private void handleButton(javafx.event.ActionEvent event) {
        String value = ((Button) event.getSource()).getText();

        if (isStart) {
            displayField.setText("");
            isStart = false;
            hasOperator = false;
        }

        if (value.matches("[0-9.]")) {
            // Если только что был введен оператор, сбрасываем поле
            if (hasOperator) {
                displayField.setText("");
                hasOperator = false;
            }
            displayField.setText(displayField.getText() + value);
        } else if (value.matches("[+\\-*/]")) {
            if (!displayField.getText().isEmpty()) {
                firstOperand = Double.parseDouble(displayField.getText());
                operator = value;
                hasOperator = true;
            }
        }
    }

    @FXML
    private void handleEquals() {
        if (!operator.isEmpty() && !displayField.getText().isEmpty()) {
            double secondOperand = Double.parseDouble(displayField.getText());
            double result = calculate(firstOperand, secondOperand, operator);

            // Форматирование результата (убираем .0 если число целое)
            if (result == (int) result) {
                displayField.setText(String.valueOf((int) result));
            } else {
                // Ограничиваем количество знаков после запятой
                displayField.setText(String.format("%.6f", result).replaceAll("0*$", "").replaceAll("\\.$", ""));
            }

            operator = "";
            isStart = true;
            hasOperator = false;
        }
    }

    @FXML
    private void handleClear() {
        displayField.setText("0");
        firstOperand = 0;
        operator = "";
        isStart = true;
        hasOperator = false;
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b != 0) {
                    return a / b;
                } else {
                    displayField.setText("Ошибка: деление на 0");
                    return 0;
                }
            default: return 0;
        }
    }
}