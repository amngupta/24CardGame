package server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import records.UserInfo;
import records.UserStatistics;

public interface RMIServerInterface  extends Remote
{
	public void createUser(UserInfo user) throws RemoteException;
	
	public boolean loginUser(UserInfo user) throws RemoteException, IOException;
	
	public UserInfo checkUser(String username, String password) throws RemoteException;

	public List<UserStatistics> getUserStats() throws RemoteException;

	public void logoutUser(UserInfo currentUser) throws RemoteException, IOException;
	
}