package hr.fer.ooup.lab4.states;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.fer.ooup.lab4.G2DRendererImpl;
import hr.fer.ooup.lab4.Point;
import hr.fer.ooup.lab4.Renderer;
import hr.fer.ooup.lab4.models.DocumentModel;
import hr.fer.ooup.lab4.models.GraphicalObject;

public class EraserState extends IdleState {
	private DocumentModel model;
	Set<GraphicalObject> removeObjects = new HashSet<GraphicalObject>();
	List<Point> points = new ArrayList<Point>();

	public EraserState(DocumentModel model) {
		super();
		this.model = model;
	}
	
	@Override
	public void mouseDragged(Point mousePoint) {
		points.add(mousePoint);
		model.notifyListeners();
		GraphicalObject go = model.findSelectedGraphicalObject(mousePoint);
		if(go == null) return;
		removeObjects.add(go);
	}
	
	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		for(GraphicalObject go : removeObjects) {
			model.removeGraphicalObject(go);
		}
		points.clear();
		model.notifyListeners();
	}
	
	@Override
	public void afterDraw(Renderer r) {
		for(int i = 1; i<points.size();i++) {
			r.drawLine(points.get(i-1), points.get(i));
		}
	}
	@Override
	public void onLeaving() {
		removeObjects.clear();
		points.clear();
	}

}
