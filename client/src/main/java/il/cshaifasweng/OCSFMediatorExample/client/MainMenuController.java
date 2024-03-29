package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.loadFXML;

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
    void switchToCommunityInfo(ActionEvent event) {

    }

    @FXML
    void switchToHistograms(ActionEvent event) {

    }

    @FXML
    void switchToTasks(ActionEvent event) {

    }

    @FXML
    void showUsername(String username){
        hi_label.setText("Hello " + username);
    }

    @FXML
    void manager_options_pressed(ActionEvent event) {
        User user = SimpleChatClient.getUser();
        if (user != null && user.isManager()) {
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
            EventBus.getDefault().unregister(this); // TODO: check why you get a warning after pressing this?
            SimpleChatClient.setUser(null);
            SimpleChatClient.setRoot("login");
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void initialize() {
        User user = SimpleChatClient.getUser(); // Write into the label the username.
        mainmenu_anchor_manager.setVisible(false);
        managerButton.setVisible(false);
        if (user != null) {
            showUsername(user.getUserName());
            if (user.isManager()) {
                managerButton.setVisible(true);
            }
        }
        else {
            System.out.println("for some reason, user is null and this is not okay, okay?");
        }

    }
}