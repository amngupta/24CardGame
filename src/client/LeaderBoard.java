package client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import records.UserStatistics;
import server.RMIServerInterface;

public class LeaderBoard extends  SubPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<UserStatistics> userStats;
	private JFrame frame;
	private RMIServerInterface serverObj;
	private JScrollPane scrollPane;

	
	public LeaderBoard(RMIServerInterface serverObj, JFrame frame ) {
		this.serverObj = serverObj;
		this.frame = frame;
		userStats = new ArrayList<>();
	}


	@Override
	public void run() {
		frame.add(this);		
		refreshInfo();		
	}


	@Override
	public void refreshInfo() {
		try {
			this.removeAll();
			userStats = serverObj.getUserStats();
			LeaderboardModel model = new LeaderboardModel(userStats);
			scrollPane = new JScrollPane(new JTable(model));
			add(scrollPane);		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
