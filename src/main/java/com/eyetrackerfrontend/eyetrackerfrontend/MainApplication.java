package com.eyetrackerfrontend.eyetrackerfrontend;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.awt.*;

import java.io.IOException;



public class MainApplication extends Application {

    public static boolean enableCursorMove = false;
    public static volatile boolean logData = false, showTargetRec = false;
    public static volatile int rawX, rawY;
    public static int X, Y;
    public static int offsetX = 0, offsetY = 0;
    public static final int screenWidth = (int) Screen.getPrimary().getBounds().getWidth(), screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
    private static Looper thread;

    @Override
    public void start(Stage stage) throws IOException {
        new MainStage();
        new SpeechStage();
        new TransparentStage();

    }

    public static void main(String[] args) {
        launch();
    }

    public static void startThread() {
        thread = new Looper();
        thread.start();
    }
    public static void endThread() throws InterruptedException {
        if (thread != null) thread.endProgram();
    }
}

class MainStage extends Stage {
    MainStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
        this.setTitle("Eye Tracker accessibility suite");
        this.setScene(scene);
        this.show();

        this.setOnCloseRequest(event -> {
            try {
                MainApplication.endThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
    }
}

class SpeechStage extends Stage {
    SpeechStage() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("GazeToSpeech.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 850);
        this.setTitle("EyeSpeak");
        this.setScene(scene);
        this.setResizable(false);
        this.show();

        this.setOnCloseRequest(event -> {
            try {
                MainApplication.endThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });

    }
}

class TransparentStage extends Stage {
    AnchorPane root = new AnchorPane();
    TransparentStage() {
//        root.setOnMouseMoved(event -> {
//            MainApplication.rawX = (int) event.getX();
//            MainApplication.rawY = (int) event.getY();
//        });
        Circle target = new Circle(0, 0, 40);
        Color targetCol = Color.rgb(255, 38, 38, 0.5);
        target.setFill(targetCol);
        root.getChildren().add(target);

        Circle recTarget = new Circle(1000, 500, 20);
        Color recTargetCol = Color.rgb(255, 38, 38);
        recTarget.setFill(recTargetCol);
        recTarget.setVisible(false);
        root.getChildren().add(recTarget);

        Scene scene = new Scene(root, Color.TRANSPARENT);
        scene.getRoot().setStyle("-fx-background-color: transparent");
        root.setMouseTransparent(true);
        this.initStyle(StageStyle.UNDECORATED);
        this.initStyle(StageStyle.TRANSPARENT);
        this.setAlwaysOnTop(true);
        this.setMaximized(true);
        this.setScene(scene);
        this.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (MainApplication.enableCursorMove) {
                    target.setFill(targetCol);

                    MainApplication.X = (int) Math.round(MainApplication.X + (MainApplication.rawX - MainApplication.X) * 0.2);
                    MainApplication.Y = (int) Math.round(MainApplication.Y + (MainApplication.rawY - MainApplication.Y) * 0.2);

                    target.setCenterX(MainApplication.X);
                    target.setCenterY(MainApplication.Y);
                } else {
                    target.setFill(Color.TRANSPARENT);
                }
                recTarget.setVisible(MainApplication.showTargetRec);
            }
        }.start();
    }
}