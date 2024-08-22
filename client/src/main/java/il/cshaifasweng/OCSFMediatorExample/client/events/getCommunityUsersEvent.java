package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class getCommunityUsersEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public getCommunityUsersEvent(Message message) {
        this.message = message;
    }
}