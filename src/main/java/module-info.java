module com.eyetrackerfrontend.eyetrackerfrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires jeromq;
    requires msgpack.core;
    requires java.desktop;
    requires jsapi;

    opens com.eyetrackerfrontend.eyetrackerfrontend to javafx.fxml;
    exports com.eyetrackerfrontend.eyetrackerfrontend;
}