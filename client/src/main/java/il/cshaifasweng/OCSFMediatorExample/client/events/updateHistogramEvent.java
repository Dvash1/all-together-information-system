package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class updateHistogramEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public updateHistogramEvent(Message message) {
        this.message = message;
    }
}

