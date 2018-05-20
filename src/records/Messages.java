package records;

import java.io.Serializable;

public class Messages<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4368437295390083329L;

	public enum MessageType {
		START_GAME, 
		GAME_PLAYERS,
		ANSWER, 
		NOTIFICATION
	}
	
	private MessageType messageType;
	private String username;
	private T message;
	
	public Messages(MessageType messageType, T message)
	{
		this.messageType = messageType;
		this.message = message;
	}

	public T getMessage() {
		return message;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public MessageType getMessageType()
	{
		return messageType;
	}

	public String getUsername() {
		return username;
	}

}


