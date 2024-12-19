package com.example.smarthome;

import Server.Config;
import Commands.Elem;
import com.example.smarthome.Commands.GraphicCommander;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class RoomManagement1Controller {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    private Button saveSettingsButton;

    @FXML
    private Button cancelSettingsButton;

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

    private int temperatureInLabel = 18;

    public int getHumidifierInLabel() {
        return humidifierInLabel;
    }

    public void setHumidifierInLabel(int humidifierInLabel) {
        this.humidifierInLabel = humidifierInLabel;
    }

    private int humidifierInLabel = 70;

    public int getPollutionInLabel() {
        return pollutionInLabel;
    }

    public void setPollutionInLabel(int pollutionInLabel) {
        this.pollutionInLabel = pollutionInLabel;
    }

    private int pollutionInLabel = 46;

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
        adminPane.setVisible(Config.getINSTANCE().isAdmin());

        temperatureLabel.setAlignment(Pos.CENTER);
        Timeline temperatureTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> temperatureLabel.setText(temperatureInLabel + new Random().nextInt( 5) - 2 + "°C")));
        temperatureTimeline.setCycleCount(Timeline.INDEFINITE);

        humidifierLabel.setAlignment(Pos.CENTER);
        Timeline humidifierTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> humidifierLabel.setText(humidifierInLabel + new Random().nextInt( 5) - 2 + "%")));
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

        GraphicCommander commander = GraphicCommander.getINSTANCE();

        commander.forMenuClickThermohygrometer(this, temperatureTimeline, humidifierTimeline, pollutionTimeline, temperatureLabel, humidifierLabel, pollutionLabel, thermohygrometerCB, Elem.thermohygrometer, 1980, 400, 140, 200, 1235, 279, 70, 100);

        commander.forMenuClickHeater(this, heaterCB, Elem.heater, 445, 606, 140, 320, 350, 380, 70, 160);

        commander.forMenuClickConditioner(this, conditionerCB, Elem.conditioner, 1556, 0, 210, 120, 978, 79, 105, 60);

        commander.forMenuClickHumidifier(this, humidifierCB, Elem.humidifier, 1065, 380, 170, 170, 706, 270, 85, 85);

        commander.forMenuClickFan(this, fanCB, Elem.fan, 1303, 293, 140, 160, 844, 224, 70, 80);

        commander.forMenuClickRobotCleaner(this, robotCleanerCB, Elem.robot_cleaner, 1975, 710, 200, 150, 1230, 432, 100, 75);

        commander.forMenuClickTime(this, timeCB, Elem.time, 1800, 100, 140, 150, 1130, 149, 70, 75);

        commander.adminQuery(queryButton, adminTextField);

        backButton.setOnAction(actionEvent -> {
            System.out.println("Кнопка Назад");

            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLfiles/ChoosingRoom1.fxml")));
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
                controller.setPreviousFxmlPath("FXMLfiles/RoomManagement1.fxml");

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
