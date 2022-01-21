package blue.endless.visualizer.impl;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.function.IntUnaryOperator;

import com.playsawdust.chipper.toolbox.ThreadUnsafeCache;

import blue.endless.visualizer.DiscreteIntSupplier;

public class DiscreteIntTileProvider implements TileProvider {
	private final ThreadUnsafeCache<Point, BufferedImage> cache;
	private final DiscreteIntSupplier supplier;
	private final IntUnaryOperator displayType;
	
	public DiscreteIntTileProvider(DiscreteIntSupplier supplier, IntUnaryOperator displayType) {
		this.supplier = supplier;
		this.displayType = displayType;
		this.cache = new ThreadUnsafeCache<Point, BufferedImage>(this::generateTile, null, 10_000L, false);
	}
	
	private BufferedImage generateTile(Point p) {
		BufferedImage im = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		
		for(int y=0; y<128; y++) {
			for(int x=0; x<128; x++) {
				int dataPoint = supplier.get(x + (p.x << 7), y + (p.y << 7));
				im.setRGB(x, y, displayType.applyAsInt(dataPoint));
			}
		}
		
		return im;
	}
	
	@Override
	public BufferedImage getTile(Point p) {
		return cache.get(p);
	}
	
	@Override
	public void poll(long monotonicTime) {
		cache.evict(monotonicTime);
	}
}
