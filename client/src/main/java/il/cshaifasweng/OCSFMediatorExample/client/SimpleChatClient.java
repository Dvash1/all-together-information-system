package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.util.List;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

import static il.cshaifasweng.OCSFMediatorExample.client.ViewTasksController.currentUser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    private static Stage stage;
    private SimpleClient client;

    private static User user = null;


    @Override
    public void start(Stage stage) throws IOException {

        EventBus.getDefault().register(this);
//    	client = SimpleClient.getClient();
//    	client.openConnection();
        scene = new Scene(loadFXML("ConnectToServer"), 500, 500); // TODO: make this modular
        stage.setScene(scene);

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

//    public void alert(String message) throws IOException {
//
//        EventBus.getDefault().register(this);
//
//        event.consume();
//
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("New message");
//        alert.setHeaderText("");
//        alert.setContentText(message);
//        alert.setGraphic(null);
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            stage.close();
//        }
//
//    }

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

        String from = (String) event.getMessage().getObjectsArr().get(1);
        System.out.println(from);
        switch(userMessage.getMessage_type()) { // Switch uses equals()
            case "Normal": // Create an alert
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("You have a new message");
                    alert.setHeaderText("From: " + from + " Sent on:" + userMessage.getWas_sent_on());
                    alert.setContentText(userMessage.getMessage());
                    alert.showAndWait();
                });
                break;

            case "Not Complete":
                // Pop up with ok/no
                break;



        }


    }





    public static void setUser(User user) {
        SimpleChatClient.user = user;
    }

    public static User getUser() {
        return user;
    }




    static void setRoot(String fxml) throws IOException { // TODO: make this modular.
        scene.setRoot(loadFXML(fxml));

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
//        Platform.exit();
//        System.exit(0);
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