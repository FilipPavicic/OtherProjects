package hr.fer.ooup.lab4.states;

import hr.fer.ooup.lab4.Point;
import hr.fer.ooup.lab4.Renderer;
import hr.fer.ooup.lab4.models.DocumentModel;
import hr.fer.ooup.lab4.models.GraphicalObject;

public class AddShapeState extends IdleState {
	
	private GraphicalObject prototype;
	private DocumentModel model;
	
	public AddShapeState(GraphicalObject prototype, DocumentModel model) {
		super();
		this.prototype = prototype;
		this.model = model;
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		GraphicalObject og = prototype.duplicate();
		model.addGraphicalObject(og);
		og.translate(mousePoint);
		
	}
}