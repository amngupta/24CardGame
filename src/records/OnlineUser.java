package records;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class OnlineUser implements Serializable {

	private static final long serialVersionUID = 8221477771824614355L;
	private String username;
	private OffsetDateTime loginTime;
	
	public OnlineUser(String username)
	{
		this.username = username;
		this.loginTime = OffsetDateTime.now();
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public OffsetDateTime getLoginTime() {
		return this.loginTime;
	}
	
}
