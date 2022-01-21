package blue.endless.visualizer;

@FunctionalInterface
public interface DiscreteDoubleSupplier {
	public double get(int x, int y);
}
