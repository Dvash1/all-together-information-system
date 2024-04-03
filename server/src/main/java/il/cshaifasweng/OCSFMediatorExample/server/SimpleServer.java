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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
	private static HashMap<String,ConnectionToClient> idToClient = new HashMap<>();
	private static HashMap<ConnectionToClient,String> clientToId = new HashMap<>();
	private static Session session;

	private static SessionFactory getSessionFactory() throws
			HibernateException {
		Configuration configuration = new Configuration();

		configuration.addAnnotatedClass(Task.class);
		configuration.addAnnotatedClass(User.class);
		configuration.addAnnotatedClass(Community.class);
		configuration.addAnnotatedClass(Emergency.class);
		configuration.addAnnotatedClass(UserMessage.class);


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
			if (getAllTasks(null, session).isEmpty()) {
				// first community
				User u1 = new User("Jan Christie", "335720074", "nJ9rS8~-", "What is your favorite color?", "Blue", true, "0523842728");
				Community c1 = new Community("Kfir", u1);
				u1.setCommunity(c1);
				User u2 = new User("Elie Dempsey", "331928044", "bZ8W7+.q", "What is your pet's name?", "Rover", false, "0538453293", c1);
				User u3 = new User("John Smith", "746158392", "password123", "What is your mother's maiden name?", "Johnson", false, "0523456789", c1);
				User u4 = new User("Alice Johnson", "538294617", "qwerty", "What city were you born in?", "New York", false, "0501234565", c1);
				User u5 = new User("Michael Brown", "921475386", "abc123", "What is the name of your first school?", "Maple Elementary", false, "0579876543", c1);
				User u6 = new User("Emily Davis", "364892175", "password", "What is your favorite movie?", "The Shawshank Redemption", false, "0512345678", c1);
				User u7 = new User("Daniel Wilson", "485739216", "123456", "What is your favorite food?", "Pizza", false, "0587654321", c1);
				User u8 = new User("Olivia Martinez", "627183945", "pass123", "Who is your favorite author?", "J.K. Rowling", false, "0543210987", c1);
				User u9 = new User("William Anderson", "819276435", "password456", "What is your dream vacation destination?", "Paris", false, "0567890123", c1);
				User u10 = new User("Sophia Garcia", "294617583", "123abc", "What is your favorite animal?", "Dog", false, "0532109876", c1);

				Task t1 = new Task("walk the dogs", LocalDateTime.now(), "Request", u2);
				Task t2 = new Task("clean the house", LocalDateTime.now().minusHours(2), "Request", u3);
				Task t3 = new Task("grocery shopping", LocalDateTime.now().minusDays(1), "Request", u4);
				Task t4 = new Task("write report", LocalDateTime.now().minusWeeks(1), "Complete", u5, u7);
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
				User u11 = new User("Emma Thompson", "111222333", "pass456", "What is your favorite hobby?", "Reading", true, "0598765432");
				Community c2 = new Community("Lemons", u11);
				u11.setCommunity(c2);
				User u12 = new User("James White", "444555666", "abc456", "What is your favorite sport?", "Football", false, "0554321098", c2);
				User u13 = new User("Ava Green", "777888999", "password789", "What is your favorite season?", "Spring", false, "0523456788", c2);
				User u14 = new User("Ethan Harris", "222333444", "qwerty123", "What is your favorite TV show?", "Friends", false, "0501234567", c2);
				User u15 = new User("Mia Lee", "555666777", "pass789", "What is your favorite holiday?", "Christmas", false, "0579876544", c2);
				User u16 = new User("Jacob Hall", "888999000", "abc789", "What is your favorite music genre?", "Pop", false, "0512345679", c2);
				User u17 = new User("Isabella Young", "333444555", "passwordabc", "What is your favorite subject?", "History", false, "0587654329", c2);
				User u18 = new User("Noah King", "666777888", "qwertyabc", "What is your favorite color?", "Green", false, "0543210986", c2);
				User u19 = new User("Sophie Baker", "999000111", "passabc", "What is your favorite book?", "To Kill a Mockingbird", false, "0567890124", c2);
				User u20 = new User("William Cook", "123456789", "abcqwerty", "What is your favorite movie genre?", "Action", false, "0532109856", c2);

				Task t6 = new Task("mow the lawn", LocalDateTime.now().minusHours(3), "Request", u11);
				Task t7 = new Task("fix the roof", LocalDateTime.now().minusDays(2), "Request", u12);
				Task t8 = new Task("paint the fence", LocalDateTime.now().minusWeeks(2), "Complete", u13, u17);
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
				User u21 = new User("Liam Murphy", "918273645", "abcpassword", "What is your favorite dessert?", "Ice Cream", true, "0598765432");
				Community c3 = new Community("Ono", u21);
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
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<UserMessage> query = cb.createQuery(UserMessage.class);
			Root<UserMessage> root = query.from(UserMessage.class); // Specify the root entity
			query.select(root); // Specify the select clause

			List<UserMessage> userMessages = session.createQuery(query).getResultList();
			if (userMessages.isEmpty()) {
				UserMessage um1 = new UserMessage("Test 1", "111222333", "444555666", "Normal");
				UserMessage um2 = new UserMessage("Test 2", "111222333", "444555666", "Normal");
				UserMessage um3 = new UserMessage("Test 3", "111222333", "444555666", "Normal");
				UserMessage um4 = new UserMessage("Test 4", "111222333", "444555666", "Normal");

				session.save(um1);
				session.save(um2);
				session.save(um3);
				session.save(um4);
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

	private static List<Task> getAllTasks(List<LocalDateTime> dateList, Session newSession) throws Exception {
		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);
		Join<Task, User> creatorJoin = root.join("taskCreator");
		Join<Task, User> volunteerJoin = root.join("taskVolunteer", JoinType.LEFT);
		query.select(root);
		if (dateList != null)
		{
			query.where(cb.between(root.get("creationTime"), dateList.get(0), dateList.get(1)));

		}
		List<Task> tasks = newSession.createQuery(query).getResultList();
		return tasks;
	}

	private static Task getTaskByTaskID(int taskId, Session newSession) {
		System.out.println("Getting task by ID");
		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);
		Join<Task, User> creatorJoin = root.join("taskCreator");
		Join<Task, User> volunteerJoin = root.join("taskVolunteer", JoinType.LEFT);

		query.select(root);
		query.where(cb.equal(root.get("id"), taskId));
		Task found_task = null;
		System.out.println("Starting query");
		try {
			found_task = newSession.createQuery(query).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();// Handle case where no task is found
		}
		return found_task;
	}

	private static List<UserMessage> getAllUsersMessagesByTeudatZehut(String teudatZehut, Session newSession) throws Exception {
		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<UserMessage> query = cb.createQuery(UserMessage.class);
		Root<UserMessage> root = query.from(UserMessage.class);
		if (!teudatZehut.isEmpty())
		{
			query.where(cb.equal(root.get("teudatZehut_to"), teudatZehut));
		}
		List<UserMessage> userMessages = newSession.createQuery(query).getResultList();
		return userMessages;
	}
	private static List<Task> getCommunityTasks(User user,List<LocalDateTime> dateList, Session newSession) throws Exception
	{

		CriteriaBuilder cb = newSession.getCriteriaBuilder();
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
		List<Task> tasks = newSession.createQuery(query).getResultList();
		return tasks;
	}

	private static List<User> getCommunityUsers(User user, Session newSession) throws Exception
	{

		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<User> query = cb.createQuery(User.class);
		Root<User> root = query.from(User.class);
		Join<User, Community> communityJoin = root.join("community");
		query.select(root);
		// fetch only users from the same community
		query.where(cb.equal(communityJoin.get("communityName"), user.getCommunity().getCommunityName()));



		List<User> users = newSession.createQuery(query).getResultList();
		return users;
	}

	private static List<Task> getWaitingTasks(User user, Session newSession) throws Exception
	{

		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);
		Join<Task, User> creatorJoin = root.join("taskCreator");
		Join<Task, User> volunteerJoin = root.join("taskVolunteer", JoinType.LEFT);
		query.select(root);
		query.where(cb.equal(creatorJoin.get("community"), user.getCommunity()),
				cb.equal(root.get("taskState"),"Awaiting approval"));


		List<Task> tasks = newSession.createQuery(query).getResultList();
		return tasks;
	}

	// used in ViewTasksController, in order to display all open tasks
	private static List<Task> getOpenTasks(User user, Session newSession) throws Exception
	{

		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);
		Join<Task, User> creatorJoin = root.join("taskCreator");
		Join<Task, User> volunteerJoin = root.join("taskVolunteer", JoinType.LEFT);
		query.select(root);
		query.where(cb.equal(creatorJoin.get("community"), user.getCommunity()),
				cb.or(cb.equal(root.get("taskState"), "Request"),
						cb.equal(root.get("taskState"), "In Progress")));

		List<Task> tasks = newSession.createQuery(query).getResultList();
		return tasks;
	}



	private static List<Emergency> getAllEmergencyCases(List<LocalDateTime> dateList, Session newSession) throws Exception {
		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<Emergency> query = cb.createQuery(Emergency.class);
		Root<Emergency> root = query.from(Emergency.class);
		Join<Emergency, User> creatorJoin = root.join("user");
		query.select(root);
		if (dateList != null)
		{
			query.where(cb.between(root.get("callTime"), dateList.get(0), dateList.get(1)));

		}
		List<Emergency> emergencies = newSession.createQuery(query).getResultList();
		return emergencies;
	}

	private static List<Emergency> getCommunityEmergencies(User user,List<LocalDateTime> dateList, Session newSession) throws Exception
	{
		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<Emergency> query = cb.createQuery(Emergency.class);
		Root<Emergency> root = query.from(Emergency.class);
		Join<Emergency, User> creatorJoin = root.join("user");
		query.select(root);
		query.where(cb.equal(creatorJoin.get("community"), user.getCommunity()));

		if(dateList != null)
		{
			query.where(cb.equal(creatorJoin.get("community"), user.getCommunity()),
					cb.between(root.get("creationTime"), dateList.get(0), dateList.get(1)));
		}
		List<Emergency> emergencys = newSession.createQuery(query).getResultList();
		return emergencys;
	}

	private static User getUserByTeudatZehut(String teudatZehut, Session newSession) throws Exception {

		User user = newSession.createQuery("FROM User WHERE teudatZehut = :teudatZehutValue", User.class)
				.setParameter("teudatZehutValue", teudatZehut)
				.uniqueResult();
		return user;
	}



	private static void sendMessageToClient(UserMessage message, Session newSession) throws Exception {
		// TODO: REMOVE DEBUG?
		// TODO: optimally, we would want the people sending to be a list. Maybe implement?
		// --DEBUG
		System.out.println("sendMessageToClient called");
		// ---
		String message_type = message.getMessage_type();
		String sender_zehut = message.getSender_zehut();
		String to_zehut = message.getTeudatZehut_to();
		String message_text = message.getMessage();
		if (message_type.isEmpty() || sender_zehut.isEmpty() || message_text.isEmpty() || to_zehut.isEmpty()) {
			return;
		} // Check if anything is empty first.

		// --DEBUG
		System.out.println("I passed the empty check");
		System.out.print("Sender is:");
		System.out.println(sender_zehut);
		System.out.print("Reciever is:");
		System.out.println(to_zehut);
		// ---

		User message_reciever_user = getUserByTeudatZehut(to_zehut, newSession);
		User message_sender_user = getUserByTeudatZehut(sender_zehut, newSession);

		System.out.println(message_sender_user.getTeudatZehut());
		System.out.println(message_reciever_user.getTeudatZehut());

		if(idToClient.containsKey(to_zehut)) { // User is logged in.
			// --DEBUG
			System.out.println("User logged in");
			// ---
			ConnectionToClient Message_Reciever_Client = idToClient.get(to_zehut);
			List<Object> messageDetails = new ArrayList<>();
			messageDetails.add(message);
			messageDetails.add(message_sender_user.getUserName());
			Message_Reciever_Client.sendToClient(new Message("New Message", messageDetails)); // Send message as an object.
			newSession.remove(message);
		}

		else { // Not connected. Save to DB.
			// --DEBUG
			System.out.println("User not logged in");
			// ---
			newSession.save(message);
			newSession.flush();
			newSession.getTransaction().commit();

		}

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
				System.out.println("teudatZehut : "+teudatZehut);
				User user = getUserByTeudatZehut(teudatZehut, session);
				boolean subscriberFound = false;
				if (user != null && !idToClient.containsKey(teudatZehut)) {
					if(password.equals(user.getPassword())) {

						// Bind client to id.
						for(SubscribedClient subscriber: SubscribersList) {
							if (subscriber.getClient() == client) {
								// Take the client from the signature and compare
								idToClient.put(teudatZehut, client); // TODO: check if both work.
								clientToId.put(client,teudatZehut);
								subscriberFound = true;
							}
						}
						if (subscriberFound) { // We log in only after making sure the client is subscribed
							message.setMessage("Login Succeed");
							message.setUser(user);
						}
						else { // Client wasn't subscribed. Shouldn't happen, debug this if happened.
							try {
								message.setMessage("Login Failed: Something went wrong. You aren't connected?");
								throw new RuntimeException("You aren't connected");
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					else {
						System.out.println(password + " versus "  + user.getPassword() );
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



			else if(request.equals("Log Out")){
				System.out.println("In Log Out");
				User user = (User) message.getObject();

				System.out.print("The ID logging out is:");
				System.out.println(user.getId());


				if (user != null) {
					String teudatZehut = user.getTeudatZehut();
					// remove from both hash maps

					clientToId.remove(idToClient.get(teudatZehut)); // Remove from clientToId too.
					idToClient.remove(teudatZehut); // Remove from idToClient the client.

					System.out.println("Successfully logged out");
					message.setMessage("log out");
					client.sendToClient(message);
					
				}
				// TODO: also add for if user is null


			}
			else if(request.equals("Forgot Password Request")){
				String[] forgotDetails = (String[])message.getObject();
				String teudatZehut = forgotDetails[0];
				String selectedQuestion = forgotDetails[1];
				String answer = forgotDetails[2];
				User user = getUserByTeudatZehut(teudatZehut, session);

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
				User user = getUserByTeudatZehut(teudatZehut, session);
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
			else if(request.equals("Task not completed on time")) {
				// Task id is in message.
				int taskID = message.getTaskID();
				ScheduledExecutorService scheduler;
				scheduler = Executors.newSingleThreadScheduledExecutor();
				System.out.println("Created Thread");
				scheduler.schedule(() -> {
					System.out.println("Scheduled event started");
					// This will be executed after 1 day
					Session new_session = sessionFactory.openSession();
					new_session.beginTransaction();
					Task task_to_check = getTaskByTaskID(taskID, new_session);
					System.out.print("Got the task. Id is: ");
					System.out.println(taskID);
					System.out.println(task_to_check.getTaskState());
					if (task_to_check.getTaskState().equals("In Progress")) { // Check if still in progress.
						// If it is, we send a message to ask why its still in progress.
						UserMessage usermessage_to_send = (UserMessage) message.getObject();
                        try {
                            sendMessageToClient(usermessage_to_send, new_session);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

					System.out.println("Task not completed on time");
					new_session.close();
					scheduler.shutdown();
				}, 2, TimeUnit.SECONDS); // ** Change here the time you want to give on a given task.
			}
			else if(request.equals("Emergency Request")){
				String teudatzehut = (String)message.getObject();
				System.out.println("teudatzehut: " + teudatzehut);
				User user = getUserByTeudatZehut(teudatzehut, session);
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

			//create task, for now send the message to all the clients
			// however its more logical to send the message only to the community manager that needs to approve
			else if(request.equals("create task"))
			{
				Task testTask = (Task) message.getObject();
				session.save(testTask);
				session.flush();
				session.getTransaction().commit();
//				client.sendToClient(message);
				sendToAllClients(message);

			}



// used for CommunityInformationController (and ViewEmergencyCalls for now)
			else if(request.equals("get tasks")) {
				User u1 = message.getUser();
				List<Task> tasks = getCommunityTasks(u1,null, session);
				// fetches all Tasks in database from the same community

				message.setObject(tasks);
				client.sendToClient(message);
			}
// used for ViewTasksController
			else if(request.equals("get open tasks")) {
				User u1 = message.getUser();
				List<Task> tasks = getOpenTasks(u1, session);
				// fetches all OPEN (AND APPROVED) TASKS in database from the same community
				getTaskByTaskID(1, session);
				System.out.println("Gotten task by ID.");
				message.setObject(tasks);
				client.sendToClient(message);
			}
// retrieve community users for CommunityInformationController

			else if(request.equals("get users")) {
				User u1 = message.getUser();
				List<User> Users = getCommunityUsers(u1, session);
				message.setObject(Users);
				client.sendToClient(message);
			}

			//called when ApproveRequestController is loaded
			else if (request.equals("get awaiting approval requests"))
			{
				User currentUser = message.getUser();
				List<Task> tasks = getWaitingTasks(currentUser, session);
				message.setObject(tasks);
				client.sendToClient(message);

			}


			// ***************************************REPLACE WITH EMERGENCY IMPLEMENTATION *********************
// note : this worked fine before i made changes to getCommunityTasks
			else if(request.equals("emergency everything"))
			{
				List<LocalDateTime> dates = (List<LocalDateTime>) message.getObject();
				List<Emergency> emergencies = getAllEmergencyCases(null, session);
				message.setMessage("emergency histogram");
				message.setObject(emergencies);
				client.sendToClient(message);
			}
			else if(request.equals("emergency my community all dates"))
			{
				User u1 = (User) message.getUser();
				List<Emergency> emergencies = getCommunityEmergencies(u1,null, session);
				message.setMessage("emergency histogram");
				message.setObject(emergencies);
				client.sendToClient(message);
			}
			else if(request.equals("emergency all community specific dates"))
			{
				List<LocalDateTime> dates = (List<LocalDateTime>) message.getObject();
				List<Emergency> emergencies = getAllEmergencyCases(dates, session);
				message.setMessage("emergency histogram");
				message.setObject(emergencies);
				client.sendToClient(message);
			}
			else if(request.equals("emergency my community specific dates"))
			{
				User u1 = (User) message.getUser();
				List<LocalDateTime> dates = (List<LocalDateTime>) message.getObject();
				List<Emergency> emergencies = getCommunityEmergencies(u1,dates, session);
				message.setMessage("emergency histogram");
				message.setObject(emergencies);
				client.sendToClient(message);
			}

// ***************************************REPLACE WITH EMERGENCY IMPLEMENTATION *********************



			else if (request.equals("Get Data")) {
				Task task = session.get(Task.class,message.getTaskID());
				message.setObject(task);
				client.sendToClient(message);

			}
			// withdrawing is being handled separately because you could change a state to be "Request" in two ways.
			else if(request.equals("Withdraw from task"))
			{
				Task task = (Task) message.getObject();
				session.update(task);
				session.flush();
				session.getTransaction().commit();
				sendToAllClients(message);
			}


			else if (request.equals("Update task")) {

				Task task = (Task) message.getObject();
				session.update(task);
				session.flush();
				session.getTransaction().commit();
				if(task.getTaskState().equals("Request"))
				{
					message.setMessage("Publish approved task");
				}

				else if (task.getTaskState().equals("In Progress"))
				{
					message.setMessage("Volunteer to task");
				}
				else if (task.getTaskState().equals("Denied"))
				{
					message.setMessage("Request denied");
					// ***MISSING IMPLEMENTATION***
					// need to send message to community member that requested the help
				}
				else
				{
					message.setMessage("Complete task");
				}
				sendToAllClients(message);
//				client.sendToClient(message);

			}

			else if (request.equals("Send message"))
			{
				// Hopefully, message has a UserMessage object in it.
				try {
					sendMessageToClient((UserMessage) message.getObject(), session);
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}


			else if (request.equals("Get user's messages")) { // Get by query a list of all the user messages for user
				String teudatZehut = (String)message.getObject();
				List<UserMessage> userMessageList = getAllUsersMessagesByTeudatZehut(teudatZehut, session);
				System.out.print("Size of messageList is:");
				System.out.println(userMessageList.size());

				System.out.print("The value of !isEmpty is: ");
				System.out.println(!userMessageList.isEmpty());

				if(!userMessageList.isEmpty()) {
//					message.setMessage("New Message"); // Use NewMessageEvent in SimpleChatClient
					for (UserMessage userMessage : userMessageList) { // Loop over list and send a message to the client.
//						message.setObject(userMessage);
						System.out.println("I'm sending a message");
						sendMessageToClient(userMessage, session);
					}
					session.flush();
					session.getTransaction().commit();
				}
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

	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client)
	{
		System.out.println("a client has disconnected");
//		super.clientDisconnected(client);
		synchronized (SubscribersList) {
			// Find the SubscribedClient that corresponds to the ConnectionToClient to remove
			SubscribedClient clientToRemove = null;
			for (SubscribedClient subscribedClient : SubscribersList) {
				if (subscribedClient.getClient() == client) {
					clientToRemove = subscribedClient;
					break;
				}
			}

// Remove the SubscribedClient from the list
			if (clientToRemove != null) {
				SubscribersList.remove(clientToRemove);
				//remove client from hashmap as well
				idToClient.remove(clientToId.get(clientToRemove.getClient())); // Remove from idToClient the client.
				clientToId.remove(clientToRemove.getClient()); // Remove from clientToId too.
			}
		}
	}

}


// to implement :
//1.
// add two hashmaps, mapping ConnectionToClient to Integer (User ID) and vice versa
// when user logs in : add mapping
// when user logs out : remove mapping
// if manager wants to send a message to a user in his community, or when a new task is posted, first search for related ConnectionToClient object

//2.add new entry to database, containing awaiting messages to user/manager. (fetch list when user logs in)
// OneToMany
// query using CriteriaQuery, delete using CriteriaDelete