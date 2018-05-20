package server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import records.UserInfo;
import records.UserStatistics;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Persistence persistence;
	
	protected RMIServer() throws RemoteException {
		super();
		
	}
	
	public void setPersistence(Persistence persistence) {
		this.persistence = persistence;
		persistence.getUserInfoPersistence().generateUserList();
		try {
			persistence.getOnlineUserPersistence().clearFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createUser(UserInfo user) throws RemoteException {
		// TODO Auto-generated method stub
		persistence.getUserInfoPersistence().createUser(user);
	}

	@Override
	public UserInfo checkUser(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		UserInfo user = persistence.getUserInfoPersistence().searchUser(username);
		if (user != null) {
			if (user.getPassword().equals(password))
				return user;
			else 
				return null;
		}
		return null;
	}

	@Override
	public List<UserStatistics> getUserStats() throws RemoteException {
		// TODO Auto-generated method stub
		List<UserStatistics> statsList = persistence.getUserInfoPersistence().getUserStatsList();
		statsList.sort(UserStatistics.AvgTimeComparator);
		// TODO Maybe update rank here itself?
		return statsList;
	}

	@Override
	public boolean loginUser(UserInfo user) throws RemoteException, IOException {
		return persistence.getOnlineUserPersistence().loginUser(user);
	}

	@Override
	public void logoutUser(UserInfo user) throws RemoteException, IOException {
		persistence.getOnlineUserPersistence().logoutUser(user);
	}

	@Override
	public UserInfo getUserInfo(String username) throws RemoteException, IOException{
		return persistence.getUserInfoPersistence().searchUser(username);
	}

}
