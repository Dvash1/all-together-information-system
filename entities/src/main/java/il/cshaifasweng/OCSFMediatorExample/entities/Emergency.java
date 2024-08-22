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
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    private String userName;

    private String teudatZehut;
    @ManyToOne
    @JoinColumn(name = "community",referencedColumnName = "communityName")
    private Community community;

    private LocalDateTime callTime;





    public Emergency(){

    }
    public Emergency(User user, LocalDateTime callTime) {
        this.user = user;
        this.callTime = callTime;
        this.community = user.getCommunity();
        this.userName = user.getUserName();
        this.teudatZehut = user.getTeudatZehut();
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

    public String getTeudatZehut() {
        return teudatZehut;
    }

    public void setTeudatZehut(String teudatZehut) {
        this.teudatZehut = teudatZehut;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}