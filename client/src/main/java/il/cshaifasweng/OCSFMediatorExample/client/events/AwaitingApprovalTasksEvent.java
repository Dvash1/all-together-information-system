package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class AwaitingApprovalTasksEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public AwaitingApprovalTasksEvent(Message message) {
        this.message = message;
    }
}