package hr.fer.ooup.lab4.models;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import hr.fer.ooup.lab4.GeometryUtil;
import hr.fer.ooup.lab4.Point;
import hr.fer.ooup.lab4.Rectangle;
import hr.fer.ooup.lab4.Renderer;

public class CompositeShape extends AbstractGraphicalObject {
	private List<GraphicalObject> shapes;
	
	

	public CompositeShape(List<GraphicalObject> shapes) {
		super();
		this.shapes = shapes;
	}

	@Override
	public Rectangle getBoundingBox() {
		Rectangle compositeRectangle = null;
		for(GraphicalObject obj : shapes) {
			if(compositeRectangle == null)
				compositeRectangle = obj.getBoundingBox();
			else
				compositeRectangle = compositeRectangle.union(obj.getBoundingBox());
		}
		return compositeRectangle;
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		Rectangle boundingBox = getBoundingBox();
		return GeometryUtil.selectinDistance(mousePoint, boundingBox);
	}

	@Override
	public void render(Renderer r) {
		shapes.forEach(g -> g.render(r));
	}

	@Override
	public String getShapeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphicalObject duplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShapeID() {
		return "@COMP";
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] parts = data.split(" ");
		if(parts.length != 1) throw new IllegalArgumentException("Shape: "+getShapeID()+" oèekuje 1 argumenta");
		try {
			int size = Integer.parseInt(parts[0]);
			List<GraphicalObject> shapes = IntStream.range(0, size).mapToObj(i -> stack.pop()).collect(Collectors.toList());
			stack.push(new CompositeShape(shapes));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}

	}

	@Override
	public void save(List<String> rows) {
		rows.add(getShapeID() + " " + shapes.size());

	}

	public List<GraphicalObject> getShapes() {
		return shapes;
	}
	

}
