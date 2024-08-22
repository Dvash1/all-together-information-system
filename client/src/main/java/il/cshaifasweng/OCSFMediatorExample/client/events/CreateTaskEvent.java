package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class CreateTaskEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public CreateTaskEvent(Message message) {
        this.message = message;
    }

}
