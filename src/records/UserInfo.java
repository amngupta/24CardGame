package records;

import java.io.Serializable;

public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3469190900572939527L;
	
	private String username; 
	private String password;
	private UserStatistics userStats; 
	
	
	public UserInfo(String username, String password)
	{
		this.username = username;
		this.password = password; 
		this.userStats = new UserStatistics(username, 0, 0, 0);
	}
	
	
	public UserInfo(String username, String password, UserStatistics us) {
		this.username = username;
		this.password = password; 
		this.userStats = us;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserStatistics getUserStats() {
		return this.userStats;
	}
	
}
