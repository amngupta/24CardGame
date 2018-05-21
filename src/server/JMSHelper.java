package server;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import records.Messages;

public class JMSHelper {
	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 3700;
	public enum JMSDestinationType {
		QUEUE,
		TOPIC
	}
	
	private String JMS_CONNECTION_FACTORY;
	private String JMS_QUEUE;
	private String JMS_TOPIC;
	
	private String host; 
	private int port; 
	private ConnectionFactory connectionFactory;
	private InitialContext jndiContext;

	private Queue queue;
	private Topic topic;
	private Connection connection;
	private Session session;
	private MessageConsumer receiver;
	private MessageProducer sender;

	public JMSHelper() throws NamingException, JMSException {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}
	
	public JMSHelper(String host, int port) throws NamingException, JMSException {
		this.host = host;
		this.port = port;
		
		JMS_CONNECTION_FACTORY = "jms/JPoker24GameConnectionFactory";
		JMS_QUEUE = "jms/JPoker24GameQueue";
		JMS_TOPIC = "jms/JPoker24GameTopic";
		
		// Access JNDI
		createJNDIContext();
		System.out.println("Context Created");

		// Lookup JMS resources
		lookupConnectionFactory();
		System.out.println("Connection Factory Found");

		lookupQueue();
		lookupTopic();
		// Create connection->session->sender
		createConnection();
	}
	
	private void createJNDIContext() throws NamingException {
		System.setProperty("org.omg.CORBA.ORBInitialHost", host);
		System.setProperty("org.omg.CORBA.ORBInitialPort", Integer.toString(port));
		try {
			jndiContext = new InitialContext();
		} catch (NamingException e) {
			System.err.println("Could not create JNDI API context: " + e);
			throw e;
		}
	}
	
	private void lookupConnectionFactory() throws NamingException {

		try {
			connectionFactory = (ConnectionFactory)jndiContext.lookup(JMS_CONNECTION_FACTORY);
		} catch (NamingException e) {
			System.err.println("JNDI API JMS connection factory lookup failed: " + e);
			throw e;
		}
	}
	
	private void lookupQueue() throws NamingException {

		try {
			queue = (Queue)jndiContext.lookup(JMS_QUEUE);
		} catch (NamingException e) {
			System.err.println("JNDI API JMS queue lookup failed: " + e);
			throw e;
		}
	}
	
	private void lookupTopic() throws NamingException {

		try {
			topic = (Topic)jndiContext.lookup(JMS_TOPIC);
		} catch (NamingException e) {
			System.err.println("JNDI API JMS topic lookup failed: " + e);
			throw e;
		}
	}
	
	private void createConnection() throws JMSException {
		try {
			connection = connectionFactory.createConnection();
			connection.start();
		} catch (JMSException e) {
			System.err.println("Failed to create connection to JMS provider: " + e);
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	public void sendMessage(Messages message, JMSDestinationType type) throws JMSException
	{
		Message m;
	
		m = this.createMessage(message);
		this.sendMessage(type, m);
	}
	
	public void sendMessage(JMSDestinationType destType, Message jmsMessage) throws JMSException {
		createSession();
		if (destType == JMSDestinationType.QUEUE)
			createSender(queue);
		else if (destType == JMSDestinationType.TOPIC)
			createSender(topic);
		try {
	        sender.send(jmsMessage);
	    } catch(JMSException e) {
	        System.err.println("Failed to boardcast message "+e);
	        throw e;
	    }
		System.out.println("Sent message of type " + destType);
	}
	
	private void createSession() throws JMSException {
		try {
			if (session == null)
			{
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			}
		} catch (JMSException e) {
			System.err.println("Failed to create session: " + e);
			throw e;
		}
	}

	public void setListener(JMSDestinationType dest, MessageListener listener, String selector) throws JMSException {
		// TODO Auto-generated method stub
		createSession();
		if (dest == JMSDestinationType.QUEUE)
			createReceiver(queue, selector);
		else if (dest == JMSDestinationType.TOPIC)
			createReceiver(topic, selector);
		
		receiver.setMessageListener(listener);
	}
	
	// Was: QueueReceiver
	private void createReceiver(Destination dest, String selector) throws JMSException {
		try {
			if (selector == null || selector.isEmpty())
				receiver = session.createConsumer(dest);
			else 
				receiver = session.createConsumer(dest, selector);
		} catch (JMSException e) {
			System.err.println("Failed to create session: " + e);
			throw e;
		}
	}
	
	public void createSender(Destination dest) throws JMSException {
	    try {
	        sender = session.createProducer(dest);
	    } catch (JMSException e) {
	        System.err.println("Failed sending to queue: " + e);
	        throw e;
	    }	
	}	
	
	public ObjectMessage createMessage(Serializable obj) throws JMSException {
	    try {
	    	createSession();
	        return session.createObjectMessage(obj);
	    } catch (JMSException e) {
	        System.err.println("Error preparing message: " + e);
	        throw e;
	    }
	}
	
	public void close() {
		if(connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}


}
