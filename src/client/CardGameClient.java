package client;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.jms.MessageListener;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import games.Cards;
import games.PostFix;
import records.UserInfo;

import javax.swing.JTextField;
import java.awt.Button;
import java.awt.Container;

import javax.swing.border.EmptyBorder;

public class CardGameClient extends SubPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField AnswerField;
	private UserInfo currentUser;
	private HomeFrame frame;
	private GameClientStub gameClientStub;
	private List<JLabel> cardLabelList;
	private JPanel PlayerPanel;
	private JLabel GameStatusInfo;
	private static final String pleaseWaitText = "Hold on while we connect you to other players...";
	private HashSet<String> cardFaceValues = new HashSet<String>();
	private List<Cards> cardFiles;
	private Container opponentsInfoPanel;

	private static final URL backCard = CardGameClient.class.getResource("/client/images/card_back.gif");
	private JPanel opponentPanel;
	private JLabel opponentLabel_1;
	private JLabel opponentLabel_2;
	private JLabel opponentLabel_3;
	private List<JLabel> opponentLabelList;
	
	/**
	 * Create the panel.
	 * @param gameClientStub2 
	 */
	public CardGameClient(UserInfo currentUser, GameClientStub gameClientStub, HomeFrame frame) {
		this.currentUser = currentUser;
		this.frame = frame;		
		this.gameClientStub = gameClientStub;
		cardFaceValues.add("+");
		cardFaceValues.add("-");
		cardFaceValues.add("*");
		cardFaceValues.add("/");
		cardFaceValues.add("(");
		cardFaceValues.add(")");
		this.opponentLabelList = new ArrayList<>();
	}

	@Override
	public void run() {
		createClientWindow();
	}

	private void createGameRequest() {
		// TODO Auto-generated method stub
		gameClientStub.sendNewGameRequest();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void createClientWindow() {
		// TODO Auto-generated method stub
		frame.getContentPane().add(this);
		cardLabelList = new ArrayList<>();
		setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel GameArea = new JPanel();
		add(GameArea);
		GameArea.setLayout(new GridLayout(0, 4, 0, 0));
		
		JLabel Card_1 = new JLabel("");
		Card_1.setHorizontalAlignment(SwingConstants.CENTER);
		Card_1.setIcon(new ImageIcon(backCard));
		GameArea.add(Card_1);
		cardLabelList.add(Card_1);
		
		JLabel Card_2 = new JLabel("");
		Card_2.setIcon(new ImageIcon(backCard));
		Card_2.setHorizontalAlignment(SwingConstants.CENTER);
		GameArea.add(Card_2);
		cardLabelList.add(Card_2);

		JLabel Card_3 = new JLabel("");
		Card_3.setIcon(new ImageIcon(backCard));
		Card_3.setHorizontalAlignment(SwingConstants.CENTER);
		GameArea.add(Card_3);
		cardLabelList.add(Card_3);

		JLabel Card_4 = new JLabel("");
		Card_4.setIcon(new ImageIcon(backCard));
		Card_4.setHorizontalAlignment(SwingConstants.CENTER);
		GameArea.add(Card_4);
		cardLabelList.add(Card_4);

		JPanel SubmissionArea = new JPanel();
		add(SubmissionArea);
		SubmissionArea.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel SubmissionForm = new JPanel();
		SubmissionArea.add(SubmissionForm);
		SubmissionForm.setLayout(new GridLayout(2, 1, 0, 0));
		
		AnswerField = new JTextField();
		SubmissionForm.add(AnswerField);
		AnswerField.setColumns(10);
		
		Button AnswerButton = new Button("Submit Answer");
		AnswerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String answer = AnswerField.getText().replaceAll("\\s", "");
				if (!answer.isEmpty()) {
					System.out.println(answer);
					if (checkAnswer(answer))
						gameClientStub.sendAnswer(answer);	
					else {
						 JOptionPane.showMessageDialog(new JFrame(), "Incorrect Answer. Try Again", "Dialog",
						            JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		});
		SubmissionForm.add(AnswerButton);
		
		PlayerPanel = new JPanel();
		PlayerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		PlayerPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		GameStatusInfo = new JLabel();
		GameStatusInfo.setHorizontalAlignment(SwingConstants.CENTER);
		PlayerPanel.add(GameStatusInfo);
		this.setInfoText(pleaseWaitText);
		SubmissionArea.add(PlayerPanel);
		
		opponentPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) opponentPanel.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		PlayerPanel.add(opponentPanel);
		opponentLabel_1 = new JLabel("");
		opponentPanel.add(opponentLabel_1);
		this.opponentLabelList.add(opponentLabel_1);
		opponentLabel_2 = new JLabel("");
		opponentPanel.add(opponentLabel_2);
		this.opponentLabelList.add(opponentLabel_2);

		opponentLabel_3 = new JLabel("");
		opponentPanel.add(opponentLabel_3);
		this.opponentLabelList.add(opponentLabel_3);

		setVisible(true);
	}

	public void updateLayout(List<Cards> cardFiles) {
		this.cardFiles = cardFiles;
		for (int i = 0; i < cardFiles.size(); i++)
		{
			Cards card = cardFiles.get(i);
			this.cardFaceValues.add(card.getFaceValue());
			String fileName = String.format("/client/images/%s", card.getFileName());
//			System.out.println(fileName);
			URL resource = CardGameClient.class.getResource(fileName);
			if (resource!= null) {
				cardLabelList.get(i).setIcon(new ImageIcon(resource));
			}
//			cardLabelList.get(i).setText(card.getFaceValue());	
		}		
	}

	public void finishGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshInfo() {
		resetCards();
		this.AnswerField.setText("");
		PlayerPanel = new JPanel();
		PlayerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		PlayerPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		GameStatusInfo = new JLabel();
		GameStatusInfo.setHorizontalAlignment(SwingConstants.CENTER);
		PlayerPanel.add(GameStatusInfo);
		setInfoText(pleaseWaitText);
	}

//	public void loadOpponentInfo(String name, UserInfo info) {
//		// TODO Auto-generated method stub
//		System.out.println(name + "   " + currentUser.getUsername());
//		setInfoText("");
//		if (!info.getUsername().equals(currentUser.getUsername())) {
//			JLabel OpponentInfo = new JLabel();
//			OpponentInfo.setHorizontalAlignment(SwingConstants.CENTER);
//			PlayerPanel.add(OpponentInfo);
//			OpponentInfo.setText(info.getUsername());
//		}	
//	}

	public void addOpponentInfo(Map<String, UserInfo> message) {
		this.setInfoText("");
		AtomicInteger counter = new AtomicInteger(0);
		message.forEach((name, info)-> {
			if (name.equalsIgnoreCase(this.currentUser.getUsername())) {
				System.out.println(name);
				String labelString = String.format("<html>User: %s <br> Average Time: %s</html>", info.getUsername(), 
						info.getUserStats().getAvgTime());
				opponentLabelList.get(counter.get()).setText(labelString);
				counter.addAndGet(1);

			}
		});
		PlayerPanel.add(opponentsInfoPanel);
	}
	
	
	
	
	public void setInfoText(String text)
	{
		GameStatusInfo.setText(String.format("<html>%s</html>", text));
	}

	public void activateGameEnd(String answer, String winningUser) {
		if (!winningUser.isEmpty())
			((GameEnd) frame.getPanel("gameEndWindow")).setEndInfo(winningUser, answer);
		frame.showPanel("gameEndWindow");
	}

	private void resetCards() {
		if (cardFiles != null)
			cardFiles.stream().forEach(i -> {
				this.cardFaceValues.remove(i.getFaceValue());
			});
		cardLabelList.forEach(i -> {
			i.setIcon(new ImageIcon(backCard));
			i.setText("");
		}); 
	}
	
	// TODO ensure only one of each?
	private boolean checkAnswer(String answer) {		
		for (Character c : answer.toCharArray())
		{
			System.out.println("Here" + c);
			if (!this.cardFaceValues.contains(c.toString())) {
				return false;
			}
		}
		PostFix pf = new PostFix(answer);
		return pf.evaluate() == 24.0;
	}

}
