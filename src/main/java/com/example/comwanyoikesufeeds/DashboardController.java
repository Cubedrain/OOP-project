package com.example.comwanyoikesufeeds;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DashboardController {
    PageOpener pageOpener = new PageOpener();
    @FXML
    private Label greetings;

    @FXML private Pane unit1;
    @FXML private Pane unit2;
    @FXML private Pane unit3;
    @FXML private Pane unit4;
    @FXML private Pane unit5;
    @FXML private Pane unit6;
    @FXML private Pane unit7;
    @FXML private Pane unit8;
    @FXML private Pane[] paneArray = new Pane[8];
    @FXML
    private Button topicBtnDash;
    @FXML
    private Button forumBtnDash;
    @FXML
    private Button accountBtnDash;
    @FXML
    private void topicBtnClicked(ActionEvent event) {
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/topicPage.fxml");
    }
    @FXML
    private void forumBtnClicked(ActionEvent event) {
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/Forum.fxml");
    }
    @FXML
    private void accountBtnClicked(ActionEvent event) {
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/AccountPage.fxml");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void initialize(){
        LocalDateTime time1 = LocalDateTime.now();
        int hours = time1.getHour();
        Student currentStudent = Session.getCurrentStudent();

        if(currentStudent != null){
            System.out.println("Current student name: " + currentStudent.getName());
            System.out.println("Current student email: " + currentStudent.getEmail());
            System.out.println("Current student admission number: " + currentStudent.getAdm());
        }
        else{
            System.out.println("Its currently empty");
        }

        if (hours < 12)
            greetings.setText("Good Morning " + currentStudent.getName());
        else if(hours < 18)
            greetings.setText("Good Afternoon " + currentStudent.getName());
        else if(hours <= 24)
            greetings.setText("Good Evening " + currentStudent.getName());
        //Units panel

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
            preparedStatement.setString(1, currentStudent.getAdm());
            ResultSet resultSet = preparedStatement.executeQuery();

            int count = 0;

            for(int i = 0;i < paneArray.length;i++){
                Pane currentPane = paneArray[i];
                if(!currentPane.getChildren().isEmpty()){
                    Label unitNameforlabel = (Label) currentPane.getChildren().get(0);
                    Label unitLecforlabel = (Label) currentPane.getChildren().get(1);
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
    @FXML
    private void addUnitClicked(ActionEvent event) {
        PageOpener pageOpener = new PageOpener();
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/ListTopicsPage.fxml");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
