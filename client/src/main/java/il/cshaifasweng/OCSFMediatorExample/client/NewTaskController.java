package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDateTime;

public class NewTaskController {

    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton B1;

    @FXML
    private RadioButton B2;

    @FXML
    private RadioButton B3;

    @FXML
    private RadioButton B4;

    @FXML
    private RadioButton otherBtn;

    @FXML
    private TextField otherTF;

    @FXML
    private Button submitBtn;

    @FXML
    void createNewTask(ActionEvent event) {
        // check if any button was selected
        Toggle toggle = toggleGroup.getSelectedToggle();
        if (toggle == null)
        {
            Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a task.");
            alert.showAndWait();

            });
            return;
        }
        // dummy user for now, will get it directly from controller when implemented
        User testUser = new User("Billy Gates","999999999","password",true);
        RadioButton selectedButton = (RadioButton) toggle;
        String btnText = selectedButton.getText();
        if(btnText.equals("other"))
        {
            btnText = otherTF.getText();
        }
        Task testTask = new Task(btnText, LocalDateTime.now(),"Request",testUser);
        try {
            Message message = new Message("create task",testTask);
            SimpleClient.getClient().sendToServer(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void newTaskCreated(newTaskEvent event)
    {
        Message message = event.getMessage();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Your request has been added,a message has been sent to your community manager (Not really)");
            alert.showAndWait();
        });
    }
    public void initialize() {
        try {
            Message message = new Message(0, "add client");
            SimpleClient.getClient().sendToServer(message);
            EventBus.getDefault().register(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // create new toggle group and add all buttons to it
        toggleGroup = new ToggleGroup();
        B1.setToggleGroup(toggleGroup);
        B2.setToggleGroup(toggleGroup);
        B3.setToggleGroup(toggleGroup);
        B4.setToggleGroup(toggleGroup);
        otherBtn.setToggleGroup(toggleGroup);
    }
}