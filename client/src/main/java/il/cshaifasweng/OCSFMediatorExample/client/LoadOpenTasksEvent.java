package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class LoadOpenTasksEvent {

    private Message message;

    public Message getMessage() {
        return message;
    }

    public LoadOpenTasksEvent(Message message) {
        this.message = message;
    }
}