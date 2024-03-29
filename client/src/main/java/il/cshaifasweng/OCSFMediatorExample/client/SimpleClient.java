package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {

	private static String serverHost = "localhost";
	private static int serverPort = 3000;
	static SimpleClient client = null;

	private String userName = null;


	private SimpleClient(String host, int port) {
		super(host, port);
	}

	public static void setHostAndPort(String host, int port)
	{
		serverHost = host;
		serverPort = port;
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
			EventBus.getDefault().post(new ForgotPasswordEvent(message));
		}
		if(request.startsWith("Password")) {
			EventBus.getDefault().post(new PasswordChangeEvent(message));
		}
		if(request.startsWith("Emergency")) {
			EventBus.getDefault().post(new EmergencyEvent(message));
		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
