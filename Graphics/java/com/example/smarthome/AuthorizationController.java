package com.example.smarthome;

import Server.Config;
import Server.PackageData;
import Server.ServerData;
import Commands.Elem;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URI;
import java.util.Objects;

public class AuthorizationController {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    private Label errorLabel;

    @FXML
    private Button forgotPassButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registrationButton;

    @FXML
    private Button singInButton;

    @FXML
    private Button supportButton;

//    @FXML
//    private void emailLink() {
//        try {
//            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
//                Desktop.getDesktop().browse(new URI("https://matatova.e@bk.ru?subject=ТЕМА&body=ТЕКСТ"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private boolean authorization() {
        Socket socket;
        try {
            socket = new Socket("127.0.0.1", 8081);

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            ServerData serverdata = new ServerData();
            PackageData packageData = new PackageData();

            packageData.setLogin(loginField.getText());
            packageData.setPassword(passwordField.getText());

            packageData.setElem(Elem.authorization);
            ServerData.Request request = new ServerData.Request(packageData);

            outputStream.write(serverdata.objToBytes(request));

            byte[] arr = new byte[500];
            inputStream.read(arr);

            ServerData.Request answer = (ServerData.Request) serverdata.bytesToObject(arr);
            System.out.println("Ответ от сервера: " + answer.getMessage().getResult());
            if (Objects.equals(answer.getMessage().getResult(), "Успешная авторизация")) {
                Config.getINSTANCE().setAdmin(answer.getMessage().isAdmin());
                Config.getINSTANCE().setLogin(answer.getMessage().getLogin());
                Config.getINSTANCE().setSession(answer.getMessage().getSession());
                return true;
            }
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setText("Неверный логин или пароль");
            return false;
//            return answer.getMessage().getResult();

        } catch (ConnectException e) {
//            errorLabel.setMaxWidth(Double.MAX_VALUE);
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setText("Сервер недоступен");

            System.err.println("Сервер недоступен. Ошибка при соединении с сервером: " + e.getMessage());
            System.err.println("Попробуйте его включить");
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        singInButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка ВОЙТИ");

            errorLabel.setVisible(false);

            if ((loginField.getText() != null && !loginField.getText().isEmpty()) &&
                    (passwordField.getText() != null && !passwordField.getText().isEmpty())) {

                boolean result = authorization();
                if (result) {
                    try {
                        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/CreateRoom.fxml")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), errorLabel);
                    fadeTransition.setFromValue(1);
                    fadeTransition.setToValue(0);
                    errorLabel.setVisible(true);
                    fadeTransition.play();
                }
            } else {
                errorLabel.setAlignment(Pos.CENTER);
                errorLabel.setText("Заполните все поля для ввода");
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), errorLabel);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                errorLabel.setVisible(true);
                fadeTransition.play();
            }


            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(4.3), errorLabel);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
            fadeTransition.setOnFinished(event -> errorLabel.setVisible(false));
            fadeTransition.play();
        });


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

        supportButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Служба поддержки");

//
//            getHostServices().showDocument("<a href=\"mailto:matatova.e@bk.ru?subject=%D0%A2%D0%95%D0%9C%D0%90&body=%D0%9F%D0%B5%D1%80%D0%B2%D0%B0%D1%8F%20%D1%81%D1%82%D1%80%D0%BE%D0%BA%D0%B0%0A%D0%92%D1%82%D0%BE%D1%80%D0%B0%D1%8F%20%D1%81%D1%82%D1%80%D0%BE%D0%BA%D0%B0\">Написать письмо</a>");
//        });
//    }
//        public static void main(String[] args) {
//            launch(args);
//        }
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
                controller.setPreviousFxmlPath("FXMLfiles/Authorization.fxml");

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
    }
}
