package client;

import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import games.Cards;
import records.Messages;
import records.Messages.MessageType;
import records.UserInfo;
import server.JMSHelper;
import server.JMSHelper.JMSDestinationType;
import server.PropertiesLoader;

public class GameClientStub implements MessageListener{

	private UserInfo currentUser;
	private JMSHelper jmsHelper;
	private CardGameClient cardGameClient;
	public GameClientStub(UserInfo currentUser) {
		PropertiesLoader prop = new PropertiesLoader();
		prop.loadProperties();
		String host = prop.getProperty("JMS_HOST");
		Integer port = Integer.parseInt(prop.getProperty("JMS_PORT"));
		this.currentUser = currentUser;
		try {
			jmsHelper = new JMSHelper(host, port);
			jmsHelper.setListener(JMSDestinationType.TOPIC, this, null);
		} catch (JMSException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setCardGameClient(CardGameClient cgc)
	{
		this.cardGameClient = cgc;
	}
	
	public void sendNewGameRequest() {
		try {

			Messages<String> req = new Messages<String>(MessageType.START_GAME, "");
			req.setUsername(currentUser.getUsername());
			Message m = jmsHelper.createMessage(req);
			jmsHelper.sendMessage(JMSDestinationType.QUEUE, m);
			System.out.println("Message Sent");
		}
		catch(JMSException e)
		{
			e.printStackTrace();
		}
	}

	public void sendAnswer(String answer) {
		try {

			Messages<String> req = new Messages<>(MessageType.ANSWER, answer);
			req.setUsername(currentUser.getUsername());
			jmsHelper.sendMessage(req, JMSDestinationType.QUEUE);
			System.out.println("Answer Sent");
		}
		catch(JMSException e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println(message.toString());
		Messages<Object> m;
		try {
			m = (Messages<Object>) ((ObjectMessage)message).getObject();
			if (m.getMessageType() == MessageType.START_GAME)
	        {
	        	Messages<List<Cards>> cards = (Messages<List<Cards>>) ((ObjectMessage)message).getObject();
//	        	send to game client
	        	cardGameClient.updateLayout(cards.getMessage());
	        	System.out.println("Starting game...");
	        }	
			if (m.getMessageType() == MessageType.GAME_PLAYERS)
	        {
//	        	Messages<List<Cards>> cards = (Messages<List<Cards>>) ((ObjectMessage)message).getObject();
//	        	send to game client
				Messages<Map<String, UserInfo>> usersMessage = (Messages<Map<String, UserInfo>>) ((ObjectMessage)message).getObject();
				cardGameClient.addOpponentInfo(usersMessage.getMessage());
				System.out.println("Got game opponnets...");
	        }
			if (m.getMessageType() == MessageType.NOTIFICATION)
			{
	        	Messages<String> notification = (Messages<String>) ((ObjectMessage)message).getObject();
	        	System.out.println("Recevied notification..." + notification.getMessage());
	        	JOptionPane.showMessageDialog(new JFrame(), notification.getMessage(), "Dialog",
			            JOptionPane.ERROR_MESSAGE);
	        	cardGameClient.activateGameEnd("", "");
			}
			if (m.getMessageType() == MessageType.ANSWER)
			{
				Messages<String> win = (Messages<String>) ((ObjectMessage)message).getObject();
				String answer = win.getMessage();
				String winningUser = win.getUsername();
				cardGameClient.activateGameEnd(answer, winningUser);
//						new Messages<String>(MessageType.ANSWER, String.format("%s,%s", user.getUsername(), answer.getAnswer()));
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
