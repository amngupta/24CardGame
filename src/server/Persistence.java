package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import persistence.OnlineUserPersistence;
import persistence.SQLOnlineUserPersistence;
import persistence.SQLUserInfoPersistence;
import persistence.UserInfoPersistence;

public class Persistence {
	private UserInfoPersistence userInfoPersistence;
	private OnlineUserPersistence onlineUserPersistence;


	private Connection conn;
	
	Persistence(Properties properties) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String DB_HOST = properties.getProperty("DB_HOST");
		String DB_USER = properties.getProperty("DB_USER");
		String DB_PASS = properties.getProperty("DB_PASS");
		String DB_NAME = properties.getProperty("DB_NAME");
		
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
