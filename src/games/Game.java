package games;

import records.UserInfo;
import server.Answer;

public interface Game {
	
	public boolean checkAnswer(Answer answer);
	public void prepareGameInstance();
	public String getProblem();
}
