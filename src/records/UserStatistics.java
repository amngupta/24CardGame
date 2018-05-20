package records;

import java.io.Serializable;
import java.util.Comparator;

public class UserStatistics implements Comparable<UserStatistics>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -666609947535936510L;
	private int numberOfGames;
	private int numberOfWins;
	private float totalTimePlayed;
	private String username;
	private int rank;
	private float avgTime;
	public UserStatistics(String user, int totalGames, int gamesWon, int totalTimePlayed)
	{
		this.username = user;
		this.numberOfGames = totalGames;
		this.numberOfWins = gamesWon;
		this.totalTimePlayed = totalTimePlayed;
	}
	
	public float getTotalTimePlayed() {
		return totalTimePlayed;
	}
	public void setTotalTimePlayed(float totalTimePlayed) {
		this.totalTimePlayed = totalTimePlayed;
	}
	public void updateTotalTimePlayed(float additionalTime) {
		this.totalTimePlayed += additionalTime;
	}
	
	
	public int getNumberOfWins() {
		return numberOfWins;
	}
	public int getNumberOfGames() {
		return numberOfGames;
	}
	public void incrementWins() {
		this.numberOfWins++;
	}
	public void incrementGames() {
		this.numberOfGames++;
	}

	public float getAvgTime() {
		return avgTime;
	}

	public int getRank() {
		return rank;
	}

	public String getUsername() {
		return username;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public void computeAvgTime(){
		this.avgTime = this.totalTimePlayed / (this.numberOfWins * 1000);
	}
	
	public int compareTo(UserStatistics compareStats) {
		float compareQuantity = ((UserStatistics) compareStats).getAvgTime();
		return (int) (this.avgTime - compareQuantity);
	}
	
	public static Comparator<UserStatistics> AvgTimeComparator    = new Comparator<UserStatistics>() {
		@Override
		public int compare(UserStatistics userOne, UserStatistics userTwo) {
		return userOne.compareTo(userTwo);
		}
	};
	
	@Override
	public String toString() {
		return username + "  " + rank + "  " + avgTime;
	}
}
