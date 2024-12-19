package com.example.smarthome;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Pos;

import java.io.IOException;

public class SupportController {
    @FXML
    private Button thxSupportButton;

    @FXML
    private Label warningLabel;

    // Поле для хранения пути к предыдущему окну
    private String previousFxmlPath;

    // Метод для установки пути к предыдущему окну
    public void setPreviousFxmlPath(String fxmlPath) {
        this.previousFxmlPath = fxmlPath;
    }

    @FXML
    public void initialize() {
        // Добавляем обработчик на кнопку
        thxSupportButton.setOnAction(actionEvent -> startCountdown());
    }

    private void startCountdown() {
        // Массив для отслеживания оставшегося времени
        final int[] secondsLeft = {3}; // Начинаем с 3 секунд

        // Создаем объект PauseTransition
        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        // Настраиваем событие при каждом завершении задержки
        pause.setOnFinished(event -> {
            secondsLeft[0]--; // Уменьшаем счетчик

            if (secondsLeft[0] > 0) {
                // Обновляем текст метки
                warningLabel.setAlignment(Pos.CENTER);
                warningLabel.setText("До закрытия окна: " + secondsLeft[0] + " секунды");
                warningLabel.setStyle("-fx-text-fill: #ac0f0f;");
                pause.play(); // Запускаем следующий цикл
            } else {
                // Закрываем текущее окно
                Stage currentStage = (Stage) thxSupportButton.getScene().getWindow();
                currentStage.close();

                // Показываем предыдущее окно
                showPreviousWindow();
            }
        });

        // Устанавливаем начальное состояние метки и запускаем отсчет
        warningLabel.setAlignment(Pos.CENTER);
        warningLabel.setText("До закрытия окна: 3 секунды");
        warningLabel.setStyle("-fx-text-fill: #ac0f0f;");
        pause.play();
    }

    private void showPreviousWindow() {
        if (previousFxmlPath == null) {
            System.err.println("Путь к предыдущему FXML не задан!");
            return;
        }

        // Загружаем предыдущее окно
        FXMLLoader loader = new FXMLLoader(getClass().getResource(previousFxmlPath));
        try {
            Stage registrationStage = new Stage();
            registrationStage.setScene(new Scene(loader.load()));
            registrationStage.setResizable(false);
            registrationStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
