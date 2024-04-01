package il.cshaifasweng.OCSFMediatorExample.client;

// *******IMPORTANT*******
// because emergency class is not implemented yet, this code is missing an implementation of "live updating" (update the histogram whenever someone presses the emergency button)
// a simple solution could be to add the emergency object to the observable list, if the requirements are met.
// TODO: after an emergency button has been processed, send a msg to relevant clients to update them.

import il.cshaifasweng.OCSFMediatorExample.entities.Emergency;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import static il.cshaifasweng.OCSFMediatorExample.client.ViewTasksController.currentUser;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.*;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

import javafx.util.StringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ViewEmergencyCalls implements Initializable {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private ToggleGroup TG1;

    private ToggleGroup TG2;

    @FXML
    private RadioButton allCommunitiesRB;

    @FXML
    private RadioButton myCommunityRB;

    @FXML
    private RadioButton allDatesRB;

    @FXML
    private RadioButton specificDatesRB;

    @FXML
    private Button displayBtn;

    @FXML
    private Button backBtn;

    @FXML
    private DatePicker endDate;

    @FXML
    private DatePicker startDate;

    @FXML
    private BarChart<String, Number> hist;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    // Going back to menu button
    @FXML
    private Button menuButton;

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
    void disablePicker(ActionEvent event) {
        startDate.setDisable(true);
        endDate.setDisable(true);
    }

    @FXML
    void enablePicker(ActionEvent event) {
        startDate.setDisable(false);
        endDate.setDisable(false);
    }


    private void updateButtonState()
    {
        Toggle first = TG1.getSelectedToggle();
        Toggle second = TG2.getSelectedToggle();
        displayBtn.setDisable(first== null || second == null);

    }

    @FXML
    void displayHistogram(ActionEvent event) {
//        first we'll check that if the user pressed specific dates, that they are valid.
        if(specificDatesRB.isSelected())
        {
            LocalDate startDateSelected = startDate.getValue();
            LocalDate endDateSelected = endDate.getValue();

            if(startDateSelected == null || endDateSelected == null)
            {
                Platform.runLater(() ->
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid dates");
                    alert.setContentText("Invalid dates chosen.\nPlease choose dates in the format dd-MM-yyyy.");
                    alert.showAndWait();
                });

                return;
            }
            else if ((startDateSelected.isAfter(endDateSelected)))
            {
                Platform.runLater(() ->
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid dates");
                    alert.setContentText("Invalid dates chosen.\nYour start date must begin before the end date.");
                    alert.showAndWait();
                });
                return;
            }
        }


        Message message;
        RadioButton first = (RadioButton) TG1.getSelectedToggle();
        RadioButton second = (RadioButton) TG2.getSelectedToggle();



        if (first == allCommunitiesRB && second == allDatesRB)
        {
            message = new Message("emergency everything",currentUser);
        }
        else if (first == myCommunityRB && second == allDatesRB)
        {
            message = new Message("emergency my community all dates",currentUser);
        }
        // second == specific dates from here
        else {
            LocalDateTime startDate = LocalDateTime.of(this.startDate.getValue(), LocalTime.MIN);
            LocalDateTime endDate = LocalDateTime.of(this.endDate.getValue(), LocalTime.MAX);
            List<LocalDateTime> dateList = new ArrayList<>();
            dateList.add(startDate);
            dateList.add(endDate);

            if (first == allCommunitiesRB) {
                message = new Message("emergency all community specific dates", dateList, currentUser);
            } else {
                message = new Message("emergency my community specific dates", dateList, currentUser);
            }
        }

        try
        {
            SimpleClient.getClient().sendToServer(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void showHistEmergency(getEmergencyData event)
    {

        Message message = event.getMessage();
        List<Emergency> emergenciesList = (List<Emergency>) message.getObject();
        Map<String, Integer> datesToAmount = new TreeMap<>();
        // Populate datesToAmount
        for (Emergency emergency : emergenciesList) {
            LocalDate date = emergency.getCallTime().toLocalDate();
            String dateString = date.toString();
            datesToAmount.put(dateString, datesToAmount.getOrDefault(dateString, 0) + 1);
        }

        // create series
        ObservableList<XYChart.Data<String, Number>> histStats = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : datesToAmount.entrySet()) {
            String formattedDate = LocalDate.parse(entry.getKey()).format(dateFormatter);

            histStats.add(new XYChart.Data<>(formattedDate, entry.getValue()));
        }

        // Create a series and set its data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setData(histStats);



        Platform.runLater(() -> {

            int maxAmount = datesToAmount.values().stream().mapToInt(Integer::intValue).max().orElse(0);
            ((NumberAxis) hist.getYAxis()).setUpperBound(maxAmount + 1);
            hist.getData().setAll(series);
            hist.setVisible(true);
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        EventBus.getDefault().register(this);

        // remove non integer values such as 0.5,1.5 etc. from Y-axis
        ((NumberAxis) hist.getYAxis()).setTickUnit(1);
        ((NumberAxis) hist.getYAxis()).setMinorTickCount(0);
        hist.getYAxis().setAutoRanging(false);



        // create ToggleGroup for buttons
        TG1 = new ToggleGroup();
        allCommunitiesRB.setToggleGroup(TG1);
        myCommunityRB.setToggleGroup(TG1);
        TG2 = new ToggleGroup();
        allDatesRB.setToggleGroup(TG2);
        specificDatesRB.setToggleGroup(TG2);

        // add listener to disable/enable display button
        TG1.selectedToggleProperty().addListener((observable, oldValue, newValue) -> updateButtonState());
        TG2.selectedToggleProperty().addListener((observable, oldValue, newValue) -> updateButtonState());

        StringConverter<LocalDate> stringConverter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter.format(object);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (Exception e) {
                        // Handle invalid date input
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Invalid date\nPlease provide a valid date in the format dd-MM-yyyy");
                            alert.showAndWait();
                        });

                        //set datePicker to show today's date
//                        return LocalDate.now();
                        return null;
                    }
                } else {
                    return null;
                }
            }
        };
    startDate.setConverter(stringConverter);
    endDate.setConverter(stringConverter);

    }

}

