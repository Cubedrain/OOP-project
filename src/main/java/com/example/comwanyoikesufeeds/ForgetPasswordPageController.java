package com.example.comwanyoikesufeeds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.*;
import java.io.IOException;


public class ForgetPasswordPageController {
    @FXML
    private TextField emailFieldForgotPassword;
    @FXML
    private TextField passwordFieldForgotPassword;
    @FXML
    private TextField confirmPasswordFieldForgotPassword;
    @FXML
    private void changePasswordHandler(ActionEvent event) {
        String email = emailFieldForgotPassword.getText(),
                password = passwordFieldForgotPassword.getText(),
                confirmationPassword = confirmPasswordFieldForgotPassword.getText();

        if (email.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty()) {
            System.out.println("Empty fields, kindly fill them in");
        }
        else{
            String sql = "SELECT * FROM tbl_students where email = ?";

            try(Connection connection = DBHandler.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, email);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    String emailFound = resultSet.getString("email");
                    if (emailFound.equals(email)) {
                        String sql2 = "UPDATE tbl_students SET password = ? WHERE email = ?";

                        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);

                        preparedStatement2.setString(1, password);
                        preparedStatement2.setString(2, email);

                        int rowsUpdated = preparedStatement2.executeUpdate();

                        if (rowsUpdated > 0) System.out.println("Rows affected are: " + rowsUpdated);
                        else System.out.println("No rows affected are: User not found" + rowsUpdated);
                    }
                }
                else{
                    ErrorHandler errorHandler = new ErrorHandler();
                    errorHandler.errorPopUp("Error","Email does not match, no email exists of such");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
