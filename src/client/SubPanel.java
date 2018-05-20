package client;

import javax.swing.JPanel;

public abstract class SubPanel extends JPanel implements Runnable {
	public abstract void refreshInfo();
	public void hidePanel()
	{
		setVisible(false);
	};
	public void showPanel() {
		setVisible(true);
	};
}
