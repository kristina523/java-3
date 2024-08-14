package com.example.calculat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private String currentExpression = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = findViewById(R.id.result_text);
    }

    // Обработка нажатия на цифры
    public void onDigitClick(View view) {
        Button button = (Button) view;
        currentExpression += button.getText().toString();
        resultText.setText(currentExpression);
    }

    // Обработка нажатия на операторы
    public void onOperatorClick(View view) {
        Button button = (Button) view;
        currentExpression += " " + button.getText().toString() + " ";
        resultText.setText(currentExpression);
    }

    // Обработка нажатия на точку
    public void onDotClick(View view) {
        if (!currentExpression.endsWith(".")) {
            currentExpression += ".";
            resultText.setText(currentExpression);
        }
    }

    // Обработка нажатия на кнопку равенства
    public void onEqualClick(View view) {
        double result = evaluateExpression(currentExpression);
        resultText.setText(String.valueOf(result));
    }

    // Обработка нажатия на функциональные кнопки (логарифм, степень, корень и процент)
    public void onFunctionClick(View view) {
        Button button = (Button) view;
        String function = button.getText().toString();

        double value = evaluateExpression(currentExpression);

        switch (function) {
            case "log":
                currentExpression = String.valueOf(Math.log10(value));
                break;
            case "√":
                currentExpression = String.valueOf(Math.sqrt(value));
                break;
            case "^":
                currentExpression += " ^ ";
                break;
            case "%":
                currentExpression = String.valueOf(value / 100);
                break;
        }

        resultText.setText(currentExpression);
    }

    // Обработка нажатия на кнопку очистки
    public void onClearClick(View view) {
        currentExpression = "";
        resultText.setText("0");
    }

    // Пример простой логики вычислений
    private double evaluateExpression(String expression) {
        try {
            String[] tokens = expression.split(" ");
            Stack<Double> values = new Stack<>();
            Stack<String> ops = new Stack<>();

            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                if (token.isEmpty())
                    continue;

                if (token.matches("[0-9.]+")) {
                    values.push(Double.parseDouble(token));
                } else if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                    while (!ops.isEmpty() && hasPrecedence(token, ops.peek())) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(token);
                } else if (token.equals("^")) {
                    values.push(Math.pow(values.pop(), Double.parseDouble(tokens[++i])));
                }
            }

            while (!ops.isEmpty()) {
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            }

            return values.pop();
        } catch (Exception e) {
            return 0;
        }
    }

    // Определение приоритета операций
    private boolean hasPrecedence(String op1, String op2) {
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))) {
            return false;
        }
        return true;
    }

    // Применение операции
    private double applyOp(String op, double b, double a) {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) throw new UnsupportedOperationException("Невозможно разделить на ноль");
                return a / b;
        }
        return 0;
    }
}
