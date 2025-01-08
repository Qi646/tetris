module com.ics.tetwis {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ics.tetwis to javafx.fxml;
    exports com.ics.tetwis;
}