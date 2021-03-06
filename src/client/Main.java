package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import records.UserInfo;
import server.PropertiesLoader;
import server.RMIServerInterface;

public class Main implements Runnable{

	private JFrame frame;
	private JTextField usernameTextField;
	private JPasswordField passwordField;
	private JPanel loginForm;
	private JPanel buttonPanel;
	private JButton loginButton;
	private JButton registerButton;
	private HomeFrame homePage;
	private ClientRegister registerPage;
	private static final String DEFAULT_HOST = "127.0.0.1";
	
	/**z
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		PropertiesLoader pl = new PropertiesLoader();
		pl.loadProperties();
		
		String host = DEFAULT_HOST;
		String service = pl.getProperty("RMI_SERVICE");
		if (pl.getProperties().contains("SERVER_HOST")) {
			host = pl.getProperty("SERVER_HOST");
			System.out.println("Setting host: "+host);
		}
		SwingUtilities.invokeLater(new Main(host, service));		
	}

	
	private RMIServerInterface serverObj;
	private UserInfo currentUser;

	public Main(String host, String serviceName) {
		try {
	        Registry registry = LocateRegistry.getRegistry(host);
	        serverObj = (RMIServerInterface) registry.lookup(serviceName);
		} 
		catch(Exception e) {
			System.err.println("Failed accessing RMI: "+ e);
			e.printStackTrace();
		}
	}


	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		loginForm = new JPanel();
		
		JLabel usernameLabel = new JLabel("Username:");
		loginForm.add(usernameLabel);
		usernameLabel.setFont(UIManager.getFont("Label.font"));
		usernameLabel.setVerticalAlignment(SwingConstants.TOP);
		
		usernameTextField = new JTextField();
		usernameLabel.setLabelFor(usernameTextField);
		loginForm.add(usernameTextField);
		usernameTextField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password:");
		loginForm.add(passwordLabel);
		passwordField = new JPasswordField();
		passwordLabel.setLabelFor(passwordField);
		loginForm.add(passwordField);
		passwordField.setColumns(10);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		frame.getContentPane().add(loginForm);
		
		buttonPanel = new JPanel();
		loginForm.add(buttonPanel);
		
		loginButton = new JButton("Login");
		buttonPanel.add(loginButton);
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				String username = usernameTextField.getText();
				String password = String.valueOf(passwordField.getPassword());
				try {
					currentUser = serverObj.checkUser(username, password);
					if (currentUser != null)
					{
						if(serverObj.loginUser(currentUser)) {
							frame.setVisible(false);
							homePage = new HomeFrame(serverObj, currentUser);
							SwingUtilities.invokeLater(homePage);
						}	
						else {
							String message = "User Already Logged In";
						    JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
						            JOptionPane.ERROR_MESSAGE);
						}
					}
					else
					{
						String message = "Invalid valid username/password";
					    JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
					            JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				    JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Dialog",
				            JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				registerPage = new ClientRegister(serverObj);
				SwingUtilities.invokeLater(registerPage);
			}
		});
		
		buttonPanel.add(registerButton);
		frame.setVisible(true);
	}
}
