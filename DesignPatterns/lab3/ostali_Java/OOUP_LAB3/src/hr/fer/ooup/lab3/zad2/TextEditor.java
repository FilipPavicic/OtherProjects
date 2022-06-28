package hr.fer.ooup.lab3.zad2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentListener;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.Line;
import javax.swing.JComponent;

public class TextEditor extends JComponent{

	private static final Object LOCK = new Object();
	boolean hasUndo;
	boolean hasRedo;
	TextEditorModel model;
	JComponent component;
	boolean tiktak = true;
	ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
	ScheduledFuture<?> future;
	static Timer timer;
	ClipboardStack clipboardStack;
	
	public TextEditor(String text) {
		component = this;
		model = new TextEditorModel(text);
		model.addCursorObserver((l) -> {
			resetTimer(timer);
			model.changeSelectionRange();
			component.repaint();
			});
		model.addTextObserver(() -> repaint());
		clipboardStack = new ClipboardStack();
		clipboardStack.addClipboardObserver(()-> {
			if(clipboardStack.textOut!= null) model.insert(clipboardStack.textOut);
		});
		UndoManager.getInstance().addListener(()-> {
			hasUndo = !UndoManager.getInstance().undoStack.isEmpty();
			hasRedo = !UndoManager.getInstance().redoStack.isEmpty();
		});		
		s = Executors.newSingleThreadScheduledExecutor();
		future = s.scheduleWithFixedDelay(() -> {
			tiktak = !tiktak;
			component.repaint();
		}, 700, 700, TimeUnit.MILLISECONDS);
		
		
	}	
	private void resetTimer(Timer timer) {
		future.cancel(true);
		tiktak = true;
		future = s.scheduleWithFixedDelay(() -> {
			tiktak = !tiktak;
			component.repaint();
		}, 700, 700, TimeUnit.MILLISECONDS);
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700,700);
	}	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("monospaced", Font.PLAIN, g2.getFont().getSize()));
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Color.black);
		int y = 0;
		Iterator<String> i = model.allLines();
		while(i.hasNext()) {
			String line = i.next();
			int topPadding = g2.getFontMetrics().getHeight() - g2.getFontMetrics().getAscent();
			int index = y / g2.getFontMetrics().getHeight();
			if(index == model.getCursorLocation().row && tiktak) {
				int axisX = g2.getFontMetrics().stringWidth(line.substring(0, model.getCursorLocation().column));
				g2.drawLine(axisX, y + topPadding, axisX, y + g2.getFontMetrics().getHeight() + topPadding);
			}
			LocationRange lr = model.selectionRangle.sorted();
			if(index >= lr.start.row && index <= lr.end.row) {
				g2.setColor(new Color(51,204,255));
				if(lr.start.row == lr.end.row) {
					g2.fillRect(
							(lr.start.column) * g2.getFontMetrics().stringWidth("a"),
							y+ topPadding, 
							(lr.end.column - lr.start.column) * g2.getFontMetrics().stringWidth("a"),
							g2.getFontMetrics().getHeight() +1
					);	
				}
				else if(index == lr.start.row) {
					g2.fillRect(
							(lr.start.column) * g2.getFontMetrics().stringWidth("a"),
							y+ topPadding, 
							(model.lines.get(index).length() - lr.start.column	) * g2.getFontMetrics().stringWidth("a"),
							g2.getFontMetrics().getHeight() +1
					);
					
				}
				else if(index == lr.end.row) {
					g2.fillRect(
							0,
							y+ topPadding, 
							(lr.end.column) * g2.getFontMetrics().stringWidth("a"),
							g2.getFontMetrics().getHeight() +1
					);
				}
				else {
					g2.fillRect(
							0,
							y+ topPadding, 
							g2.getFontMetrics().stringWidth(line),
							g2.getFontMetrics().getHeight() +1
					);
				}
				g2.setColor(Color.black);
			}
			g2.drawString(line, 0, y+= g2.getFontMetrics().getHeight());
		}
	}	
	void dispose(){
		s.shutdownNow();
	}
}
