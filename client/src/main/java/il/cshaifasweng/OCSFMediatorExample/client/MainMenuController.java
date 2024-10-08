package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CommunityManagerUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import il.cshaifasweng.OCSFMediatorExample.client.events.*;
import java.io.IOException;


public class MainMenuController {
    @FXML
    private Button communityButton;

    @FXML
    private Label hi_label;

    @FXML
    private Button histogramButton;

    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane mainmenuAnchor;

    @FXML
    private AnchorPane mainmenu_anchor_manager;

    @FXML
    private Button managerButton;

    @FXML
    private Button tasksListButton;

    @FXML
    private Button emergencyButton;
    @FXML
    private Button requestButton;


    @FXML
    void switchToCommunityInfo(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                EventBus.getDefault().unregister(this);
                SimpleChatClient.setRoot("CommunityInformation");
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    @FXML
    void switchToApproveRequests (ActionEvent event) {
        Platform.runLater(() -> {
            try {
                EventBus.getDefault().unregister(this);
                SimpleChatClient.setRoot("ApproveRequest");
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }
    @FXML
    void emergency_button_press(ActionEvent event) throws IOException {
        SimpleChatClient.sendEmergencyRequest(SimpleChatClient.getUser());

    }

    @FXML
    void switchToHistograms(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                EventBus.getDefault().unregister(this);
                SimpleChatClient.setRoot("ViewEmergencyCalls");
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    @FXML
    void switchToTasks(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                EventBus.getDefault().unregister(this);
                SimpleChatClient.setRoot("ViewTasks");
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    @FXML
    void showUsername(String username){
        hi_label.setText("Hello " + username);
    }

    @FXML
    void manager_options_pressed(ActionEvent event) {
        User user = SimpleChatClient.getUser();
        if (user != null && (user instanceof CommunityManagerUser)) {
            if (mainmenu_anchor_manager.isVisible()) {
                mainmenu_anchor_manager.setVisible(false);
            }
            else {
                mainmenu_anchor_manager.setVisible(true);
            }
        }
    }
    @FXML
    void logout(ActionEvent event) throws IOException {
        try {
            Message newMessage = new Message("Log Out");
            newMessage.setObject(SimpleChatClient.getUser());
            SimpleClient.getClient().sendToServer(newMessage);
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Subscribe
    public void logOutFinish(LogOutEvent event) {

        String response = event.getMessage().getMessage();
        Platform.runLater(() -> {
            try {
                EventBus.getDefault().unregister(this);
                SimpleChatClient.setUser(null);
                SimpleChatClient.setRoot("login");
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    public void initialize() {
        EventBus.getDefault().register(this);
        User user = SimpleChatClient.getUser(); // Get the user
        mainmenu_anchor_manager.setVisible(false);
        managerButton.setVisible(false);

        if (user != null) {
            showUsername(SimpleChatClient.getUser().getUserName());
            if (user instanceof CommunityManagerUser) {
                managerButton.setVisible(true);
            }

            Message newMessage = new Message("Get user's messages");
            newMessage.setObject(user.getTeudatZehut());
            try {
                SimpleClient.getClient().sendToServer(newMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("For some reason, user is null and this is not okay, okay?");
        }
    }
}