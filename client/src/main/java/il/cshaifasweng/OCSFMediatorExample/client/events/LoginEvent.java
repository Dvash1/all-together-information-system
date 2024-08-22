package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;


public class LoginEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public LoginEvent(Message message) {
        this.message = message;
    }
}



