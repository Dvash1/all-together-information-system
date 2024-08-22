package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Message implements Serializable {
    int taskID;
    String message;
    Object object; // will cast to specific object type when needed.
    List<Object> objectsArr;

    User user; // message sending user



    /*  Main constructor  */
    public Message(int taskID, String message, Object object,User user) {
        this.taskID = taskID;
        this.message = message;
        this.object = object;
        this.user = user;
    }


    /*  Null constructor  */
    public Message(int taskID, String message) {
        this.taskID = taskID;
        this.message = message;
        this.object = null;
    }
    public Message(String message,Object object,User user) {
        this.message = message;
        this.object = object;
        this.user = user;
    }
    public Message(String message,User user) {
        this.message = message;
        this.object = null;
        this.user = user;
    }
    public Message(String message) {
        this.message = message;
        this.object = null;
    }

    public Message(String message, Object object) {
        this.message = message;
        this.object = object;
    }

    public Message(String message, List<Object> objectsArr) {
        this.message = message;
        this.objectsArr = objectsArr;
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

    public void appendToObjectsArr(Object object) {
        this.objectsArr.add(object);
    }
    public void removeLastFromObjectsArr() {
        this.objectsArr.removeLast();
    }
    public void setObjectsArr(List<Object> objectArray) {
        this.objectsArr = objectArray;
    }

    public List<Object> getObjectsArr() {
        return objectsArr;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

