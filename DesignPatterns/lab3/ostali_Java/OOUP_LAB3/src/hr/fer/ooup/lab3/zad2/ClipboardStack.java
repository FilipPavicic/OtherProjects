package hr.fer.ooup.lab3.zad2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ClipboardStack extends Stack<String>{
	String textOut;
	List<ClipboardObserver > clipboardObservers  = new ArrayList<>();
	
	public void addClipboardObserver(ClipboardObserver  o) {
		clipboardObservers.add(o);
	}	
	public void removeClipboardObserver(ClipboardObserver  o) {
		clipboardObservers.remove(o);
	}
	public void notifyAllClipboardObserver() {
		for (ClipboardObserver  clipboardObserver : clipboardObservers) {
			clipboardObserver.updateClipboard();
		}
	}
	public ClipboardStack() {
		super();
	}
	@Override
	public String push(String item) {
		String pushIntem =  super.push(item);
		textOut = null;
		notifyAllClipboardObserver();
		return pushIntem;
	}	
	@Override
	public synchronized String pop() {
		if(this.isEmpty()) return null; 
		String popItem =  super.pop();
		textOut = null;
		notifyAllClipboardObserver();
		return popItem;		
	}	
	@Override
	public synchronized String peek() {
		if(this.isEmpty()) return null; 
		textOut = super.peek();
		notifyAllClipboardObserver();
		return textOut;	
	}
}