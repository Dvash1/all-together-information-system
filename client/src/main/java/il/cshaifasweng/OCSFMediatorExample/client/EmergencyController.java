package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class EmergencyController {

    @FXML
    private Button emergency_send_button;

    @FXML
    private TextField emergency_teudatzeut_fill;

    @FXML
    void emergency_send(ActionEvent event) throws IOException {
        if (emergency_teudatzeut_fill.getText().isEmpty() || (!emergency_teudatzeut_fill.getText().matches("\\d+")
            || emergency_teudatzeut_fill.getText().length() != 9)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid teudat zzeut, try again");
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.show();
        }
        else {
            String teudatzeut = emergency_teudatzeut_fill.getText();
            Message message = new Message("Emergency Request", teudatzeut, null);
            SimpleClient.getClient().sendToServer(message);
        }

    }
    @Subscribe
    public void emergencyResponse(EmergencyEvent event) {
        String response = event.getMessage().getMessage();
        System.out.println(response);
        if(response.equals("Emergency Call Succeed")){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your emergency call has been forwarded");
                alert.setTitle("Emergency Call Success");
                alert.setHeaderText(null);
                alert.show();
            });
        }
        else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No user found with the given the teudat zeut");
                alert.setTitle("Emergency Call Failed");
                alert.setHeaderText(null);
                alert.show();
            });
        }
    }
    public void initialize() {
        EventBus.getDefault().register(this);
    }

}
