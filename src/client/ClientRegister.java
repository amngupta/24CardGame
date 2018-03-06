package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import records.UserInfo;
import server.RMIServerInterface;

public class ClientRegister implements Runnable {

	private JFrame frame;
	private JTextField usernameField;
	private JLabel lblPassword;
	private JPasswordField passwordField;
	private JLabel lblConfirmPassword;
	private JPasswordField cpasswordField;
	private JButton btnRegister;
	private JButton btnCancel;
	private RMIServerInterface serverObj;

	public ClientRegister(RMIServerInterface serverObj2) {
		this.serverObj = serverObj2;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void run() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);		
		frame.getContentPane().setLayout(new GridLayout(0, 2, 1, 1));
		
		JLabel usernameLabel = new JLabel("Username: ");
		usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(usernameLabel);
		
		usernameField = new JTextField();
		usernameLabel.setLabelFor(usernameField);
		frame.getContentPane().add(usernameField);
		usernameField.setColumns(10);
		
		lblPassword = new JLabel("Password: ");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblPassword);
		
		passwordField = new JPasswordField();
		lblPassword.setLabelFor(passwordField);
		frame.getContentPane().add(passwordField);
		
		lblConfirmPassword = new JLabel("Confirm Password: ");
		lblConfirmPassword.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblConfirmPassword);
		
		cpasswordField = new JPasswordField();
		frame.getContentPane().add(cpasswordField);
		
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String username = usernameField.getText();
				System.out.println(username);

				if (username != null && !username.isEmpty())
				{
					String pw1 = String.valueOf(passwordField.getPassword());
					String pw2 = String.valueOf(cpasswordField.getPassword());
					System.out.println(pw1+ "   " +pw2);
					if (pw1.equals(pw2))
					{
						UserInfo newUser = new UserInfo(username, pw1);
						try {
							serverObj.createUser(newUser);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						    JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Dialog",
						            JOptionPane.ERROR_MESSAGE);
						}
						frame.setVisible(false);
					}
					else {
						String message = "Password Mismatch";
					    JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
					            JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					String message = "Need valid username";
				    JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
				            JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		frame.getContentPane().add(btnRegister);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				frame.dispose();;
			}
		});
		frame.getContentPane().add(btnCancel);	
		
		frame.setVisible(true);
	}

}
