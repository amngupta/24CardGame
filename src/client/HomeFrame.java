package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import records.UserInfo;
import server.RMIServerInterface;

public class HomeFrame implements Runnable {

	private JFrame frame;
	private JPanel menuPanel;
	private UserInfo currentUser;
	private RMIServerInterface serverObj;
	private UserInfoPage userInfoPage;
	private LeaderBoard leaderboardPage;
	
	
	public HomeFrame(RMIServerInterface serverObj, UserInfo currentUser) {
		// TODO Auto-generated constructor stub
		this.serverObj = serverObj;
		this.currentUser = currentUser;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));		
		
		JButton profileButton = new JButton("User Profile");
		menuPanel.add(profileButton);
		
		JButton playButton = new JButton("Play Game");
		menuPanel.add(playButton);
		
		JButton leaderButton = new JButton("Leader Board");
		menuPanel.add(leaderButton);
		
		JButton logoutButton = new JButton("Logout");
		menuPanel.add(logoutButton);
		//		infoPanel.setVisible(true);
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					serverObj.logoutUser(currentUser);
				} catch (IOException e) {
					e.printStackTrace();
				}
				frame.dispose();
				System.exit(0);
			}			
		});
		leaderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				showLeaderBoard();
			}			
		});
		
		profileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				showInfoPage();
			}			
		});
		
		frame.getContentPane().add(menuPanel);
		frame.setVisible(true);
		showInfoPage();
	}
	
	public void showInfoPage()
	{
		System.out.println("Calling InfoPage");
		hideAllPages();
		if (userInfoPage == null)
		{
			userInfoPage = new UserInfoPage(serverObj, currentUser, frame);
			SwingUtilities.invokeLater(userInfoPage);
		}
		else 
		{
			userInfoPage.setVisible(true);
		}
	}
	
	private void hideAllPages()
	{
		if (leaderboardPage != null)
			leaderboardPage.setVisible(false);
		if (userInfoPage != null)
			userInfoPage.setVisible(false);
	}
	
	public void showLeaderBoard() 
	{
		System.out.println("Calling Leaderboard");
		hideAllPages();
		if (leaderboardPage == null)
		{
			try {
				leaderboardPage = new LeaderBoard(serverObj.getUserStats(), frame);
				SwingUtilities.invokeLater(leaderboardPage);
				leaderboardPage.setVisible(true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				String message = "Unable to fetch leaderboard";
			    JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
			            JOptionPane.ERROR_MESSAGE);
			}	
		}
		else
		{
			leaderboardPage.setVisible(true);
		}
	}
}
