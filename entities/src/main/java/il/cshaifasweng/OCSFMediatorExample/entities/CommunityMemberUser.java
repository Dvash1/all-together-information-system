package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue("member")
public class CommunityMemberUser extends User implements Serializable {
    public CommunityMemberUser(String userName, String teudatZehut, String password, String secretQuestion, String secretQuestionAnswer, String phoneNumber, Community community) {
        super(userName, teudatZehut, password, secretQuestion, secretQuestionAnswer, phoneNumber, community);
    }

    public CommunityMemberUser(String userName, String teudatZehut, String password, String secretQuestion, String secretQuestionAnswer, String phoneNumber) {
        super(userName, teudatZehut, password, secretQuestion, secretQuestionAnswer, phoneNumber);
    }

    public CommunityMemberUser() {
    }
}
