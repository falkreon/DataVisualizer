package blue.endless.visualizer;

public class Test {
	public static void main(String... args) {
		VisualizerFrame frame = new VisualizerFrame();
		DiscreteIntSupplier sineWaver = (x,y)->{
			double sine = (Math.sin(x/17.0) + Math.sin(x/11.0)*0.5 + Math.sin(y/29.0) + Math.sin(y/13.0)) / 3.0;
			
			return (int) (sine * Integer.MAX_VALUE);
		};
		frame.getVisualizer().setDataSupplier(sineWaver, IntDisplayType.SIGNED_INTENSITY);
		frame.setVisible(true);
	}
}
