package hr.fer.ooup.lab4.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import hr.fer.ooup.lab4.GeometryUtil;
import hr.fer.ooup.lab4.Point;

public class DocumentModel {

	public final static double SELECTION_PROXIMITY = 10;

	// Kolekcija svih grafi�kih objekata:
	private List<GraphicalObject> objects = new ArrayList<>();
	// Read-Only proxy oko kolekcije grafi�kih objekata:
	private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
	// Kolekcija prijavljenih promatra�a:
	private List<DocumentModelListener> listeners = new ArrayList<>();
	// Kolekcija selektiranih objekata:
	private List<GraphicalObject> selectedObjects = new ArrayList<>();
	// Read-Only proxy oko kolekcije selektiranih objekata:
	private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

	// Promatra� koji �e biti registriran nad svim objektima crte�a...
	private final GraphicalObjectListener goListener = new GraphicalObjectListener() {

		@Override
		public void graphicalObjectChanged(GraphicalObject go) {
			notifyListeners();
			
		}

		@Override
		public void graphicalObjectSelectionChanged(GraphicalObject go) {
			if(go.isSelected()) selectedObjects.add(go);
			else selectedObjects.remove(go);
			notifyListeners();
		}
		
	};
	
	// Konstruktor...
	public DocumentModel() {}

	// Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
	// i potom obavijeste svi promatra�i modela
	public void clear() {
		for(GraphicalObject obj: objects) {
			removeGraphicalObject(obj);
		}
		notifyListeners();
	}

	// Dodavanje objekta u dokument (pazite je li ve� selektiran; registrirajte model kao promatra�a)
	public void addGraphicalObject(GraphicalObject obj) {
		if(obj.isSelected()) {
			selectedObjects.add(obj);
		}
		objects.add(obj);
		obj.addGraphicalObjectListener(goListener);
	
	}
	
	// Uklanjanje objekta iz dokumenta (pazite je li ve� selektiran; odregistrirajte model kao promatra�a)
	public void removeGraphicalObject(GraphicalObject obj) {
		if(obj.isSelected()) {
			selectedObjects.remove(obj);
		}
		objects.remove(obj);
		obj.removeGraphicalObjectListener(goListener);
		
	}

	// Vrati nepromjenjivu listu postoje�ih objekata (izmjene smiju i�i samo kroz metode modela)
	public List<GraphicalObject> list() {
		return roObjects;
	}

	// Prijava...
	public void addDocumentModelListener(DocumentModelListener l) {
		listeners.add(l);
	}
	
	// Odjava...
	public void removeDocumentModelListener(DocumentModelListener l) {
		listeners.remove(l);
	}

	// Obavje�tavanje...
	public void notifyListeners() {
		for(DocumentModelListener l : listeners) {
			l.documentChange();
		}
	}
	
	// Vrati nepromjenjivu listu selektiranih objekata
	public List<GraphicalObject> getSelectedObjects() {
		return roSelectedObjects;
	}

	// Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
	// Time �e se on iscrtati kasnije (pa �e time mo�da ve�i dio biti vidljiv)
	public void increaseZ(GraphicalObject go) {
		moveZForDelta(go, 1);
		notifyListeners();
	}
	
	// Pomakni predani objekt u listi objekata na jedno mjesto ranije...
	public void decreaseZ(GraphicalObject go) {
		moveZForDelta(go, -1);
		notifyListeners();
	}
	
	private void moveZForDelta(GraphicalObject go, int delta) {
		int index = objects.indexOf(go);
		if(index ==  -1) return;
		if(index + delta < 0 || index + delta > objects.size() -1 ) return;
		objects.remove(index);
		objects.add(index + delta ,go);
	}
	
	// Prona�i postoji li u modelu neki objekt koji klik na to�ku koja je
	// predana kao argument selektira i vrati ga ili vrati null. To�ka selektira
	// objekt kojemu je najbli�a uz uvjet da ta udaljenost nije ve�a od
	// SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
	public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
		double min = SELECTION_PROXIMITY;
		GraphicalObject goRet = null;
		for(GraphicalObject go: objects) {
			if(go.selectionDistance(mousePoint) < min) {
				min = go.selectionDistance(mousePoint);
				goRet = go;
			}
		}
		return goRet;
	}

	// Prona�i da li u predanom objektu predana to�ka mi�a selektira neki hot-point.
	// To�ka mi�a selektira onaj hot-point objekta kojemu je najbli�a uz uvjet da ta
	// udaljenost nije ve�a od SELECTION_PROXIMITY. Vra�a se indeks hot-pointa 
	// kojeg bi predana to�ka selektirala ili -1 ako takve nema. Status selekcije 
	// se pri tome NE dira.
	public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
		double min = SELECTION_PROXIMITY;
		int index = -1;
		for(int i = 0; i< object.getNumberOfHotPoints(); i++) {
			double hp = GeometryUtil.distanceFromPoint(object.getHotPoint(i),mousePoint);
			if(hp < min) {
				index = i;
				min = hp;
			}
		}
		return index;
	}

}