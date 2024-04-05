package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import static il.cshaifasweng.OCSFMediatorExample.client.ViewTasksController.currentUser;
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
    private RadioButton B5;

    @FXML
    private RadioButton B6;

    @FXML
    private RadioButton B7;

    @FXML
    private RadioButton B8;

    @FXML
    private RadioButton otherBtn;

    @FXML
    private TextField otherTF;

    @FXML
    private Button submitBtn;

    // Going back to menu button
    @FXML
    private Button menuButton;

    @FXML
    private Button emergencyButton;

    @FXML
    void emergency_button_press(ActionEvent event) throws IOException {
        SimpleChatClient.sendEmergencyRequest(SimpleChatClient.getUser());
    }
    @FXML
    void switchToMenu(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                //change back to ViewTasks
                SimpleChatClient.setRoot("ViewTasks"); // ***** Switch this to correct root when menu is implemented
            } catch (IOException e) {

                e.printStackTrace();
            }
        });

    }

    @FXML
    private Button backBtn;



    @FXML
    void showPreviousScene(ActionEvent event)
    {
        try {
            EventBus.getDefault().unregister(this);
            SimpleChatClient.setRoot("ViewTasks");
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }

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

        RadioButton selectedButton = (RadioButton) toggle;
        String btnText = selectedButton.getText();
        if(btnText.equals("other"))
        {
            btnText = otherTF.getText();
        }

        Task testTask = new Task(btnText, LocalDateTime.now(),"Awaiting approval",currentUser);
        try {
            Message message = new Message("create task",testTask,currentUser);
            SimpleClient.getClient().sendToServer(message);
            System.out.println("send to server from NewTaskController");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void newTaskCreated(CreateTaskEvent event)
    {
        EventBus.getDefault().unregister(this);
        Message message = event.getMessage();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Your request has been added,a message has been sent to your community manager (Not really)");
            alert.showAndWait();

            // close down new task window, go back to viewtasks
            Platform.runLater(() -> {
                try {
                    SimpleChatClient.setRoot("ViewTasks");
                } catch (IOException e) {

                    e.printStackTrace();
                }
            });
        });

    }
    public void initialize() {
        System.out.println("newTaskController initialized");
        EventBus.getDefault().register(this);


        // create new toggle group and add all buttons to it
        toggleGroup = new ToggleGroup();
        B1.setToggleGroup(toggleGroup);
        B2.setToggleGroup(toggleGroup);
        B3.setToggleGroup(toggleGroup);
        B4.setToggleGroup(toggleGroup);
        B5.setToggleGroup(toggleGroup);
        B6.setToggleGroup(toggleGroup);
        B7.setToggleGroup(toggleGroup);
        B8.setToggleGroup(toggleGroup);
        otherBtn.setToggleGroup(toggleGroup);
    }
}
