package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class LoadTasksEvent {

    private Message message;

    public Message getMessage() {
        return message;
    }

    public LoadTasksEvent(Message message) {
        this.message = message;
    }
}