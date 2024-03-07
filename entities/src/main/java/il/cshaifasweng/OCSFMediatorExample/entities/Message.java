package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    int id;
    LocalDateTime timeStamp;
    String message;
    Object object; // will cast to specific object type when needed, a more general approach


    /*  Main constructor  */
    public Message(int id, LocalDateTime timeStamp, String message, Object object) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.message = message;
        this.object = object;
    }


    /*  Null constructor  */
    public Message(int id, String message) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.object = null;
    }


    public int getId() {
        return id;
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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}


