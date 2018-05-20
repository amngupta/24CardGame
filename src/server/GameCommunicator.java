package server;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import games.Game;
import records.Messages;
import records.UserInfo;
import records.Messages.MessageType;
import server.JMSHelper.JMSDestinationType;

public class GameCommunicator implements MessageListener{
	
	private Map<Integer, GameSession> gameSessionsMap;
	private Persistence persistence;
	private Game game;
	private JMSHelper jmsHelper;
	private boolean newGame;
	GameCommunicator(Persistence persistence, JMSHelper jmsHelper, Game game)
	{
		this.persistence = persistence;
		this.jmsHelper = jmsHelper;
		this.game = game;
		try {
			jmsHelper.setListener(JMSDestinationType.QUEUE, this, null);
			System.out.println("Set as listener");
		} catch (JMSException e) {
			e.printStackTrace();
		}
		this.newGame = true;
		gameSessionsMap = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {    
        Messages<String> m;
		try {
			m = (Messages<String>) ((ObjectMessage)message).getObject();
        	UserInfo user = persistence.getUserInfoPersistence().searchUser(m.getUsername());
			if (m.getMessageType() == MessageType.START_GAME)
	        {
				if (newGame)
				{
					this.prepareGame();
					newGame = false;
				}
				this.gameSessionsMap.get(1).addUserToSession(user);
	        }	
			if (m.getMessageType() == MessageType.ANSWER)
			{
				Answer ans = new Answer(m.getMessage());
				gameSessionsMap.get(1).reviewSubmittedAnswer(user, ans);
			}
			if (m.getMessageType() == MessageType.NOTIFICATION)
			{
				
			}
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void prepareGame() {
		// TODO Auto-generated method stub
		GameSession gm = new GameSession(1, persistence, this, game);
		gm.readyToStart();
		this.gameSessionsMap.put(1, gm);
	}
	
	public <T> void informClients(Messages<T> m)
	{
		try {
			jmsHelper.sendMessage(m, JMSDestinationType.TOPIC);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void gameEnded(int id) {
		this.newGame = true;
		this.gameSessionsMap.remove(id);
	}
}
