package il.cshaifasweng.OCSFMediatorExample.client;

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


/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;



    @Override
    public void start(Stage stage) throws IOException {

    	EventBus.getDefault().register(this);
//    	client = SimpleClient.getClient();
//    	client.openConnection();
        scene = new Scene(loadFXML("ConnectToServer"), 1280, 900);
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

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
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

	public static void main(String[] args)
    {
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