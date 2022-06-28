package hr.fer.ooup.lab3.zad2;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TextEditorModel {
	List<String> lines;
	private Location cursorLocation;
	LocationRange selectionRangle;
	List<CursorObserver> cursorObservers = new ArrayList<>();
	List<TextObserver> textObservers = new ArrayList<>();
	boolean isSelected = false;

	public TextEditorModel(String text) {
		lines = new ArrayList<String>(Arrays.asList(text.split("\n")));
		cursorLocation = new Location(0,0);
		selectionRangle = new LocationRange(cursorLocation,cursorLocation);
	}	
	public List<String> getLines() {
		return lines;
	}
	public void setLines(List<String> lines) {
		this.lines = lines;
	}
	public Location getCursorLocation() {
		return cursorLocation;
	}
	public void setCursorLocation(Location cursorLocation) {
		this.cursorLocation = cursorLocation;
		this.notifyAllCursorObservers();
	}
	public Iterator<String> allLines() {
		return lines.iterator();
	}
	public Iterator<String> linesRange(int index1,int index2) {
		return lines.subList(index1, index2).iterator();
	}	
	public void addTextObserver(TextObserver o) {
		textObservers.add(o);
	}
	public void removeTextObserver(TextObserver o) {
		textObservers.remove(o);
	}
	public void notifyAllTextObserver() {
		for (TextObserver textObserver : textObservers) {
			textObserver.text();
		}
	}
	public void addCursorObserver(CursorObserver o) {
		cursorObservers.add(o);
	}	
	public void removeCursorObserver(CursorObserver o) {
		cursorObservers.remove(o);
	}
	public void notifyAllCursorObservers() {
		for (CursorObserver cursorObserver : cursorObservers) {
			cursorObserver.updateCursorLocation(cursorLocation);
		}
	}
	public void moveCursorLeft() {
		if(cursorLocation.column  == 0) {
			if(cursorLocation.row == 0) return;
			cursorLocation = new Location(cursorLocation.row - 1, lines.get(cursorLocation.row - 1).length());
			notifyAllCursorObservers();
			return;
		}
		cursorLocation = new Location(cursorLocation.row, cursorLocation.column - 1);
		notifyAllCursorObservers();
	}
	public void moveCursorRight() {
		if(cursorLocation.column  == lines.get(cursorLocation.row).length()) {
			if(cursorLocation.row + 1 == lines.size()) return;
			cursorLocation = new Location(cursorLocation.row + 1, 0);
			notifyAllCursorObservers();
			return;
		}
		cursorLocation = new Location(cursorLocation.row, cursorLocation.column + 1);
		notifyAllCursorObservers();
	}
	public void moveCursorUp() {
		if(cursorLocation.row == 0) return;
		if(cursorLocation.column > lines.get(cursorLocation.row - 1).length()) cursorLocation.column = lines.get(cursorLocation.row - 1).length();
		cursorLocation = new Location(cursorLocation.row - 1, cursorLocation.column);
		notifyAllCursorObservers();
	}
	public void moveCursorDown() {
		if(cursorLocation.row + 1 == lines.size()) return;
		if(cursorLocation.column > lines.get(cursorLocation.row + 1).length()) cursorLocation.column = lines.get(cursorLocation.row + 1).length();
		cursorLocation = new Location(cursorLocation.row + 1, cursorLocation.column);
		notifyAllCursorObservers();
	}
	public void deleteBefore() {
		if(!selectionRangle.isStartEndSame()) {
			deleteSelected();
			return;
		}
		var prevLines = new ArrayList<String>(lines);
		var prevCursorLocation = cursorLocation;
		if(cursorLocation.column == 0) {
			if(cursorLocation.row == 0) return;
			int currentRow = cursorLocation.row;
			moveCursorLeft();
			lines.set(currentRow - 1, lines.get(currentRow - 1) + lines.get(currentRow));
			lines.remove(lines.get(currentRow));
			notifyAllTextObserver();
			return;
		}
		String line = lines.get(cursorLocation.row);
		lines.set(cursorLocation.row,line.substring(0,cursorLocation.column - 1) + line.substring(cursorLocation.column,line.length()));
		moveCursorLeft();
		notifyAllTextObserver();
		var afterLines = new ArrayList<String>(lines);
		var afterCursorLocation = cursorLocation;
		UndoManager.getInstance().push(new EditAction() {		
			@Override
			public void execute_undo() {
				lines = prevLines;
				cursorLocation = prevCursorLocation;
				notifyAllTextObserver();
				
			}		
			@Override
			public void execute_do() {
				lines = afterLines;
				cursorLocation = afterCursorLocation;
				notifyAllTextObserver();			
			}
		});
	}
	public void deleteAfter() {
		if(!selectionRangle.isStartEndSame()) {
			deleteSelected();
			return;
		}
		var prevLines = new ArrayList<String>(lines);
		var prevCursorLocation = cursorLocation;
		if(cursorLocation.column == lines.get(cursorLocation.row).length() ) {
			if(cursorLocation.row == lines.size() -1 ) return;
			int currentRow = cursorLocation.row;
			lines.set(currentRow , lines.get(currentRow ) + lines.get(currentRow + 1));
			lines.remove(lines.get(currentRow + 1));
			notifyAllTextObserver();
			var afterLines = new ArrayList<String>(lines);
			var afterCursorLocation = cursorLocation;
			UndoManager.getInstance().push(new EditAction() {			
				@Override
				public void execute_undo() {
					lines = prevLines;
					cursorLocation = prevCursorLocation;
					notifyAllTextObserver();				
				}			
				@Override
				public void execute_do() {
					lines = afterLines;
					cursorLocation = afterCursorLocation;
					notifyAllTextObserver();				
				}
			});
			return;
		}
		String line = lines.get(cursorLocation.row);
		lines.set(cursorLocation.row,line.substring(0,cursorLocation.column) + line.substring(cursorLocation.column + 1,line.length()));
		notifyAllTextObserver();
	}
	public void deleteSelected() {
		var prevLines = new ArrayList<String>(lines);
		var prevCursorLocation = cursorLocation;
		var prevSelectionRange = selectionRangle;
		LocationRange lr = selectionRangle.sorted();
		if(lr.start.row == lr.end.row) {
			String line = lines.get(lr.start.row);
			lines.set(cursorLocation.row,line.substring(0,lr.start.column) + line.substring(lr.end.column,line.length()));
		}
		else {
			Iterator<String> i = linesRange(lr.start.row, lr.end.row +1);
			String s;
			int removeEnterIndex = lr.end.row;
			for(int j = lr.start.row ;i.hasNext(); s = i.next(),j++) {
				String line = lines.get(j);
				int a = j == lr.start.row ? lr.start.column : 0;
				int b = j == lr.end.row ? lr.end.column  : lines.get(j).length();
				lines.set(j, line.substring(0,a) + line.substring(b,line.length()));
			}
			for(int u = lr.start.row +1  ;u < lr.end.row;u++) {
				lines.remove(lr.start.row +1);
				removeEnterIndex--;
			}
			lines.set(removeEnterIndex - 1, lines.get(removeEnterIndex - 1) + lines.get(removeEnterIndex));
			lines.remove(lines.get(removeEnterIndex));
		}
		cursorLocation = lr.start;
		notifyAllTextObserver();
		notifyAllCursorObservers();
		var afterLines = new ArrayList<String>(lines);
		var afterCursorLocation = cursorLocation;
		var afterSelectionRange = selectionRangle;
		UndoManager.getInstance().push(new EditAction() {		
			@Override
			public void execute_undo() {
				lines = prevLines;
				cursorLocation = prevCursorLocation;
				selectionRangle = prevSelectionRange;
				notifyAllTextObserver();				
			}			
			@Override
			public void execute_do() {
				lines = afterLines;
				cursorLocation = afterCursorLocation;
				selectionRangle = afterSelectionRange;
				notifyAllTextObserver();				
			}
		});
	}
	LocationRange getSelectionRange() {
		return selectionRangle;
	}
	void setSelectionRange(LocationRange range) {
		this.selectionRangle = range;
	}	
	void changeSelectionRange() {
		if(!isSelected) setSelectionRange(new LocationRange(cursorLocation,cursorLocation));
		selectionRangle.end = cursorLocation;		
	}
	void insert(char c,boolean addUndo){
		var prevLines = new ArrayList<String>(lines);
		var prevCursorLocation = cursorLocation;
		var prevSelectionRange = selectionRangle;
		if(!selectionRangle.isStartEndSame()) {
			deleteSelected();
		}
		String line = lines.get(cursorLocation.row);
		if(c == '\n') {
			lines.set(cursorLocation.row,line.substring(0,cursorLocation.column));
			lines.add(cursorLocation.row +1, line.substring(cursorLocation.column,line.length()));
			cursorLocation.column = 0;
			cursorLocation.row = cursorLocation.row +1;
			notifyAllTextObserver();
			notifyAllCursorObservers();	
			return;			
		}		
		lines.set(cursorLocation.row,line.substring(0,cursorLocation.column) + c +line.substring(cursorLocation.column,line.length()));
		moveCursorRight();
		notifyAllTextObserver();
		notifyAllCursorObservers();
		var afterLines = new ArrayList<String>(lines);
		var afterCursorLocation = cursorLocation;
		var afterSelectionRange = selectionRangle;
		if(addUndo) UndoManager.getInstance().push(new EditAction() {			
			@Override
			public void execute_undo() {
				lines = prevLines;
				cursorLocation = prevCursorLocation;
				selectionRangle = prevSelectionRange;
				notifyAllTextObserver();
				
			}			
			@Override
			public void execute_do() {
				lines = afterLines;
				cursorLocation = afterCursorLocation;
				selectionRangle = afterSelectionRange;
				notifyAllTextObserver();			
			}
		});
	}
	void insert(String text) {
		var prevLines = new ArrayList<String>(lines);
		var prevCursorLocation = cursorLocation;
		var prevSelectionRange = selectionRangle;
		for(int i = 0;i<text.length();i++) {
			insert(text.charAt(i),false);
		}
		var afterLines = new ArrayList<String>(lines);
		var afterCursorLocation = cursorLocation;
		var afterSelectionRange = selectionRangle;
		UndoManager.getInstance().push(new EditAction() {			
			@Override
			public void execute_undo() {
				lines = prevLines;
				cursorLocation = prevCursorLocation;
				selectionRangle = prevSelectionRange;
				notifyAllTextObserver();
				
			}			
			@Override
			public void execute_do() {
				lines = afterLines;
				cursorLocation = afterCursorLocation;
				selectionRangle = afterSelectionRange;
				notifyAllTextObserver();				
			}
		});
	}
	public String getSelectedText() {
		LocationRange lr = selectionRangle.sorted();
		if(lr.start.row == lr.end.row) {
			String line = lines.get(lr.start.row);
			return line.substring(lr.start.column,lr.end.column);
		}
		else {
			List<String> tmp = new ArrayList<String>();
			Iterator<String> i = linesRange(lr.start.row, lr.end.row +1);
			String s;
			int removeEnterIndex = lr.end.row;
			for(int j = lr.start.row ;i.hasNext(); s = i.next(),j++) {
				String line = lines.get(j);
				int a = j == lr.start.row ? lr.start.column : 0;
				int b = j == lr.end.row ? lr.end.column  : lines.get(j).length();
				tmp.add(line.substring(a,b));
			}
			return tmp.stream().collect(Collectors.joining("\n"));			
		}
	}
}
