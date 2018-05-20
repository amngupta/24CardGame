package games;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.IntStream;

import records.UserInfo;
import server.Answer;

public class CardGame implements Game{
	
	private static double GAME_ANSWER = 24.0;
	
	List<Cards> cardsList;
	Random generator;
	private ArrayList<Cards> selectedCards;
	
	
	public CardGame()
	{
		this.generator = new Random();
		this.cardsList = Cards.generateCardsList();
	}

	public List<Cards> getSelectedCards()
	{
		return selectedCards;
	}
	
	private Cards getCard(int ranNum){
		Cards c = cardsList.get(ranNum);
		Integer val = c.getValue();
		if (selectedCards.stream().map(Cards::getValue).filter(val::equals).findFirst().isPresent()) {
			return getCard(generator.nextInt(52));
		};
		return c;
	}

	@Override
	public boolean checkAnswer(Answer answer) {
		if (answer.getAnswer().isEmpty())
		{
			return false;
		}
		PostFix pf = new PostFix(answer.getAnswer());
		Double ans = pf.evaluate();
		return ans.equals(GAME_ANSWER);
	}
	
	@Override
	public void prepareGameInstance()
	{
		selectedCards = new ArrayList<Cards>();
//		for (int i = 0; i < 4; i++)
//		{
//			selectedCards.add(getCard(generator.nextInt(52)));
//		}
		selectedCards.add(getCard(4));
		selectedCards.add(getCard(11));
		selectedCards.add(getCard(1));
		selectedCards.add(getCard(2));		
	}

	@Override
	public String getProblem() {
		// TODO Auto-generated method stub
		String cardsFileNames = "";
		for (Cards c: getSelectedCards())
		{
			cardsFileNames += ((Cards) c).getFileName() + ",";
		}
		return cardsFileNames;
	}
	
}
