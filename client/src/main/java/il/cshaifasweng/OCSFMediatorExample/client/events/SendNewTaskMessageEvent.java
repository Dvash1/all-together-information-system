package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class SendNewTaskMessageEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public SendNewTaskMessageEvent(Message message) {
        this.message = message;
    }
}