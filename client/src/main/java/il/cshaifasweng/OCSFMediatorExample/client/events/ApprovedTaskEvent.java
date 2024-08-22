package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class ApprovedTaskEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public ApprovedTaskEvent(Message message) {
        this.message = message;
    }
}