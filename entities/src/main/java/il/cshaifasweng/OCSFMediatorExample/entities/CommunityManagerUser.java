package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue("manager")
public class CommunityManagerUser extends User implements Serializable {

    @ManyToOne
    @JoinColumn(name = "community_managing",referencedColumnName = "communityName")
    private Community communityManaging;

    public CommunityManagerUser(String userName, String teudatZehut, String password, String secretQuestion, String secretQuestionAnswer, String phoneNumber, Community community) {
        super(userName, teudatZehut, password, secretQuestion, secretQuestionAnswer, phoneNumber, community);
    }

    public CommunityManagerUser(String userName, String teudatZehut, String password, String secretQuestion, String secretQuestionAnswer, String phoneNumber) {
        super(userName, teudatZehut, password, secretQuestion, secretQuestionAnswer, phoneNumber);
    }

    public CommunityManagerUser() {
    }

    public Community getCommunityManaging() {
        return communityManaging;
    }

    public void setCommunityManaging(Community communityManaging) {
        this.communityManaging = communityManaging;
    }
}
