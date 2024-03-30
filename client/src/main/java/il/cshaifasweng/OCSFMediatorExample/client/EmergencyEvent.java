package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class EmergencyEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public EmergencyEvent(Message message) {
        this.message = message;
    }
}



