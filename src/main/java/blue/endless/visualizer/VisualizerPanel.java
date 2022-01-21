package blue.endless.visualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.function.IntUnaryOperator;

import javax.swing.JComponent;

import com.playsawdust.chipper.toolbox.lipstick.MonotonicTime;

import blue.endless.visualizer.impl.DiscreteIntTileProvider;
import blue.endless.visualizer.impl.TileProvider;

public class VisualizerPanel extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private double xOfs = 0;
	private double yOfs = 0;
	private double scale = 8;
	private double minValue = 0.25;
	
	private TileProvider tileProvider = null;
	
	private Point ofsAtDragStart = null;
	private Point dragStart = null;
	
	public VisualizerPanel() {
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				switch (e.getScrollType()) {
				case MouseWheelEvent.WHEEL_UNIT_SCROLL:
					int units = -e.getUnitsToScroll();
					scale += units * 0.1;
					if (scale<minValue) scale = minValue;
					VisualizerPanel.this.repaint();
					break;
				}
			}
		});
		
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				dragStart = e.getPoint();
				ofsAtDragStart = new Point((int)xOfs, (int)yOfs);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dragStart = null;
				ofsAtDragStart = null;
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point dragLoc = e.getPoint();
				int dx = dragLoc.x - dragStart.x;
				int dy = dragLoc.y - dragStart.y;
				
				dx = (int) (dx / scale);
				dy = (int) (dy / scale);
				
				xOfs = ofsAtDragStart.x - dx;
				yOfs = ofsAtDragStart.y - dy;
				
				VisualizerPanel.this.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {}
		});
	}
	
	
	public void setDataSupplier(DiscreteIntSupplier supplier, IntUnaryOperator displayType) {
		tileProvider = new DiscreteIntTileProvider(supplier, displayType);
		this.repaint();
	}
	
	//public void setDataSupplier(DiscreteDoubleSupplier supplier) {
	//	
	//}
	
	@Override
	public void paint(Graphics g) {
		if (tileProvider==null) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		} else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			int tileSize = (int)(128 * scale);
			
			int startx = (((int) Math.floor(xOfs)) >> 7) - 1;
			int starty = (((int) Math.floor(yOfs)) >> 7) - 1;
			
			int tilesWide = (int) Math.ceil((this.getWidth() / 128.0) / scale) + 2;
			int tilesHigh = (int) Math.ceil((this.getHeight() / 128.0) / scale) + 2;
			
			int fractionalX = ((int) xOfs) % 128;
			int fractionalY = ((int) yOfs) % 128;
			
			for(int y=0; y<tilesHigh; y++) {
				for(int x=0; x<tilesWide; x++) {
					g.drawImage(tileProvider.getTile(startx+x, starty+y), (x-1)*tileSize + (int) ((-fractionalX)*scale), (int) (y-1)*tileSize + (int) ((-fractionalY)*scale), tileSize, tileSize, this);
				}
			}
			
			long now = MonotonicTime.millis();
			tileProvider.poll(now);
		}
		
	}
}
