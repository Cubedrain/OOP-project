package com.example.comwanyoikesufeeds;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class topicPageController {
    Student currentStudent = Session.getCurrentStudent();
    @FXML
    private AnchorPane rootAnchor;
    @FXML
    private Label dateText;
    @FXML
    private void openInsertTopicPage(ActionEvent event) {
        PageOpener pageOpener = new PageOpener();
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/InsertTopic.fxml");
    }
    @FXML
    private EventHandler<ActionEvent> removeNote(String admNo, String unitName){
        return actionEvent -> {
            Button srcBtn = (Button) actionEvent.getSource();
            Pane parentPane = (Pane) srcBtn.getParent();
            AnchorPane parentAnchor = (AnchorPane) parentPane.getParent();

            String sql = "DELETE FROM tbl_notes WHERE admno = ? AND unitName = ?";
            try(Connection connection = DBHandler.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, admNo);
                preparedStatement.setString(2, unitName);

                int rowsUpdated = preparedStatement.executeUpdate();
                if(rowsUpdated > 1) System.out.println("Deleted the row");
                else System.out.println("Deleted no rows");

                parentAnchor.getChildren().remove(parentPane);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        };
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
            int positionY = 0;
            while(resultSet.next()){
                String unitNameText = resultSet.getString("unitname");
                String topicNameText = resultSet.getString("topicheader");
                String notesText = resultSet.getString("comment");

                Label unitName = new Label();
                Label topicName = new Label();
                Label comment = new Label();
                Button removeNote = new Button();
                removeNote.setText("Remove Note");

                unitName.setText(unitNameText);
                topicName.setText(topicNameText);
                comment.setText(notesText);
                removeNote.setOnAction(removeNote(currentStudent.getAdm(), unitNameText));
                Pane newPane = new Pane();

                newPane.getChildren().addAll(unitName,topicName,comment,removeNote);

                unitName.setLayoutX(15);
                topicName.setLayoutX(15);
                comment.setLayoutX(15);
                unitName.setUnderline(true);
                unitName.setLayoutY(10);
                topicName.setLayoutY(30);
                comment.setLayoutY(60);
                removeNote.setLayoutX(250);
                removeNote.setLayoutY(100);
                newPane.setId("topicPane");
                newPane.setLayoutX(112);
                newPane.setLayoutY(positionY);
                newPane.setStyle(
                        "-fx-border-radius: 20px;-fx-border-color: black;-fx-border-style: solid;-fx-padding: 20px;"
                );
                removeNote.setStyle(
                        "-fx-font-family: \"Open Sans\",sans-serif;\n" +
                        "-fx-border-color: transparent;\n" +
                        "-fx-background-color: transparent;"
                );
                newPane.setPrefWidth(357);
                newPane.setPrefHeight(150);

                rootAnchor.getChildren().add(newPane);
                positionY += 170;

            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
