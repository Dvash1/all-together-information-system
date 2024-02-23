package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    int id;
    LocalDateTime timeStamp;
    String message;
    Task task;


    /*  Main constructor  */
    public Message(int id, LocalDateTime timeStamp, String message, Task task) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.message = message;
        this.task = task;
    }


    /*  Null constructor  */
    public Message(int id, String message) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.task = null;
    }


    public int getId() {
        return id;
    }

    public Task getTask() {
        return task;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
