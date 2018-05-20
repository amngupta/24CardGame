package client;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import records.UserInfo;
import records.UserStatistics;
import server.RMIServerInterface;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class UserInfoPage extends  SubPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private UserInfo currentUser;
	private RMIServerInterface serverObj;
	private UserStatistics userStats;
	private JLabel rankLabel;
	private JLabel totalGamesText;
	private JLabel averageTimeText;
	private JLabel winsText;
	
	public UserInfoPage(RMIServerInterface serverObj, UserInfo currentUser, JFrame frame) {
		this.serverObj = serverObj;
		this.currentUser = currentUser;
		this.frame = frame;
		userStats = this.currentUser.getUserStats();		
	}


	@Override
	public void refreshInfo() {
		try {
			currentUser = serverObj.getUserInfo(currentUser.getUsername());
			userStats = currentUser.getUserStats();
			userStats.computeAvgTime();
			rankLabel.setText("You are currently Rank " + userStats.getRank());
			averageTimeText.setText(Float.toString(userStats.getAvgTime()));
			winsText.setText(Integer.toString(userStats.getNumberOfWins()));
			totalGamesText.setText(Integer.toString(userStats.getNumberOfGames()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.getContentPane().add(this);
		setLayout(new GridLayout(3, 1, 0, 0));
		
		JLabel userNameText = new JLabel(currentUser.getUsername());
		userNameText.setAlignmentX(Component.RIGHT_ALIGNMENT);
		userNameText.setVerticalAlignment(SwingConstants.TOP);
		userNameText.setHorizontalAlignment(SwingConstants.LEFT);
		add(userNameText);
		userNameText.setFont(new Font("Tahoma", Font.BOLD, 32));
		
		JPanel listPanel = new JPanel();
		add(listPanel);
		listPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel winsLabel = new JLabel("Wins: ");
		listPanel.add(winsLabel);
		winsText = new JLabel(Integer.toString(userStats.getNumberOfWins()));
		listPanel.add(winsText);
		
		JLabel lblTotalGames = new JLabel("Total Games: ");
		listPanel.add(lblTotalGames);
		totalGamesText = new JLabel(Integer.toString(userStats.getNumberOfGames()));
		listPanel.add(totalGamesText);
		userStats.computeAvgTime();

		JLabel lblAverageTime = new JLabel("Average Time (seconds): ");
		listPanel.add(lblAverageTime);
		averageTimeText = new JLabel(Float.toString(userStats.getAvgTime()));
		listPanel.add(averageTimeText);
		
		rankLabel = new JLabel("You are currently Rank " + userStats.getRank());
		add(rankLabel);
		rankLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
	}

}
