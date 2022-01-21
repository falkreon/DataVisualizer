package blue.endless.visualizer;

import java.util.function.IntUnaryOperator;

public class IntDisplayType {
	private IntDisplayType() {}; //Don't make these
	
	/**
	 * Interprets the data as color-with-alpha, as raw TYPE_INT_ARGB pixels.
	 */
	public static final IntUnaryOperator ARGB = (it)->it;
	
	/**
	 * Interprets the data as RGB colors in the default sRGB colorspace, ignoring any alpha channel if present
	 */
	public static final IntUnaryOperator RGB = (it)->it | 0xFF_000000;
	
	/**
	 * Interprets the data as an unsigned intensity value between 0 and 255, inclusive. Produces a greyscale image.
	 */
	public static final IntUnaryOperator BYTE_INTENSITY = IntDisplayType::intensityColor;
	
	/**
	 * Interprets the data as a signed 32-bit intensity value and produces a greyscale image where 0 is medium-gray,
	 * Integer.MIN_VALUE is black, and Integer.MAX_VALUE is white.
	 */
	public static final IntUnaryOperator SIGNED_INTENSITY = IntDisplayType::signedIntensity;
	
	/**
	 * Interprets the data as an unsigned 32-bit intensity value and produces a greyscale image.
	 */
	public static final IntUnaryOperator UNSIGNED_INTENSITY = (it)->intensityColor(it>>24);
	
	//implementation
	
	private static final int HALF_INTENSITY = intensityColor(128);
	/**
	 * Interprets the data as a signed 32-bit intensity value and produces a greyscale image where 0 is medium-gray,
	 * Integer.MIN_VALUE is black, and Integer.MAX_VALUE is white.
	 */
	private static int signedIntensity(int it) {
		if (it==0) {
			return HALF_INTENSITY;
		} else if (it>0) {
			int scaled = (it >> 24) & 0x7F;      //(2147483647 >> 24) == 0x7F == 127
			return intensityColor(128 + scaled); //128 + 127 == 255
		} else {
			/*
			 * So, -(-2147483648) == -2147483648 because of overflow, so we can't say (-it) >> 24.
			 * Instead, we can say -(it >>> 24), because -2147483648 >>> 24 == 0xFFFFFF80 == -128.
			 * But even further, instead of negating it and then subtracting, we can skip that and just add them.
			 */
			int scaled = it >>> 24;
			return intensityColor(128 + scaled); //128 + -128 == 0
		}
	}
	
	/**
	 * Takes a value scaled to 0..255 and makes a color out of it
	 */
	private static int intensityColor(int i) {
		i &= 0xFF;
		return 0xFF_000000
			| i << 16
			| i << 8
			| i;
	}
}
