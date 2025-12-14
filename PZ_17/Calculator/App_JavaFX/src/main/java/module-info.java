module com.example.app_javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.app_javafx to javafx.fxml;
    exports com.example.app_javafx;
}