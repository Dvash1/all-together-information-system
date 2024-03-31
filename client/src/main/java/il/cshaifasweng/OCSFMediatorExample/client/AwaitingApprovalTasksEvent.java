package il.cshaifasweng.OCSFMediatorExample.client;

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