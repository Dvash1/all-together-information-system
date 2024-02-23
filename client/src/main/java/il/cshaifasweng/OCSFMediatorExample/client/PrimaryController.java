package il.cshaifasweng.OCSFMediatorExample.client;


import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import org.hibernate.Session;
import org.hibernate.query.Query;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class PrimaryController {
	@FXML
	private ListView<String> myListView;

	@FXML
	private Label myLabel;

	@FXML
	private TextField myTextField1;

	@FXML
	private TextField myTextField2;

	@FXML
	private TextField myTextField3;

	@FXML
	private TextField myTextField4;

	@FXML
	private TextField myTextField5;

	@FXML // fx:id="TaskDetailsBox"
	private AnchorPane TaskDetailsBox; // Value injected by FXMLLoader


	@FXML
	private Button status_button;
	String[] tasks = {"task1","task2","task3","task4","task5","task6"};

	String currentTask;


	private int msgId;

	@FXML
	void updateStateTaskEvent(ActionEvent event) {
		currentTask = myListView.getSelectionModel().getSelectedItem();
		try {
			int taskId = Integer.parseInt("" + currentTask.charAt(4));

			Message message = new Message(taskId,"Update State");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Subscribe
	public void taskEvent(NewTaskEvent event){
		Message message = event.getMessage();
		Task task = message.getTask();
		myTextField1.setText("Created by:" + task.getTaskCreator().getUserName());
		myTextField2.setText("Created on:" + task.getCreationTime());
		myTextField3.setText("Task ID:" + task.getId());
		myTextField4.setText("Task state:" + task.getTaskState());
		myTextField5.setText("Description:" + task.getRequiredTask());

	}
	@Subscribe
	public void updateEvent(UpdateTaskEvent event) {
		Message message = event.getMessage();
		Task task = message.getTask();
		myTextField1.setText("Created by:" + task.getTaskCreator().getUserName());
		myTextField2.setText("Created on:" + task.getCreationTime());
		myTextField3.setText("Task ID:" + task.getId());
		myTextField4.setText("Task state:" + task.getTaskState());
		myTextField5.setText("Description:" + task.getRequiredTask());
	}
	@FXML
	void initialize() {
		try {
			Message message = new Message(0, "add client");
			SimpleClient.getClient().sendToServer(message);

			EventBus.getDefault().register(this);
			myListView.getItems().addAll(tasks);
			myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
					currentTask = myListView.getSelectionModel().getSelectedItem();

					int taskId = Integer.parseInt("" + currentTask.charAt(4));
					try {
						Message message1 = new Message(taskId, "Get Data");
						SimpleClient.getClient().sendToServer(message1);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			});


		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
