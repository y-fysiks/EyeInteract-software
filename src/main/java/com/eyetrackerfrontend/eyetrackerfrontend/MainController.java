package com.eyetrackerfrontend.eyetrackerfrontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML
    public ToggleButton toggleFollowButton;
    public VBox root;
    public ToggleButton connectButton;
    public ToggleButton toggleShowTargetButton;
    public ToggleButton toggleRecordButton;
    public Button calibrateButton;

    public void toggleFollow(ActionEvent actionEvent) {
        MainApplication.enableCursorMove = toggleFollowButton.isSelected();
    }

    public void connect(ActionEvent actionEvent) {
        if (connectButton.isSelected()) MainApplication.startThread();
        else connectButton.setSelected(true);
    }

    public void toggleShowTarget(ActionEvent actionEvent) {
        MainApplication.showTargetRec = toggleShowTargetButton.isSelected();
    }

    public void toggleRecord(ActionEvent actionEvent) {
        MainApplication.logData = toggleRecordButton.isSelected();
    }

    public void calibrate(ActionEvent actionEvent) {
        MainApplication.offsetX = 900 - 15 - MainApplication.X;
        MainApplication.offsetY = 400 - 15 - MainApplication.Y;
    }
}