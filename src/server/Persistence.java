package server;

import persistence.OnlineUserPersistence;
import persistence.TOnlineUserPersistence;
import persistence.TUserInfoPersistence;
import persistence.UserInfoPersistence;

public class Persistence {
	private UserInfoPersistence userInfoPersistence;
	private OnlineUserPersistence onlineUserPersistence;

	Persistence(){
		userInfoPersistence = new TUserInfoPersistence("UserInfo.txt");
		onlineUserPersistence = new TOnlineUserPersistence("OnlineUser.txt");
	}
	
	UserInfoPersistence getUserInfoPersistence() {
		return userInfoPersistence;
	}
	
	OnlineUserPersistence getOnlineUserPersistence() {
		return onlineUserPersistence;
	}
	
}
