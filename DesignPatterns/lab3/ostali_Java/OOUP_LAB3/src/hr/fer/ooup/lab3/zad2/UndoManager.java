package hr.fer.ooup.lab3.zad2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class UndoManager {
	Stack<EditAction> undoStack;
	Stack<EditAction> redoStack;
	List<Runnable> liseners = new ArrayList<>();
	void addListener(Runnable l) {
		liseners.add(l);
		
	}
	void notifyAllListners() {
		for (Runnable l : liseners) {
			l.run();
		}
	}
	private static final UndoManager instance = new UndoManager();
	
	public static UndoManager getInstance() {
		return instance;
	}
	private UndoManager() {
		undoStack = new Stack<EditAction>();
		redoStack = new Stack<EditAction>();
	}
	void undo() {
		EditAction action = undoStack.pop();
		action.execute_undo();
		redoStack.push(action);
		notifyAllListners();
	}
	void redo() {
		EditAction action = redoStack.pop();
		action.execute_do();
		undoStack.push(action);
		notifyAllListners();
	}
	public void push(EditAction c) {
		redoStack.clear();
		undoStack.push(c);
		notifyAllListners();
	}
}
