package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class DenialMessageEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public DenialMessageEvent(Message message) {
        this.message = message;
    }
}