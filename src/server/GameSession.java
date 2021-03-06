package server;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import games.CardGame;
import games.Cards;
import games.Game;
import records.Messages;
import records.Messages.MessageType;
import records.UserInfo;

public class GameSession{
	private Map<String, UserInfo> sessionUsers;
	private Game game;
	private OffsetDateTime startTime;
	private Persistence persistence;
	private GameCommunicator gameComm;
	private Thread gameWaitingThread;
	
	public boolean gameInSession;
	private int id;
	private static final int TIME_WINDOW  = 10000;
	
	public GameSession(int id, Persistence p, GameCommunicator gameCommunicator, Game game){
		this.id = id;
		this.persistence = p;
		this.gameComm = gameCommunicator;
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
			System.out.println(user.getUsername());
			System.out.println(user.getUserStats().toString());
			user.getUserStats().incrementWins();
			System.out.println("Correct Answer");
			Messages<String> win = new Messages<String>(MessageType.ANSWER, answer.getAnswer());
			win.setUsername(user.getUsername());
			gameComm.informClients(win);
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
			persistence.getUserInfoPersistence().recomputeRankings();
			this.endGame();
		}
		else 
		{
			System.out.println("Incorrect Answer");
		}		
	}

	public void readyToStart() {
		game.prepareGameInstance();
		GameLobbyThread gi = new GameLobbyThread();
		gameWaitingThread = new Thread(gi);
		gameWaitingThread.start();
	}
	
	private class GameLobbyThread implements Runnable {
		@Override
		public void run() {
			OffsetDateTime firstJoinTime = null;
			boolean setFirstJoin = false;
			System.out.println("Thread running...");
			while (true)
			{
				if (sessionUsers.size() > 0)
				{
					if (setFirstJoin)
					{	
						if (Duration.between(firstJoinTime, OffsetDateTime.now()).toMillis() >= TIME_WINDOW)
							break;
					}
					else {
						firstJoinTime = OffsetDateTime.now();
						setFirstJoin = true;
						System.out.println("Added first user");						
					}
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
		Messages<String> notification = new Messages<>(MessageType.NOTIFICATION,  reason);
		gameComm.informClients(notification);
		System.out.println("Game Cancelled");
		gameInSession = false;
		this.endGame();
	}

	public void startGame() {
		startTime = OffsetDateTime.now();
		try {
			Messages<Map<String, UserInfo>> users = new Messages<>(MessageType.GAME_PLAYERS, sessionUsers);
			gameComm.informClients(users);
			Messages<List<Cards>> cards = new Messages<>(MessageType.START_GAME,  ((CardGame) game).getSelectedCards());
			gameComm.informClients(cards);
			System.out.println("Game Problem Sent");
			gameInSession = true;
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void endGame() {
		sessionUsers.clear();
		gameComm.gameEnded(id);
	}
}
