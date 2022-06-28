package hr.fer.ooup.lab4.models;

import java.util.ArrayList;
import java.util.List;

import hr.fer.ooup.lab4.GeometryUtil;
import hr.fer.ooup.lab4.Point;

/**
 * @author Filip
 *
 */
public abstract class AbstractGraphicalObject implements GraphicalObject{
	Point[] hotPoints;
	boolean[] hotPointSelected;
	boolean selected;
	List<GraphicalObjectListener> listeners = new ArrayList<>();
	
	public AbstractGraphicalObject(Point... hotPoints) {
		super();
		this.hotPoints = hotPoints;
	}
	
	@Override
	public Point getHotPoint(int index) {
		return hotPoints[index];
	}
	
	@Override
	public void setHotPoint(int index, Point point) {
		hotPoints[index] = point;
		notifyListeners();
	}
	
	@Override
	public int getNumberOfHotPoints() {
		return hotPoints.length;
	}
	
	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		return GeometryUtil.distanceFromPoint(hotPoints[index], mousePoint);
	}
	
	@Override
	public boolean isHotPointSelected(int index) {
		return hotPointSelected[index];
	}
	
	@Override
	public void setHotPointSelected(int index, boolean selected) {
		hotPointSelected[index] = selected;
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		notifySelectionListeners();
	}
	
	@Override
	public void translate(Point delta) {
		for (Point point : hotPoints) {
			point.translate(delta);
		}
		notifyListeners();
	}
	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.add(l);
	}
	
	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.remove(l);
	}
	
	public void notifyListeners() {
		for (GraphicalObjectListener l : listeners) {
			l.graphicalObjectChanged(this);
		}
	}
	
	public void notifySelectionListeners() {
		for (GraphicalObjectListener l : listeners) {
			l.graphicalObjectSelectionChanged(this);
		}
	}
	
	
	
	

}
