package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class WithdrawEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public WithdrawEvent(Message message) {
        this.message = message;
    }
}