package com.example.smarthome;

import Commands.Response;
import Server.Config;
import javafx.animation.*;
import Commands.Elem;
import com.example.smarthome.Commands.GraphicCommander;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class RoomManagement2Controller {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    private Button saveSettingsButton;

    @FXML
    private Button devicesButton;

    @FXML
    private Label pollutionLabel;

    @FXML
    private Pane adminPane;

    @FXML
    private Button queryButton;

    @FXML
    private TextField adminTextField;

    @FXML
    private ScrollPane menuScrollPane;

    @FXML
    private Label humidifierLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private CheckBox timeCB;

    @FXML
    private CheckBox thermohygrometerCB;

    @FXML
    private CheckBox robotCleanerCB;

    @FXML
    private CheckBox humidifierCB;

    @FXML
    private CheckBox heaterCB;

    @FXML
    private CheckBox fanCB;

    @FXML
    private CheckBox conditionerCB;

    @FXML
    private Pane tempPane;

    @FXML
    private Button backButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button supportButton;

    public int getTemperatureInLabel() {
        return temperatureInLabel;
    }

    public void setTemperatureInLabel(int temperatureInLabel) {
        this.temperatureInLabel = temperatureInLabel;
    }

    private int temperatureInLabel = 15;

    public int getHumidifierInLabel() {
        return humidifierInLabel;
    }

    public void setHumidifierInLabel(int humidifierInLabel) {
        this.humidifierInLabel = humidifierInLabel;
    }

    private int humidifierInLabel = 55;

    public int getPollutionInLabel() {
        return pollutionInLabel;
    }

    public void setPollutionInLabel(int pollutionInLabel) {
        this.pollutionInLabel = pollutionInLabel;
    }

    private int pollutionInLabel = 30;

    private void updatePollution(Object roomControllers, int delta) {
        if (roomControllers instanceof RoomManagement1Controller rm1Controller &&
                rm1Controller.getPollutionInLabel() < 100) {
            rm1Controller.setPollutionInLabel(rm1Controller.getPollutionInLabel() + delta);
        } else if (roomControllers instanceof RoomManagement2Controller rm2Controller &&
                rm2Controller.getPollutionInLabel() < 100) {
            rm2Controller.setPollutionInLabel(rm2Controller.getPollutionInLabel() + delta);
        }
    }
    @FXML
    void initialize() {
        devicesButton.setOnMouseClicked(event -> {
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), menuScrollPane);

            if (menuScrollPane.isVisible()) {
                // Если меню уже видно, то плавно исчезаем
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                fadeTransition.setOnFinished(e -> menuScrollPane.setVisible(false));  // Скрываем после завершения анимации
            } else {
                // Если меню скрыто, то плавно показываем
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                menuScrollPane.setVisible(true);  // Показываем перед анимацией
            }

            fadeTransition.play();
        });


        adminPane.setVisible(Config.getINSTANCE().isAdmin());

        temperatureLabel.setAlignment(Pos.CENTER);
        Timeline temperatureTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> temperatureLabel.setText(temperatureInLabel + new Random().nextInt(5) - 2 + "°C")));
        temperatureTimeline.setCycleCount(Timeline.INDEFINITE);

        humidifierLabel.setAlignment(Pos.CENTER);
        Timeline humidifierTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> humidifierLabel.setText(humidifierInLabel + new Random().nextInt(5) - 2 + "%")));
        humidifierTimeline.setCycleCount(Timeline.INDEFINITE);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> updatePollution(this, 5)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        pollutionLabel.setAlignment(Pos.CENTER);
        Timeline pollutionTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            int value = pollutionInLabel + new Random().nextInt(10) - 2;
                if (value <= 100 && value > 0) pollutionLabel.setText(value + "%");
                else if (value <= 0) pollutionLabel.setText("0%");
                else pollutionLabel.setText("100%");
        }));
        pollutionTimeline.setCycleCount(Timeline.INDEFINITE);

        //        saveSettingsButton.setOnMouseClicked(event -> {
        File file = new File("Property");
        try {
            Scanner input = new Scanner(file);
            if (input.hasNextLine() && Objects.equals(input.nextLine(), "true")) {
                Timeline setSceneTimeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event ->
                        GraphicCommander.getINSTANCE().setScene(this, temperatureTimeline, humidifierTimeline, pollutionTimeline, temperatureLabel, humidifierLabel, pollutionLabel, thermohygrometerCB, Response.thermohygrometer, 2000, 400, 140, 200, 1178, 279, 70, 100)
                ));
                setSceneTimeline.setCycleCount(1);
                setSceneTimeline.play();
                thermohygrometerCB.fire();
            }
        } catch (FileNotFoundException e) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
//        });

        saveSettingsButton.setOnMouseClicked(event -> {
            PrintWriter writer;
            try {
                writer = new PrintWriter("Property", StandardCharsets.UTF_8);
            } catch (IOException e) {
                try {
                    file.createNewFile();
                    writer = new PrintWriter("Property", StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            writer.println(thermohygrometerCB.isSelected());
            writer.println(heaterCB.isSelected());
            writer.println(conditionerCB.isSelected());
            writer.println(humidifierCB.isSelected());
            writer.println(fanCB.isSelected());
            writer.println(robotCleanerCB.isSelected());
            writer.println(timeCB.isSelected());
            writer.close();
        });

        GraphicCommander commander = GraphicCommander.getINSTANCE();

        commander.forMenuClickThermohygrometer(this, temperatureTimeline, humidifierTimeline, pollutionTimeline, temperatureLabel, humidifierLabel, pollutionLabel, thermohygrometerCB, Elem.thermohygrometer, 2000, 400, 140, 200, 1178, 279, 70, 100);

        commander.forMenuClickHeater(this, heaterCB, Elem.heater, 1300, 800, 210, 200, 828, 479, 105, 100);

        commander.forMenuClickConditioner(this, conditionerCB, Elem.conditioner, 800, 0, 280, 280, 578, 79, 140, 140);

        commander.forMenuClickHumidifier(this, humidifierCB, Elem.humidifier, 1212, 338, 50, 50, 788, 250, 25, 25);

        commander.forMenuClickFan(this, fanCB, Elem.fan, 700, 200, 400, 400, 528, 179, 200, 200);

        commander.forMenuClickRobotCleaner(this, robotCleanerCB, Elem.robot_cleaner, 1900, 860, 200, 200, 1128, 509, 100, 100);

        commander.forMenuClickTime(this, timeCB, Elem.time, 2000, 140, 200, 240, 1178, 149, 100, 120);

        commander.adminQuery(queryButton, adminTextField);

        backButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Назад");

            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/ChoosingRoom2.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });

        profileButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Профиль");

            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/Profile.fxml")));
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
                controller.setPreviousFxmlPath("FXMLfiles/RoomManagement2.fxml");

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

