package client;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import records.UserStatistics;

public class LeaderBoard extends  SubPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<UserStatistics> userStats;
	private LeaderboardModel model;
	private JFrame frame;

	
	public LeaderBoard(List<UserStatistics> userStats, JFrame frame ) {
		this.userStats = userStats;
		this.frame = frame;
	}


	@Override
	public void run() {
		frame.add(this);
		model = new LeaderboardModel(userStats);
		add(new JScrollPane(new JTable(model)));		
	}


	@Override
	public void refreshInfo() {
		// TODO Auto-generated method stub
		
	}
}
