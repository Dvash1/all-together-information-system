package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.util.List;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static il.cshaifasweng.OCSFMediatorExample.client.ViewTasksController.currentUser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.plaf.synth.Region;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static Stage stage;
    private SimpleClient client;

    private static User user = null;


    @Override
    public void start(Stage primaryStage) throws IOException {

        EventBus.getDefault().register(this);
//        Parent fxml_loaded = loadFXML("ConnectToServer");
//        scene = new Scene(loadFXML("ConnectToServer"), 1280, 900);
//        stage.setScene(scene);
//        SimpleChatClient.stage = stage;
        stage = primaryStage;
        setRoot("ConnectToServer"); // Load initial FXML
        stage.show();
        stage.setOnCloseRequest(event -> {
            event.consume();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("");
            alert.setContentText("Are you sure you want to close the ATIS application?");
            alert.setGraphic(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage.close();
            }
        });
        stage.show();
    }

    // delete/replace with sending message to users
    @Subscribe
    public void testEvent(getDataEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("This is a test");
            alert.setHeaderText("");
            alert.setContentText("Hopefully this is shown to all users");
            alert.showAndWait();
        });
    }

    @Subscribe
    public void NewMessageEvent(NewMessageEvent event) {
        System.out.println(event.getMessage().getObjectsArr().size());
        UserMessage userMessage = (UserMessage) event.getMessage().getObjectsArr().get(0); // Get the usermessage
        String formatted_date = (userMessage.getWas_sent_on()).format(formatter);
        String from = (String) event.getMessage().getObjectsArr().get(1);
        System.out.println(from);

        switch(userMessage.getMessage_type()) { // Switch uses equals()
            case "Community":
            case "Normal": // Create an alert
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("You have a new message");
                    if (userMessage.getMessage_type().equals("Community")) {
                        alert.setHeaderText("From: System, Sent on: " + formatted_date);
                    }
                    else {
                        alert.setHeaderText("From: " + from + ", Sent on: " + formatted_date);
                    }
                    alert.setContentText(userMessage.getMessage());
                    alert.showAndWait();
                });
                break;

            case "Not Complete":
                // Pop up with ok/no
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Pending task completion");
                    alert.setHeaderText("Sent on: " + formatted_date);
                    alert.setContentText(userMessage.getMessage());
                    alert.setGraphic(null);


                    ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                    ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().addAll(buttonTypeYes,buttonTypeNo);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get().equals(buttonTypeYes)) {
                        // Pop a textinputdialog.
                        TextInputDialog tiDialog = new TextInputDialog();
                        tiDialog.setTitle("Send a completion message to your manager");
                        tiDialog.setHeaderText("Please enter a message to send to your community manager: ");
                        tiDialog.setContentText("Message: ");


                        Optional<String> dialog_result = tiDialog.showAndWait();

                        if (dialog_result.isPresent()) {
                            String to_manager_text = "Task done by: \"" + user.getUserName() + "\"\nHas been marked complete with the message:\n\"" + dialog_result.get() + "\"";
                            UserMessage to_manager_message = new UserMessage(to_manager_text , user.getTeudatZehut(), user.getCommunity().getCommunityManager().getTeudatZehut(), "Normal");
                            Message to_send = new Message("Send message", to_manager_message);

                            try {
                                SimpleClient.getClient().sendToServer(to_send);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // Update the task to complete.
                            Message message = new Message("Complete the task",user);
                            message.setTaskID(userMessage.getTask_id());
                            try {
                                SimpleClient.getClient().sendToServer(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            // Pop a successfully sent alert.
                            Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                            alert3.setTitle("You have a new message");
                            alert3.setHeaderText("");
                            alert3.setContentText("Message has been sent successfully.");
                            alert3.showAndWait();


                        }
                        else {
                            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setTitle("You have a new message");
                            alert2.setHeaderText("");
                            alert2.setContentText("Once you have completed the task, please update your community manager.\nIf you're experiencing trouble completing the task, you may withdraw through the task list.");
                            alert2.showAndWait();
                        }
                    } else  {
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("You have a new message");
                        alert2.setHeaderText("");
                        alert2.setContentText("Once you have completed the task, please update your community manager.\nIf you're experiencing trouble completing the task, you may withdraw through the task list.");
                        alert2.showAndWait();
                    }
                });
                break;


        }
    }

    public static void sendEmergencyRequest(User requestingUser)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("");
        alert.setContentText("Are you sure you want to proceed?");
        alert.setGraphic(null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Emergency emergencyRequest = new Emergency(requestingUser,LocalDateTime.now());
            Message emergencyMessage = new Message("Emergency Request",emergencyRequest,requestingUser);
            try {
                SimpleClient client = SimpleClient.getClient();
                client.sendToServer(emergencyMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Your emergency call has been forwarded");
                alert2.setTitle("Emergency Call Success");
                alert2.setHeaderText(null);
                alert2.showAndWait();
            });
        }




    }
    public static void setUser(User user) {
        SimpleChatClient.user = user;
    }

    public static User getUser() {
        return user;
    }




    static void setRoot(String fxml) throws IOException { // TODO: make work :)
        Parent root = loadFXML(fxml);
        scene = new Scene(root);
        // Set the width and height of the scene to match the loaded FXML
        double width = root.prefWidth(-1);
        double height = root.prefHeight(width);

        System.out.print("width:");
        System.out.println(width);
        System.out.print("Height:");
        System.out.println(height);
        // Get the width and height of the scene from the FXML

        scene.setRoot(root);
        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);

    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChatClient.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    

    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        super.stop();
    }

	public static void main(String[] args) {
        launch();
        try {
            System.out.println("closing connection to server");
            SimpleClient client = SimpleClient.getClient();
            client.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}