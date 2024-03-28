package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import javax.persistence.Query;

import org.hibernate.Criteria;

import javax.persistence.criteria.*;



public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private static HashMap<String,SubscribedClient> idToClient = new HashMap<>();
	private static Session session;

	private static SessionFactory getSessionFactory() throws
			HibernateException {
		Configuration configuration = new Configuration();

		configuration.addAnnotatedClass(Task.class);
		configuration.addAnnotatedClass(User.class);
		configuration.addAnnotatedClass(Community.class);
		configuration.addAnnotatedClass(Emergency.class);


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
			if (getAllTasks(null).isEmpty()) {
				// first community
				User u1 = new User("Jan Christie", "335720074", "nJ9rS8~-", "What is your favorite color?", "Blue", true,"0523842728");
				Community c1 = new Community("Kfir",u1);
				u1.setCommunity(c1);
				User u2 = new User("Elie Dempsey", "331928044", "bZ8W7+.q", "What is your pet's name?", "Rover", false,"0538453293" ,c1);
				User u3 = new User("John Smith", "746158392", "password123", "What is your mother's maiden name?", "Johnson", false, "0523456789", c1);
				User u4 = new User("Alice Johnson", "538294617", "qwerty", "What city were you born in?", "New York", false, "0501234565", c1);
				User u5 = new User("Michael Brown", "921475386", "abc123", "What is the name of your first school?", "Maple Elementary", false, "0579876543", c1);
				User u6 = new User("Emily Davis", "364892175", "password", "What is your favorite movie?", "The Shawshank Redemption", false, "0512345678", c1);
				User u7 = new User("Daniel Wilson", "485739216", "123456", "What is your favorite food?", "Pizza", false, "0587654321", c1);
				User u8 = new User("Olivia Martinez", "627183945", "pass123", "Who is your favorite author?", "J.K. Rowling", false, "0543210987", c1);
				User u9 = new User("William Anderson", "819276435", "password456", "What is your dream vacation destination?", "Paris", false, "0567890123", c1);
				User u10 = new User("Sophia Garcia", "294617583", "123abc", "What is your favorite animal?", "Dog", false, "0532109876", c1);

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

				User u11 = new User("Emma Thompson", "746158392", "pass456", "What is your favorite hobby?", "Reading", true,"0598765432");
				Community c2 = new Community("Lemons",u11);
				u11.setCommunity(c2);
				User u12 = new User("James White", "538294617", "abc456", "What is your favorite sport?", "Football", false, "0554321098", c2);
				User u13 = new User("Ava Green", "921475386", "password789", "What is your favorite season?", "Spring", false, "0523456788", c2);
				User u14 = new User("Ethan Harris", "364892175", "qwerty123", "What is your favorite TV show?", "Friends", false, "0501234567", c2);
				User u15 = new User("Mia Lee", "485739216", "pass789", "What is your favorite holiday?", "Christmas", false, "0579876544", c2);
				User u16 = new User("Jacob Hall", "627183945", "abc789", "What is your favorite music genre?", "Pop", false, "0512345679", c2);
				User u17 = new User("Isabella Young", "819276435", "passwordabc", "What is your favorite subject?", "History", false, "0587654329", c2);
				User u18 = new User("Noah King", "294617583", "qwertyabc", "What is your favorite color?", "Green", false, "0543210986", c2);
				User u19 = new User("Sophie Baker", "173849265", "passabc", "What is your favorite book?", "To Kill a Mockingbird", false, "0567890124", c2);
				User u20 = new User("William Cook", "582716394", "abcqwerty", "What is your favorite movie genre?", "Action", false, "0532109856", c2);


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

				User u21 = new User("Liam Murphy", "918273645", "abcpassword", "What is your favorite dessert?", "Ice Cream", true,"0598765432");
				Community c3 = new Community("Ono",u21);
				u21.setCommunity(c3);
				User u22 = new User("Grace Turner", "726394185", "qwerty12345", "What is your favorite fruit?", "Strawberry", false, "0554321091", c3);
				User u23 = new User("Mason Parker", "364598217", "password!@#", "What is your favorite drink?", "Lemonade", false, "0523456780", c3);
				User u24 = new User("Zoe Evans", "485726391", "abc123!@#", "What is your favorite board game?", "Monopoly", false, "0501234563", c3);
				User u25 = new User("Harper Edwards", "582619347", "password123!@#", "What is your favorite animal?", "Cat", false, "0579876545", c3);
				User u26 = new User("Benjamin Collins", "726391845", "qwerty!@#$", "What is your favorite TV series?", "Breaking Bad", false, "0512345675", c3);
				User u27 = new User("Aria Stewart", "917364825", "pass!@#$%", "What is your favorite outdoor activity?", "Hiking", false, "0587654323", c3);
				User u28 = new User("Lucas Watson", "364819257", "abc!@#$%^", "What is your favorite indoor activity?", "Reading", false, "0543210989", c3);
				User u29 = new User("Layla Harris", "485726193", "qwerty!@#$%^&", "What is your favorite ice cream flavor?", "Vanilla", false, "0567890120", c3);
				User u30 = new User("Henry Clark", "819273645", "password!@#$%^&*", "What is your favorite movie?", "The Godfather", false, "0532109874", c3);


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

	private static List<Task> getAllTasks(List<LocalDateTime> dateList) throws Exception {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);
		Join<Task, User> creatorJoin = root.join("taskCreator");
		Join<Task, User> volunteerJoin = root.join("taskVolunteer", JoinType.LEFT);
		query.select(root);
		if (dateList != null)
		{
			query.where(cb.between(root.get("creationTime"), dateList.get(0), dateList.get(1)));

		}
		List<Task> tasks = session.createQuery(query).getResultList();
		return tasks;
	}

	private static List<Task> getCommunityTasks(User user,List<LocalDateTime> dateList) throws Exception
	{

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);
		Join<Task, User> creatorJoin = root.join("taskCreator");
		Join<Task, User> volunteerJoin = root.join("taskVolunteer", JoinType.LEFT);
		query.select(root);
		query.where(cb.equal(creatorJoin.get("community"), user.getCommunity()));

		if(dateList != null)
		{
			query.where(cb.equal(creatorJoin.get("community"), user.getCommunity()),
					cb.between(root.get("creationTime"), dateList.get(0), dateList.get(1)));
		}
		List<Task> tasks = session.createQuery(query).getResultList();
		return tasks;
	}

	private static List<User> getCommunityUsers(User user) throws Exception
	{

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> query = cb.createQuery(User.class);
		Root<User> root = query.from(User.class);
		Join<User, Community> communityJoin = root.join("community");
		query.select(root);
		// fetch only users from the same community
		query.where(cb.equal(communityJoin.get("communityName"), user.getCommunity().getCommunityName()));



		List<User> users = session.createQuery(query).getResultList();
		return users;
	}

	// Idea: we can implement a hashmap for efficient way to search users, but I'm not sure how it works
	private static List<User> getAllUsers() throws Exception {
		List<User> users = session.createQuery("FROM User ORDER BY userName", User.class).getResultList();
		return users;
	}
	private static User getUserByTeudatZehut(String teudatZehut) throws Exception {
		List<User> users = getAllUsers();
		for (User user : users) {
			if (teudatZehut.equals(user.getTeudatZehut())) {
				return user; // User with the specified username found
			}
		}
		return null;

	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Message message = (Message) msg;
		String request = message.getMessage();
		System.out.println("request is: "+ request);
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();

			// TODO: give the user 5-6 times to try and connect and screw him for 30 seconds if he makes mistakes
			// When a user needs to connect
			if(request.equals("Login Request")){
				String[] loginDetails = (String[])message.getObject();
				String teudatZehut = loginDetails[0];
				String password = loginDetails[1];
				User user = getUserByTeudatZehut(teudatZehut);
				if (user != null && !idToClient.containsKey(teudatZehut)) {
					if(password.equals(user.getPassword())) {
//						idToClient.put()
						message.setMessage("Login Succeed");
					}
					else {
						message.setMessage("Login Failed: Wrong Password");
					}
				}
				else {
					if(user == null) {
						message.setMessage("Login Failed: No Such User Exists");
					}
					else {
						message.setMessage("Login Failed: Someone is already connected to the given ID");
					}
				}
				System.out.println("message sent: "+message.getMessage());
				client.sendToClient(message);
			}
			else if(request.equals("Forgot Password Request")){
				String[] forgotDetails = (String[])message.getObject();
				String teudatZehut = forgotDetails[0];
				String selectedQuestion = forgotDetails[1];
				String answer = forgotDetails[2];
				User user = getUserByTeudatZehut(teudatZehut);

				if (user != null && user.getSecretQuestion().equals(selectedQuestion) && user.getSecretQuestionAnswer().equals(answer)) {
					message.setMessage("Forgot Password: Match");
				}
				else {
					message.setMessage("Forgot Password: Fail");
				}
				System.out.println("message sent: " + message.getMessage());
				client.sendToClient(message);
			}
			else if(request.equals("New Password Request")) {
				String[] details = (String[]) message.getObject();
				String teudatZehut = details[0];
				String newPassword = details[1];
				User user = getUserByTeudatZehut(teudatZehut);
				if (user != null) {
					user.setPassword(newPassword);
					session.flush();
					if (user.getPassword().equals(newPassword)) {
						message.setMessage("Password Change Succeed");
					}
				} else {
					message.setMessage("Password Change Failed");
				}
			}
			else if(request.equals("Emergency Request")){
				String teudatzehut = (String)message.getObject();
				System.out.println("teudatzehut: " + teudatzehut);
				User user = getUserByTeudatZehut(teudatzehut);
				if(user != null) {
					Emergency emergency = new Emergency(user, LocalDateTime.now());
					session.save(emergency);
					session.getTransaction().commit();
					message.setMessage("Emergency Call Succeed");
				}
				else {
					message.setMessage("Emergency Call Failed");
				}
				System.out.println("message sent: " + message.getMessage());
				client.sendToClient(message);
			}
//			if(request.equals("Test")) {
////				User u1 = new User("Joe Biden","USA","999999999","iforgotmypasswword",true);
////				Task t1 = new Task("Mow the lawn in the white house",LocalDateTime.now(),"Bakasha",u1);
////				session.save(u1);
////				session.save(t1);
//				Task task = session.get(Task.class,1);
//				task.setRequiredTask("Bombing Ron Spector!!!");
//				session.update(task);
//				session.flush();
//
//				session.getTransaction().commit();
//
//				message.setTask(task);
//				message.setMessage("Test");
//				client.sendToClient(message);
//			}
//			else if (request.equals("Get Data")) {
//				Task task = session.get(Task.class,message.getId());
//				message.setTask(task);
//
//				client.sendToClient(message);
//
//			}
//			else if (request.equals("Update State")) {
//				Task task = session.get(Task.class,message.getId());
//				task.setTaskState("Betipul");
//				message.setTask(task);
//
//				session.update(task);
//				session.flush();
//
//				client.sendToClient(message);
//			}
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
