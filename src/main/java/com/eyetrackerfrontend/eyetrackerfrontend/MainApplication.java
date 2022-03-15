package com.eyetrackerfrontend.eyetrackerfrontend;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.msgpack.core.MessageUnpacker;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.msgpack.core.MessagePack;

import java.io.IOException;
import java.util.Arrays;


public class MainApplication extends Application {

    public static boolean enableCursorMove = false;
    public static int rawX, rawY, X, Y;

    @Override
    public void start(Stage stage) throws IOException {
        new MainStage();
        new TransparentStage();

        ZContext ctx = new ZContext();
        ZMQ.Socket pupil_remote = ctx.createSocket(SocketType.REQ);
        String address = "tcp://127.0.0.1";
        String port = "50020";

        pupil_remote.connect(address + ":" + port);

        pupil_remote.send("SUB_PORT");
        byte[] sub_portA = pupil_remote.recv();
        StringBuilder sub_portSB = new StringBuilder();
        for (byte b : sub_portA) {
            sub_portSB.append((char) b);
        }
        String sub_port = sub_portSB.toString();

        System.out.println(sub_port);

        ZMQ.Socket subscriber = ctx.createSocket(SocketType.SUB);
        subscriber.connect(address + ":" + sub_port);
        subscriber.subscribe("gaze_on_surfaces");

        while (true) {
            byte[] topicA = subscriber.recv();
            String topic = Arrays.toString(topicA);
            byte[] payload = subscriber.recv();
            MessageUnpacker message = MessagePack.newDefaultUnpacker(payload);
            double x = message.unpackDouble();
            double y = message.unpackDouble();
            System.out.println(topic + ": " + x + " " + y);
        }
    }

    public static void main(String[] args) {
        launch();
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
            Platform.exit();
        });
    }
}

class TransparentStage extends Stage {
    AnchorPane root = new AnchorPane();
    TransparentStage() throws IOException {
        root.setOnMouseMoved(event -> {
            MainApplication.rawX = (int) event.getX();
            MainApplication.rawY = (int) event.getY();
        });

        Circle target = new Circle(0, 0, 40);
        Color targetCol = Color.rgb(255, 38, 38, 0.5);
        target.setFill(targetCol);
        target.setMouseTransparent(true);
        root.getChildren().add(target);

        Scene scene = new Scene(root, Color.TRANSPARENT);
        scene.getRoot().setStyle("-fx-background-color: transparent");
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

                    MainApplication.X = (int) Math.round(MainApplication.X + (MainApplication.rawX - MainApplication.X) * 0.3);
                    MainApplication.Y = (int) Math.round(MainApplication.Y + (MainApplication.rawY - MainApplication.Y) * 0.3);

                    target.setCenterX(MainApplication.X);
                    target.setCenterY(MainApplication.Y);
                } else {
                    target.setFill(Color.TRANSPARENT);
                }
            }
        }.start();
    }
}