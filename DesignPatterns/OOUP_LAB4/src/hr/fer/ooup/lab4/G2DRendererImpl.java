package hr.fer.ooup.lab4;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;


public class G2DRendererImpl implements Renderer {

	private Graphics2D g2d;
	
	public G2DRendererImpl(Graphics2D g2d) {
		this.g2d = g2d;
	}

	@Override
	public void drawLine(Point s, Point e) {
		g2d.setColor(Color.blue);
		g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
	}

	@Override
	public void fillPolygon(Point[] points) {
		int[] xs = Arrays.stream(points).mapToInt(Point::getX).toArray();
		int[] ys = Arrays.stream(points).mapToInt(Point::getY).toArray();
		g2d.setColor(Color.blue);
		g2d.fillPolygon(xs, ys, points.length);
		g2d.setColor(Color.red);
		g2d.drawPolygon(xs, ys, points.length);
	}

}