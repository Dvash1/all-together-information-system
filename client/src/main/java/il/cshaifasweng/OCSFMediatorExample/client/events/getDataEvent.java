package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class getDataEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public getDataEvent(Message message) {
        this.message = message;
    }
}



