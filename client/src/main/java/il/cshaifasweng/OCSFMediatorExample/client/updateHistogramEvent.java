package il.cshaifasweng.OCSFMediatorExample.client;

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

