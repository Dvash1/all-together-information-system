package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class SendCompletionMessageEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public SendCompletionMessageEvent(Message message) {
        this.message = message;
    }
}