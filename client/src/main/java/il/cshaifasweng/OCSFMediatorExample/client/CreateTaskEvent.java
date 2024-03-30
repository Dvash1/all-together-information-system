package il.cshaifasweng.OCSFMediatorExample.client;

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
