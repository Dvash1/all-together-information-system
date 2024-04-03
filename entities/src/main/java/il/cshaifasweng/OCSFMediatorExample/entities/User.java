package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    private String userName;
    @ManyToOne
    @JoinColumn(name = "community",referencedColumnName = "communityName")
    private Community community;

    private String teudatZehut;

    private String password;

    private boolean isManager;
    private String secretQuestion;
    private String secretQuestionAnswer;
    private String phoneNumber;
    private int numberOfLoginTries;
    private boolean isLocked;

    public User(String userName, String teudatZehut, String password,String secretQuestion, String secretQuestionAnswer,boolean isManager,String phoneNumber, Community community) {
        this.userName = userName;
        this.teudatZehut = teudatZehut;
        this.password = password;
        this.secretQuestion = secretQuestion;
        this.secretQuestionAnswer = secretQuestionAnswer;
        this.isManager = isManager;
        this.phoneNumber = phoneNumber;
        this.community = community;
        this.numberOfLoginTries = 0;
        this.isLocked = false;

    }
    public User(String userName, String teudatZehut, String password,String secretQuestion, String secretQuestionAnswer,boolean isManager,String phoneNumber) {
        this.userName = userName;
        this.teudatZehut = teudatZehut;
        this.password = password;
        this.secretQuestion = secretQuestion;
        this.secretQuestionAnswer = secretQuestionAnswer;
        this.isManager = isManager;
        this.phoneNumber = phoneNumber;
        this.numberOfLoginTries = 0;
        this.isLocked = false;
    }
    public User()
    {

    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public String getTeudatZehut() {
        return teudatZehut;
    }

    public void setTeudatZehut(String teudatZehut) {
        this.teudatZehut = teudatZehut;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretQuestionAnswer() {
        return secretQuestionAnswer;
    }

    public void setSecretQuestionAnswer(String secretQuestionAnswer) {
        this.secretQuestionAnswer = secretQuestionAnswer;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getnumberOfLoginTries() {
        return numberOfLoginTries;
    }
    public void incrementNumberOfLoginTries() {
        this.numberOfLoginTries++;
    }
    public void resetNumberOfLoginTries() {
        this.numberOfLoginTries = 0;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

}