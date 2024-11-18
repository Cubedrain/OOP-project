package com.example.comwanyoikesufeeds;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SUFeedsApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SUFeedsApp.class.getResource("login_signup_page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 535,500 );
        stage.setTitle("SU Feeds Credentials Page!");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
    public static void main(String[] args) {
        launch();
    }
}