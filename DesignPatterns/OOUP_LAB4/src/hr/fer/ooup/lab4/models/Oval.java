package hr.fer.ooup.lab4.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import hr.fer.ooup.lab4.GeometryUtil;
import hr.fer.ooup.lab4.Point;
import hr.fer.ooup.lab4.Rectangle;
import hr.fer.ooup.lab4.Renderer;

public class Oval extends AbstractGraphicalObject{
	

	public Oval(Point down, Point right) {
		super(down,right);
		
	}
	public Oval() {
		this(new Point(0, 10), new Point(10, 0));
	}

	@Override
	public Rectangle getBoundingBox() {
		int heigth = (getHotPoint(1).getX() - getHotPoint(0).getX()) * 2;
		int weigth = (getHotPoint(0).getY() - getHotPoint(1).getY()) *2 ;
		return new Rectangle(getHotPoint(1).getX() - heigth, getHotPoint(0).getY() - weigth, heigth, weigth);
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		Rectangle boundingBox = getBoundingBox();
		return GeometryUtil.selectinDistance(mousePoint, boundingBox);
	}

	@Override
	public void render(Renderer r) {
		Rectangle boundingBox = getBoundingBox();
		int p = getHotPoint(0).getX();
		int q = getHotPoint(1).getY();
		Point[] points = eclipsePoints(p, q, boundingBox.getWidth() / 2, boundingBox.getHeight() / 2);
		r.fillPolygon(points);
		
	}
	// srediste S(p,q), a velika poluos, b mala poluos
	private Point[] eclipsePoints(int p,int q, int a, int b) {
		List<Point> points = new ArrayList<Point>();
		int lastX = 0;
		for(int y = q + b; y >= q - b; y--) {
			double x = a * 1.0 /b * Math.sqrt(b*b - Math.pow((y - q),2));
			int xInt = Math.round((float)x);
//			if(y != q + b && xInt == lastX) continue;
//			lastX = xInt;
			points.add(new Point(xInt + p, y));
		}
		return Stream.concat(
				points.stream().skip(1),
				IntStream.
					range(0, points.size()).
					mapToObj(i -> {
						int index = points.size() - i - 1;
						Point point = points.get(index);
						return new Point((point.getX() - p) * -1 + p, point.getY());
					}).skip(1)).toArray(Point[]::new);
	}

	@Override
	public String getShapeName() {
		return "Oval";
	}

	@Override
	public GraphicalObject duplicate() {
		return new Oval(getHotPoint(0).duplicate(), getHotPoint(1).duplicate());
	}

	@Override
	public String getShapeID() {
		return "@OVAL";
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] parts = data.split(" ");
		if(parts.length != 4) throw new IllegalArgumentException("Shape: "+getShapeID()+" oèekuje 4 argumenta");
		try {
			Point right = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
			Point down = new Point(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
			stack.push(new Oval(down,right));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}
		
	}

	@Override
	public void save(List<String> rows) {
		Point s = getHotPoint(1);
		Point e = getHotPoint(0);
		String line = getShapeID() + " "  +s.getX()+ " " +s.getY()+ " " + e.getX() + " " + e.getY(); 
		rows.add(line);
		
	}

}
