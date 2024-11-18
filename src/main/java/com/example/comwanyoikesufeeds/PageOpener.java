package com.example.comwanyoikesufeeds;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PageOpener {
    public void pageOpener(String pageName){
        try {
            FXMLLoader fxmlPage = new FXMLLoader(getClass().getResource(pageName));
            Parent root = fxmlPage.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
