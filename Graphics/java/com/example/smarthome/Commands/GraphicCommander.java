package com.example.smarthome.Commands;

import Server.Config;
import Server.PackageData;
import Server.ServerData;
import Commands.*;
import com.example.smarthome.RoomManagement1Controller;
import com.example.smarthome.RoomManagement2Controller;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;


public class GraphicCommander {
    private static GraphicCommander INSTANCE;

    public static GraphicCommander getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new GraphicCommander();
        return INSTANCE;
    }

    private static final String imagePath1 = "com/example/smarthome/Pictures/Rooms/FullFirstRoom.png";
    private static final String imagePath2 = "com/example/smarthome/Pictures/Rooms/FullSecondRoom.png";

    private String query;

    private final HashMap<CheckBox, ImageView> menuCheckBoxImageViewHashMap = new HashMap<>();

    private void updateTemperature(Object roomControllers, int delta) {
        if (roomControllers instanceof RoomManagement1Controller rm1Controller) {
            rm1Controller.setTemperatureInLabel(rm1Controller.getTemperatureInLabel() + delta);
        } else if (roomControllers instanceof RoomManagement2Controller rm2Controller) {
            rm2Controller.setTemperatureInLabel(rm2Controller.getTemperatureInLabel() + delta);
        }
    }

    private void updateHumidifier(Object roomControllers, int delta) {
        if (roomControllers instanceof RoomManagement1Controller rm1Controller) {
            rm1Controller.setHumidifierInLabel(rm1Controller.getHumidifierInLabel() + delta);
        } else if (roomControllers instanceof RoomManagement2Controller rm2Controller) {
            rm2Controller.setHumidifierInLabel(rm2Controller.getHumidifierInLabel() + delta);
        }
    }

    private void updatePollution(Object roomControllers) {
        if (roomControllers instanceof RoomManagement1Controller rm1Controller &&
                rm1Controller.getPollutionInLabel() > 0) {
            rm1Controller.setPollutionInLabel(rm1Controller.getPollutionInLabel() + -15);
        } else if (roomControllers instanceof RoomManagement2Controller rm2Controller &&
                rm2Controller.getPollutionInLabel() > 0) {
            rm2Controller.setPollutionInLabel(rm2Controller.getPollutionInLabel() + -15);
        }
    }

    public void adminQuery(Button queryButton, TextField query) {
        queryButton.setOnMouseClicked(actionEvent -> {
            System.out.println("Запрос");
            this.query = query.getText();
            runCommand(Elem.admin);
        });
    }


    public void forMenuClickThermohygrometer(Object roomControllers, Timeline temperatureTimeline, Timeline humidifierTimeline, Timeline pollutionTimeline, Label temperatureLabel, Label humidifierLabel, Label pollutionLabel, CheckBox menucheckBox, Elem elem, double x, double y, double width, double height, double setX, double setY, double fitWidth, double fitHeight) {
        menucheckBox.setOnMouseClicked(actionEvent -> {
            setScene(roomControllers, temperatureTimeline, humidifierTimeline, pollutionTimeline, temperatureLabel, humidifierLabel, pollutionLabel, menucheckBox, runCommand(elem), x, y, width, height, setX, setY, fitWidth, fitHeight);
        });
    }

    public void setScene(Object roomControllers, Timeline temperatureTimeline, Timeline humidifierTimeline, Timeline pollutionTimeline, Label temperatureLabel, Label humidifierLabel, Label pollutionLabel, CheckBox menucheckBox, Response response, double x, double y, double width, double height, double setX, double setY, double fitWidth, double fitHeight) {
        Pane root = (Pane) menucheckBox.getScene().getRoot();
        ImageView imageView = menuCheckBoxImageViewHashMap.computeIfAbsent(menucheckBox, checkBox -> {
            String imagePath = (roomControllers instanceof RoomManagement1Controller) ? imagePath1 : imagePath2;
            return new ImageView(new Image(imagePath));
        });

        if (response != Response.thermohygrometer) {
            System.out.println("Response: " + response);
            menucheckBox.fire();

            Text errorDevicesText = new Text("Вы не приобрели данное устройство");
            errorDevicesText.setLayoutX(25.0);
            errorDevicesText.setLayoutY(580.0);
            errorDevicesText.setFont(new Font(15.0));

            errorDevicesText.setFill(Color.RED);

            errorDevicesText.setStroke(Color.BLACK);
            errorDevicesText.setStrokeWidth(0.3);

            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.3), errorDevicesText);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
            errorDevicesText.setVisible(true);
            fadeTransition.play();

            root.getChildren().add(errorDevicesText);

            return;
        }

        if (!root.getChildren().contains(imageView)) {
            root.getChildren().add(imageView);

            imageView.setX(setX);
            imageView.setY(setY);
            imageView.setFitWidth(fitWidth);
            imageView.setFitHeight(fitHeight);
            imageView.setViewport(new Rectangle2D(x, y, width, height));

            temperatureTimeline.play();
            humidifierTimeline.play();
            pollutionTimeline.play();
            temperatureLabel.setVisible(true);
            humidifierLabel.setVisible(true);
            pollutionLabel.setVisible(true);
        } else {
            root.getChildren().remove(imageView);

            temperatureTimeline.stop();
            humidifierTimeline.stop();
            pollutionTimeline.stop();
            temperatureLabel.setVisible(false);
            humidifierLabel.setVisible(false);
            pollutionLabel.setVisible(false);
        }
    }

    Timeline timeline;
    public void forMenuClickRobotCleaner(Object roomControllers, CheckBox menucheckBox, Elem elem, double x, double y, double width, double height, double setX, double setY, double fitWidth, double fitHeight) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> updatePollution(roomControllers)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        menucheckBox.setOnMouseClicked(actionEvent -> {
            Pane root = (Pane) menucheckBox.getScene().getRoot();
            ImageView imageView = menuCheckBoxImageViewHashMap.computeIfAbsent(menucheckBox, checkBox -> {
                String imagePath = (roomControllers instanceof RoomManagement1Controller) ? imagePath1 : imagePath2;
                return new ImageView(new Image(imagePath));
            });

            Response response = runCommand(elem);
            System.out.println("Response: " + response);

            if (response != Response.robot_cleaner) {
                menucheckBox.fire();

                Text errorDevicesText = new Text("Вы не приобрели данное устройство");
                errorDevicesText.setLayoutX(25.0);
                errorDevicesText.setLayoutY(580.0);
                errorDevicesText.setFont(new Font(15.0));

                errorDevicesText.setFill(Color.RED);

                errorDevicesText.setStroke(Color.BLACK);
                errorDevicesText.setStrokeWidth(0.3);

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.3), errorDevicesText);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                errorDevicesText.setVisible(true);
                fadeTransition.play();

                root.getChildren().add(errorDevicesText);

                return;
            }

            if (!root.getChildren().contains(imageView)) {
                root.getChildren().add(imageView);

                imageView.setX(setX);
                imageView.setY(setY);
                imageView.setFitWidth(fitWidth);
                imageView.setFitHeight(fitHeight);
                imageView.setViewport(new Rectangle2D(x, y, width, height));
                timeline.play();

            } else {
                root.getChildren().remove(imageView);
                timeline.stop();
            }
        });
    }

    public void forMenuClickTime(Object roomControllers, CheckBox menucheckBox, Elem elem, double x, double y, double width, double height, double setX, double setY, double fitWidth, double fitHeight) {
        menucheckBox.setOnMouseClicked(actionEvent -> {
            Pane root = (Pane) menucheckBox.getScene().getRoot();
            ImageView imageView = menuCheckBoxImageViewHashMap.computeIfAbsent(menucheckBox, checkBox -> {
                String imagePath = (roomControllers instanceof RoomManagement1Controller) ? imagePath1 : imagePath2;
                return new ImageView(new Image(imagePath));
            });

            Response response = runCommand(elem);
            System.out.println("Response: " + response);

            if (response != Response.time) {
                menucheckBox.fire();

                Text errorDevicesText = new Text("Вы не приобрели данное устройство");
                errorDevicesText.setLayoutX(25.0);
                errorDevicesText.setLayoutY(580.0);
                errorDevicesText.setFont(new Font(15.0));

                errorDevicesText.setFill(Color.RED);

                errorDevicesText.setStroke(Color.BLACK);
                errorDevicesText.setStrokeWidth(0.3);

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.3), errorDevicesText);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                errorDevicesText.setVisible(true);
                fadeTransition.play();

                root.getChildren().add(errorDevicesText);

                return;
            }

            // Проверяем, есть ли галочка в CheckBox
            if (menucheckBox.isSelected()) {
                // Добавляем изображение
                root.getChildren().add(imageView);
                imageView.setX(setX);
                imageView.setY(setY);
                imageView.setFitWidth(fitWidth);
                imageView.setFitHeight(fitHeight);
                imageView.setViewport(new Rectangle2D(x, y, width, height));

                // Создаем Label для отображения времени
                Label timeLabel = new Label();
                root.getChildren().add(timeLabel);

                if (roomControllers instanceof RoomManagement1Controller) {
                    // Координаты и оформление для RoomManagement1Controller
                    timeLabel.setLayoutX(1221.0);
                    timeLabel.setLayoutY(17.0);
                    timeLabel.setPrefHeight(33.0);
                    timeLabel.setPrefWidth(361.0);
                    timeLabel.setTextAlignment(TextAlignment.CENTER);
                    timeLabel.setTextFill(Color.web("#2f5597"));
                    timeLabel.setFont(Font.font("Ink Free", 24.0));
                } else {
                    // Координаты и оформление для другого класса
                    timeLabel.setLayoutX(14.0);
                    timeLabel.setLayoutY(18.0);
                    timeLabel.setPrefHeight(33.0);
                    timeLabel.setPrefWidth(355.0);
                    timeLabel.setTextAlignment(TextAlignment.CENTER);
                    timeLabel.setTextFill(Color.web("#2f5597"));
                    timeLabel.setFont(Font.font("Ink Free", 24.0));
                }

                // Создаем Timeline для обновления времени каждую секунду
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                    timeLabel.setText("Текущее время: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();

            } else {
                // Удаляем изображение и Label, если галочка снята
                root.getChildren().remove(imageView);
                root.getChildren().removeIf(node -> node instanceof Label && node.getLayoutX() == 1221.0 && node.getLayoutY() == 17.0);
            }
        });
    }


    public void forMenuClickHumidifier(Object roomControllers, CheckBox menucheckBox, Elem elem, double x, double y, double width, double height, double setX, double setY, double fitWidth, double fitHeight) {
        menucheckBox.setOnMouseClicked(actionEvent -> {
            Pane root = (Pane) menucheckBox.getScene().getRoot();
            ImageView imageView = menuCheckBoxImageViewHashMap.computeIfAbsent(menucheckBox, checkBox -> {
                String imagePath = (roomControllers instanceof RoomManagement1Controller) ? imagePath1 : imagePath2;
                return new ImageView(new Image(imagePath));
            });

            Response response = runCommand(elem);
            System.out.println("Response: " + response);

            if (response != Response.humidifier) {
                menucheckBox.fire();

                Text errorDevicesText = new Text("Вы не приобрели данное устройство");
                errorDevicesText.setLayoutX(25.0);
                errorDevicesText.setLayoutY(580.0);
                errorDevicesText.setFont(new Font(15.0));

                errorDevicesText.setFill(Color.RED);

                errorDevicesText.setStroke(Color.BLACK);
                errorDevicesText.setStrokeWidth(0.3);

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.3), errorDevicesText);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                errorDevicesText.setVisible(true);
                fadeTransition.play();

                root.getChildren().add(errorDevicesText);

                return;
            }

            int a;
            if (!root.getChildren().contains(imageView)) {
                root.getChildren().add(imageView);
                a = 3;
                imageView.setX(setX);
                imageView.setY(setY);
                imageView.setFitWidth(fitWidth);
                imageView.setFitHeight(fitHeight);
                imageView.setViewport(new Rectangle2D(x, y, width, height));

            } else {
                a = -3;
                root.getChildren().remove(imageView);
            }

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> updateHumidifier(roomControllers, a)));
            timeline.setCycleCount(5);
            timeline.play();
        });
    }

    public void forMenuClickFan(Object roomControllers, CheckBox menucheckBox, Elem elem, double x, double y, double width, double height, double setX, double setY, double fitWidth, double fitHeight) {
        menucheckBox.setOnMouseClicked(actionEvent -> {
            Pane root = (Pane) menucheckBox.getScene().getRoot();
            ImageView imageView = menuCheckBoxImageViewHashMap.computeIfAbsent(menucheckBox, checkBox -> {
                String imagePath = (roomControllers instanceof RoomManagement1Controller) ? imagePath1 : imagePath2;
                return new ImageView(new Image(imagePath));
            });

            Response response = runCommand(elem);
            System.out.println("Response: " + response);

            if (response != Response.fan) {
                menucheckBox.fire();

                Text errorDevicesText = new Text("Вы не приобрели данное устройство");
                errorDevicesText.setLayoutX(25.0);
                errorDevicesText.setLayoutY(580.0);
                errorDevicesText.setFont(new Font(15.0));

                errorDevicesText.setFill(Color.RED);

                errorDevicesText.setStroke(Color.BLACK);
                errorDevicesText.setStrokeWidth(0.3);

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.3), errorDevicesText);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                errorDevicesText.setVisible(true);
                fadeTransition.play();

                root.getChildren().add(errorDevicesText);

                return;
            }

            int a;
            if (!root.getChildren().contains(imageView)) {
                root.getChildren().add(imageView);
                a = -3;
                imageView.setX(setX);
                imageView.setY(setY);
                imageView.setFitWidth(fitWidth);
                imageView.setFitHeight(fitHeight);
                imageView.setViewport(new Rectangle2D(x, y, width, height));

            } else {
                a = 3;
                root.getChildren().remove(imageView);
            }

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> updateHumidifier(roomControllers, a)));
            timeline.setCycleCount(5);
            timeline.play();
        });
    }

    public void forMenuClickHeater(Object roomControllers, CheckBox menucheckBox, Elem elem, double x, double y, double width, double height, double setX, double setY, double fitWidth, double fitHeight) {
        menucheckBox.setOnMouseClicked(actionEvent -> {
            Pane root = (Pane) menucheckBox.getScene().getRoot();
            ImageView imageView = menuCheckBoxImageViewHashMap.computeIfAbsent(menucheckBox, checkBox -> {
                String imagePath = (roomControllers instanceof RoomManagement1Controller) ? imagePath1 : imagePath2;
                return new ImageView(new Image(imagePath));
            });

            Response response = runCommand(elem);
            System.out.println("Response: " + response);

            if (response != Response.heater) {
                menucheckBox.fire();

                Text errorDevicesText = new Text("Вы не приобрели данное устройство");
                errorDevicesText.setLayoutX(25.0);
                errorDevicesText.setLayoutY(580.0);
                errorDevicesText.setFont(new Font(15.0));

                errorDevicesText.setFill(Color.RED);

                errorDevicesText.setStroke(Color.BLACK);
                errorDevicesText.setStrokeWidth(0.3);

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.3), errorDevicesText);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                errorDevicesText.setVisible(true);
                fadeTransition.play();

                root.getChildren().add(errorDevicesText);

                return;
            }

            int a;
            if (!root.getChildren().contains(imageView)) {
                root.getChildren().add(imageView);
                a = 3;
                imageView.setX(setX);
                imageView.setY(setY);
                imageView.setFitWidth(fitWidth);
                imageView.setFitHeight(fitHeight);
                imageView.setViewport(new Rectangle2D(x, y, width, height));

            } else {
                a = -3;
                root.getChildren().remove(imageView);
            }

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> updateTemperature(roomControllers, a)));
            timeline.setCycleCount(5);
            timeline.play();
        });
    }

    public void forMenuClickConditioner(Object roomControllers, CheckBox menucheckBox, Elem elem, double x, double y, double width, double height, double setX, double setY, double fitWidth, double fitHeight) {
        menucheckBox.setOnMouseClicked(actionEvent -> {
            Pane root = (Pane) menucheckBox.getScene().getRoot();
            ImageView imageView = menuCheckBoxImageViewHashMap.computeIfAbsent(menucheckBox, checkBox -> {
                String imagePath = (roomControllers instanceof RoomManagement1Controller) ? imagePath1 : imagePath2;
                return new ImageView(new Image(imagePath));
            });

            Response response = runCommand(elem);
            System.out.println("Response: " + response);

            if (response != Response.conditioner) {
                menucheckBox.fire();

                Text errorDevicesText = new Text("Вы не приобрели данное устройство");
                errorDevicesText.setLayoutX(25.0);
                errorDevicesText.setLayoutY(580.0);
                errorDevicesText.setFont(new Font(15.0));

                errorDevicesText.setFill(Color.RED);

                errorDevicesText.setStroke(Color.BLACK);
                errorDevicesText.setStrokeWidth(0.3);

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.3), errorDevicesText);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                errorDevicesText.setVisible(true);
                fadeTransition.play();

                root.getChildren().add(errorDevicesText);

                return;
            }

            int a;
            if (!root.getChildren().contains(imageView)) {
                root.getChildren().add(imageView);
                a = -2;
                imageView.setX(setX);
                imageView.setY(setY);
                imageView.setFitWidth(fitWidth);
                imageView.setFitHeight(fitHeight);
                imageView.setViewport(new Rectangle2D(x, y, width, height));

            } else {
                a = 2;
                root.getChildren().remove(imageView);
            }

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> updateTemperature(roomControllers, a)));
            timeline.setCycleCount(5);
            timeline.play();
        });
    }

    public Response runCommand(Elem elem) {
        try {
            Socket socket = new Socket("127.0.0.1", 8081);

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            ServerData serverdata = new ServerData();
            PackageData packageData = new PackageData();

            packageData.setLogin(Config.getINSTANCE().getLogin());
            packageData.setAdmin(Config.getINSTANCE().isAdmin());
            packageData.setQuery(query);
            packageData.setSession(Config.getINSTANCE().getSession());

            packageData.setElem(elem);
            ServerData.Request request = new ServerData.Request(packageData);

            outputStream.write(serverdata.objToBytes(request));

            byte[] arr = new byte[500];
            inputStream.read(arr);

            ServerData.Request answer = (ServerData.Request) serverdata.bytesToObject(arr);
            System.out.println("Ответ от сервера: " + answer.getMessage().getResult());

            return answer.getMessage().getResponse();

        } catch (IOException e) {
            return null;
        }
    }
}


