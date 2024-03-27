package il.cshaifasweng.OCSFMediatorExample.client;

import static il.cshaifasweng.OCSFMediatorExample.client.ViewTasksController.currentUser;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommunityInformationController {


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    @FXML
    private ListView<User> memberList;

    @FXML
    private TableView<Task> tasksTableView1;

    @FXML
    private TableView<Task> tasksTableView2;

    @FXML
    private TableColumn<Task, LocalDateTime> creationTimeTC1;

    @FXML
    private TableColumn<Task, LocalDateTime> creationTimeTC2;

    @FXML
    private TableColumn<Task, User> requesterTC2;

    @FXML
    private TableColumn<Task, String> requiredTaskTC1;

    @FXML
    private TableColumn<Task, String> requiredTaskTC2;

    @FXML
    private TableColumn<Task, String> taskStateTC1;

    @FXML
    private TableColumn<Task, String> taskStateTC2;

    @FXML
    private TableColumn<Task, User> volunteerTC1;

    @FXML
    private Button backBtn;
    // lists of all users&tasks from the same community (as the logged-in user)
    private List<User> users;

    private List<Task> tasks;

    private ObservableList<Task> taskListTC1 ;
    private ObservableList<Task> taskListTC2 ;
    @FXML
    private Button exportBtnFirst;
    @FXML
    private Button exportBtnSecond;

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



    @Subscribe
    public void displayNewTask(CreateTaskEvent event)
    {

        Message message = event.getMessage();
        Task task = (Task) message.getObject();
        // only add if they are from the same community
        if (task.getTaskCreator().getCommunity().getCommunityName().equals(currentUser.getCommunity().getCommunityName())) {

            tasks.add(task);
            User selectedUser = memberList.getSelectionModel().getSelectedItem();
            // if the current selected user in the listview is also the one that asked for help, we will add him manually to the tableview
            if(selectedUser != null && selectedUser.getId() == task.getTaskCreator().getId())
            {
                taskListTC1.add(task);
            }

        }

    }

    @Subscribe
    public void displayNewVolunteer(VolunteerToTaskEvent event)
    {
        Message message = event.getMessage();
        Task newTask = (Task) message.getObject();
        User selectedUser = memberList.getSelectionModel().getSelectedItem();

        updateTaskInList(newTask);
        // in general, we need to verify that the tasks we're adding is from the same community (because we are sending to all clients)
        // however, because the task ids are unique, this should not be an issue in this scenario.

//        if (newTask.getTaskCreator().getCommunity().getCommunityName().equals(currentUser.getCommunity().getCommunityName()))
//            {
//                updateTaskInList(newTask);
//            }
        // if the current selected user just volunteered to a task, we'll update the GUI
        if(selectedUser != null)
        {
            if (newTask.getTaskVolunteer() != null && selectedUser.getId() == newTask.getTaskVolunteer().getId()) {
                taskListTC2.add(newTask);

            }
            // if someone volunteered to a task the current selected user requested help with, we'll update the GUI
            else if (newTask.getTaskCreator().getId() == selectedUser.getId()) {
                for (int i = 0; i < taskListTC1.size(); i++) {
                    Task task = taskListTC1.get(i);
                    if (task.getId() == newTask.getId()) {
                        taskListTC1.set(i, newTask);
                    }
                }
            }
        }
    }

    @Subscribe
    public void withdrawVolunteering(WithdrawEvent event)
    {
        Message message = event.getMessage();
        Task newTask = (Task) message.getObject();
        updateTaskInList(newTask);

        User selectedUser = memberList.getSelectionModel().getSelectedItem();
        if (selectedUser != null)
        {
            for (int i = 0; i < taskListTC2.size(); i++) {
                Task task = taskListTC2.get(i);
                if (task.getId() == newTask.getId()) {
                    taskListTC2.remove(task);
                    break;
                }
            }
            for (int i = 0; i < taskListTC1.size(); i++) {
                Task task = taskListTC1.get(i);
                if (task.getId() == newTask.getId()) {
                    taskListTC1.set(i, newTask);
                    break;
                }

            }
        }
    }

    @Subscribe
    public void displayApprovedTask(ApprovedTaskEvent event)
    {
        Message message = event.getMessage();
        Task newTask = (Task) message.getObject();
        updateTaskInList(newTask);
        updateTaskState(newTask);
    }

    @Subscribe
    public void displayDeniedTask(DeniedTaskEvent event)
    {
        System.out.println("displayDeniedTask called");
        Message message = event.getMessage();
        Task newTask = (Task) message.getObject();
        updateTaskInList(newTask);
        updateTaskState(newTask);
    }

    @Subscribe
    public void displayCompletedTask(CompleteTaskEvent event)
    {
        Message message = event.getMessage();
        Task newTask = (Task) message.getObject();
        updateTaskInList(newTask);
        updateTaskState(newTask);
    }




    // call this function when only the state of a task is updated (awaiting approval -> Request,In progress -> Completed,  etc)
    private void updateTaskState(Task newTask)
    {
        User selectedUser = memberList.getSelectionModel().getSelectedItem();
        // if the current selected user just completed a task, we'll update the GUI
        if (selectedUser != null)
        {
            if(newTask.getTaskVolunteer() != null && selectedUser.getId() == newTask.getTaskVolunteer().getId())
            {
                for (int i = 0;i< taskListTC2.size();i++)
                {
                    Task task = taskListTC2.get(i);
                    if(task.getId() == newTask.getId())
                    {
                        taskListTC2.set(i,newTask);
                    }
                }

            }
            // if someone completed a task the current selected user requested help with, we'll update the GUI
            else if(newTask.getTaskCreator().getId() == selectedUser.getId())
            {
                for (int i = 0;i< taskListTC1.size();i++)
                {
                    Task task = taskListTC1.get(i);
                    if(task.getId() == newTask.getId())
                    {
                        taskListTC1.set(i,newTask);
                    }
                }
            }
        }
    }

    private void exportData(TableView<Task> table) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(table.getScene().getWindow());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // header
                writer.write("Required task,Creation time,Completion time,Task state,Requested by,Volunteer");
                writer.newLine();

                // iterate through every selected task,and write tasks details to writer
                for (Task task : table.getItems())
                {
                    String time = (task.getCompletionTime() == null) ? "" : task.getCompletionTime().format(formatter);
                    String volunteer = (task.getTaskVolunteer() == null) ? "None" : task.getTaskVolunteer().getUserName();

                    writer.write(task.getRequiredTask() + "," + task.getCreationTime().format(formatter)
                            + "," + time + "," +task.getTaskState() + "," + task.getTaskCreator().getUserName()+ "," + volunteer);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void exportFirst(ActionEvent event) {
        exportData(tasksTableView1);
    }

    @FXML
    void exportSecond(ActionEvent event) {
        exportData(tasksTableView2);
    }


    private void updateTaskInList(Task newTask)
    {
        // search for index, then replace with the updated task
        for (int i = 0;i< tasks.size();i++)
        {
            Task task = tasks.get(i);
            if(task.getId() == newTask.getId())
            {
                tasks.set(i,newTask);
            }
        }
    }



    @Subscribe
    public void displayUsers(getCommunityUsersEvent event)
    {
        Message message = event.getMessage();
        users = new ArrayList<User>();
        users = (List<User>) message.getObject();
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        memberList.setItems(userList);

    }

    @Subscribe
    public void getTasks(LoadAllTasksEvent event)
    {
        Message message = event.getMessage();
        tasks = new ArrayList<>();
        tasks = (List<Task>) message.getObject();
    }


    public void initialize()
    {

        EventBus.getDefault().register(this);


        // set what the cell in the listview should display when holding a User class object
        // in this case, we'll display the users full name
        memberList.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getUserName());
                }
            }
        });



        // setup for first table

        requiredTaskTC1.setCellValueFactory(new PropertyValueFactory<>("requiredTask"));
        creationTimeTC1.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        //add formatter to LocalDateTime object
        creationTimeTC1.setCellFactory(column -> new TableCell<Task, LocalDateTime>() {
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


        taskStateTC1.setCellValueFactory(new PropertyValueFactory<>("taskState"));

        // Display an empty cell if there is no volunteer to the task
        volunteerTC1.setCellValueFactory(data -> {
            User taskVolunteer = data.getValue().getTaskVolunteer();
            String userName = (taskVolunteer != null) ? taskVolunteer.getUserName() : "";
            return new SimpleObjectProperty(userName);

        });




        // setup for second table

        requiredTaskTC2.setCellValueFactory(new PropertyValueFactory<>("requiredTask"));
        creationTimeTC2.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        //add formatter to LocalDateTime object
        creationTimeTC2.setCellFactory(column -> new TableCell<Task, LocalDateTime>() {
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

        taskStateTC2.setCellValueFactory(new PropertyValueFactory<>("taskState"));
        requesterTC2.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getTaskCreator().getUserName()));



        memberList.setOnMouseClicked(e -> {
            User selectedUser = memberList.getSelectionModel().getSelectedItem();
            if(selectedUser!=null)
            {
// Filter lists to display only relevant tasks

                List<Task> filteredTasksTV1 = tasks.stream()
                        .filter(task -> selectedUser.getUserName().equals(task.getTaskCreator().getUserName()))
                        .collect(Collectors.toList());

                taskListTC1 = FXCollections.observableArrayList(filteredTasksTV1);
                tasksTableView1.setItems(taskListTC1);

                List<Task> filteredTasksTV2 = tasks.stream()
                        .filter(task -> {
                            User volunteer = task.getTaskVolunteer();
                            return volunteer != null && selectedUser.getUserName().equals(volunteer.getUserName());
                        })
                        .collect(Collectors.toList());
                taskListTC2 = FXCollections.observableArrayList(filteredTasksTV2);
                tasksTableView2.setItems(taskListTC2);

            }
        });



//send messages to server


        try {
            Message newMessage = new Message("get tasks",currentUser);
            SimpleClient.getClient().sendToServer(newMessage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Message newMessage = new Message("get users",currentUser);
            SimpleClient.getClient().sendToServer(newMessage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }



    }





}
