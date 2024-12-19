package com.example.smarthome;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RecoveryPasswordController {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    private Button authenticationButton;

    @FXML
    private TextField emailField;

    @FXML
    private Button recoveryPassButton;

    @FXML
    private Button registrationButton;

    @FXML
    private Button supportButton;

    @FXML
    private PasswordField verifyField;

    @FXML
    void initialize() {
        registrationButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Регистрация");

            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/Registration.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });

        authenticationButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Авторизация");

            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/Authorization.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });

        recoveryPassButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Восстановить");

            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/Authorization.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
//
//        emailField.setOnAction(actionEvent -> {
//            System.out.println("Кнопка Регистрация");
//
//            Stage currentStage = (Stage) emailField.getScene().getWindow();
//            currentStage.hide();
//
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("FXMLfiles/Authorization.fxml"));
//
//            try {
//                Parent root = loader.load();
//
//                Stage newStage = new Stage();
//                newStage.setScene(new Scene(root));
//                newStage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        verifyField.setOnAction(actionEvent -> {
//            System.out.println("Кнопка Регистрация");
//
//            Stage currentStage = (Stage) verifyField.getScene().getWindow();
//            currentStage.hide();
//
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("FXMLfiles/Authorization.fxml"));
//
//            try {
//                Parent root = loader.load();
//
//                Stage newStage = new Stage();
//                newStage.setScene(new Scene(root));
//                newStage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

        supportButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Служба поддержки");

            // Получаем текущее окно
            Stage currentStage = (Stage) supportButton.getScene().getWindow();
            currentStage.hide(); // Скрываем текущее окно

            // Создаем загрузчик FXML
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLfiles/Support.fxml"));

            try {
                // Загружаем FXML
                Parent root = loader.load();

                // Получаем контроллер SupportController
                SupportController controller = loader.getController();

                // Передаем путь к текущему FXML (или любому другому файлу)
                controller.setPreviousFxmlPath("FXMLfiles/RecoveryPassword.fxml");

                // Создаем новое окно
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));

                // Запрещаем изменение размера окна
                newStage.setResizable(false);

                // Показываем новое окно
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
