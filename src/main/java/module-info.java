module com.sjimtv {
    requires javafx.controls;
    requires javafx.fxml;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;
    requires com.google.gson;
    requires spring.web;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires org.apache.commons.io;
    requires ffmpeg;
    requires org.jsoup;

    opens com.sjimtv;
    exports com.sjimtv;

    opens com.sjimtv.server;
    exports com.sjimtv.server;

    opens com.sjimtv.control;
    exports com.sjimtv.control;

    opens com.sjimtv.mediaplayer;
    exports com.sjimtv.mediaplayer;

    opens com.sjimtv.showStructure;
    exports com.sjimtv.showStructure;

    opens com.sjimtv.filemanager;
    exports com.sjimtv.filemanager;
}

