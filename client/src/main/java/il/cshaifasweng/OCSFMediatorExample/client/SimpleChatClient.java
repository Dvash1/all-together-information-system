package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    private SimpleClient client;

    private static User user = null; // This is the client's user. TODO: need to make this a PRIVATE and not shared across member.


    @Override
    public void start(Stage stage) throws IOException {
    	EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
    	client.openConnection();
        scene = new Scene(loadFXML("ConnectToServer"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    @Subscribe
    public void mashuEvent(NewTaskEvent event) {

    }


    public static void setUser(User user) {
        SimpleChatClient.user = user;
    }

    public static User getUser() {
        return user;
    }




    static void setRoot(String fxml) throws IOException {
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
        Platform.exit();
        System.exit(0);
    }

	public static void main(String[] args) {
        launch();
    }

}