package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
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
    private Button button;

    @FXML
    private Label hi_label;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane mainmenu_anchor_all;
    @FXML
    private AnchorPane mainmenu_anchor_manager;

    @FXML
    private Button tasks_list;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void showUsername(String username){
        hi_label.setText("Hello " + username);
    }
    @FXML
    void manager_options_pressed(ActionEvent event) {
//        if(USER IS MANAGER){
//            mainmenu_anchor_all.setVisible(false);
//            mainmenu_anchor_manager.setVisible(true);
//        }

    }
    @FXML
    void logout(ActionEvent event) throws IOException {
        try {
            EventBus.getDefault().unregister(this);
            SimpleChatClient.setRoot("login");
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void initialize() {
//        try {
//            println("hello");
////            EventBus.getDefault().register(this);
////            Message message = new Message(0, "add client");
////            SimpleClient.getClient().sendToServer(message);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}