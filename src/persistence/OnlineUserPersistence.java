package persistence;

import java.io.IOException;

import records.UserInfo;

public interface OnlineUserPersistence {
	public boolean loginUser(UserInfo user) throws IOException;
	public void logoutUser(UserInfo user) throws IOException;
	public boolean isLoggedIn(UserInfo user);
	
	public void clearServerCache() throws IOException;
}
