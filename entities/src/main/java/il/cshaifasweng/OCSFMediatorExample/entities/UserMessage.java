package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name = "User_Messages")
public class UserMessage implements Serializable {

    String message;

    String teudatZehut_sender;
    String teudatZehut_to;
    LocalDateTime was_sent_on = LocalDateTime.now();
    String message_type;

    @Id
    @GeneratedValue
    private Long id;



    /*  Main constructor  */


    public UserMessage(String message, String sender_zehut, String to_zehut, String message_type) {
        this.message = message;
        this.teudatZehut_sender = sender_zehut;
        this.teudatZehut_to = to_zehut;
        this.message_type = message_type;
        this.was_sent_on = LocalDateTime.now();
    }

    public UserMessage() {

    }

    public String getTeudatZehut_to() {
        return teudatZehut_to;
    }

    public void setTeudatZehut_to(String getTeudatZehut_to) {
        this.teudatZehut_to = getTeudatZehut_to;
    }

    public String getMessage() {
        return message;
    }

    public String getSender_zehut() {
        return teudatZehut_sender;
    }

    public LocalDateTime getWas_sent_on() {
        return was_sent_on;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender_zehut(String sender_zehut) {
        this.teudatZehut_sender = sender_zehut;
    }

    public void setWas_sent_on(LocalDateTime was_sent_on) {
        this.was_sent_on = was_sent_on;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }
    public Long getId() {
        return id;
    }
}

