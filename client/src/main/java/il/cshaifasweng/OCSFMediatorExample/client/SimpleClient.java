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
		// TODO: make this a switch instead? :))
		Message message = (Message) msg;
		String request = message.getMessage();
		System.out.println("handleMessageFromServer: "+request);
		if (message.getMessage().equals("alert everybody"))
		{
			EventBus.getDefault().post(new getDataEvent(message));
		}
		else if (message.getMessage().equals("create task"))
		{
			EventBus.getDefault().post(new CreateTaskEvent(message));
		}
		else if (message.getMessage().equals("Publish approved task"))
		{
			EventBus.getDefault().post(new ApprovedTaskEvent(message));
		}
		else if (message.getMessage().equals("Request denied"))
		{
			EventBus.getDefault().post(new DeniedTaskEvent(message));
		}
		else if (message.getMessage().equals("Volunteer to task"))
		{
			EventBus.getDefault().post(new VolunteerToTaskEvent(message));
		}
		else if (message.getMessage().equals("Withdraw from task"))
		{
			EventBus.getDefault().post(new WithdrawEvent(message));
		}
		else if (message.getMessage().equals("Complete task"))
		{
			EventBus.getDefault().post(new CompleteTaskEvent(message));
		}
		else if (message.getMessage().equals("get tasks"))
		{
			EventBus.getDefault().post(new LoadAllTasksEvent(message));
		}
		else if (message.getMessage().equals("get open tasks"))
		{
			EventBus.getDefault().post(new LoadOpenTasksEvent(message));
		}
		else if (message.getMessage().equals("emergency histogram"))
		{
			EventBus.getDefault().post(new getEmergencyData(message));
		}
		else if (message.getMessage().equals("update histogram"))
		{
			EventBus.getDefault().post(new updateHistogramEvent(message));
		}
		else if (message.getMessage().equals("get awaiting approval requests"))
		{
			EventBus.getDefault().post(new AwaitingApprovalTasksEvent(message));
		}
		else if (message.getMessage().equals("log out"))
		{
			EventBus.getDefault().post(new LogOutEvent(message));
		}
		else if (message.getMessage().equals("get users"))
		{
			EventBus.getDefault().post(new getCommunityUsersEvent(message));
		}

		else if (message.getMessage().equals("get user")) {
			EventBus.getDefault().post(new GetUserEvent(message));
		}
// Display alerts/message to users/community manager

		else if (message.getMessage().equals("Send denial message to user")) {
			EventBus.getDefault().post(new DenialMessageEvent(message));
		}
		else if (message.getMessage().equals("Send completion message to manager")) {
			EventBus.getDefault().post(new SendCompletionMessageEvent(message));
		}
		else if (message.getMessage().equals("Send new task message to manager")) {
			EventBus.getDefault().post(new SendNewTaskMessageEvent(message));
		}
		else if (message.getMessage().equals("alert users no volunteer")) {
			EventBus.getDefault().post(new NoVolunteerEvent(message));
		}

		else if(request.startsWith("Login")) {
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
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
