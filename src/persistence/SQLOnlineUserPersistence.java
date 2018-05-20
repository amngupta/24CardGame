package persistence;

import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

import records.OnlineUser;
import records.UserInfo;

public class SQLOnlineUserPersistence implements OnlineUserPersistence {

	private Map<String, OnlineUser> onlineUserMap;

	
	public SQLOnlineUserPersistence(Connection conn) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean loginUser(UserInfo user) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void logoutUser(UserInfo user) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLoggedIn(UserInfo user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clearFile() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
