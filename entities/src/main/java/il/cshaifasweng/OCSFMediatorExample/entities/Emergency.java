package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.Community;
@Entity
@Table (name = "emergencies")
public class Emergency implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emergency_id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "creator_id",referencedColumnName = "user_id")
    private User user;
//    @ManyToOne
//    @JoinColumn(name = "creator_id",referencedColumnName = "community_id")
//    private Community community;
    private LocalDateTime callTime;

    public Emergency(){

    }
    public Emergency(User user, LocalDateTime callTime) {
        this.user = user;
        this.callTime = callTime;
    }

    public int getId() {
        return this.id;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCallTime() {
        return callTime;
    }

    public void setCallTime(LocalDateTime callTime) {
        this.callTime = callTime;
    }
}