package com.example.comwanyoikesufeeds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class topicPageController {
    Student currentStudent = Session.getCurrentStudent();
    @FXML
    private Pane newPane = new Pane();
    @FXML
    private Label unitName= new Label();
    @FXML
    private Label topicName = new Label();
    @FXML
    private Label notes = new Label();
    @FXML
    private Label dateText;
    @FXML
    private void openInsertTopicPage(ActionEvent event) {
        PageOpener pageOpener = new PageOpener();
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/InsertTopic.fxml");
    }
    @FXML
    public void initialize(){
        LocalDateTime time1 = LocalDateTime.now();
        int hours = time1.getHour();
        dateText.setText(hours + ":" + time1.getMinute());

        try(Connection connection = DBHandler.getConnection()){
            String sql = "SELECT * FROM tbl_notes where admno = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentStudent.getAdm());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                private Pane newPane = new Pane();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
