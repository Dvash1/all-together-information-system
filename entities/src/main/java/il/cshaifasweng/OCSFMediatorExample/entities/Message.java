package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    int taskID;
    String message;
    Object object; // will cast to specific object type when needed, a more general approach


    /*  Main constructor  */
    public Message(int taskID, String message, Object object) {
        this.taskID = taskID;
        this.message = message;
        this.object = object;
    }


    /*  Null constructor  */
    public Message(int taskID, String message) {
        this.taskID = taskID;
        this.message = message;
        this.object = null;
    }
    public Message(String message,Object object) {
        this.message = message;
        this.object = object;
    }
    public Message(String message) {
        this.message = message;
        this.object = null;
    }



    public int getTaskID() {
        return taskID;
    }


    public String getMessage() {
        return message;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}


