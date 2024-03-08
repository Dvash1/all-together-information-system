package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ViewTasksController {

    public static User currentUser;

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

    private ObservableList<Task> taskList ;



    @FXML
    void openNewTask(ActionEvent event) {

        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("NewTask");
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    @FXML
    void volunteerToTask(ActionEvent event) {

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
        // taskList.add(new task to be added)
        // this should update all the clients interfaces automatically ( assuming they are browsing tasks, we can check what is the currect scene and decide what to do )
    }

    @Subscribe
    public void setLoggedUnUser(GetUserEvent event)
    {
        Message message = event.getMessage();
        currentUser = (User) message.getObject();
    }
    public void initialize()
    {
        // set values for columns
        requiredTaskTC.setCellValueFactory(new PropertyValueFactory<>("requiredTask"));
        creationTimeTC.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        taskStateTC.setCellValueFactory(new PropertyValueFactory<>("taskState"));
        taskCreatorTC.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getTaskCreator().getUserName()));

        // Display an empty cell if there is no volunteer to the task
        taskVolunteerTC.setCellValueFactory(data -> {
            User taskVolunteer = data.getValue().getTaskVolunteer();
            String userName = (taskVolunteer != null) ? taskVolunteer.getUserName() : "";
            return new SimpleObjectProperty(userName);

        });

// should not be here
        try {
            Message message = new Message(0, "add client");
            SimpleClient.getClient().sendToServer(message);
            EventBus.getDefault().register(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Message message = new Message("get tasks");
            SimpleClient.getClient().sendToServer(message);
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

    }
}
