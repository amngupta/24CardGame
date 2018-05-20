package server;

import java.time.OffsetDateTime;

public class Answer {
	private String answer; 
	private OffsetDateTime answerTime;
	
	Answer(String answer)
	{
		this.answer = answer;
		this.answerTime = OffsetDateTime.now();
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public OffsetDateTime getAnswerTime() {
		return answerTime;
	}
	
}
