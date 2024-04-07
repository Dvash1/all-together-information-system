package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class getEmergencyData {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public getEmergencyData(Message message) {
        this.message = message;
    }

}