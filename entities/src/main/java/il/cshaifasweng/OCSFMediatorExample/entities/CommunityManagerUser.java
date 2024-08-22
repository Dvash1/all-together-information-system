package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DiscriminatorValue("manager")
public class CommunityManagerUser extends User implements Serializable {

    @ManyToOne
    @JoinColumn(name = "community_managing",referencedColumnName = "communityName")
    private Community communityManaging;



    @OneToMany
    @JoinTable(
            name = "community_manager_to_users",
            joinColumns = @JoinColumn(name = "community_manager_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> managedUsers;

    public CommunityManagerUser(String userName, String teudatZehut, String password, String secretQuestion, String secretQuestionAnswer, String phoneNumber, Community community) {
        super(userName, teudatZehut, password, secretQuestion, secretQuestionAnswer, phoneNumber, community);
    }

    public CommunityManagerUser(String userName, String teudatZehut, String password, String secretQuestion, String secretQuestionAnswer, String phoneNumber) {
        super(userName, teudatZehut, password, secretQuestion, secretQuestionAnswer, phoneNumber);
    }

    public CommunityManagerUser() {
    }

    public List<User> getManagedUsers() {
        return managedUsers;
    }

    public void setManagedUsers(List<User> managedUsers) {
        this.managedUsers = managedUsers;
    }

    public Community getCommunityManaging() {
        return communityManaging;
    }

    public void setCommunityManaging(Community communityManaging) {
        this.communityManaging = communityManaging;
    }
}
