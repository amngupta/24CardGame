package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import games.CardGame;
import records.UserInfo;
import server.GameSession;

class CardGameTest {

	@Test
	void test() {
		
//		Prepare 4 cards and send to users 
		CardGame cg = new CardGame();
		cg.prepareGameInstance();
		
//		MUST BE DONE THROUGH QUEUE, ADD TIMER THREAD
		GameSession gm = new GameSession(null, null, cg);
		gm.addUserToSession(new UserInfo("aman", "a"));
		gm.addUserToSession(new UserInfo("aan", "a"));
		gm.addUserToSession(new UserInfo("an", "a"));
		gm.addUserToSession(new UserInfo("n", "a"));

		gm.broadcastStart();
	}
	
	@Test
	void test2() {
		
//		Prepare 4 cards and send to users 
		CardGame cg = new CardGame();
		cg.prepareGameInstance();
		
		String t = cg.getProblem();
		System.out.println(t);
	}

	
	
	
}
