package com.example.smarthome;

import Server.Config;
import Server.PackageData;
import Server.ServerData;
import Commands.Elem;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class RegistrationController {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    private ComboBox<String> sexComboBox;

    @FXML
    private Button thxRegistrationButton;

    @FXML
    private Label warningLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Button authenticationButton;

    @FXML
    private DatePicker dateOfBirthField;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button singUpButton;

    @FXML
    private Button supportButton;

    // Поле для хранения пути к предыдущему окну
    private String previousFxmlPath;

    // Метод для установки пути к предыдущему окну
    public void setPreviousFxmlPath(String fxmlPath) {
        this.previousFxmlPath = fxmlPath;
    }

    @FXML
    private boolean registration() {
        Socket socket;
        try {
            socket = new Socket("127.0.0.1", 8081);

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            ServerData serverdata = new ServerData();
            PackageData packageData = new PackageData();

            packageData.setLogin(loginField.getText());
            packageData.setPassword(passwordField.getText());
            packageData.setSex((String) sexComboBox.getValue());

            packageData.setElem(Elem.registration);
            ServerData.Request request = new ServerData.Request(packageData);

            outputStream.write(serverdata.objToBytes(request));

            byte[] arr = new byte[500];
            inputStream.read(arr);

            ServerData.Request answer = (ServerData.Request) serverdata.bytesToObject(arr);
            System.out.println("Ответ от сервера: " + answer.getMessage().getResult());
            if (Objects.equals(answer.getMessage().getResult(), "Пользователь успешно создан")) {
                Config.getINSTANCE().setLogin(answer.getMessage().getLogin());
                Config.getINSTANCE().setSession(answer.getMessage().getSession());

            return true;
            }
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setText("Такой логин уже используется");
            return false;
//            return answer.getMessage().getResult();

        } catch (ConnectException e) {
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setText("Сервер недоступен");

            System.err.println("Сервер недоступен. Ошибка при соединении с сервером:: " + e.getMessage());
            System.err.println("Попробуйте его включить");
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @FXML
//    private boolean ValidateEmail() {
//        String email = emailField.getText();
//        return email.matches("\\b[\\w.-]+@[\\w.-]+\\.\\w{2,4}\\b");
//    }

    @FXML
    void initialize() {
//        supportButton.setOnAction(actionEvent -> {
//            System.out.println("Кнопка Войти под регистрацией");
//
//            try {
//                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/Support.fxml")));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//            scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        });

        singUpButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Зарегистрироваться");

            errorLabel.setVisible(false);

            if ((loginField.getText() != null && !loginField.getText().isEmpty()) &&
                    (passwordField.getText() != null && !passwordField.getText().isEmpty())) {

                boolean result = registration();  // Предполагаем, что registration() возвращает результат

                if (result) {
                    try {
                        System.out.println("Кнопка Служба поддержки");

                        // Получаем текущее окно
                        Stage currentStage = (Stage) supportButton.getScene().getWindow();
                        currentStage.hide();  // Скрываем текущее окно (Регистрация)

                        // Создаем загрузчик FXML для окна "Support"
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLfiles/SuccessfulRegistration.fxml"));

                        // Загружаем FXML
                        Parent root = loader.load();

                        // Получаем контроллер SuccessfulRegistrationController
                        SuccessfulRegistrationController controller = loader.getController();

                        // Передаем путь к предыдущему FXML (в данном случае Registration)
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
                } else {
//                    TranslateTransition translateTransition = new TranslateTransition();
//                    PathTransition pathTransition = new PathTransition();
//                    pathTransition.setPath(new Path());

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        dateOfBirthField.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);
                } else {
                    return null;
                }
            }
        });

        dateOfBirthField.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

            String cleanInput = newValue.replaceAll("\\D", "");

            if (cleanInput.length() >= 2) {
                cleanInput = cleanInput.substring(0, 2) + "." + cleanInput.substring(2);
            }
            if (cleanInput.length() >= 5) {
                cleanInput = cleanInput.substring(0, 5) + "." + cleanInput.substring(5);
            }

            if (cleanInput.length() > 10) {
                cleanInput = cleanInput.substring(0, 10);
            }

            if (!newValue.equals(cleanInput)) {
                dateOfBirthField.getEditor().setText(cleanInput);
                dateOfBirthField.getEditor().positionCaret(cleanInput.length());
            }
        });

        dateOfBirthField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dateOfBirthField.getEditor().setText(formatter.format(newValue));
            }
        });

        sexComboBox.getItems().setAll("Мужской", "Женский");
        sexComboBox.getSelectionModel().select("Мужской");

        sexComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            System.out.println("Выбранный пол: " + newValue);

            if (oldValue != null) {
                System.out.println("Предыдущее значение: " + oldValue);
            }
        });

        authenticationButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Войти под регистрацией");

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
                controller.setPreviousFxmlPath("FXMLfiles/Registration.fxml");

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

