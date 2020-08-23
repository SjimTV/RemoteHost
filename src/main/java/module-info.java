module com.sjimtv {
    requires javafx.controls;
    requires javafx.fxml;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;
    requires com.google.gson;

    opens com.sjimtv;
    exports com.sjimtv;

    opens com.sjimtv.showStructure;
    exports com.sjimtv.showStructure;
}

