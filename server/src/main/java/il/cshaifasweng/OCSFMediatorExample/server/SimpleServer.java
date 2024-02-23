package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import javax.persistence.criteria.Join;



public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

	private static Session session;

	private static SessionFactory getSessionFactory() throws
			HibernateException {
		Configuration configuration = new Configuration();

		configuration.addAnnotatedClass(Task.class);
		configuration.addAnnotatedClass(User.class);

		ServiceRegistry serviceRegistry = new
				StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();

		return configuration.buildSessionFactory(serviceRegistry);
	}

	public SimpleServer(int port) {
		super(port);
		
	}


	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {



		Message message = (Message) msg;
		String request = message.getMessage();
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();
			//we got an empty message, so we will send back an error message with the error details.
			if (request.isBlank()){
				message.setMessage("Error! we got an empty message");
				client.sendToClient(message);
			}
			//we got a request to change submitters IDs with the updated IDs at the end of the string, so we save
			// the IDs at data field in Message entity and send back to all subscribed clients a request to update
			//their IDs text fields. An example of use of observer design pattern.
			//message format: "change submitters IDs: 123456789, 987654321"
			else if(request.startsWith("change submitters IDs:")){
				message.setData(request.substring(23));
				message.setMessage("update submitters IDs");
				sendToAllClients(message);
			}
			else if(request.equals("Test")) {
//				User u1 = new User("Joe Biden","USA","999999999","iforgotmypasswword",true);
//				Task t1 = new Task("Mow the lawn in the white house",LocalDateTime.now(),"Bakasha",u1);
//				session.save(u1);
//				session.save(t1);
				Task task = session.get(Task.class,1);
				task.setRequiredTask("Bombing Ron Spector!!!");
				session.update(task);
				session.flush();

				session.getTransaction().commit();

				message.setTask(task);
				message.setMessage("Test");
				client.sendToClient(message);
			}
			//we got a request to add a new client as a subscriber.
			else if (request.equals("add client")){
				SubscribedClient connection = new SubscribedClient(client);
				SubscribersList.add(connection);
				message.setMessage("client added successfully");
				client.sendToClient(message);
			}
			//we got a message from client requesting to echo Hello, so we will send back to client Hello world!
			else if(request.startsWith("echo Hello")){
				message.setMessage("Hello World!");
				client.sendToClient(message);
			}
			else if(request.startsWith("send Submitters IDs")){
				message.setMessage("318535614, 320616113");
				client.sendToClient(message);
				//add code here to send submitters IDs to client
			}
			else if (request.startsWith("send Submitters")){
				message.setMessage("Lior, Daniel");
				client.sendToClient(message);
				//add code here to send submitters names to client
			}
			else if (request.equals("what's the time?")) {
				String today = LocalDate.now().toString();
				message.setMessage(today);
				client.sendToClient(message);
				// do we need to format the date?

			}
			else if (request.startsWith("multiply")){

				String ss = request.substring(9); // Skip "multiply "
				String[] operands = ss.split("\\*"); // Split the string at '*'
				if (operands.length == 2) {
					int first = Integer.parseInt(operands[0].trim()); // Parse the first operand
					int second = Integer.parseInt(operands[1].trim()); // Parse the second operand
					int result = first * second; // Calculate the result
					message.setMessage(Integer.toString(result)); // Set the result as the message
					client.sendToClient(message); // Send the message to the client
				} else {
					// Handle invalid input format
					message.setMessage(request);
					sendToAllClients(message);
				}
			}else{
				message.setMessage(request);
				sendToAllClients(message);
				//add code here to send received message to all clients.
				//The string we received in the message is the message we will send back to all clients subscribed.
				//Example:
					// message received: "Good morning"
					// message sent: "Good morning"
				//see code for changing submitters IDs for help
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception exception) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			System.err.println("An error occured, changes have been rolled back.");
			exception.printStackTrace();
		}
		finally {
			session.close();
		}
	}

	public void sendToAllClients(Message message) {
		try {
			for (SubscribedClient SubscribedClient : SubscribersList) {
				SubscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
