package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class GetUserEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public GetUserEvent(Message message) {
        this.message = message;
    }
}