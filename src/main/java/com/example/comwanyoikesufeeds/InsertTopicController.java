package com.example.comwanyoikesufeeds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertTopicController {
    Student currentStudent = Session.getCurrentStudent();
    @FXML private TextField unitName;
    @FXML private TextField topicName;
    @FXML private TextField notes;
    @FXML
    private void backBtnClicked(ActionEvent event) {
        PageOpener pageOpener = new PageOpener();
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/dashboard.fxml");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void addNoteBtnClicked(ActionEvent event) {
        ErrorHandler popupHandler = new ErrorHandler();
        if(unitName.getText().length() <= 0 || topicName.getText().length() <= 0 || notes.getText().length() <= 0) popupHandler.errorPopUp("Empty fields","Ensure all fields have values");
        try(Connection connection = DBHandler.getConnection()){
            String sql = "INSERT INTO tbl_notes (unitName,topicHeader,comment,admNo) VALUES (?,?,?,?)";
            String sql2 = "SELECT * FROM tbl_units where unitName = ? and admno = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);

            preparedStatement2.setString(1,unitName.getText());
            preparedStatement2.setString(2, currentStudent.getAdm());
            ResultSet resultSet = preparedStatement2.executeQuery();
            if(resultSet.next()) {
                preparedStatement.setString(1, unitName.getText());
                preparedStatement.setString(2, topicName.getText());
                preparedStatement.setString(3, notes.getText());
                preparedStatement.setString(4, currentStudent.getAdm());

                int newRows = preparedStatement.executeUpdate();

                if (newRows > 0) System.out.println("Successfully entered new rows in notes table");
                else System.out.println("Failed to enter new rows in notes table");
            }
            else{
                popupHandler.errorPopUp("Failed to add note","You have not registered for such a unit.");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
