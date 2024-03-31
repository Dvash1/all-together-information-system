package il.cshaifasweng.OCSFMediatorExample.client;

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