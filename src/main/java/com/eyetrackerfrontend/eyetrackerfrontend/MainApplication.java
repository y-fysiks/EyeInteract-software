package com.eyetrackerfrontend.eyetrackerfrontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import java.io.IOException;



public class MainApplication extends Application {

    public static boolean enableCursorMove = false;
    public static volatile boolean logData = false, showTargetRec = false;
    public static volatile int rawX, rawY;
    public static int X, Y;
    public static int offsetX = 0, offsetY = 0;
    public static final int screenWidth = 1550, screenHeight = 750;
    private static Looper thread;

    @Override
    public void start(Stage stage) throws IOException {
        new MainStage();
        new SpeechStage();
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
        Scene scene = new Scene(fxmlLoader.load(), 1700, 900);
        this.setTitle("EyeSpeak");
        this.setScene(scene);
        this.setResizable(false);
        this.show();

        this.setOnCloseRequest(event -> {
            try {
                GazeToSpeechController.tts.deallocate();
                MainApplication.endThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
    }
}