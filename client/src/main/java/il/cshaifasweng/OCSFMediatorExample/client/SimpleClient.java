package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		Message message = (Message) msg;
		if (message.getMessage().equals("Get Data")) {
			EventBus.getDefault().post(new getDataEvent(message));
		} else if (message.getMessage().equals("Update State")) {
			EventBus.getDefault().post(new UpdateTaskEvent(message));
		} else if (message.getMessage().equals("create task")) {
			EventBus.getDefault().post(new NewTaskEvent(message));
		} else if (message.getMessage().equals("get tasks")) {
			EventBus.getDefault().post(new LoadTasksEvent(message));
		}
		else if (message.getMessage().equals("get user")) {
			EventBus.getDefault().post(new GetUserEvent(message));
		}

	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
