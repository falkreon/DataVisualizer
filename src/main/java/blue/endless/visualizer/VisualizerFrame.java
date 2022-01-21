package blue.endless.visualizer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class VisualizerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private VisualizerPanel panel = new VisualizerPanel();

	public VisualizerFrame() {
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.setTitle("Visualizer");
		this.setMinimumSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public VisualizerPanel getVisualizer() { return panel; }
}
