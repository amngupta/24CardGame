package server;

import java.rmi.Naming;

import games.CardGame;

public class Main {

	public static void main(String[] args) {
		try {
			Persistence persist = new Persistence();
			RMIServer app = new RMIServer();
			
			app.setPersistence(persist);
			System.out.println("Persistence Service registered");
			JMSHelper jms = new JMSHelper();

			CardGame cg = new CardGame();
			GameCommunicator gv = new GameCommunicator(persist, jms, cg);
			System.setSecurityManager(new SecurityManager());
			Naming.rebind("24Cards", app);
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
