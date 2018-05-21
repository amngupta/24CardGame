package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.Naming;
import java.util.Enumeration;
import java.util.Properties;

import games.CardGame;

public class Main {
	
	public static void main(String[] args) {
		try {
			PropertiesLoader pl = new PropertiesLoader();
			pl.loadProperties();
			Properties properties = pl.getProperties();
			String JMS_HOST = pl.getProperty("JMS_HOST");
			Integer JMS_PORT = Integer.parseInt(pl.getProperty("JMS_PORT"));
			String RMI_SERVICE = pl.getProperty("RMI_SERVICE");
			Boolean isDebug = Boolean.valueOf(pl.getProperty("DEBUG_MODE"));
			
			Persistence persist = new Persistence(properties);
			RMIServer app = new RMIServer();
			
			app.setPersistence(persist);
			System.out.println("Persistence Service registered");
			

			JMSHelper jms = new JMSHelper(JMS_HOST, JMS_PORT);

			CardGame cg = new CardGame(isDebug);
			GameCommunicator gv = new GameCommunicator(persist, jms, cg);
			app.setGameCommunicator(gv);
			
			System.setSecurityManager(new SecurityManager());
			Naming.rebind(RMI_SERVICE, app);
			System.out.println("RMI Service registered");

//			Wait here for users
//			gm.addUserToSession(persist.getUserInfoPersistence().searchUser("aman"));
//			gm.addUserToSession(persist.getUserInfoPersistence().searchUser("amn"));
			gv.prepareGame();
			
			
//			int count = app.count("The quick brown fox jumps over a lazy dog");
//			System.out.println("There are "+count+" words");
		} catch(Exception e) {
			System.err.println("Exception thrown: "+e);
			e.printStackTrace();
		}
	}
	
}
