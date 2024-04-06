package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "communities")
public class Community implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private int id;

    private String communityName;

    @OneToOne
    @JoinColumn(name = "community_manager", referencedColumnName = "userName")
    private User communityManager;

    // not sure where to put the mappedBy, either here or in User.
    @OneToMany(mappedBy = "community")
    private List<User> communityUsers;


    public Community() {

    }

    public Community(String communityName, User communityManager, List<User> communityUsers) {
        this.communityName = communityName;
        this.communityManager = communityManager;
        this.communityUsers = communityUsers;
    }
    public Community(String communityName, User communityManager) {
        this.communityName = communityName;
        this.communityManager = communityManager;

    }

    public int getId() {
        return id;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public User getCommunityManager() {
        return communityManager;
    }

    public void setCommunityManager(User communityManager) {
        this.communityManager = communityManager;
    }

    public List<User> getCommunityUsers() {
        return communityUsers;
    }

    public void setCommunityUsers(List<User> communityUsers) {
        this.communityUsers = communityUsers;
    }
}

