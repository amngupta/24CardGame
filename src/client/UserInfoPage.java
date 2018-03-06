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

public class UserInfoPage extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private UserInfo currentUser;
	private RMIServerInterface serverObj;
	private UserStatistics userStats;
	
	public UserInfoPage(RMIServerInterface serverObj, UserInfo currentUser, JFrame frame) {
		this.serverObj = serverObj;
		this.currentUser = currentUser;
		this.frame = frame;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void run() {
		this.createInfoPanel();
	}
	
	void createInfoPanel() {
		userStats = this.currentUser.getUserStats();
		
		frame.getContentPane().add(this);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
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
		
		JLabel winsText = new JLabel(Integer.toString(userStats.getNumberOfWins()));
		listPanel.add(winsText);
		
		JLabel lblTotalGames = new JLabel("Total Games: ");
		listPanel.add(lblTotalGames);
		
		JLabel totalGamesText = new JLabel(Integer.toString(userStats.getNumberOfGames()));
		listPanel.add(totalGamesText);
		
		JLabel lblAverageTime = new JLabel("Average Time: ");
		listPanel.add(lblAverageTime);
		JLabel averageTimeText = new JLabel(Float.toString(userStats.getAvgTime()));
		listPanel.add(averageTimeText);
		
		userStats.computeAvgTime();
		
		JLabel rankLabel = new JLabel("You are currently Rank " + userStats.getRank());
		add(rankLabel);
		rankLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
	}

}
