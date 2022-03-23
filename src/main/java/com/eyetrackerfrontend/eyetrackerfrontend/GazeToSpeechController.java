package com.eyetrackerfrontend.eyetrackerfrontend;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import java.io.*;
import java.util.ArrayList;

public class GazeToSpeechController {
    public GridPane wordGrid;
    public AnchorPane root;
    private BufferedReader br;
    private final ArrayList<String> wordList = new ArrayList<>();
    private final ArrayList<Phrase> phraseList = new ArrayList<>();
    public final static TTS tts = new TTS();


    /**
     * Initializes all the variables and sets up the scene. Called when the fxml file is loaded.
     */
    @FXML
    public void initialize() throws IOException {
        try {
            br = new BufferedReader(new FileReader("src/main/resources/com/eyetrackerfrontend/eyetrackerfrontend/wordList.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("World list not found");
            e.printStackTrace();
        }
        String s;
        while ((s = br.readLine()) != null && !s.equals("")) {
            wordList.add(s);
        }

        tts.speak("Hello, world. ");

        Circle target = new Circle(0, 0, 40);
        Color targetCol = Color.rgb(255, 38, 38, 0.5);
        target.setFill(targetCol);
        root.getChildren().add(target);

        Circle recTarget = new Circle(900, 400, 20);
        Color recTargetCol = Color.rgb(255, 38, 38);
        recTarget.setFill(recTargetCol);
        recTarget.setVisible(false);
        root.getChildren().add(recTarget);

        System.out.println(wordGrid.getColumnCount());
        int cnt = 0;
        for (int i = 0; i < wordGrid.getRowCount(); i++) {
            for (int j = 0; j < wordGrid.getColumnCount(); j++) {
                VBox box = new VBox();
                box.setAlignment(Pos.CENTER);
                wordGrid.add(box, j, i);
                if (cnt < wordList.size()) {
                    Phrase phrase = new Phrase(wordList.get(cnt), i, j);
                    phraseList.add(phrase);
                    Label lbl = new Label(phrase.text);
                    lbl.setFont(Font.font("Times New Roman", 36));
                    lbl.setTextAlignment(TextAlignment.CENTER);
                    cnt++;
                    box.getChildren().add(lbl);
                }

            }
        }



        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (MainApplication.enableCursorMove) {
                    target.setVisible(true);

                    MainApplication.X = (int) Math.round(MainApplication.X + (MainApplication.rawX - MainApplication.X) * 0.2);
                    MainApplication.Y = (int) Math.round(MainApplication.Y + (MainApplication.rawY - MainApplication.Y) * 0.2);

                    target.setCenterX(75 + MainApplication.X);
                    target.setCenterY(75 + MainApplication.Y);

                    for (Phrase p : phraseList) {
                        if (MainApplication.X > p.leftBound && MainApplication.X < p.rightBound && MainApplication.Y < p.bottomBound && MainApplication.Y < p.topBound) {
                            p.cntFixation++;
                            if (p.cntFixation > 120) {
                                System.out.println(p.text);
                                tts.speak(p.text);
                                p.cntFixation = 0;
                            }
                        } else p.cntFixation = 0;
                    }

                } else {
                    target.setVisible(false);
                }
                recTarget.setVisible(MainApplication.showTargetRec);
            }
        }.start();
    }
}

class Phrase {
    String text;
    int row, col;
    double leftBound, rightBound, bottomBound, topBound;
    int cntFixation = 0;

    public Phrase(String t_, int r_, int c_) {
        text = t_;
        row = r_;
        col = c_;
        leftBound = col * 175 + 75;
        rightBound = (col + 1) * 175 + 75;
        topBound = 750 - row * 175;
        bottomBound = 750 - (row + 1) * 175;
    }
}
