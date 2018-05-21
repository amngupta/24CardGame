package client;

import javax.swing.JPanel;

public abstract class SubPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	public abstract void refreshInfo();
	public void hidePanel()
	{
		setVisible(false);
	};
	public void showPanel() {
		setVisible(true);
	};
}
