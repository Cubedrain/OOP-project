package com.example.comwanyoikesufeeds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.io.IOException;

public class ListTopicsPageController {
    ErrorHandler errorHandler = new ErrorHandler();
    @FXML
    private void backBtnClicked(ActionEvent event) {
        PageOpener pageOpener = new PageOpener();
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/dashboard.fxml");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private TextField unitName;
    @FXML
    private TextField unitLec;
    @FXML
    private void addUnit(ActionEvent event) {
        String UnitName = this.unitName.getText();
        String UnitLec = this.unitLec.getText();

        if(unitName.getText() == "" || unitLec.getText() == "") {
            ErrorHandler popup = new ErrorHandler();
            popup.errorPopUp("Incomplete credentials","Please ensure all fields are filled.");
        }
        else{
            String sql = "INSERT INTO tbl_units(unitName,unitLec,admNo) VALUES(?,?,?)";
            try(Connection connection = DBHandler.getConnection()){
                Student currentStudent = Session.getCurrentStudent();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, UnitName);
                preparedStatement.setString(2, UnitLec);

                if(currentStudent != null){preparedStatement.setString(3,currentStudent.getAdm());}
                else {preparedStatement.setString(3,"176664");}

                int rowsUpdated = preparedStatement.executeUpdate();

                if(rowsUpdated > 0) {
                    System.out.println("Entered in new units");
                    errorHandler.infoPopUp("Unit Addition","Unit has been added");
                }
                else{
                    System.out.println("Something went wrong");
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
