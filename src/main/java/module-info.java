module com.ics.tetris {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ics.tetris to javafx.fxml;
    exports com.ics.tetris;
}