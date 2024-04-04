package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class EmergencyController {

    @FXML
    private Button emergency_send_button;

    @FXML
    private TextField emergency_phonenumber_fill;

    @FXML
    void emergency_send(ActionEvent event) throws IOException {
        if (emergency_phonenumber_fill.getText().isEmpty() || (!emergency_phonenumber_fill.getText().matches("\\d+")
            || emergency_phonenumber_fill.getText().length() != 10)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid phone number, try again");
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.show();
        }
        else {
            String phoneNumber = emergency_phonenumber_fill.getText();
            Message message = new Message("Emergency Request", phoneNumber, null);
            SimpleClient.getClient().sendToServer(message);
        }

    }
    @Subscribe
    public void emergencyResponse(EmergencyEvent event) {
        String response = event.getMessage().getMessage();
        System.out.println(response);
        if(response.equals("Emergency Call Succeeded")){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your emergency call has been forwarded");
                alert.setTitle("Emergency Call Success");
                alert.setHeaderText(null);
                alert.showAndWait();
                EventBus.getDefault().unregister(this);
                Stage stage = (Stage) emergency_send_button.getScene().getWindow();
                stage.close();

            });
        }
        else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No user found with the given the phone number");
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
