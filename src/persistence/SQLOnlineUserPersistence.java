package persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import records.OnlineUser;
import records.UserInfo;

public class SQLOnlineUserPersistence implements OnlineUserPersistence {

	private Map<String, OnlineUser> onlineUserMap;
	private Connection conn;

	public SQLOnlineUserPersistence(Connection conn) {
		this.conn = conn;
		onlineUserMap = new HashMap<>();
	}

	@Override
	public boolean loginUser(UserInfo user) throws IOException {
		if (!onlineUserMap.containsKey(user.getUsername())){
			System.out.println("Adding user to login map");
			OnlineUser newOnlineUser = new OnlineUser(user.getUsername());			
			try {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO onlineusers (username, loginTime, is_logged) VALUES (?, ?, ?)");
				stmt.setString(1, user.getUsername());
				stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				stmt.setBoolean(3,  true);
				stmt.execute();
				onlineUserMap.put(user.getUsername(), newOnlineUser);
				System.out.println("Record created");
				return true;
			} catch (SQLException | IllegalArgumentException e) {
				System.err.println("Error inserting record: "+e);
				e.printStackTrace();
			}			
		}
		return false;
	}

	@Override
	public void logoutUser(UserInfo user) throws IOException {
		if (onlineUserMap.containsKey(user.getUsername()))
		{
			try {
				PreparedStatement stmt = conn.prepareStatement("DELETE from onlineusers WHERE username = ? AND is_logged = TRUE");
				stmt.setString(1, user.getUsername());
				int rows = stmt.executeUpdate();
				if(rows > 0) {
					System.out.println("Information of "+user.getUsername()+" updated");
				} else {
					System.out.println(user.getUsername()+" not found!");
				}
			} catch (SQLException e) {
				System.err.println("Error reading record: "+e);
				throw new IOException("Unable to logout");
			}
			onlineUserMap.remove(user.getUsername());
		}
		else 
		{
			throw new IOException("Unable to logout");
		}
	}

	@Override
	public boolean isLoggedIn(UserInfo user) {
		return onlineUserMap.containsKey(user.getUsername());
	}

	@Override
	public void clearServerCache() throws IOException {
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE from onlineusers WHERE is_logged = TRUE");
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.println("Cleaned DB");
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
			throw new IOException("Unable to logout");
		}	
		finally {
			onlineUserMap.clear();
		}
	}

}
