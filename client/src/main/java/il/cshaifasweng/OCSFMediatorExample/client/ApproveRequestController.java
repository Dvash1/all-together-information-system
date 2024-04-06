package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import il.cshaifasweng.OCSFMediatorExample.client.events.*;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApproveRequestController {
    private User currentUser = SimpleChatClient.getUser();
    private ToggleGroup toggleGroup;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    @FXML
    private RadioButton approveRB;

    @FXML
    private RadioButton denyRB;

    @FXML
    private TableColumn<Task, LocalDateTime> creationTimeTC;

    @FXML
    private TableColumn<Task, String> requiredTaskTC;

    @FXML
    private TableColumn<Task, User> taskCreatorTC;

    private ObservableList<Task> taskList ;


    @FXML
    private TextArea denyRequestTA;

    @FXML
    private Button submitBtn;
    @FXML
    private Button backBtn;
    @FXML
    private TableView<Task> tasksTableView;

    @FXML
    private Button emergencyButton;

    @FXML
    void emergency_button_press(ActionEvent event) throws IOException {
        SimpleChatClient.sendEmergencyRequest(SimpleChatClient.getUser());
    }

    @FXML
    void showPreviousScene(ActionEvent event)
    {
        try {
            EventBus.getDefault().unregister(this);
            SimpleChatClient.setRoot("mainmenu");
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }

    // I assume that there is only one community manager, so i wont write any code supporting "live update" in the case of two community managers (from the same community) browsing the table.
    @FXML
    void handleSubmission(ActionEvent event)
    {
        Message newMessage;
        RadioButton selectedRB = (RadioButton) toggleGroup.getSelectedToggle();
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();



        if (selectedRB == approveRB) {
            //Approve request, publish to all users from the community

            selectedTask.setTaskState("Request");
            newMessage = new Message("Update task",selectedTask,currentUser);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Request approved");
                alert.setHeaderText(null);
                alert.setContentText("The request has been approved and has now been published to the members of your community.");
                alert.showAndWait();
            });
        }
        else
        {
            //Deny request, send message to user who uploaded it
            selectedTask.setTaskState("Denied");

            newMessage = new Message("Update task",selectedTask,currentUser);
            String text = "The task \"" + selectedTask.getRequiredTask() + "\"\nhas been denied with the message:\n" + denyRequestTA.getText();
            UserMessage user_message = new UserMessage(text, currentUser.getTeudatZehut(), selectedTask.getTaskCreator().getTeudatZehut() , "Normal");
            // To, From, Text
            Message denialMessage = new Message("Send message",user_message);
            try {
                SimpleClient.getClient().sendToServer(denialMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }


            denyRequestTA.clear();
            Platform.runLater(() ->{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Request denied");
                alert.setHeaderText(null);
                alert.setContentText("The request has been denied.\nAn explanatory message regarding the reason for denial has been sent to the user.\n");
                alert.showAndWait();
            });
        }

        try {

            SimpleClient.getClient().sendToServer(newMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        taskList.remove(selectedTask);

// clear selections
        submitBtn.setDisable(true);
        toggleGroup.selectToggle(null);
        tasksTableView.getSelectionModel().clearSelection();

    }


    @Subscribe
    public void displayTasks(AwaitingApprovalTasksEvent event)
    {
        Message message = event.getMessage();
        taskList = FXCollections.observableArrayList((List<Task>) message.getObject());
        tasksTableView.setItems(taskList);
    }

    //add newly created task to list
    @Subscribe
    public void createTask(CreateTaskEvent event)
    {
        Message message = event.getMessage();
        taskList.add((Task) message.getObject());
    }

    public void initialize() {

        EventBus.getDefault().register(this);
        // create toggle group for radio buttons, once an option has been selected the submit button will be clickable
         toggleGroup = new ToggleGroup();
         approveRB.setToggleGroup(toggleGroup);
         denyRB.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                submitBtn.setDisable(false);
            }
            if (newToggle == denyRB) {
                denyRequestTA.setDisable(false);
            } else {
                denyRequestTA.setDisable(true);
            }
            System.out.println("toggleGroup listener consumed");
        });


        taskCreatorTC.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getTaskCreator().getUserName()));
        // set values for columns
        requiredTaskTC.setCellValueFactory(new PropertyValueFactory<>("requiredTask"));
        creationTimeTC.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        //add formatter to LocalDateTime object
        creationTimeTC.setCellFactory(column -> new TableCell<Task, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        try {
            Message newMessage = new Message("get awaiting approval requests",currentUser);
            SimpleClient.getClient().sendToServer(newMessage);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}