package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class LogOutEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public LogOutEvent(Message message) {
        this.message = message;
    }
}


