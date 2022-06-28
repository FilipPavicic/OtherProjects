package hr.fer.ooup.lab4.models;

import java.util.List;
import java.util.Stack;

import hr.fer.ooup.lab4.GeometryUtil;
import hr.fer.ooup.lab4.Point;
import hr.fer.ooup.lab4.Rectangle;
import hr.fer.ooup.lab4.Renderer;

public class LineSegment extends AbstractGraphicalObject{
	
		

	public LineSegment(Point start, Point end) {
		super(start,end);
		// TODO Auto-generated constructor stub
	}
	public LineSegment() {
		this(new Point(0, 0), new Point(10, 0));
	}

	@Override
	public Rectangle getBoundingBox() {
		int minX = Math.min(getHotPoint(0).getX(), getHotPoint(1).getX());
		int minY = Math.min(getHotPoint(0).getY(), getHotPoint(1).getY());
		int maxX = Math.max(getHotPoint(0).getX(), getHotPoint(1).getX());
		int maxY = Math.max(getHotPoint(0).getY(), getHotPoint(1).getY());
		return new Rectangle(minX, minY, maxX-minX, maxY-minY);
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
	}

	@Override
	public void render(Renderer r) {
		r.drawLine(getHotPoint(0), getHotPoint(1));
		
	}

	@Override
	public String getShapeName() {
		return "Linija";
	}

	@Override
	public GraphicalObject duplicate() {
		return new LineSegment(getHotPoint(0).duplicate(), getHotPoint(1).duplicate());
	}

	@Override
	public String getShapeID() {
		return "@LINE";
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] parts = data.split(" ");
		if(parts.length != 4) throw new IllegalArgumentException("Shape: "+getShapeID()+" oèekuje 4 argumenta");
		try {
			Point s = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
			Point e = new Point(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
			stack.push(new LineSegment(s, e));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void save(List<String> rows) {
		Point s = getHotPoint(0);
		Point e = getHotPoint(1);
		String line = getShapeID() + " "  +s.getX()+ " " +s.getY()+ " " + e.getX() + " " + e.getY(); 
		rows.add(line);
	}

}
