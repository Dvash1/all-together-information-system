package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class NewTaskEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public NewTaskEvent(Message message) {
        this.message = message;
    }
}
