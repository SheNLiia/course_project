package com.example.smarthome;

import Server.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ProfileController {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    private Label nickmameLabel;

    @FXML
    private Button logOutButton;

    @FXML
    private Button backButton;

    @FXML
    private Button forgotPassButton;

    @FXML
    private Button supportButton;

//    @FXML
//    void emailLink(ActionEvent event) {
//
//    }

    @FXML
    void initialize() {
        nickmameLabel.setText("Никнейм: " + Config.getINSTANCE().getLogin());

        logOutButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Выбрать первую комнату");
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/HomePage.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });

        backButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка На главную");

            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/CreateRoom.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });

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
                controller.setPreviousFxmlPath("FXMLfiles/Profile.fxml");

                // Создаем новое окно
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));

                newStage.setTitle("SmartHome");

                // Запрещаем изменение размера окна
                newStage.setResizable(false);

                // Показываем новое окно
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        forgotPassButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Забыли пароль");

            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/RecoveryPassword.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }
}
