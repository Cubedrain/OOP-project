package com.example.comwanyoikesufeeds;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.sql.Connection;
import java.time.LocalDateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForumController {
    Student currentStudent = Session.getCurrentStudent();
    ErrorHandler errorHandler = new ErrorHandler();
    @FXML
    private AnchorPane rootAnchor;
    @FXML
    private TextField commentInput;
    @FXML
    private Label dateText;
    @FXML
    private void addComment(ActionEvent event) {
        String comment = commentInput.getText();
        if(comment != null && !comment.isEmpty()) {
            try(Connection connection = DBHandler.getConnection()){
                String sql = "INSERT INTO tbl_forum(name,admissionno,comments) VALUES(?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, currentStudent.getName());
                preparedStatement.setString(2, currentStudent.getAdm());
                preparedStatement.setString(3, comment);

                int rows = preparedStatement.executeUpdate();

                if(rows > 0) {
                    System.out.println("Comment added successfully");
                    errorHandler.infoPopUp("Comment Addition","Comment added successfully");
                }
                else System.out.println("Comment not added successfully");
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    @FXML
    private EventHandler<ActionEvent> removeComment(String admNo, String comment){
        return actionEvent -> {
            Button srcBtn = (Button) actionEvent.getSource();
            Pane parentPane = (Pane) srcBtn.getParent();
            AnchorPane parentAnchor = (AnchorPane) parentPane.getParent();

            String sql = "DELETE FROM tbl_forum WHERE admno = ? AND comments = ?";
            try(Connection connection = DBHandler.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, admNo);
                preparedStatement.setString(2, comment);

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
            String sql = "SELECT * FROM tbl_forum ORDER BY created_at";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            int positionY = 0;

//            Creating new elements
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String comment = resultSet.getString("comments");
                String dateCreated = resultSet.getString("created_at");

                Label nameLabel = new Label();
                Label commentLabel = new Label();
                Label dateCreatedLabel = new Label();
                Button removeComment = new Button();

                nameLabel.setText(name);
                commentLabel.setText(comment);
                dateCreatedLabel.setText(dateCreated);
                removeComment.setText("Remove Comment");

                Pane newPane = new Pane();
                newPane.getChildren().addAll(nameLabel, commentLabel,dateCreatedLabel,removeComment);

                nameLabel.setLayoutX(15);
                nameLabel.setLayoutY(10);
                nameLabel.setUnderline(true);
                commentLabel.setLayoutX(25);
                commentLabel.setLayoutY(40);
                dateCreatedLabel.setLayoutX(80);
                dateCreatedLabel.setLayoutY(80);
                removeComment.setLayoutX(150);
                removeComment.setLayoutY(250);
                newPane.setStyle(
                        "-fx-border-radius: 20px;-fx-border-color: black;-fx-border-style: solid;-fx-padding: 20px;"
                );
                removeComment.setStyle(
                "-fx-font-family: \"Open Sans\",sans-serif;\n" +
                "-fx-border-color: black;\n" +
                "-fx-border-style: solid;\n" +
                "-fx-background-color: transparent;"
                );
                newPane.setLayoutX(112);
                newPane.setLayoutY(positionY);
                newPane.setPrefWidth(301);
                newPane.setPrefHeight(100);
                removeComment.setOnAction(removeComment(currentStudent.getAdm(), comment));
                positionY += 120;
                rootAnchor.getChildren().add(newPane);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
