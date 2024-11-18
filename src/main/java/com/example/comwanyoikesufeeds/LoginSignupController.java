package com.example.comwanyoikesufeeds;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Map;
import java.util.HashMap;

//Database Connections
import java.sql.*;
import java.io.IOException;
import java.util.regex.*;

public class LoginSignupController {
    @FXML
    private Pane loginPane;
    @FXML
    private Pane signUpPane;
    @FXML
    //The button at first is for changing to the login section
    private Button optionBtn;
    @FXML
    private void optionBtnClicked(ActionEvent event) {
        Boolean loginPageOpen;
        if(signUpPane.getOpacity() == 1) loginPageOpen = false; // meaning the sign up page is open
        else loginPageOpen = true;

        if(loginPageOpen) {
            optionBtn.setText("Log in");
            signUpPane.setOpacity(1);
            signUpPane.setDisable(false);
            loginPane.setOpacity(0);
            loginPane.setDisable(true);
        }
        else{
            optionBtn.setText("Sign Up");
            signUpPane.setOpacity(0);
            signUpPane.setDisable(true);
            loginPane.setOpacity(1);
            loginPane.setDisable(false);
        }
    }
    @FXML
    private Label textField;
    @FXML
    private Text signupStatusText;
    @FXML
    private Text loginStatusText;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField admFieldSignup;
    @FXML
    private TextField pwdFieldSignup;
    @FXML
    private TextField admissionFieldLogin;
    @FXML
    private TextField passwordFieldLogin;
    @FXML
    private void signUpBtnClicked(ActionEvent event) {
        String name = nameField.getText(),
            email = emailField.getText(),
            adm = admFieldSignup.getText(),
            pwd = pwdFieldSignup.getText();

        String pattern = "^[A-Za-z\\s]+$"
                ,pattern2 = "^[A-Za-z]+@strathmore.edu"
                ,pattern3 = "^[0-9]+$";
        Pattern nameChecker = Pattern.compile(pattern),
            emailChecker = Pattern.compile(pattern2),
            admChecker = Pattern.compile(pattern3);
        Matcher nameValidator = nameChecker.matcher(name),
            emailValidator = emailChecker.matcher(email),
            admissionValidator = admChecker.matcher(adm);

        if(nameField.getText() == ""  || emailField.getText() == "" || admFieldSignup.getText() == "" || pwdFieldSignup.getText() == ""){
            System.out.println("The fields are empty");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Credentials");
            alert.setContentText("Ensure you enter some credentials inside");
            alert.showAndWait();
        }
        else if(nameValidator.matches() == false){
            System.out.println("The field for name has numbers inside");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Credentials");
            alert.setContentText("Ensure your name is entered correctly");
            alert.showAndWait();
        }
        else if(emailValidator.matches() == false){
            System.out.println("The field for email doesn't follow the required order");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Credentials");
            alert.setContentText("Ensure your email is entered correctly and has the right domain of @strathmore.edu");
            alert.showAndWait();
        }
        else if(admissionValidator.matches() == false){
            System.out.println("The field for admission contains invalid inputed numbers");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Credentials");
            alert.setContentText("Ensure your admission number is valid and only contains numerical input alone");
            alert.showAndWait();
        }
        else if(pwd.length() < 3 || pwd.length() > 16){
            System.out.println("The field for pwd is either too long or too short");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Password");
            alert.setContentText("Ensure your password is of ranging length between [3-16]");
            alert.showAndWait();
        }
        else{
            String sql = "INSERT INTO tbl_students(Name,Email,AdmissionNo,Password) VALUES(?,?,?,?)";
            try(Connection connection = DBHandler.getConnection()){
                System.out.println("Connected to PostgresSQL database!");

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, adm);
                preparedStatement.setString(4, pwd);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " rows affected");

                Student current;
                current = new Student(name,adm,email);
                Session.setCurrentStudent(current);

                signupStatusText.setOpacity(1);
                System.out.println("Signup successful, user has been stored in session");
                signupStatusText.setText("Registered successfully");

                PageOpener pageOpener = new PageOpener();
                pageOpener.pageOpener("/com/example/comwanyoikesufeeds/dashboard.fxml");
            }
            catch(SQLException e){
                System.err.println("Connection with the PostgreSQL Database failed!");

                String sqlState = e.getSQLState();
                int errorCode = e.getErrorCode();

                System.out.println("SQLException occurred");
                System.out.println("SQLState: " + sqlState);
                System.out.println("Error Code: " + errorCode);
                System.out.println("Message: " + e.getMessage());

                // Handle specific SQLStates
//                if ("08001".equals(sqlState)) System.out.println("Connection error. Please check your database server.");
//                else if ("23000".equals(sqlState)) System.out.println("Integrity constraint violation. Duplicate key or other constraint issue.");
//                else if ("42000".equals(sqlState)) System.out.println("Syntax error in SQL statement.");
                if("23505".equals(sqlState)){
                    System.out.println("A duplicate value has been found");
                    ErrorHandler popup = new ErrorHandler();
                    if(e.getMessage() == "ERROR: duplicate key value violates unique constraint unique_email_address"){
                        popup.errorPopUp("Duplicate Email","The email entered already exists");
                    }
                    else if(e.getMessage() == "ERROR: duplicate key value violates unique constraint \"unique_admission_no\""){
                        popup.errorPopUp("Duplicate Admission No","The admission number entered already exists");
                    }
                }
                else System.out.println("An unknown SQL error occurred.");
                e.printStackTrace();
            }
        }
        System.out.println(name);
        System.out.println(adm);
        System.out.println(pwd);
    }
    @FXML
    private void loginBtnClicked(ActionEvent event) {
        String password = passwordFieldLogin.getText();
        String admissionNo = admissionFieldLogin.getText();

        String pattern = "^[0-9]+$";
        Pattern admChecker = Pattern.compile(pattern);
        Matcher admMatcher = admChecker.matcher(admissionNo);

        ErrorHandler popup = new ErrorHandler();

        if (admissionNo == "" || password == ""){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Credentials");
            alert.setContentText("Ensure you enter some credentials in both fields");
            alert.showAndWait();
        }
        if(admMatcher.matches() == false) popup.errorPopUp("Invalid Credentials","Invalid admission number entered, ensure it only contains valid credentials");
        else{
            //adm checking in database
            String sql = "SELECT * FROM tbl_students where admissionNo = ?";

            try(Connection connection = DBHandler.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, admissionNo);

                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("Email");
                    String passwordChecker = resultSet.getString("password");

                    System.out.println("Name: " + name);
                    System.out.println("Email: " + email);
                    System.out.println("Password: " + passwordChecker);

                    if(passwordChecker.equals(password)){

                        Student current;
                        current = new Student(name,email,admissionNo);
                        Session.setCurrentStudent(current);
                        loginStatusText.setText("Log In successful and session created");

                        PageOpener pageOpener = new PageOpener();
                        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/dashboard.fxml");
                        //Closing the page afterward
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                    }
                    else{
                        loginStatusText.setText("Log In unsuccessful");
                        popup.errorPopUp("Invalid Password","Invalid password, please try again");
                    }
                }
                else{
                    popup.errorPopUp("Invalid Credentials","No such user exists of such an admission number");
                    System.out.println("User not found with such an admission number" + admissionNo);
                }
                resultSet.close();
                preparedStatement.close();
            }
            catch(SQLException e){
                System.err.println("Connection with the PostgreSQL Database failed!");
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void forgotPasswordBtnClicked(ActionEvent event) {
        PageOpener pageOpener = new PageOpener();
        pageOpener.pageOpener("/com/example/comwanyoikesufeeds/ForgetPasswordPage.fxml");
    }
}