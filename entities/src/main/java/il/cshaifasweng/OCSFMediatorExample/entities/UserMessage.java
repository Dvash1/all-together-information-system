package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "User_Messages")
public class UserMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private int id;
    String message;

    String teudatZehut_sender;
    String teudatZehut_to;
    LocalDateTime was_sent_on = LocalDateTime.now();
    LocalDateTime to_send_on;
    String message_type;

    int task_id = 0;



    /*  Non Scheduled Main constructor  */
    public UserMessage(String message, String sender_zehut, String to_zehut, String message_type) {
        this.message = message;
        this.teudatZehut_sender = sender_zehut;
        this.teudatZehut_to = to_zehut;
        this.message_type = message_type;
        this.was_sent_on = LocalDateTime.now();
    }

    /* Scheduled constructor, need to have send-on */
    public UserMessage(String message, String sender_zehut, String to_zehut, String message_type, LocalDateTime to_send_on) {
        this.message = message;
        this.teudatZehut_sender = sender_zehut;
        this.teudatZehut_to = to_zehut;
        this.message_type = message_type;
        this.was_sent_on = LocalDateTime.now();
        this.to_send_on = to_send_on;
    }
    public UserMessage() {

    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public LocalDateTime getTo_send_on() {
        return to_send_on;
    }

    public void setTo_send_on(LocalDateTime to_send_on) {
        this.to_send_on = to_send_on;
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
    public int getId() {
        return id;
    }
}

