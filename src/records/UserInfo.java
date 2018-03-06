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
	
	public UserInfo(String username, String password) {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.password = password; 
		userStats = new UserStatistics(username);
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
