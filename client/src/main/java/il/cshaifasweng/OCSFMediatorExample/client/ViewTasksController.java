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
import java.util.List;

public class ViewTasksController {

    public static User currentUser = SimpleChatClient.getUser();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    @FXML
    private TableColumn<Task, LocalDateTime> creationTimeTC;

    @FXML
    private TableColumn<Task, String> requiredTaskTC;

    @FXML
    private TableColumn<Task, User> taskCreatorTC;

    @FXML
    private TableColumn<Task, String> taskStateTC;

    @FXML
    private TableColumn<Task, User> taskVolunteerTC;

    @FXML
    private TableView<Task> tasksTableView;

    @FXML
    private Button volunteerBtn;

    @FXML
    private Button requestBtn;

    @FXML
    private Button completeBtn;

    @FXML
    private Button withdrawBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button emergencyButton;

    private ObservableList<Task> taskList ;

    @FXML
    private TextArea messageToManagerTA;



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

    @Subscribe
    public void displayNewTask(ApprovedTaskEvent event) {

        System.out.println("displayNewTask called");
        Message message = event.getMessage();
        Task task = (Task) message.getObject();
        // only add if they are from the same community
        if (task.getTaskCreator().getCommunity().getCommunityName().equals(currentUser.getCommunity().getCommunityName())) {
            taskList.add(task);
        }
    }

    @FXML
    void openNewTask(ActionEvent event) {

        Platform.runLater(() -> {
            try {

                EventBus.getDefault().unregister(this);
                SimpleChatClient.setRoot("NewTask");
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }
    @FXML
    void completeTask(ActionEvent event) {

        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        selectedTask.setTaskState("Complete");
        selectedTask.setCompletionTime(LocalDateTime.now());
        String text = messageToManagerTA.getText();
        messageToManagerTA.clear();

        try {
            Message message = new Message(selectedTask.getId(),"Complete the task");
            message.setUser(currentUser);
            message.setObject(text);
            SimpleClient.getClient().sendToServer(message);

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Task completed");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully completed the task.\nA message has been sent to your community manager.");
                alert.showAndWait();
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void completeTaskUpdate(CompleteTaskEvent event)
    {
        // in general, we need to verify that the tasks we're adding is from the same community (because we are sending to all clients)
        // however, because the task ids are unique, this should not be an issue in this scenario.

        Message message = event.getMessage();
        Task updatedTask = (Task) message.getObject();
        // search for matching index in the observableList,then update task
        for (int i = 0;i< taskList.size();i++)
        {
            Task task = taskList.get(i);
            if(task.getId() == updatedTask.getId())
            {
                // if a different client is selecting the to be removed row

                if(tasksTableView.getSelectionModel().getSelectedItem() == task)
                {
                    withdrawBtn.setDisable(true);
                    completeBtn.setDisable(true);
                    tasksTableView.getSelectionModel().clearSelection();
                }
                taskList.remove(task);
                break;
            }
        }
    }


    @FXML
    void withdrawVolunteering(ActionEvent event)
    {
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        selectedTask.setTaskState("Request");
        selectedTask.setTaskVolunteer(null);

        completeBtn.setDisable(true);
        withdrawBtn.setDisable(true);


        try {
            Message message = new Message("Withdraw from task",selectedTask,currentUser);
            SimpleClient.getClient().sendToServer(message);

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully withdrawn from volunteering");
                alert.showAndWait();
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void updateTaskWithdraw(WithdrawEvent event)
    {

        Message message = event.getMessage();
        Task updatedTask = (Task) message.getObject();

        for (int i = 0;i< taskList.size();i++)
        {
            Task task = taskList.get(i);
            if(task.getId() == updatedTask.getId())
            {
                taskList.set(i,updatedTask);
            }
        }
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if(selectedTask != null && selectedTask.getId() == updatedTask.getId())
        {
            volunteerBtn.setDisable(false);
        }


    }




    @FXML
    void volunteerToTask(ActionEvent event) {

        currentUser = SimpleChatClient.getUser();
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        selectedTask.setTaskState("In Progress");
        selectedTask.setTaskVolunteer(currentUser);
        try {
            Message message = new Message("Update task",selectedTask,currentUser);
            SimpleClient.getClient().sendToServer(message);

            // Scheduler implemented in simpleserver
//            String manager_zehut = currentUser.getCommunity().getCommunityManager().getTeudatZehut();
//            String message_text = "24 hours have passed on the task:\n\"" + selectedTask.getRequiredTask() + "\"\nBy: " + selectedTask.getTaskCreator().getUserName() + "\n" + "Are you finished with the task?";
//            UserMessage not_completed_usermessage = new UserMessage(message_text, manager_zehut, currentUser.getTeudatZehut(),"Not Complete");


            Message not_completed_message = new Message(selectedTask.getId(),"Task not completed on time");
            SimpleClient.getClient().sendToServer(not_completed_message);

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully volunteered for the task.\nYou have 24 hours");
                alert.showAndWait();
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


// same as function for complete task, need to merge them to one.
    @Subscribe
	public void updateToInProgress(VolunteerToTaskEvent event)
    {
        // in general, we need to verify that the tasks we're adding is from the same community (because we are sending to all clients)
        // however, because the task ids are unique, this should not be an issue in this scenario.
        Message message = event.getMessage();
        Task updatedTask = (Task) message.getObject();

        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        // disable volunteer button in the chance that multiple users are hovering the same task
        if(selectedTask != null && selectedTask.getId() == updatedTask.getId())
        {
            volunteerBtn.setDisable(true);
        }

        // Update observableList
        for (int i = 0;i< taskList.size();i++)
        {
            Task task = taskList.get(i);
            if(task.getId() == updatedTask.getId())
            {
                taskList.set(i,updatedTask);
            }
        }


    }


    @Subscribe
    public void loadTasks(LoadOpenTasksEvent event) {
        System.out.println("loadTasks called");
        Message message = event.getMessage();
        taskList = FXCollections.observableArrayList((List<Task>) message.getObject());
        tasksTableView.setItems(taskList);
    }


    public void initialize()
    {
        EventBus.getDefault().register(this);
        currentUser = SimpleChatClient.getUser();
        messageToManagerTA.setText("");

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
                }
                else
                {
                    setText(item.format(formatter));
                }
            }
        });


        taskStateTC.setCellValueFactory(new PropertyValueFactory<>("taskState"));
        taskCreatorTC.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getTaskCreator().getUserName()));

        // Display an empty cell if there is no volunteer to the task
        taskVolunteerTC.setCellValueFactory(data -> {
            User taskVolunteer = data.getValue().getTaskVolunteer();
            String userName = (taskVolunteer != null) ? taskVolunteer.getUserName() : "";
            return new SimpleObjectProperty(userName);

        });


        // enable/disable volunteering&task completion buttons
        tasksTableView.setOnMouseClicked(e -> {
            Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
            // check if user can volunteer to the task
            // can also override the equals function in Task instead of comparing ID's
            if(selectedTask != null)
            {
                if (selectedTask.getTaskState().equals("Request") && selectedTask.getTaskCreator().getId() != currentUser.getId()) {

                    volunteerBtn.setDisable(false);
                } else {
                    volunteerBtn.setDisable(true);
                }
                //getTaskVolunteer() might return null, so we must check for that as well
                if (selectedTask.getTaskVolunteer() != null && selectedTask.getTaskState().equals("In Progress") && selectedTask.getTaskVolunteer().getId() == currentUser.getId()) {
                    completeBtn.setDisable(false);
                    withdrawBtn.setDisable(false);

                } else {
                    completeBtn.setDisable(true);
                    withdrawBtn.setDisable(true);
                }
            }
        });
        try {
            Message newMessage = new Message("get open tasks",currentUser);
            SimpleClient.getClient().sendToServer(newMessage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
