package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class newTaskEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public newTaskEvent(Message message) {
        this.message = message;
    }
}