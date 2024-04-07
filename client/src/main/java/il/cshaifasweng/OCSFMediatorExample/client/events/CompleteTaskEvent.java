package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class CompleteTaskEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public CompleteTaskEvent(Message message) {
        this.message = message;
    }

}
