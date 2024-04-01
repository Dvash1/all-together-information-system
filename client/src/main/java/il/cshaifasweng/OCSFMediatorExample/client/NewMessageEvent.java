package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class NewMessageEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public NewMessageEvent(Message message) {
        this.message = message;
    }
}