package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import persistence.OnlineUserPersistence;
import persistence.SQLOnlineUserPersistence;
import persistence.SQLUserInfoPersistence;
import persistence.UserInfoPersistence;

public class Persistence {
	private UserInfoPersistence userInfoPersistence;
	private OnlineUserPersistence onlineUserPersistence;
	private static final String DB_HOST = "localhost";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "rootroot";
	private static final String DB_NAME = "agupta2";
	

	private Connection conn;
	
	Persistence() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://"+DB_HOST+
				"/"+DB_NAME+
				"?user="+DB_USER+
				"&password="+DB_PASS);
		System.out.println("Database connection successful.");
		userInfoPersistence = new SQLUserInfoPersistence(conn);
		onlineUserPersistence = new SQLOnlineUserPersistence(conn);
//		onlineUserPersistence = new TOnlineUserPersistence("OnlineUser.txt");
	}
	
	UserInfoPersistence getUserInfoPersistence() {
		return userInfoPersistence;
	}
	
	OnlineUserPersistence getOnlineUserPersistence() {
		return onlineUserPersistence;
	}
	
}
