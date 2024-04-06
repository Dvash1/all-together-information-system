package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class VolunteerToTaskEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public VolunteerToTaskEvent(Message message) {
        this.message = message;
    }
}
