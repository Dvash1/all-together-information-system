package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
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
import javax.persistence.criteria.*;
import java.util.concurrent.*;

public class SimpleServer extends AbstractServer {

	private static final long noVolunteerTime = 1;
	private static final TimeUnit noVolunteerTimeUnits = TimeUnit.MINUTES;

	private static final long toCompleteTime = 1;
	private static final TimeUnit toCompleteTimeUnits = TimeUnit.MINUTES;

	private static final long lockingTime = 30;
	private static final TimeUnit lockingTimeUnits = TimeUnit.SECONDS;

	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private static HashMap<String,ConnectionToClient> idToClient = new HashMap<>();
	private static HashMap<ConnectionToClient,String> clientToId = new HashMap<>();
	private static HashMap<Integer, Pair<ScheduledExecutorService,ScheduledFuture>> taskIDtoThread = new HashMap<>();
	private static Session session;

	private static SessionFactory getSessionFactory() throws
			HibernateException {
		Configuration configuration = new Configuration();

		configuration.addAnnotatedClass(Task.class);
		configuration.addAnnotatedClass(User.class);
		configuration.addAnnotatedClass(CommunityManagerUser.class);
		configuration.addAnnotatedClass(CommunityMemberUser.class);
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
				CommunityManagerUser u1 = new CommunityManagerUser("Jan Christie", "335720074", "nJ9rS8~-", "What is your favorite color?", "Blue","0523842728");
				Community c1 = new Community("Kfir",u1);
				u1.setCommunity(c1);
				u1.setCommunityManaging(c1);
				CommunityMemberUser u2 = new CommunityMemberUser("Elie Dempsey", "331928044", "bZ8W7+.q", "What is your pet's name?", "Rover","0538453293" ,c1);
				CommunityMemberUser u3 = new CommunityMemberUser("John Smith", "746158392", "password123", "What is your mother's maiden name?", "Johnson", "0523456789", c1);
				CommunityMemberUser u4 = new CommunityMemberUser("Alice Johnson", "538294617", "qwerty", "What city were you born in?", "New York", "0501234565", c1);
				CommunityMemberUser u5 = new CommunityMemberUser("Michael Brown", "921475386", "abc123", "What is the name of your first school?", "Maple Elementary",  "0579876543", c1);
				CommunityMemberUser u6 = new CommunityMemberUser("Emily Davis", "364892175", "password", "What is your favorite movie?", "The Shawshank Redemption",  "0512345678", c1);
				CommunityMemberUser u7 = new CommunityMemberUser("Daniel Wilson", "485739216", "123456", "What is your favorite food?", "Pizza",  "0587654321", c1);
				CommunityMemberUser u8 = new CommunityMemberUser("Olivia Martinez", "627183945", "pass123", "Who is your favorite author?", "J.K. Rowling",  "0543210987", c1);
				CommunityMemberUser u9 = new CommunityMemberUser("William Anderson", "819276435", "password456", "What is your dream vacation destination?", "Paris", "0567890123", c1);
				CommunityMemberUser u10 = new CommunityMemberUser("Sophia Garcia", "294617583", "123abc", "What is your favorite animal?", "Dog",  "0532109876", c1);

				Task t1 = new Task("walk the dogs", LocalDateTime.now(),"Request",u2);
				Task t2 = new Task("clean the house", LocalDateTime.now().minusHours(2), "In Progress", u3,u4);
				Task t3 = new Task("grocery shopping", LocalDateTime.now().minusDays(1), "In Progress", u4,u3);
				Task t4 = new Task("write report", LocalDateTime.now().minusWeeks(1).minusDays(4), "Complete", u5,u7);
				t4.setCompletionTime(LocalDateTime.now().minusDays(5));
				Task t5 = new Task("prepare presentation", LocalDateTime.now().minusDays(2), "Request", u6);

				Emergency e1 = new Emergency(u1, LocalDateTime.now().minusDays(2));
				Emergency e2 = new Emergency(u2, LocalDateTime.now().minusDays(4));
				Emergency e3 = new Emergency(u3, LocalDateTime.now().minusDays(1));
				Emergency e4 = new Emergency(u4, LocalDateTime.now().minusDays(3));
				Emergency e5 = new Emergency(u5, LocalDateTime.now().minusDays(5));
				Emergency e6 = new Emergency(u6, LocalDateTime.now().minusDays(2));
				Emergency e7 = new Emergency(u7, LocalDateTime.now().minusDays(4));
				Emergency e8 = new Emergency(u8, LocalDateTime.now().minusDays(1));
				Emergency e9 = new Emergency(u9, LocalDateTime.now().minusDays(3));
				Emergency e10 = new Emergency(u10, LocalDateTime.now().minusDays(5));


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

				session.save(e1);
				session.save(e2);
				session.save(e3);
				session.save(e4);
				session.save(e5);
				session.flush();

				session.save(e6);
				session.save(e7);
				session.save(e8);
				session.save(e9);
				session.save(e10);
				session.flush();
				taskNotVolunteer(t1,2,sessionFactory,TimeUnit.SECONDS);
				taskVolunteerDidNotFinishOnTime(t2,5,sessionFactory,TimeUnit.SECONDS);
				taskVolunteerDidNotFinishOnTime(t3,12,sessionFactory,TimeUnit.SECONDS);
				taskNotVolunteer(t5,1,sessionFactory,TimeUnit.HOURS);
// second community
				CommunityManagerUser u11 = new CommunityManagerUser("Emma Thompson", "111222333", "pass456", "What is your favorite hobby?", "Reading", "0598765432");
				Community c2 = new Community("Lemons", u11);
				u11.setCommunity(c2);
				u11.setCommunityManaging(c2);
				CommunityMemberUser u12 = new CommunityMemberUser("James White", "444555666", "abc456", "What is your favorite sport?", "Football",  "0554321098", c2);
				CommunityMemberUser u13 = new CommunityMemberUser("Ava Green", "777888999", "password789", "What is your favorite season?", "Spring", "0523456788", c2);
				CommunityMemberUser u14 = new CommunityMemberUser("Ethan Harris", "222333444", "qwerty123", "What is your favorite TV show?", "Friends", "0501234567", c2);
				CommunityMemberUser u15 = new CommunityMemberUser("Mia Lee", "555666777", "pass789", "What is your favorite holiday?", "Christmas", "0579876544", c2);
				CommunityMemberUser u16 = new CommunityMemberUser("Jacob Hall", "888999000", "abc789", "What is your favorite music genre?", "Pop",  "0512345679", c2);
				CommunityMemberUser u17 = new CommunityMemberUser("Isabella Young", "333444555", "passwordabc", "What is your favorite subject?", "History",  "0587654329", c2);
				CommunityMemberUser u18 = new CommunityMemberUser("Noah King", "666777888", "qwertyabc", "What is your favorite color?", "Green", "0543210986", c2);
				CommunityMemberUser u19 = new CommunityMemberUser("Sophie Baker", "999000111", "passabc", "What is your favorite book?", "To Kill a Mockingbird", "0567890124", c2);
				CommunityMemberUser u20 = new CommunityMemberUser("William Cook", "123456789", "abcqwerty", "What is your favorite movie genre?", "Action", "0532109856", c2);

				Task t6 = new Task("mow the lawn", LocalDateTime.now().minusWeeks(1), "Complete", u11);
				t6.setCompletionTime(LocalDateTime.now().minusDays(3));
				Task t7 = new Task("fix the roof", LocalDateTime.now().minusDays(2), "In Progress", u12,u13);
				Task t8 = new Task("paint the fence", LocalDateTime.now().minusWeeks(2), "Complete", u13, u17);
				t8.setCompletionTime(LocalDateTime.now().minusWeeks(1).minusDays(4));
				Task t9 = new Task("clean the garage", LocalDateTime.now().minusDays(3), "Request", u14);
				Task t10 = new Task("organize the shed", LocalDateTime.now().minusWeeks(3), "Complete", u15);
				t10.setCompletionTime(LocalDateTime.now().minusWeeks(1).minusDays(3));

				Emergency e11 = new Emergency(u11, LocalDateTime.now().minusDays(3));
				Emergency e12 = new Emergency(u12, LocalDateTime.now().minusDays(1));
				Emergency e13 = new Emergency(u13, LocalDateTime.now().minusDays(2));
				Emergency e14 = new Emergency(u14, LocalDateTime.now().minusDays(4));
				Emergency e15 = new Emergency(u15, LocalDateTime.now().minusDays(5));
				Emergency e16 = new Emergency(u16, LocalDateTime.now().minusDays(2));
				Emergency e17 = new Emergency(u17, LocalDateTime.now().minusDays(3));
				Emergency e18 = new Emergency(u18, LocalDateTime.now().minusDays(1));
				Emergency e19 = new Emergency(u19, LocalDateTime.now().minusDays(4));
				Emergency e20 = new Emergency(u20, LocalDateTime.now().minusDays(5));

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
				taskVolunteerDidNotFinishOnTime(t7,5,sessionFactory,TimeUnit.HOURS);
				taskNotVolunteer(t9,32,sessionFactory,TimeUnit.MINUTES);

				session.save(e11);
				session.save(e12);
				session.save(e13);
				session.save(e14);
				session.save(e15);
				session.flush();

				session.save(e16);
				session.save(e17);
				session.save(e18);
				session.save(e19);
				session.save(e20);
				session.flush();

				// third community

				CommunityManagerUser u21 = new CommunityManagerUser("Liam Murphy", "918273645", "abcpassword", "What is your favorite dessert?", "Ice Cream","0598765432");

				Community c3 = new Community("Ono",u21);
				u21.setCommunity(c3);
				u21.setCommunityManaging(c3);
				CommunityMemberUser u22 = new CommunityMemberUser("Grace Turner", "726394185", "qwerty12345", "What is your favorite fruit?", "Strawberry", "0554321091", c3);
				CommunityMemberUser u23 = new CommunityMemberUser("Mason Parker", "364598217", "password!@#", "What is your favorite drink?", "Lemonade", "0523456780", c3);
				CommunityMemberUser u24 = new CommunityMemberUser("Zoe Evans", "485726391", "abc123!@#", "What is your favorite board game?", "Monopoly",  "0501234563", c3);
				CommunityMemberUser u25 = new CommunityMemberUser("Harper Edwards", "582619347", "password123!@#", "What is your favorite animal?", "Cat", "0579876545", c3);
				CommunityMemberUser u26 = new CommunityMemberUser("Benjamin Collins", "726391845", "qwerty!@#$", "What is your favorite TV series?", "Breaking Bad",  "0512345675", c3);
				CommunityMemberUser u27 = new CommunityMemberUser("Aria Stewart", "917364825", "pass!@#$%", "What is your favorite outdoor activity?", "Hiking",  "0587654323", c3);
				CommunityMemberUser u28 = new CommunityMemberUser("Lucas Watson", "364819257", "abc!@#$%^", "What is your favorite indoor activity?", "Reading",  "0543210989", c3);
				CommunityMemberUser u29 = new CommunityMemberUser("Layla Harris", "485726193", "qwerty!@#$%^&", "What is your favorite ice cream flavor?", "Vanilla", "0567890120", c3);
				CommunityMemberUser u30 = new CommunityMemberUser("Henry Clark", "819273645", "password!@#$%^&*", "What is your favorite movie?", "The Godfather",  "0532109874", c3);

				Task t11 = new Task("rake the leaves", LocalDateTime.now().minusHours(4), "In Progress", u21,u22);

				Task t12 = new Task("clean the gutters", LocalDateTime.now().minusDays(5), "Request", u22);
				Task t13 = new Task("trim the hedges", LocalDateTime.now().minusWeeks(3), "Complete", u23, u27);
				t13.setCompletionTime(LocalDateTime.now().minusWeeks(2));
				Task t14 = new Task("wash the car", LocalDateTime.now().minusDays(4), "In Progress", u24,u29);
				Task t15 = new Task("sweep the driveway", LocalDateTime.now().minusWeeks(4), "Complete", u25);
				t13.setCompletionTime(LocalDateTime.now().minusWeeks(1));

				Emergency e21 = new Emergency(u21, LocalDateTime.now().minusDays(2));
				Emergency e22 = new Emergency(u22, LocalDateTime.now().minusDays(4));
				Emergency e23 = new Emergency(u23, LocalDateTime.now().minusDays(1));
				Emergency e24 = new Emergency(u24, LocalDateTime.now().minusDays(3));
				Emergency e25 = new Emergency(u25, LocalDateTime.now().minusDays(5));
				Emergency e26 = new Emergency(u26, LocalDateTime.now().minusDays(2));
				Emergency e27 = new Emergency(u27, LocalDateTime.now().minusDays(4));
				Emergency e28 = new Emergency(u28, LocalDateTime.now().minusDays(1));
				Emergency e29 = new Emergency(u29, LocalDateTime.now().minusDays(3));
				Emergency e30 = new Emergency(u30, LocalDateTime.now().minusDays(5));

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

				taskVolunteerDidNotFinishOnTime(t11,20,sessionFactory,TimeUnit.HOURS);
				taskVolunteerDidNotFinishOnTime(t14,24,sessionFactory,TimeUnit.HOURS);
				taskNotVolunteer(t12,17,sessionFactory,TimeUnit.SECONDS);

				session.save(e21);
				session.save(e22);
				session.save(e23);
				session.save(e24);
				session.save(e25);
				session.flush();

				session.save(e26);
				session.save(e27);
				session.save(e28);
				session.save(e29);
				session.save(e30);
				session.flush();



			}
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<UserMessage> query = cb.createQuery(UserMessage.class);
			Root<UserMessage> root = query.from(UserMessage.class); // Specify the root entity
			query.select(root); // Specify the select clause

			List<UserMessage> userMessages = session.createQuery(query).getResultList();
			if (userMessages.isEmpty()) {
				for (int i = 1; i <= 10; i++) {
					UserMessage um = new UserMessage("Test " + i, "111222333", "444555666", "Normal");
					session.save(um);
				}
				for (int i = 1; i <= 10; i++) {
					UserMessage um = new UserMessage("Community Test " + i, "111222333", "444555666", "Community");
					session.save(um);
				}
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

	private static void taskVolunteerDidNotFinishOnTime(Task selectedTask, long timeUnit, SessionFactory sessionFactory, TimeUnit time) {
		System.out.println("Starting thread on base task that did not finish on time.");
		int taskID = selectedTask.getId();
		User currentUser = selectedTask.getTaskVolunteer();
		shutDown_pair(taskID);

		ScheduledExecutorService scheduler;
		scheduler = Executors.newSingleThreadScheduledExecutor();

		Runnable to_do = new Runnable() {
			@Override
			public void run() {
				// Code to execute
				// This will be executed after allotted time
				try (Session new_session = sessionFactory.openSession()) {
					new_session.beginTransaction();
					Task task_to_check = getTaskByTaskID(taskID, new_session);
					String manager_zehut = currentUser.getCommunity().getCommunityManager().getTeudatZehut();
					String message_text = "24 hours have passed on the task:\n\"" + selectedTask.getRequiredTask() + "\"\nBy: " + selectedTask.getTaskCreator().getUserName() + "\n" + "Are you finished with the task?";
					UserMessage usermessage_to_send = new UserMessage(message_text, manager_zehut, currentUser.getTeudatZehut(),"Not Complete");
					if (task_to_check.getTaskState().equals("In Progress")) { // Check if still in progress.
						// If it is, we send a message to ask why it's still in progress.
						usermessage_to_send.setTask_id(taskID);
						try {
							sendMessageToClient(usermessage_to_send, new_session);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					System.out.println("Task not completed on time");
					new_session.close();

					scheduler.schedule(this, toCompleteTime, toCompleteTimeUnits);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		System.out.println("Created Thread");
		ScheduledFuture<?> scheduledFuture = scheduler.schedule(to_do, timeUnit, time); // ** Change here the time you want to give on a given task.
		taskIDtoThread.put(taskID, new Pair<>(scheduler, scheduledFuture));
	}








	private static void taskNotVolunteer(Task task, long timeUnit, SessionFactory sessionFactory, TimeUnit time) {
		// Give 24 hours for someone to volunteer.
		shutDown_pair(task.getId());

		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//		System.out.println("Created Thread in SWITCH");

		Runnable to_do = new Runnable() {
			@Override
			public void run() {
				System.out.println("Switch Scheduled event started");
				// This will be executed after 1 day
				Session new_session = sessionFactory.openSession();
				new_session.beginTransaction();

				System.out.print("(Switch)Got the task. Id is: ");
				System.out.println(task.getId());
				System.out.println(task.getTaskState());

				if (task.getTaskState().equals("Request")) { // Check if still in request mode.
					// If it is, we send a message to everyone to tell them nobody volunteered yet.
					System.out.println("state is Request");
					String text_for_message = "The task: \"" + task.getRequiredTask() + "\"\nWas not volunteered to and 24 hours have passed.";
					List<User> community_list = new ArrayList<>();

					// GET LIST
					try {
						community_list = getCommunityUsers(task.getTaskCreator(), new_session);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// -----
					System.out.println("Starting loop");
					try {
						for (User user_to_send : community_list) { // Loop through entire community and send them the message.
							UserMessage usermessage_to_send = new UserMessage(text_for_message, task.getTaskCreator().getTeudatZehut(), user_to_send.getTeudatZehut(), "Community");
							System.out.println("sending message to user with TeudatZehut : " + user_to_send.getTeudatZehut());
							sendMessageToClient(usermessage_to_send, new_session);
						}

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						new_session.getTransaction().commit();
						new_session.close();
					}
				}

//				System.out.println("Task not volunteered to on time");

				ScheduledFuture<?> scheduledFuture = scheduler.schedule(this, noVolunteerTime, noVolunteerTimeUnits);
				taskIDtoThread.get(task.getId()).setSecond(scheduledFuture);
			}
		};

		ScheduledFuture<?> scheduledFuture = scheduler.schedule(to_do, timeUnit, time); // ** Change here the time you want to give on a given task.
		taskIDtoThread.put(task.getId(), new Pair<>(scheduler, scheduledFuture));
	}

	private static List<Task> getAllTasks(List<LocalDateTime> dateList, Session newSession) throws Exception {
		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);
		query.select(root);
		if (dateList != null)
		{
			query.where(cb.between(root.get("creationTime"), dateList.get(0), dateList.get(1)));

		}
		List<Task> tasks = newSession.createQuery(query).getResultList();
		return tasks;
	}

	private static Task getTaskByTaskID(int taskId, Session newSession) {
//		System.out.println("Getting task by ID");
		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<Task> query = cb.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);

		query.select(root);
		query.where(cb.equal(root.get("id"), taskId));
		Task found_task = null;
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
		query.select(root);
		query.where(cb.equal(creatorJoin.get("community"), user.getCommunity()),
				cb.or(cb.equal(root.get("taskState"), "Request"),
						cb.equal(root.get("taskState"), "In Progress")));

		List<Task> tasks = newSession.createQuery(query).getResultList();
		return tasks;
	}

	// Idea: we can implement a hashmap for efficient way to search users, but I'm not sure how it works
	private static List<User> getAllUsers() throws Exception {
		List<User> users = session.createQuery("FROM User ORDER BY userName", User.class).getResultList();
		return users;
	}

	private static List<Emergency> getAllEmergencyCases(List<LocalDateTime> dateList, Session newSession) throws Exception {
		CriteriaBuilder cb = newSession.getCriteriaBuilder();
		CriteriaQuery<Emergency> query = cb.createQuery(Emergency.class);
		Root<Emergency> root = query.from(Emergency.class);
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
					cb.between(root.get("callTime"), dateList.get(0), dateList.get(1)));
		}
		List<Emergency> emergencies = newSession.createQuery(query).getResultList();
		return emergencies;
	}

	private static User getUserByTeudatZehut(String teudatZehut, Session newSession) throws Exception {

		User user = newSession.createQuery("FROM User WHERE teudatZehut = :teudatZehutValue", User.class)
				.setParameter("teudatZehutValue", teudatZehut)
				.uniqueResult();
		return user;
	}

	private static User getUserByPhoneNumber(String phoneNumber, Session newSession) throws Exception {

		User user = newSession.createQuery("FROM User WHERE phoneNumber = :phoneNumberValue", User.class)
				.setParameter("phoneNumberValue", phoneNumber)
				.uniqueResult();
		return user;
	}

	private static void createTaskNotCompleteThread(int taskID, UserMessage usermessage_to_send,long timeUnit, SessionFactory sessionFactory, TimeUnit time) {
		shutDown_pair(taskID);

		ScheduledExecutorService scheduler;
		scheduler = Executors.newSingleThreadScheduledExecutor();

		Runnable to_do = new Runnable() {
			@Override
			public void run() {
				// This will be executed after allotted time
				Session new_session = sessionFactory.openSession();
				new_session.beginTransaction();
				Task task_to_check = getTaskByTaskID(taskID, new_session);

				if (task_to_check.getTaskState().equals("In Progress")) { // Check if still in progress.
					// If it is, we send a message to ask why it's still in progress.
					usermessage_to_send.setTask_id(taskID);
					try {
						sendMessageToClient(usermessage_to_send, new_session);
						System.out.println("called sendMessageToClient");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("Task not completed on time");
				new_session.close();

				scheduler.schedule(this,timeUnit,time);
			}
		};
		System.out.println("Created Thread");
		ScheduledFuture<?> scheduledFuture = scheduler.schedule(to_do, timeUnit, time); // ** Change here the time you want to give on a given task.
		taskIDtoThread.put(taskID, new Pair<>(scheduler,scheduledFuture));
	}


	private static void createNoVolunteerThread(int taskID,long timeUnit, SessionFactory sessionFactory, TimeUnit time) {
		// Give 24 hours for someone to volunteer.
		shutDown_pair(taskID);


		ScheduledExecutorService scheduler;
		scheduler = Executors.newSingleThreadScheduledExecutor();
		System.out.println("Created Thread in SWITCH");

		Runnable to_do = new Runnable() {
			@Override
			public void run() {
//				System.out.println("Switch Scheduled event started");
				// This will be executed after 1 day
				Session new_session = sessionFactory.openSession();
				new_session.beginTransaction();
				Task task_to_check = getTaskByTaskID(taskID, new_session);
				User user = task_to_check.getTaskCreator();

				if (task_to_check.getTaskState().equals("Request")) { // Check if still in request mode.
					// If it is, we send a message to everyone to tell them nobody volunteered yet.
					String text_for_message = "The task: \"" + task_to_check.getRequiredTask() + "\"\nWas not volunteered to and 24 hours have passed.";
					List<User> community_list = new ArrayList<>();
					// GET LIST
					try {
						community_list = getCommunityUsers(user, new_session);
					} catch (Exception e) {
						e.printStackTrace();
					}
//					System.out.println("Starting loop");
					try {
						for (User user_to_send : community_list) { // Loop through entire community and send them the message.
							UserMessage usermessage_to_send = new UserMessage(text_for_message,user.getTeudatZehut(),user_to_send.getTeudatZehut(),"Community");
							System.out.println("sending message to user with TeudatZehut : " + user_to_send.getTeudatZehut());
							sendMessageToClient(usermessage_to_send, new_session);
						}

					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {

						new_session.getTransaction().commit();
						new_session.close();
					}
				}

//				System.out.println("Task not volunteered to on time");
				ScheduledFuture<?> scheduledFuture = scheduler.schedule(this,timeUnit,time);
				taskIDtoThread.get(taskID).setSecond(scheduledFuture);
			}
		};
		ScheduledFuture<?> scheduledFuture = scheduler.schedule(to_do, timeUnit, time); // ** Change here the time you want to give on a given task.
		taskIDtoThread.put(taskID, new Pair<>(scheduler,scheduledFuture));
	}
	private static void sendMessageToClient(UserMessage message, Session newSession) throws Exception {
		// --DEBUG
//		System.out.println("sendMessageToClient called");
		// ---
		String message_type = message.getMessage_type();
		String sender_zehut = message.getSender_zehut();
		String to_zehut = message.getTeudatZehut_to();
		String message_text = message.getMessage();
		if (message_type.isEmpty() || sender_zehut.isEmpty() || message_text.isEmpty() || to_zehut.isEmpty()) {
			return;
		} // Check if anything is empty first.

		// --DEBUG
//		System.out.print("Sender is:");
//		System.out.println(sender_zehut);
//		System.out.print("Reciever is:");
//		System.out.println(to_zehut);
		// ---

		User message_sender_user = getUserByTeudatZehut(sender_zehut, newSession);

		if (idToClient.containsKey(to_zehut)) { // User is logged in.
			// --DEBUG
//			System.out.println("User logged in");
			// ---
			ConnectionToClient Message_Reciever_Client = idToClient.get(to_zehut);
			List<Object> messageDetails = new ArrayList<>();
			messageDetails.add(message);
			messageDetails.add(message_sender_user.getUserName());
			Message_Reciever_Client.sendToClient(new Message("New Message", messageDetails)); // Send message as an object.
			newSession.remove(message);
			newSession.flush();
		} else { // Not connected. Save to DB.
			newSession.save(message);
			newSession.flush();

		}
	}

	private static void shutDown_pair(int taskID) {
		if (taskIDtoThread.containsKey(taskID)) {
			// Put the new pair in instead
			Pair<ScheduledExecutorService, ScheduledFuture> hashedPair = taskIDtoThread.get(taskID);

			// Shut down thread and cancel scheduled future
			hashedPair.getSecond().cancel(true);
			hashedPair.getFirst().shutdown();

			taskIDtoThread.remove(taskID);
		}
	}


	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Message message = (Message) msg;
		String request = message.getMessage();
		System.out.println("handleMessageFromClient - request is: " + request);
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();

            switch (request) {
                case "Login Request" -> {
                    String[] loginDetails = (String[]) message.getObject();
                    String teudatZehut = loginDetails[0];
                    String password = loginDetails[1];
                    System.out.println("teudatZehut : " + teudatZehut);
                    User user = getUserByTeudatZehut(teudatZehut, session);
                    boolean subscriberFound = false;
                    if (user != null && !idToClient.containsKey(teudatZehut) && !user.isLocked()) {
                        password = user.get_SHA_512_SecurePassword(password, user.getSalt());
                        if (password.equals(user.getPasswordHash())) {

                            // Bind client to id.
                            for (SubscribedClient subscriber : SubscribersList) {
                                if (subscriber.getClient() == client) {
                                    // Take the client from the signature and compare
                                    idToClient.put(teudatZehut, client);
                                    clientToId.put(client, teudatZehut);
                                    subscriberFound = true;
                                }
                            }
                            if (subscriberFound) { // We log in only after making sure the client is subscribed
                                message.setMessage("Login Succeed");
                                user.resetNumberOfLoginTries();
                                session.flush();
                                message.setUser(user);
                            } else { // Client wasn't subscribed. Shouldn't happen, debug this if happened.
                                try {
                                    message.setMessage("Login Failed: Something went wrong. You aren't connected?");
                                    throw new RuntimeException("You aren't connected");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println(password + " versus " + user.getPasswordHash());
                            user.incrementNumberOfLoginTries();

                            session.flush();
                            if (user.isLocked()) {
                                message.setMessage("Login Failed: Locked");
                            } else if (user.getnumberOfLoginTries() >= 6) {
                                if (!user.isLocked()) {
                                    user.setLocked(true);
                                    session.flush();
                                }
                                message.setMessage("Login Failed: Locked");

                                // Create a personal scheduler for the user if not already created
                                System.out.println("Scheduler Created");

                                // Schedule the task to unlock the user after 30 seconds
                                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                                scheduler.schedule(() -> {
                                    // Log thread information
                                    System.out.println("Scheduled task is running on thread: " + Thread.currentThread().getName());
                                    try {
                                        Session session2 = sessionFactory.openSession();
                                        session2.beginTransaction();
                                        user.resetNumberOfLoginTries();
                                        user.setLocked(false);
                                        session2.update(user);
                                        session2.flush();
                                        session2.getTransaction().commit();
                                        session2.close();
                                        System.out.println("30 seconds passed, user is unlocked");
                                        scheduler.shutdown();
                                    } catch (Exception e) {
                                        // Log and handle the exception
                                        System.err.println("Exception occurred in scheduled task:");
                                        e.printStackTrace();
                                    }
                                }, lockingTime, lockingTimeUnits);
                                System.out.println("After scheduler scheduled");

                                // Additional logging to check if the main thread is still active
                                System.out.println("Main thread is active: " + Thread.currentThread().getName() + " " + Thread.currentThread().getState());
                            } else {
                                message.setMessage("Login Failed: Wrong Password");
                            }
                        }
                    } else {
                        if (user == null) {
                            message.setMessage("Login Failed: No Such User Exists");
                        } else if (user.isLocked()) {
                            message.setMessage("Login Failed: Locked");
                        } else {
                            message.setMessage("Login Failed: Someone is already connected to the given ID");
                        }
                    }
                    System.out.println("message sent: " + message.getMessage());
                    client.sendToClient(message);
                }
                case "Log Out" -> {
                    System.out.println("In Log Out");
                    User user = (User) message.getObject();

                    System.out.print("The ID logging out is:");
                    System.out.println(user.getId());


					String teudatZehut = user.getTeudatZehut();
					// remove from both hash maps

					clientToId.remove(idToClient.get(teudatZehut)); // Remove from clientToId too.
					idToClient.remove(teudatZehut); // Remove from idToClient the client.

					System.out.println("Successfully logged out");
					message.setMessage("log out");
					client.sendToClient(message);



                }
                case "Forgot Password Request" -> {
                    String[] forgotDetails = (String[]) message.getObject();
                    String teudatZehut = forgotDetails[0];
                    String selectedQuestion = forgotDetails[1];
                    String answer = forgotDetails[2];
                    User user = getUserByTeudatZehut(teudatZehut, session);

                    if (user != null && user.getSecretQuestion().equals(selectedQuestion) && user.getSecretQuestionAnswer().equals(answer)) {
                        message.setMessage("Forgot Password: Match");
                    } else {
                        message.setMessage("Forgot Password: Fail");
                    }
                    System.out.println("message sent: " + message.getMessage());
                    client.sendToClient(message);
                }
                case "New Password Request" -> {
                    String[] details = (String[]) message.getObject();
                    String teudatZehut = details[0];
                    String newPassword = details[1];
                    User user = getUserByTeudatZehut(teudatZehut, session);
                    if (user != null) {
                        System.out.println("user!=null");
                        user.setPassword(newPassword);
                        session.flush();
                        String hashedPassword = user.get_SHA_512_SecurePassword(newPassword, user.getSalt());
                        if (user.getPasswordHash().equals(hashedPassword)) {
                            System.out.println("user.getPasswordHash().equals(newPassword)");
                            message.setMessage("Password Change Succeed");
                        }
                    } else {
                        message.setMessage("Password Change Failed");
                    }
                    System.out.println("Forgot Password Request: Message Sent:" + message.getMessage());
                    client.sendToClient(message);
                }
                case "Task not completed on time" -> {
                    // Task id is in message.
                    int taskID = message.getTaskID();
                    createTaskNotCompleteThread(taskID, (UserMessage) message.getObject(), toCompleteTime, sessionFactory, toCompleteTimeUnits);
                }
                case "Emergency Request" -> {
                    User user;
                    Emergency newEmergency = null;
                    // button was pressed when logged in
                    if (message.getUser() != null) {

                        newEmergency = (Emergency) message.getObject();
                        user = newEmergency.getUser(); // or message.getUser()
                        session.save(newEmergency);
                        session.flush();
                        session.getTransaction().commit();
                        message.setMessage("Emergency Call Succeeded");
                    }
                    // button pressed from Log in menu
                    else {
                        String phoneNumber = (String) message.getObject();
                        user = getUserByPhoneNumber(phoneNumber, session);

                        //found user in database with matching phone number
                        if (user != null) {
                            newEmergency = new Emergency(user, LocalDateTime.now());
                            session.save(newEmergency);
                            session.flush();
                            session.getTransaction().commit();
                            message.setMessage("Emergency Call Succeeded");
                        } else
                        // no user with matching phone number, send error message to user
                        {
                            message.setMessage("Emergency Call Failed");
                        }
                    }
                    System.out.println("message sent: " + message.getMessage());
                    client.sendToClient(message);
                    if (message.getMessage().equals("Emergency Call Succeeded")) {
                        Message update = new Message("update histogram", newEmergency, user);
                        sendToAllClients(update);
                    }
                }

                case "create task" -> {
                    Task testTask = (Task) message.getObject();
                    session.save(testTask);
                    session.flush();
                    session.getTransaction().commit();
                    sendToAllClients(message);
                }


				// used for CommunityInformationController (and ViewEmergencyCalls for now)
                case "get tasks" -> {
                    User u1 = message.getUser();
                    List<Task> tasks = getCommunityTasks(u1, null, session);
                    // fetches all Tasks in database from the same community

                    message.setObject(tasks);
                    client.sendToClient(message);
                }

				// used for ViewTasksController
                case "get open tasks" -> {
                    User u1 = message.getUser();
                    List<Task> tasks = getOpenTasks(u1, session);
                    // fetches all OPEN (AND APPROVED) TASKS in database from the same community
                    message.setObject(tasks);
                    client.sendToClient(message);
                }

				// retrieve community users for CommunityInformationController
                case "get users" -> {
                    User u1 = message.getUser();
                    List<User> Users = getCommunityUsers(u1, session);
                    message.setObject(Users);
                    client.sendToClient(message);
                }


                //called when ApproveRequestController is loaded
                case "get awaiting approval requests" -> {
                    User currentUser = message.getUser();
                    List<Task> tasks = getWaitingTasks(currentUser, session);
                    message.setObject(tasks);
                    client.sendToClient(message);

                }

                case "emergency everything" -> {
                    List<LocalDateTime> dates = (List<LocalDateTime>) message.getObject();
                    List<Emergency> emergencies = getAllEmergencyCases(null, session);
                    message.setMessage("emergency histogram");
                    message.setObject(emergencies);
                    client.sendToClient(message);
                }
                case "emergency my community all dates" -> {
                    User u1 = (User) message.getUser();
                    List<Emergency> emergencies = getCommunityEmergencies(u1, null, session);
                    message.setMessage("emergency histogram");
                    message.setObject(emergencies);
                    client.sendToClient(message);
                }
                case "emergency all community specific dates" -> {
                    List<LocalDateTime> dates = (List<LocalDateTime>) message.getObject();
                    List<Emergency> emergencies = getAllEmergencyCases(dates, session);
                    message.setMessage("emergency histogram");
                    message.setObject(emergencies);
                    client.sendToClient(message);
                }
                case "emergency my community specific dates" -> {
                    User u1 = (User) message.getUser();
                    List<LocalDateTime> dates = (List<LocalDateTime>) message.getObject();
                    List<Emergency> emergencies = getCommunityEmergencies(u1, dates, session);
                    message.setMessage("emergency histogram");
                    message.setObject(emergencies);
                    client.sendToClient(message);
                }


                case "Get Data" -> {
                    Task task = session.get(Task.class, message.getTaskID());
                    message.setObject(task);
                    client.sendToClient(message);

                }

                // withdrawing is being handled separately because you could change a state to be "Request" in two ways.
                case "Withdraw from task" -> {
                    Task task = (Task) message.getObject();
                    createNoVolunteerThread(task.getId(), noVolunteerTime, sessionFactory, noVolunteerTimeUnits);

                    session.update(task);
                    session.flush();
                    session.getTransaction().commit();
                    sendToAllClients(message);
                }
                case "Update task", "Complete the task" -> {
                    Task task;
                    if (request.equals(("Update task"))) {
                        task = (Task) message.getObject();
                    } else {
                        task = getTaskByTaskID(message.getTaskID(), session);
                        task.setTaskState("Complete");
                        task.setCompletionTime(LocalDateTime.now());

						User user = message.getUser();
						String dialog_result = (String) message.getObject();
						String to_manager_text = "The task: \"" + task.getRequiredTask() + "\"\nWhich was created by: \"" + task.getTaskCreator().getUserName() + "\"\nAnd volunteered to by: \"" + user.getUserName() + "\"\nHas been marked complete with the message:\n\"" + dialog_result + "\"";
						UserMessage to_manager_message = new UserMessage(to_manager_text , user.getTeudatZehut(), user.getCommunity().getCommunityManager().getTeudatZehut(), "Normal");
						message.setObject(task);

						sendMessageToClient(to_manager_message, session);
					}
                    session.update(task);
                    session.flush();
                    session.getTransaction().commit();

                    switch (task.getTaskState()) {
                        case "Request" -> message.setMessage("Publish approved task");
                        case "In Progress" -> message.setMessage("Volunteer to task");
                        case "Denied" -> message.setMessage("Request denied");
                        default -> message.setMessage("Complete task");
                    }
                    sendToAllClients(message);
                    switch (task.getTaskState()) {
                        case "Request":
                            int taskID_original = ((Task) (message.getObject())).getId();
                            shutDown_pair(taskID_original);
                            createNoVolunteerThread(taskID_original, noVolunteerTime, sessionFactory, noVolunteerTimeUnits);
                            break;

                    }

                }
                case "Send message" -> {
                    // Hopefully, message has a UserMessage object in it.
                    try {
                        sendMessageToClient((UserMessage) message.getObject(), session);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "Get user's messages" -> { // Get by query a list of all the user messages for user
                    String teudatZehut = (String) message.getObject();
                    List<UserMessage> userMessageList = getAllUsersMessagesByTeudatZehut(teudatZehut, session);
                    System.out.print("Size of messageList is:");
                    System.out.println(userMessageList.size());

                    System.out.print("The value of !isEmpty is: ");
                    System.out.println(!userMessageList.isEmpty());

                    if (!userMessageList.isEmpty()) {
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
                case "add client" -> {
                    SubscribedClient connection = new SubscribedClient(client);
                    SubscribersList.add(connection);
//				message.setMessage("client added successfully");
//				client.sendToClient(message);
                }
                default -> {
                    message.setMessage(request);
                    sendToAllClients(message);
                }
            }
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception exception) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			System.err.println("An error occurred, changes have been rolled back.");
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