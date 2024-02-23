package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table (name = "tasks")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int id;

    private String requiredTask;
    private LocalDateTime creationTime;
    private String taskState;
    @ManyToOne
    @JoinColumn(name = "creator_id",referencedColumnName = "user_id")
    private User taskCreator;

    @ManyToOne
    @JoinColumn(name = "volunteer_id",referencedColumnName = "user_id")
    private User taskVolunteer;
    public Task() {
    }

    public Task(String requiredTask, LocalDateTime creationTime, String taskState, User taskCreator,User taskVolunteer) {
        this.requiredTask = requiredTask;
        this.creationTime = creationTime;
        this.taskState = taskState;
        this.taskCreator = taskCreator;
        this.taskVolunteer = taskVolunteer;


    }
    public Task(String requiredTask, LocalDateTime creationTime, String taskState, User taskCreator) {
        this.requiredTask = requiredTask;
        this.creationTime = creationTime;
        this.taskState = taskState;
        this.taskCreator = taskCreator;
    }
    public int getId() {
        return id;
    }


    public String getRequiredTask() {
        return requiredTask;
    }

    public void setRequiredTask(String requiredTask) {
        this.requiredTask = requiredTask;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public User getTaskCreator() {
        return taskCreator;
    }

    public void setTaskCreator(User taskCreator) {
        this.taskCreator = taskCreator;
    }


    public User getTaskVolunteer() {
        return taskVolunteer;
    }

    public void setTaskVolunteer(User taskVolunteer) {
        this.taskVolunteer = taskVolunteer;
    }

}