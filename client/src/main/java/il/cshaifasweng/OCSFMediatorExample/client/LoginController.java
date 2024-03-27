package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.loadFXML;

public class LoginController {
    @FXML
    private TextField answer_field_forgot;

    @FXML
    private Button backButton_forgot;

    @FXML
    private Button backButton_new_pass;
    @FXML
    private AnchorPane forgot_form;
    @FXML
    private AnchorPane new_password_form;

    @FXML
    private Button forgot_password_button;

    @FXML
    private Button login_button;

    @FXML
    private TextField login_field;

    @FXML
    private AnchorPane login_form;

    @FXML
    private AnchorPane main_login_anchor;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button proceed_forgot_password;

    @FXML
    private ComboBox<String> question_bar_forgot;

    @FXML
    private Button secondaryButton;

    @FXML
    private TextField username_field_forgot;
    @FXML
    private TextField new_password_field;
    @FXML
    private Button new_password_proceed;
    @FXML
    private TextField confirm_password_field;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void emergency_button_press(ActionEvent event) {
        //pop-ups alarm that shows that a new emergency call has been forwarded
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Your emergency call has been forwarded");
        alert.setTitle("Emergency Button");
        alert.setHeaderText("New Emergency Call Forwarded!");
        alert.show();
        //TODO: add the emergency to the database (time,when,where,who,whom,whose,wok)
    }

    @FXML
    void switch_form(ActionEvent event) {
        if(event.getSource() == forgot_password_button) {
            login_form.setVisible(false);
            forgot_form.setVisible(true);
            new_password_form.setVisible(false);
        }
        else if(event.getSource() == backButton_forgot) {
            login_form.setVisible(true);
            forgot_form.setVisible(false);
            new_password_form.setVisible(false);
        }
    }
    @FXML
    void login_button_push(ActionEvent event) throws IOException {
        String username = login_field.getText();
        String password = password_field.getText();
        String[] loginDetails = new String[]{username,password};
        Message message = new Message( "Login Request",loginDetails,null);
        SimpleClient.getClient().sendToServer(message);
        System.out.println("message sent with parameters: " + loginDetails[0] + "," + loginDetails[1]);

    }
    @Subscribe
    public void login(LoginEvent event) throws IOException {
        //TODO check why there's an exception when signing out and in again.
        System.out.println("IN login");
        String message = event.getMessage().getMessage();
        if(message.equals("Login Succeed")) {
            Platform.runLater(() -> {
                try {
                    scene = new Scene(loadFXML("mainmenu"), 434, 445);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage = (Stage) login_form.getScene().getWindow();
                stage.setScene(scene);
            });
        }
        else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "There is no such Username, or the password is wrong");
                alert.setTitle("Login Failed");
                alert.setHeaderText("Login Failed");
                alert.show();
            });
        }
    }
    private String[] securityQuestions = {
            "What is your favorite color?", "What is your pet's name?", "What is your mother's maiden name?", "What city were you born in?",
            "What is the name of your first school?", "What is your favorite movie?", "What is your favorite food?",
            "Who is your favorite author?", "What is your dream vacation destination?", "What is your favorite animal?", "What is your favorite hobby?",
            "What is your favorite sport?", "What is your favorite season?", "What is your favorite TV show?", "What is your favorite holiday?", "What is your favorite music genre?",
            "What is your favorite subject?", "What is your favorite book?", "What is your favorite movie genre?", "What is your favorite dessert?", "What is your favorite fruit?",
            "What is your favorite drink?", "What is your favorite board game?", "What is your favorite TV series?",
            "What is your favorite outdoor activity?", "What is your favorite indoor activity?", "What is your favorite ice cream flavor?"
    };
    @FXML
    void forgot_password(ActionEvent event) throws IOException {
        if (username_field_forgot.getText().isEmpty()
                || question_bar_forgot.getSelectionModel().getSelectedItem() == null
                || answer_field_forgot.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill all blank fields");
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.show();
        }

        String username = username_field_forgot.getText();
        String selectedQuestion = (String) question_bar_forgot.getSelectionModel().getSelectedItem();
        String answer = answer_field_forgot.getText();

        String[] forgotDetails = new String[]{username,selectedQuestion,answer};
        Message message = new Message( "Forgot Password Request",forgotDetails,null);
        SimpleClient.getClient().sendToServer(message);
    }

    @Subscribe
    public void password_change_form(ForgotPasswordEvent event) throws IOException {
        String message = event.getMessage().getMessage();
        if(message.equals("Forgot Password: Match")) {
            Platform.runLater(() -> {
                login_form.setVisible(false);
                forgot_form.setVisible(false);
                new_password_form.setVisible(true);
            });
        }
        else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "There is no such Username, or the password is wrong");
                alert.setTitle("Login Failed");
                alert.setHeaderText("Login Failed");
                alert.show();
            });
        }
    }
    // push
    @FXML
    void new_password(ActionEvent event) throws IOException {
        if (new_password_field.getText().isEmpty() || confirm_password_field.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill all blank fields");
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.show();
        }
        else if (!new_password_field.getText().equals(confirm_password_field.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Passwords do not match");
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.show();
        }
        else {
            String userName = username_field_forgot.getText();
            String password = new_password_field.getText();
            String[] details = new String[]{userName,password};
            Message message = new Message( "New Password Request",details,null);
            SimpleClient.getClient().sendToServer(message);
        }
    }
    @Subscribe
    public void passwordChanged(PasswordChangeEvent event) {
        String message = event.getMessage().getMessage();
        if(message.equals("Password Change Succeed")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your password has successfully been changed!");
                alert.setTitle("Password change");
                alert.setHeaderText(null);
                alert.show();
                login_form.setVisible(true);
                forgot_form.setVisible(false);
                new_password_form.setVisible(false);
            });
        }
        else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password Change Failed!");
                alert.setTitle("Password Change Failed");
                alert.setHeaderText(null);
                alert.show();
            });
        }
    }
    @FXML
    void switchToPrimary(ActionEvent event) throws IOException {
        SceneManager.switchScene("primary.fxml",event);
    }
    public void initialize() {
        question_bar_forgot.getItems().addAll(securityQuestions);
        try {
            EventBus.getDefault().register(this);
            Message message = new Message(0, "add client");
            SimpleClient.getClient().sendToServer(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}