package com.example.randomnumgen;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label number; // Label для отображения случайного числа

    @FXML
    private TextField minRange; // Поле для ввода минимального значения диапазона

    @FXML
    private TextField maxRange; // Поле для ввода максимального значения диапазона

    @FXML
    private TextField numberListField; // Поле для ввода списка чисел

    @FXML
    private TextField excludedNumbersField; // Поле для ввода исключаемых чисел

    @FXML
    private CheckBox excludeNumbersCheckBox; // CheckBox для исключения чисел

    @FXML
    private CheckBox excludeRepetitionsCheckBox; // CheckBox для исключения повторений

    @FXML
    private RadioButton rangeRadioButton; // RadioButton для выбора диапазона

    @FXML
    private RadioButton listRadioButton; // RadioButton для выбора списка

    @FXML
    private ToggleGroup sourceToggleGroup; // ToggleGroup для RadioButton

    @FXML
    private Button create; // Кнопка для генерации числа

    private Set<Integer> generatedNumbers = new HashSet<>(); // Set для хранения сгенерированных чисел
    private boolean allUniqueRepeated = false; // Флаг, указывающий, что все уникальные числа повторились

    @FXML
    void initialize() {
        assert number != null : "fx:id=\"number\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert minRange != null : "fx:id=\"minRange\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert maxRange != null : "fx:id=\"maxRange\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert numberListField != null : "fx:id=\"numberListField\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert excludedNumbersField != null : "fx:id=\"excludedNumbersField\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert excludeNumbersCheckBox != null : "fx:id=\"excludeNumbersCheckBox\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert excludeRepetitionsCheckBox != null : "fx:id=\"excludeRepetitionsCheckBox\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert rangeRadioButton != null : "fx:id=\"rangeRadioButton\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert listRadioButton != null : "fx:id=\"listRadioButton\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert sourceToggleGroup != null : "fx:id=\"sourceToggleGroup\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert create != null : "fx:id=\"create\" was not injected: check your FXML file 'hello-view.fxml'.";

        // Инициализация обработчиков событий
        rangeRadioButton.setOnAction(event -> updateVisibility());
        listRadioButton.setOnAction(event -> updateVisibility());
        excludeNumbersCheckBox.setOnAction(event -> updateVisibility());
        create.setOnAction(event -> generateRandomNumber());

        // Начальная установка видимости полей
        updateVisibility();
    }

    /**
     * Обновляет видимость полей ввода в зависимости от выбранных опций.
     */
    private void updateVisibility() {
        boolean isRangeSelected = rangeRadioButton.isSelected();
        boolean isListSelected = listRadioButton.isSelected();
        boolean isExcludeNumbersChecked = excludeNumbersCheckBox.isSelected();

        // Поля для диапазона
        minRange.setVisible(isRangeSelected);
        maxRange.setVisible(isRangeSelected);

        // Поле для списка чисел
        numberListField.setVisible(isListSelected);

        // Поле для исключаемых чисел
        excludedNumbersField.setVisible(isExcludeNumbersChecked);
    }

    /**
     * Метод для генерации случайного числа в заданном диапазоне или из списка и отображения его в Label.
     */
    private void generateRandomNumber() {
        try {
            if (rangeRadioButton.isSelected()) {
                // Генерация из диапазона
                int min = Integer.parseInt(minRange.getText());
                int max = Integer.parseInt(maxRange.getText());

                // Получаем список исключаемых чисел
                List<Integer> excludedNumbers = new ArrayList<>();
                if (excludeNumbersCheckBox.isSelected() && excludedNumbersField.getText() != null && !excludedNumbersField.getText().isEmpty()) {
                    excludedNumbers = Arrays.stream(excludedNumbersField.getText().split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                }

                // Создаем список доступных чисел для генерации
                List<Integer> availableNumbers = new ArrayList<>();
                for (int i = min; i <= max; i++) {
                    if (!excludedNumbers.contains(i)) {
                        availableNumbers.add(i);
                    }
                }

                // Исключаем повторения, если установлен флажок
                if (excludeRepetitionsCheckBox.isSelected()) {
                    availableNumbers.removeAll(generatedNumbers);
                }

                if (availableNumbers.isEmpty()) {
                    if (!allUniqueRepeated) {
                        number.setText("Все уникальные значения повторились!");
                        allUniqueRepeated = true;
                        generatedNumbers.clear(); // Очищаем историю, чтобы начать заново
                        return;
                    } else {
                        generatedNumbers.clear(); // Очищаем историю и генерируем заново
                        allUniqueRepeated = false;
                        // Восстанавливаем список доступных чисел после очистки generatedNumbers
                        for (int i = min; i <= max; i++) {
                            if (!excludedNumbers.contains(i)) {
                                availableNumbers.add(i);
                            }
                        }
                    }
                }
                allUniqueRepeated = false;

                Random random = new Random();
                int randomIndex = random.nextInt(availableNumbers.size());
                int randomNumber = availableNumbers.get(randomIndex);
                number.setText(String.valueOf(randomNumber));

                generatedNumbers.add(randomNumber); // Добавляем число в список сгенерированных
            } else if (listRadioButton.isSelected()) {
                // Генерация из списка
                String numbersText = numberListField.getText();
                List<Integer> numbers = Arrays.stream(numbersText.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                int min = Integer.parseInt(minRange.getText());
                int max = Integer.parseInt(maxRange.getText());

                // Получаем список исключаемых чисел
                List<Integer> excludedNumbers = new ArrayList<>();
                if (excludeNumbersCheckBox.isSelected() && excludedNumbersField.getText() != null && !excludedNumbersField.getText().isEmpty()) {
                    excludedNumbers = Arrays.stream(excludedNumbersField.getText().split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                }

                numbers.removeAll(excludedNumbers);

                List<Integer> availableNumbers = new ArrayList<>();
                for (int i = min; i <= max; i++) {
                    if (!excludedNumbers.contains(i)) {
                        availableNumbers.add(i);
                    }
                }

                // Исключаем повторения, если установлен флажок
                if (excludeRepetitionsCheckBox.isSelected()) {
                    availableNumbers.removeAll(generatedNumbers);
                }

                if (availableNumbers.isEmpty()) {
                    if (!allUniqueRepeated) {
                        number.setText("Все уникальные значения повторились!");
                        allUniqueRepeated = true;
                        generatedNumbers.clear(); // Очищаем историю, чтобы начать заново
                        return;
                    } else {
                        generatedNumbers.clear(); // Очищаем историю и генерируем заново
                        allUniqueRepeated = false;
                        // Восстанавливаем список доступных чисел после очистки generatedNumbers
                        for (int i = min; i <= max; i++) {
                            if (!excludedNumbers.contains(i)) {
                                availableNumbers.add(i);
                            }
                        }
                    }
                }
                allUniqueRepeated = false;

                if (numbers.isEmpty()) {
                    number.setText("Список пуст");
                    return;
                }

                Random random = new Random();
                int randomIndex = random.nextInt(numbers.size());
                number.setText(String.valueOf(numbers.get(randomIndex)));
            } else {
                number.setText("Выберите источник");
            }
        } catch (NumberFormatException e) {
            number.setText("Ошибка ввода");
        }
    }
}
