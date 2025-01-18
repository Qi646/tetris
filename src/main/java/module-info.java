module com.ics.tetris {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.ics.tetris to javafx.fxml;
    exports com.ics.tetris;
}