package server;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import games.CardGame;
import games.Cards;
import games.Game;
import records.Messages;
import records.Messages.MessageType;
import records.UserInfo;
import server.JMSHelper.JMSDestinationType;

@SuppressWarnings("unchecked")
public class GameSession{

	private Map<String, UserInfo> sessionUsers;
	private Game game;
	private OffsetDateTime startTime;
	private Persistence persistence;
	private JMSHelper jmsInstance;
	private Thread gameThread;
	public boolean gameInSession;
	private boolean correctAnswer;
	private static final int TIME_WINDOW  = 10000;
	

	public GameSession(Persistence p, JMSHelper jmsInstance, Game game){
		this.persistence = p;
		this.jmsInstance = jmsInstance;
		sessionUsers = new ConcurrentHashMap<>();
		this.game = game;
		gameInSession = false;
	}
	
	
	public void addUserToSession(UserInfo user){
		if (persistence.getOnlineUserPersistence().isLoggedIn(user))
		{
			sessionUsers.putIfAbsent(user.getUsername(), user);
	    	System.out.println("Added user to existing session" + user.getUsername());
		}
	}
	
	public Map<String, UserInfo> getSessionUsers() {
		return sessionUsers; 
	}
	
	public void reviewSubmittedAnswer(UserInfo user, Answer answer) {
		if (game.checkAnswer(answer)) {
			// Inform Clients
			user.getUserStats().incrementWins();
			System.out.println("Correct Answer");
			Messages<String> win = new Messages<String>(MessageType.ANSWER, answer.getAnswer());
			win.setUsername(user.getUsername());
			this.informClients(win);
			correctAnswer = true;
			sessionUsers.forEach((key, value)-> {
				float additionalTime = Duration.between(this.startTime, answer.getAnswerTime()).toMillis();
				value.getUserStats().incrementGames();
				value.getUserStats().updateTotalTimePlayed(additionalTime);
				try {
					persistence.getUserInfoPersistence().updateUser(value);
				}
				catch(IOException e)
				{
					System.out.println("Unable to update user");
					e.printStackTrace();
				}
			});
			this.endGame();
		}
		else 
		{
			// Inform user wrong answer
			System.out.println("Incorrect Answer");
		}
		
//		Update to persistence
		
	}

	public void readyToStart() {
		game.prepareGameInstance();
		GameLobbyThread gi = new GameLobbyThread();
		gameThread = new Thread(gi);
		gameThread.start();
	}

	protected <T> void informClients(Messages<T> m)
	{
		try {
			jmsInstance.sendMessage(m, JMSDestinationType.TOPIC);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class GameLobbyThread implements Runnable {
		@Override
		public void run() {
			OffsetDateTime firstJoinTime = null;
			boolean setFirstJoin = false;
			System.out.println("Thread running...");
			while (true)
			{
				if (sessionUsers.size() == 1)
				{
					firstJoinTime = OffsetDateTime.now();
					setFirstJoin = true;
					System.out.println("Added first user");
				}
				if (setFirstJoin)
				{	
					if (Duration.between(firstJoinTime, OffsetDateTime.now()).toMillis() >= TIME_WINDOW)
						break;
				}
				if (sessionUsers.size() == 4) {
					break;
				}
			}
			System.out.println("End of join window");
			if (sessionUsers.size() > 1)
			{
				startGame();
			}
			else 
			{
				cancelGame("No other players found");
			}
		}
	}

	public void cancelGame(String reason) {
		sessionUsers.clear();
		Messages<String> notification = new Messages<>(MessageType.NOTIFICATION,  reason);
		informClients(notification);
		System.out.println("Game Cancelled");
		gameInSession = false;
	}

	public void startGame() {
		startTime = OffsetDateTime.now();
		try {
			Messages<Map<String, UserInfo>> users = new Messages<>(MessageType.GAME_PLAYERS, sessionUsers);
			informClients(users);
//			TODO	Fix the getSelectedCards stuff
			Messages<List<Cards>> cards = new Messages<>(MessageType.START_GAME,  ((CardGame) game).getSelectedCards());
			informClients(cards);
			System.out.println("Game Problem Sent");
			gameInSession = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void endGame() {
		// TODO Auto-generated method stub
//		if (correctAnswer)
//			cancelGame("Correct Answer");
		
	}
}
