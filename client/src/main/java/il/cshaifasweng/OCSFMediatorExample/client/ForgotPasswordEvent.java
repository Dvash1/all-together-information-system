package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class ForgotPasswordEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public ForgotPasswordEvent(Message message) {
        this.message = message;
    }
}



