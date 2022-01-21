package blue.endless.visualizer.impl;

import java.awt.Point;
import java.awt.image.BufferedImage;

public interface TileProvider {
	public default BufferedImage getTile(int x, int y) {
		return getTile(new Point(x,y));
	}
	public BufferedImage getTile(Point p);
	public void poll(long monotonicTime);
}
