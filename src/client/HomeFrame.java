package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import records.UserInfo;
import server.RMIServerInterface;

public class HomeFrame extends JFrame implements Runnable {

	private JPanel menuPanel;
	private UserInfo currentUser;
	private RMIServerInterface serverObj;
	private GameClientStub gameClientStub;
	private Map<String, SubPanel> panelsMap;
	
	public HomeFrame(RMIServerInterface serverObj, UserInfo currentUser) {
		// TODO Auto-generated constructor stub
		this.serverObj = serverObj;
		this.currentUser = currentUser;
		this.panelsMap = new HashMap<>();
		this.gameClientStub = new GameClientStub(currentUser);
		this.initializePanels();
		this.gameClientStub.setCardGameClient((CardGameClient) this.panelsMap.get("gameWindow"));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setBounds(100, 100, 500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
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
		
		playButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent arg0) {
				showPanel("gameEndWindow");
			}
		});
		
		
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					serverObj.logoutUser(currentUser);
				} catch (IOException e) {
					e.printStackTrace();
				}
				dispose();
				System.exit(0);
			}			
		});
		
		leaderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				showPanel("leaderboard");		
			}			
		});
		
		profileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				showPanel("userInfo");		
			}			
		});
		
		getContentPane().add(menuPanel);
		setVisible(true);
		showPanel("userInfo");		
	}
	
	private void initializePanels() {
		SubPanel userInfoPage = new UserInfoPage(serverObj, currentUser, this);
		this.panelsMap.put("userInfo", userInfoPage);
		SwingUtilities.invokeLater(userInfoPage);
		
		SubPanel leaderboardPage = new LeaderBoard(serverObj, this);
		this.panelsMap.put("leaderboard", leaderboardPage);
		SwingUtilities.invokeLater(leaderboardPage);
		
		SubPanel gameWindow = new CardGameClient(currentUser, this.gameClientStub, this);
		this.panelsMap.put("gameWindow", gameWindow);
		SwingUtilities.invokeLater(gameWindow);
		
		SubPanel gameEnd = new GameEnd(currentUser, this.gameClientStub, this);
		this.panelsMap.put("gameEndWindow", gameEnd);
		SwingUtilities.invokeLater(gameEnd);
	}

	public void showPanel(String panel)
	{
		this.panelsMap.forEach((k, v)->{
			if (k.equals(panel))
			{
				v.refreshInfo();
				v.showPanel();
			}
			else
			{
				v.hidePanel();
			}
		});
	}
	
	public void hidePanel(String panel)
	{
		this.panelsMap.get(panel).hidePanel();
	}
	
	public SubPanel getPanel(String panelName)
	{
		return panelsMap.get(panelName);
	}
	
	
}
