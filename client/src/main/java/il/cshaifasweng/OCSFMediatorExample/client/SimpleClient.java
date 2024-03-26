package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class 	SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		Message message = (Message) msg;
		String request = message.getMessage();
		if(request.equals("Get Data")) {
			EventBus.getDefault().post(new NewTaskEvent(message));
		}
		if (request.equals("Update State")) {
			EventBus.getDefault().post(new UpdateTaskEvent(message));
		}
		if(request.startsWith("Login")) {
			EventBus.getDefault().post(new LoginEvent(message));
		}
		if(request.startsWith("Forgot Password")) {
			EventBus.getDefault().post(new LoginEvent(message));
		}

	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
