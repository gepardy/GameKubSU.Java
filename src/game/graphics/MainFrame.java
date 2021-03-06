package game.graphics;

import game.engine.Settings;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public MainCanvas mainCanvas;
	
	public MainFrame() {
		setTitle(Settings.Frame.title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainCanvas = new MainCanvas();
		mainCanvas.setPreferredSize(new Dimension((int) Settings.World.width, (int) Settings.World.height));
		add(mainCanvas);
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame(new Runner());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

}
