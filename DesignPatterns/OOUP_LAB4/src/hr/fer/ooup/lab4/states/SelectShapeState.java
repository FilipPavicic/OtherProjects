package hr.fer.ooup.lab4.states;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.ooup.lab4.Point;
import hr.fer.ooup.lab4.Rectangle;
import hr.fer.ooup.lab4.Renderer;
import hr.fer.ooup.lab4.models.CompositeShape;
import hr.fer.ooup.lab4.models.DocumentModel;
import hr.fer.ooup.lab4.models.GraphicalObject;

public class SelectShapeState extends IdleState {

	private static final Map<Integer, Runnable> KEYS = new HashMap<>();
	public void createKeys() {
		KEYS.put(KeyEvent.VK_PLUS,() -> {
			if(model.getSelectedObjects().size() != 1) return;
			GraphicalObject go = model.getSelectedObjects().get(0);
			model.increaseZ(go);
		});
		KEYS.put(KeyEvent.VK_MINUS,() -> {
			if(model.getSelectedObjects().size() != 1) return;
			GraphicalObject go = model.getSelectedObjects().get(0);
			model.decreaseZ(go);
		});
		KEYS.put(KeyEvent.VK_G, () -> {
			List<GraphicalObject> objects = new ArrayList<GraphicalObject>(model.getSelectedObjects());
			for(GraphicalObject obj: objects) {
				model.removeGraphicalObject(obj);
			}
			GraphicalObject go = new CompositeShape(objects);
			model.addGraphicalObject(go);
			go.setSelected(true);
			model.notifyListeners();
		});
		KEYS.put(KeyEvent.VK_U, () -> {
			if(model.getSelectedObjects().size() != 1) return;
			GraphicalObject go = model.getSelectedObjects().get(0);
			if(!(go instanceof CompositeShape)) return;
			CompositeShape cs = (CompositeShape) go;
			List<GraphicalObject> objects = cs.getShapes();
			model.removeGraphicalObject(go);
			for(GraphicalObject object: objects) {
				model.addGraphicalObject(object);
				object.setSelected(true);
			}
			model.notifyListeners();
			
			

		});
	}


	private DocumentModel model;
	private static final int HP_SIZE = 4;

	public SelectShapeState(DocumentModel model) {
		super();
		this.model = model;
		createKeys();
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		if(!ctrlDown) {
			onLeaving();
		}
		GraphicalObject newGo= model.findSelectedGraphicalObject(mousePoint);
		if(newGo == null) return;
		newGo.setSelected(true);
	}
	@Override
	public void mouseDragged(Point mousePoint) {
		if(model.getSelectedObjects().size() == 1) {
			GraphicalObject go = model.getSelectedObjects().get(0);
			int indexSelectedHotPoint = model.findSelectedHotPoint(go, mousePoint);
			if(indexSelectedHotPoint == -1) return;
			go.setHotPoint(indexSelectedHotPoint, mousePoint);
		}
	}

	@Override
	public void keyPressed(int keyCode) {
		//System.out.println(keyCode);
		Runnable action = KEYS.get(keyCode);
		if(action != null) action.run();
	}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {
		if(go.isSelected()) {
			Rectangle boundingBox = go.getBoundingBox();
			//Kutevi Oval
			Point A = new Point(boundingBox.getX(),boundingBox.getY());
			Point B = new Point(A.getX(), A.getY() + boundingBox.getHeight());
			Point C = new Point(A.getX() + boundingBox.getWidth(), A.getY() + boundingBox.getHeight());
			Point D = new Point(A.getX() + boundingBox.getWidth(), A.getY());
			r.drawLine(A, B);
			r.drawLine(B, C);
			r.drawLine(C, D);
			r.drawLine(D, A);
			if(model.getSelectedObjects().size() == 1) drawHotPoints(r,go);
		}
	}

	private void drawHotPoints(Renderer r,GraphicalObject go) {
		for(int i = 0; i < go.getNumberOfHotPoints(); i++) {
			Point hp = go.getHotPoint(i);
			Point A = new Point(hp.getX() - HP_SIZE, hp.getY() - HP_SIZE);
			Point B = new Point(hp.getX() + HP_SIZE, hp.getY() - HP_SIZE);
			Point C = new Point(hp.getX() + HP_SIZE, hp.getY() + HP_SIZE);
			Point D = new Point(hp.getX() - HP_SIZE, hp.getY() + HP_SIZE);
			r.drawLine(A, B);
			r.drawLine(B, C);
			r.drawLine(C, D);
			r.drawLine(D, A);

		}

	}
	@Override
	public void onLeaving() {
		List<GraphicalObject> objects = new ArrayList<GraphicalObject>(model.getSelectedObjects());
		for(GraphicalObject obj: objects) {
			obj.setSelected(false);
		}
	}
}
