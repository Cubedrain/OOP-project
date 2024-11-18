package com.example.comwanyoikesufeeds;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountPageController {
    Student currentStudent = Session.getCurrentStudent();
    ErrorHandler errorHandler = new ErrorHandler();
    @FXML private Pane unit1;
    @FXML private Pane unit2;
    @FXML private Pane unit3;
    @FXML private Pane unit4;
    @FXML private Pane unit5;
    @FXML private Pane unit6;
    @FXML private Pane unit7;
    @FXML private Pane unit8;
    @FXML private Pane[] paneArray = new Pane[8];
    @FXML private TextField newName;
    @FXML private TextField newEmail;
    @FXML private Label dateText;
    @FXML
        private void logOutBtnClicked(ActionEvent event) {
            Session.clear();
            PageOpener pageOpener = new PageOpener();
            pageOpener.pageOpener("/com/example/comwanyoikesufeeds/login_signup_page.fxml");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    @FXML
    private void backBtnClicked(ActionEvent event) {
        PageOpener pageOpener = new PageOpener();
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/dashboard.fxml");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void changeNameBtn(ActionEvent event) {
        String sql = "UPDATE tbl_students SET name = ? where admissionno = ?";
        String newUpdatedName = newName.getText();

        if(newUpdatedName.length() > 3 && newUpdatedName.length() < 30){
            try(Connection connection = DBHandler.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,  newName.getText());
                preparedStatement.setString(2, currentStudent.getAdm());

                int rows = preparedStatement.executeUpdate();
                if(rows > 0) System.out.println("Updated");
                else System.out.println("Not Updated");
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        else{
            errorHandler.errorPopUp("Empty Field","Please set a new field");
        }
    }
    @FXML
    private void changeEmailBtn(ActionEvent event) {
        String pattern2 = "^[A-Za-z]+@strathmore.edu";
        Pattern emailChecker = Pattern.compile(pattern2);
        Matcher emailValidator = emailChecker.matcher(newEmail.getText());

        String sql = "UPDATE tbl_students SET Email = ? where admissionno = ?";
        if(newEmail.getText() != null && emailValidator.matches() == true){
            try(Connection connection = DBHandler.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,  newEmail.getText());
                preparedStatement.setString(2, currentStudent.getAdm());

                preparedStatement.executeUpdate();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        else{
            ErrorHandler popupError = new ErrorHandler();
            popupError.errorPopUp("Invalid Email","Email is using an invalid email, should be using strathmore domain(@strathmore.edu)");
        }
    }
    @FXML
    private EventHandler<ActionEvent> removeUnitClicked(String admNo,String unitName){
        return actionEvent -> {
            Button srcBtn = (Button) actionEvent.getSource();
            Pane parentPane = (Pane) srcBtn.getParent();

            Label unitNameDeleted = (Label) parentPane.getChildren().get(0);
            Label unitLecDeleted = (Label) parentPane.getChildren().get(1);

            String sql = "DELETE FROM tbl_units WHERE admno = ? AND unitName = ?";
            try(Connection connection = DBHandler.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, admNo);
                preparedStatement.setString(2, unitName);

                int rowsUpdated = preparedStatement.executeUpdate();
                if(rowsUpdated > 1) System.out.println("Deleted the row");
                else System.out.println("Deleted no rows");

                unitNameDeleted.setText("--");
                unitLecDeleted.setText("--");
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        };
    }
    @FXML
    public void initialize() {
        LocalDateTime time1 = LocalDateTime.now();
        int hours = time1.getHour();

        if(time1.getMinute() < 10) dateText.setText(hours + ":0" + time1.getMinute());
        else dateText.setText(hours + ":" + time1.getMinute());

        newName.setPromptText(currentStudent.getName());
        newEmail.setPromptText(currentStudent.getEmail());

        paneArray[0] = unit1;
        paneArray[1] = unit2;
        paneArray[2] = unit3;
        paneArray[3] = unit4;
        paneArray[4] = unit5;
        paneArray[5] = unit6;
        paneArray[6] = unit7;
        paneArray[7] = unit8;

        String sql = "SELECT * FROM tbl_units where admno = ?";
        try(Connection connection = DBHandler.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            if(currentStudent != null) preparedStatement.setString(1,currentStudent.getAdm());
            else preparedStatement.setString(1,"176664");

            ResultSet resultSet = preparedStatement.executeQuery();

            int count = 0;

            for(int i = 0;i < paneArray.length;i++){
                Pane currentPane = paneArray[i];
                if(!currentPane.getChildren().isEmpty()){
                    Label unitNameforlabel = (Label) currentPane.getChildren().get(0);
                    Label unitLecforlabel = (Label) currentPane.getChildren().get(1);
                    Button removeUnitBtn = (Button) currentPane.getChildren().get(2);
                    removeUnitBtn.setOnAction(null);
                    unitNameforlabel.setText("--");
                    unitLecforlabel.setText("--");
                }
            }

            while (resultSet.next()) {
                String unitName = resultSet.getString("unitName");
                String unitLec = resultSet.getString("unitLec");

                Pane currentPane = paneArray[count];
                Label unitNameforlabel = (Label) currentPane.getChildren().get(0);
                Label unitLecforlabel = (Label) currentPane.getChildren().get(1);
                Button removeUnitBtn = (Button) currentPane.getChildren().get(2);
                removeUnitBtn.setOnAction(removeUnitClicked(currentStudent.getAdm(),unitName));

                unitNameforlabel.setText(unitName);
                unitLecforlabel.setText(unitLec);

                count += 1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println(LocalDateTime.now());
    }
}
