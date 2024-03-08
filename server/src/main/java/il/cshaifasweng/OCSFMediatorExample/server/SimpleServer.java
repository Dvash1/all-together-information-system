package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Community;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.*;


public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

	private static Session session;

	private static SessionFactory getSessionFactory() throws
			HibernateException {
		Configuration configuration = new Configuration();

		configuration.addAnnotatedClass(Task.class);
		configuration.addAnnotatedClass(User.class);
		configuration.addAnnotatedClass(Community.class);

		ServiceRegistry serviceRegistry = new
				StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();

		return configuration.buildSessionFactory(serviceRegistry);
	}

	public SimpleServer(int port) {
		super(port);
		
	}

	public static void generateDataBase()
	{
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();

//			if () {
//				create users/tasks/communities
//			}

			session.getTransaction().commit();
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

	private static List<Task> getAllTasks() throws Exception {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);
		Join<Task, User> creatorJoin = root.join("taskCreator");
		Join<Task, User> volunteerJoin = root.join("taskVolunteer", JoinType.LEFT);
		query.select(root);
		List<Task> tasks = session.createQuery(query).getResultList();
		return tasks;
	}


	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {



		Message message = (Message) msg;
		String request = message.getMessage();
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();

			if(request.equals("Test")) {
//				User u1 = new User("Joe Biden","USA","999999999","iforgotmypasswword",true);
//				Task t1 = new Task("Mow the lawn in the white house",LocalDateTime.now(),"Bakasha",u1);
//				session.save(u1);
//				session.save(t1);
				Task task = session.get(Task.class,1);
				task.setRequiredTask("Bombing Ron Spector!!!");
				session.update(task);
				session.flush();

				session.getTransaction().commit();

				message.setObject(task);
				message.setMessage("Test");
				client.sendToClient(message);
			}
			else if(request.equals("create task"))
			{
				Task testTask = (Task) message.getObject();
//				User u1 = testTask.getTaskCreator();
//				session.save(u1);
				session.save(testTask);
				session.flush();
				session.getTransaction().commit();
				client.sendToClient(message);

			}
			else if(request.equals("get tasks")) {
				List<Task> tasks = getAllTasks();
				// for now just fetch all Tasks in database
				// will need to add a Query "WHERE taskCreator_communityName == LoggedInUser_communityName in order to fetch tasks that are only from specific community
				// Might need to add communityName attribute to Task class to simplify things
				message.setObject(tasks);
				client.sendToClient(message);
			}
			else if (request.equals("get user")) {
				User u1 = session.get(User.class,2);
				message.setObject(u1);
				client.sendToClient(message);

			}


			else if (request.equals("Get Data")) {
				Task task = session.get(Task.class,message.getTaskID());
				message.setObject(task);
				client.sendToClient(message);

			}
			else if (request.equals("Update State")) {
				Task task = session.get(Task.class,message.getTaskID());
				task.setTaskState("Betipul");
				message.setObject(task);

				session.update(task);
				session.flush();

				client.sendToClient(message);
				session.getTransaction().commit();
			}
			else if (request.equals("add client")){
				SubscribedClient connection = new SubscribedClient(client);
				SubscribersList.add(connection);
//				message.setMessage("client added successfully");
//				client.sendToClient(message);

			}
			else{
				message.setMessage(request);
				sendToAllClients(message);

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
