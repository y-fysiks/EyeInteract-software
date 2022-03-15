package com.eyetrackerfrontend.eyetrackerfrontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.zeromq.ZMQ;

public class MainController {
    @FXML
    public Label gazeInfoY;
    public ToggleButton toggleFollowButton;
    public VBox root;

    public void toggleFollow(ActionEvent actionEvent) {
        MainApplication.enableCursorMove = toggleFollowButton.isSelected();
    }
}