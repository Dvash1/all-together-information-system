package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {

	private static String serverHost = "localhost";
	private static int serverPort = 3000;
	static SimpleClient client = null;



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
		System.out.println("handleMessageFromServer: "+request);
        switch (message.getMessage()) {
            case "alert everybody" -> EventBus.getDefault().post(new getDataEvent(message));
            case "create task" -> EventBus.getDefault().post(new CreateTaskEvent(message));
            case "Publish approved task" -> EventBus.getDefault().post(new ApprovedTaskEvent(message));
            case "Request denied" -> EventBus.getDefault().post(new DeniedTaskEvent(message));
            case "New Message" -> EventBus.getDefault().post(new NewMessageEvent(message));
            case "Volunteer to task" -> EventBus.getDefault().post(new VolunteerToTaskEvent(message));
            case "Withdraw from task" -> EventBus.getDefault().post(new WithdrawEvent(message));
            case "Complete task" -> EventBus.getDefault().post(new CompleteTaskEvent(message));
            case "get tasks" -> EventBus.getDefault().post(new LoadAllTasksEvent(message));
            case "get open tasks" -> EventBus.getDefault().post(new LoadOpenTasksEvent(message));
            case "emergency histogram" -> EventBus.getDefault().post(new getEmergencyData(message));
            case "update histogram" -> EventBus.getDefault().post(new updateHistogramEvent(message));
            case "get awaiting approval requests" -> EventBus.getDefault().post(new AwaitingApprovalTasksEvent(message));
            case "log out" -> EventBus.getDefault().post(new LogOutEvent(message));
            case "get users" -> EventBus.getDefault().post(new getCommunityUsersEvent(message));
            case "get user" -> EventBus.getDefault().post(new GetUserEvent(message));
            case "Send denial message to user" -> EventBus.getDefault().post(new DenialMessageEvent(message));
            case "Send completion message to manager" -> EventBus.getDefault().post(new SendCompletionMessageEvent(message));
            case "Send new task message to manager" -> EventBus.getDefault().post(new SendNewTaskMessageEvent(message));
            case "alert users no volunteer" -> EventBus.getDefault().post(new NoVolunteerEvent(message));

			default -> {
				if(request.startsWith("Login")) {
					EventBus.getDefault().post(new LoginEvent(message));
				}
				else if(request.startsWith("Forgot Password")) {
					EventBus.getDefault().post(new ForgotPasswordEvent(message));
				}
				else if(request.startsWith("Password")) {
					EventBus.getDefault().post(new PasswordChangeEvent(message));
				}
				else if(request.startsWith("Emergency")) {
					EventBus.getDefault().post(new EmergencyEvent(message));
				}
			}
        }


	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient(serverHost, serverPort);
		}
		return client;
	}

}
