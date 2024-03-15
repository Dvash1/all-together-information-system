package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Community;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.time.LocalDateTime;
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

	public static void createDataBase()
	{
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();

			//check if database is empty first
			if (getAllTasks().isEmpty()) {
			// first community
				User u1 = new User("Jan Christie","335720074","nJ9rS8~-",true);
				Community c1 = new Community("Kfir",u1);
				u1.setCommunity(c1);
				User u2 = new User("Elie Dempsey",c1,"331928044","bZ8W7+.q",false);
				User u3 = new User("John Smith", c1, "746158392", "password123", false);
				User u4 = new User("Alice Johnson", c1, "538294617", "qwerty", false);
				User u5 = new User("Michael Brown", c1, "921475386", "abc123", false);
				User u6 = new User("Emily Davis", c1, "364892175", "password", false);
				User u7 = new User("Daniel Wilson", c1, "485739216", "123456", false);
				User u8 = new User("Olivia Martinez", c1, "627183945", "pass123", false);
				User u9 = new User("William Anderson", c1, "819276435", "password456", false);
				User u10 = new User("Sophia Garcia", c1, "294617583", "123abc", false);
				Task t1 = new Task("walk the dogs", LocalDateTime.now(),"Request",u2);
				Task t2 = new Task("clean the house", LocalDateTime.now().minusHours(2), "Request", u3);
				Task t3 = new Task("grocery shopping", LocalDateTime.now().minusDays(1), "Request", u4);
				Task t4 = new Task("write report", LocalDateTime.now().minusWeeks(1), "Complete", u5,u7);
				t4.setCompletionTime(LocalDateTime.now().minusDays(5));
				Task t5 = new Task("prepare presentation", LocalDateTime.now().minusDays(2), "Request", u6);

				session.save(u1);
				session.save(c1);
				session.save(u2);
				session.save(u3);
				session.save(u4);
				session.save(u5);
				session.flush();

				session.save(u6);
				session.save(u7);
				session.save(u8);
				session.save(u9);
				session.save(u10);
				session.flush();


				session.save(t1);
				session.save(t2);
				session.save(t3);
				session.save(t4);
				session.save(t5);
				session.flush();



			// second community

				User u11 = new User("Emma Thompson","746158392", "pass456", true);
				Community c2 = new Community("Lemons",u11);
				u11.setCommunity(c2);
				User u12 = new User("James White", c2, "538294617", "abc456", false);
				User u13 = new User("Ava Green", c2, "921475386", "password789", false);
				User u14 = new User("Ethan Harris", c2, "364892175", "qwerty123", false);
				User u15 = new User("Mia Lee", c2, "485739216", "pass789", false);
				User u16 = new User("Jacob Hall", c2, "627183945", "abc789", false);
				User u17 = new User("Isabella Young", c2, "819276435", "passwordabc", false);
				User u18 = new User("Noah King", c2, "294617583", "qwertyabc", false);
				User u19 = new User("Sophie Baker", c2, "173849265", "passabc", false);
				User u20 = new User("William Cook", c2, "582716394", "abcqwerty", false);

				Task t6 = new Task("mow the lawn", LocalDateTime.now().minusHours(3), "Request", u11);
				Task t7 = new Task("fix the roof", LocalDateTime.now().minusDays(2), "Request", u12);
				Task t8 = new Task("paint the fence", LocalDateTime.now().minusWeeks(2), "Complete", u13,u17);
				t8.setCompletionTime(LocalDateTime.now().minusDays(2));
				Task t9 = new Task("clean the garage", LocalDateTime.now().minusDays(3), "Request", u14);
				Task t10 = new Task("organize the shed", LocalDateTime.now().minusWeeks(3), "Request", u15);

				session.save(u11);
				session.save(c2);
				session.save(u12);
				session.save(u13);
				session.save(u14);
				session.save(u15);
				session.flush();

				session.save(u16);
				session.save(u17);
				session.save(u18);
				session.save(u19);
				session.save(u20);
				session.flush();


				session.save(t6);
				session.save(t7);
				session.save(t8);
				session.save(t9);
				session.save(t10);
				session.flush();


				// third community

				User u21 = new User("Liam Murphy", "918273645", "abcpassword", true);
				Community c3 = new Community("Ono",u21);
				u21.setCommunity(c3);
				User u22 = new User("Grace Turner", c3, "726394185", "qwerty12345", false);
				User u23 = new User("Mason Parker", c3, "364598217", "password!@#", false);
				User u24 = new User("Zoe Evans", c3, "485726391", "abc123!@#", false);
				User u25 = new User("Harper Edwards", c3, "582619347", "password123!@#", false);
				User u26 = new User("Benjamin Collins", c3, "726391845", "qwerty!@#$", false);
				User u27 = new User("Aria Stewart", c3, "917364825", "pass!@#$%", false);
				User u28 = new User("Lucas Watson", c3, "364819257", "abc!@#$%^", false);
				User u29 = new User("Layla Harris", c3, "485726193", "qwerty!@#$%^&", false);
				User u30 = new User("Henry Clark", c3, "819273645", "password!@#$%^&*", false);
				Task t11 = new Task("rake the leaves", LocalDateTime.now().minusHours(4), "Request", u21);
				Task t12 = new Task("clean the gutters", LocalDateTime.now().minusDays(5), "Request", u22);
				Task t13 = new Task("trim the hedges", LocalDateTime.now().minusWeeks(3), "Complete", u23, u27);
				t13.setCompletionTime(LocalDateTime.now().minusWeeks(2));
				Task t14 = new Task("wash the car", LocalDateTime.now().minusDays(4), "Request", u24);
				Task t15 = new Task("sweep the driveway", LocalDateTime.now().minusWeeks(4), "Request", u25);


				session.save(u21);
				session.save(c3);
				session.save(u22);
				session.save(u23);
				session.save(u24);
				session.save(u25);
				session.flush();

				session.save(u26);
				session.save(u27);
				session.save(u28);
				session.save(u29);
				session.save(u30);
				session.flush();


				session.save(t11);
				session.save(t12);
				session.save(t13);
				session.save(t14);
				session.save(t15);
				session.flush();
			}




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

// same code as above, but will retrieve only tasks that are from the same community as the input user.
//
	private static List<Task> getCommunityTasks(User user) throws Exception {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);
		Join<Task, User> creatorJoin = root.join("taskCreator");
		Join<Task, User> volunteerJoin = root.join("taskVolunteer", JoinType.LEFT);
		query.select(root);
		query.where(cb.equal(creatorJoin.get("community"), user.getCommunity()));
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
				User u1 = (User) message.getObject();
				List<Task> tasks = getCommunityTasks(u1);
				// for now just fetch all Tasks in database
				// will need to add a Query "WHERE taskCreator_communityName == LoggedInUser_communityName in order to fetch tasks that are only from specific community
				// Might need to add communityName attribute to Task class to simplify things
				message.setObject(tasks);
				client.sendToClient(message);
			}
			else if(request.equals("get emergency")) {
				// testing with Tasks because emergency is not implemented yet.
				User u1 = (User) message.getObject();
				List<Task> tasks = getCommunityTasks(u1);
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
			else if (request.equals("Update task")) {

				Task task = (Task) message.getObject();
				session.update(task);
				session.flush();
				if (task.getTaskState().equals("In Progress"))
				{
					message.setMessage("Volunteer to task");
				}
				else
				{
					message.setMessage("Complete task");
				}
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
