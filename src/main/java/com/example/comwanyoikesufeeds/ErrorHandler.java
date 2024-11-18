package com.example.comwanyoikesufeeds;

import javafx.scene.control.Alert;

public class ErrorHandler {
    public void errorPopUp(String titleError,String msgError) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titleError);
        alert.setContentText(msgError);
        alert.showAndWait();
    }
    public void infoPopUp(String titleInfo,String msgInfo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleInfo);
        alert.setContentText(msgInfo);
        alert.showAndWait();
    }
}
