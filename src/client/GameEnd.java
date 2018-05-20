package client;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import records.UserInfo;

public class GameEnd extends SubPanel{
	private UserInfo currentUser;
	private HomeFrame frame;
	private GameClientStub gameClientStub;
	private JLabel winnerInfoLabel;
	private JLabel answerLabel;

	/**
	 * Create the panel.
	 * @param gameClientStub2 
	 */
	public GameEnd(UserInfo currentUser, GameClientStub gameClientStub, HomeFrame frame) {
		this.currentUser = currentUser;
		this.frame = frame;		
		this.gameClientStub = gameClientStub;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		frame.getContentPane().add(this);
		setLayout(new GridLayout(2, 1, 0, 0));

		JPanel WinnerInfoArea = new JPanel();
		add(WinnerInfoArea);
		WinnerInfoArea.setLayout(new GridLayout(0, 2, 0, 0));
			
		winnerInfoLabel = new JLabel("");
		winnerInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		WinnerInfoArea.add(winnerInfoLabel);
		
		answerLabel = new JLabel("");
		answerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		WinnerInfoArea.add(answerLabel);

		
		Button NewGameButton = new Button("New Game");
		NewGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gameClientStub.sendNewGameRequest();
				frame.showPanel("gameWindow");
			}
		});
		add(NewGameButton);
		
		setVisible(true);
	}

	@Override
	public void refreshInfo() {
		// TODO Auto-generated method stub
		
	}
	
	public void setEndInfo(String username, String answer)
	{
		this.answerLabel.setText(String.format("Answer: %s", answer));
		this.winnerInfoLabel.setText(String.format("Winner: %s", username));
	}
	
}
