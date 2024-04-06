package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;


    private String userName;
    @ManyToOne
    @JoinColumn(name = "community_part_of",referencedColumnName = "communityName")
    private Community community;

    private String teudatZehut;

    private String passwordHash;

    private String secretQuestion;
    private String secretQuestionAnswer;
    private String phoneNumber;
    private int numberOfLoginTries;
    private boolean isLocked;
    private String salt;
    public User(String userName, String teudatZehut, String password,String secretQuestion, String secretQuestionAnswer,String phoneNumber, Community community) {
        this.userName = userName;
        this.teudatZehut = teudatZehut;
        this.salt = generateSalt();
        this.passwordHash = get_SHA_512_SecurePassword(password,this.salt);
        this.secretQuestion = secretQuestion;
        this.secretQuestionAnswer = secretQuestionAnswer;
        this.phoneNumber = phoneNumber;
        this.community = community;
        this.numberOfLoginTries = 0;
        this.isLocked = false;
    }
    public User(String userName, String teudatZehut, String password,String secretQuestion, String secretQuestionAnswer,String phoneNumber) {
        this.userName = userName;
        this.teudatZehut = teudatZehut;
        this.salt = generateSalt();
        this.passwordHash = get_SHA_512_SecurePassword(password,this.salt);
        this.secretQuestion = secretQuestion;
        this.secretQuestionAnswer = secretQuestionAnswer;
        this.phoneNumber = phoneNumber;
        this.numberOfLoginTries = 0;
        this.isLocked = false;
    }

    public User() {

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

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPassword(String password) {
        this.salt = generateSalt();
        this.passwordHash = get_SHA_512_SecurePassword(password, this.salt);
    }
    public String getSalt() {
        return this.salt;
    }
    private String generateSalt() {
        // Generate a secure random salt
        SecureRandom secureRandom = new SecureRandom();
        byte[] saltBytes = new byte[16];
        secureRandom.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
    public String get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
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