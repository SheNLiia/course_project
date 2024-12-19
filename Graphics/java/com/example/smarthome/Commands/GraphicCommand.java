package com.example.smarthome.Commands;

import javafx.scene.image.ImageView;

public interface GraphicCommand {
    void work(ImageView view, double x, double y, double width, double height, double setX, double setY, double fitWidth, double fitHeight);
}
