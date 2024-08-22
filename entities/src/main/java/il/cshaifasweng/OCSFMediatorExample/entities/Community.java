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




    public Community() {

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


}

