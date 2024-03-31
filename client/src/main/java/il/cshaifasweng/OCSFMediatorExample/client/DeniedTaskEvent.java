package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class DeniedTaskEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public DeniedTaskEvent(Message message) {
        this.message = message;
    }
}