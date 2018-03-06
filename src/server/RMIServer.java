package server;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import persistence.TUserInfoPersistence;
import persistence.OnlineUserPersistence;
import persistence.TOnlineUserPersistence;
import persistence.UserInfoPersistence;
import records.UserInfo;
import records.UserStatistics;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

	private UserInfoPersistence userInfoPersistence;
	private OnlineUserPersistence onlineUserPersistence;
	
	protected RMIServer() throws RemoteException {
		super();
		userInfoPersistence = new TUserInfoPersistence("UserInfo.txt");
		onlineUserPersistence = new TOnlineUserPersistence("OnlineUser.txt");
		userInfoPersistence.generateUserList();
		try {
			onlineUserPersistence.clearFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		try {
			
			RMIServer app = new RMIServer();
			System.setSecurityManager(new SecurityManager());
			Naming.rebind("24Cards", app);
			System.out.println("Service registered");

//			int count = app.count("The quick brown fox jumps over a lazy dog");
//			System.out.println("There are "+count+" words");
		} catch(Exception e) {
			System.err.println("Exception thrown: "+e);
		}
	}
	
	@Override
	public void createUser(UserInfo user) throws RemoteException {
		// TODO Auto-generated method stub
		this.userInfoPersistence.createUser(user);
	}

	@Override
	public UserInfo checkUser(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		UserInfo user = this.userInfoPersistence.searchUser(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}

	@Override
	public List<UserStatistics> getUserStats() throws RemoteException {
		// TODO Auto-generated method stub
		List<UserStatistics> statsList = this.userInfoPersistence.getUserStatsList();
		statsList.sort(UserStatistics.AvgTimeComparator);
		// TODO Maybe update rank here itself?
		return statsList;
	}

	@Override
	public boolean loginUser(UserInfo user) throws RemoteException, IOException {
		return this.onlineUserPersistence.loginUser(user);
	}

	@Override
	public void logoutUser(UserInfo user) throws RemoteException, IOException {
		this.onlineUserPersistence.logoutUser(user);
	}

}
