package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class UpdateTaskEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public UpdateTaskEvent(Message message) {
        this.message = message;
    }
}
