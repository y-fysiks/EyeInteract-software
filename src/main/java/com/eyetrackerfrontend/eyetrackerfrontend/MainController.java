package com.eyetrackerfrontend.eyetrackerfrontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.util.Arrays;

public class MainController {
    @FXML
    public Label gazeInfoY;
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
        int xDiff = 1000 - MainApplication.rawX;
        int yDiff = 500 - MainApplication.rawY;
    }
}