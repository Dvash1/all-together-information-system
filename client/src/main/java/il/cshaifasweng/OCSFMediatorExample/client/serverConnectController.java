package il.cshaifasweng.OCSFMediatorExample.client;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class serverConnectController
{
    @FXML
    private Button connectBtn;

    @FXML
    private TextField textfield;


    @FXML
    void connectToServer(ActionEvent event) {
        try {
            String text = textfield.getText();
            int index = text.indexOf(":");
            if (index == -1)
            {
                throw new IllegalArgumentException("Missing ':' in server ip");
            }
            String host = text.substring(0,index);
            int port = Integer.parseInt(text.substring((index + 1)));
            SimpleClient.setHostAndPort(host,port);
            SimpleClient client = SimpleClient.getClient();
            client.openConnection(); // can throw an exception

            Platform.runLater(() ->{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Connection to server established.\nWelcome to ATIS.");
                alert.showAndWait();
                try {
                    SimpleChatClient.setRoot("ViewTasks");
                } catch (IOException e) {

                    e.printStackTrace();
                }
            });


        } catch (Exception e) {
            client = null;
            Platform.runLater(() ->{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("There was an error establishing a connection to the server.\nPlease make sure you have entered the correct server address.");
                alert.showAndWait();
            });
            e.printStackTrace();
        }

    }

}

