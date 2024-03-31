package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class LoadAllTasksEvent {

    private Message message;

    public Message getMessage() {
        return message;
    }

    public LoadAllTasksEvent(Message message) {
        this.message = message;
    }
}