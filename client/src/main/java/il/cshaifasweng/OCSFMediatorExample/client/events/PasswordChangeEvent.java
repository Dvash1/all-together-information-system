package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class PasswordChangeEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public PasswordChangeEvent(Message message) {
        this.message = message;
    }
}



