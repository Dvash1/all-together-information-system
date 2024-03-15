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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewTasksController {

    public static User currentUser;

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

    private ObservableList<Task> taskList ;


//*** DELETE ****
    @FXML
    private Button tempBtn;

//*** DELETE ****


    @FXML
    void completeTask(ActionEvent event) {

        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        selectedTask.setTaskState("Complete");
        selectedTask.setCompletionTime(LocalDateTime.now());
        try {
            Message message = new Message("Update task",selectedTask);
            SimpleClient.getClient().sendToServer(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void completeTaskUpdate(CompleteTaskEvent event) {

        Message message = event.getMessage();
        Task updatedTask = (Task) message.getObject();
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();

        int index = taskList.indexOf(selectedTask);
        taskList.set(index, updatedTask);

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Task completed");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully completed the task.\nA message has been sent to your community manager.(NOT REALLY NEED TO IMPLEMENT)");
            alert.showAndWait();
        });
    }
    @FXML
    void openNewTask(ActionEvent event) {

        Platform.runLater(() -> {
            try {
                //change back to ViewTasks
                SimpleChatClient.setRoot("NewTask");
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }


//**************** DELETE ******************//
    @FXML
    void switchToViewEmergency(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                //change back to ViewTasks
                SimpleChatClient.setRoot("ViewEmergencyCalls");
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }
//**************** DELETE ******************//





    @FXML
    void volunteerToTask(ActionEvent event) {

        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        selectedTask.setTaskState("In Progress");
        selectedTask.setTaskVolunteer(currentUser);
        try {
            Message message = new Message("Update task",selectedTask);
            SimpleClient.getClient().sendToServer(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Subscribe
	public void updateToInProgress(VolunteerToTaskEvent event)
    {
        Message message = event.getMessage();
        Task updatedTask = (Task) message.getObject();
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();

        int index = taskList.indexOf(selectedTask);
        taskList.set(index,updatedTask);

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully volunteered for the task.\nYou have 24 hours");
            alert.showAndWait();
        });

    }
    @Subscribe
    public void loadTasks(LoadTasksEvent event)
    {
        Message message = event.getMessage();
        taskList = FXCollections.observableArrayList((List<Task>) message.getObject());
        tasksTableView.setItems(taskList);

        // ****IMPORTANT****
        //in the future when one client creates a new task, the steps to update the GUI of all clients should be:
        //- message will send a message to ALL subscribed clients with the new Task
        //- we create a new Event class that will do the following line :
        // check that the currentUser and the newly created task come from the same community
        // check that the current displayed scene is the view tasks one ***
        // call : taskList.add(new task to be added)
        // this should update all the clients interfaces automatically ( assuming they are browsing tasks)
        // *** we might not have to check the current scene, maybe adding the task to taskList will be enough.
    }

    // "Log in"
    @Subscribe
    public void setLoggedInUser(GetUserEvent event)
    {
        Message message = event.getMessage();
        currentUser = (User) message.getObject();

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
                } else {
                    completeBtn.setDisable(true);
                }
            }
        });
        // ******* remove the function when Log in will be implemented **********
        try {
            Message newMessage = new Message("get tasks",currentUser);
            SimpleClient.getClient().sendToServer(newMessage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }






    public void initialize()
    {
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

//        // enable/disable volunteering&task completion buttons
//        tasksTableView.setOnMouseClicked(e -> {
//            Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
//            // check if user can volunteer to the task
//            // can also override the equals function in User instead of comparing ID's
//            if (selectedTask.getTaskState().equals("Request") && selectedTask.getTaskCreator().getId() != currentUser.getId()) {
//
//                volunteerBtn.setDisable(false);
//            } else {
//                volunteerBtn.setDisable(true);
//            }
        //getTaskVolunteer() might return null so we must check for that as well
    //        if (selectedTask.getTaskVolunteer() != null && selectedTask.getTaskState().equals("In Progress") && selectedTask.getTaskVolunteer().getId() != currentUser.getId())
    //        {
    //            completeBtn.setDisable(false);
    //        }
    //        else {
    //            completeBtn.setDisable(true);
    //        }
//        });


        try {
            Message message = new Message(0, "add client");
            SimpleClient.getClient().sendToServer(message);
            EventBus.getDefault().register(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Message message = new Message("get user");
            SimpleClient.getClient().sendToServer(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // uncomment when Log in is implemented
        //
//        try {
//            Message message = new Message("get tasks",currentUser);
//            SimpleClient.getClient().sendToServer(message);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}
