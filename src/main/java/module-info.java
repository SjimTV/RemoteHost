module com.sjimtv {
    requires javafx.controls;
    requires javafx.fxml;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;

    opens com.sjimtv to javafx.fxml;
    exports com.sjimtv;
}