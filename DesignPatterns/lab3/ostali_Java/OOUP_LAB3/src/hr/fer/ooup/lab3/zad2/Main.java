package hr.fer.ooup.lab3.zad2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorListener;

import hr.fer.ooup.lab3.zad2.plugins.Plugin;
import hr.fer.zemris.ooup.lab3.model.Animal;

public class Main extends JFrame {

	String text = "Ovo je inicijalni test\nKoji se proteže u više redova\nOvo bi trebalo biti u 3 redu.\nOvo je jako dugaèka reèenica te zbog toga se mora prelomiti automatski u dva retka nadam se da hoæe.";
	TextEditor textEditor;
	Action openAction;
	Action closeAction;
	Action saveAction;
	Action undoAction;
	Action redoAction;
	Action copyAction;
	Action cutAction;
	Action pasteAction;
	Action pasteAndTakeAction;
	Action deleteSelectionAction;
	Action clearDocumentAction;
	Action cursorStartAction;
	Action cursorEndAction;
	Path filePath;
	List<Plugin> plugins;
	private static final String PLUGINS_PATH = "\\bin\\hr\\fer\\ooup\\lab3\\zad2\\plugins";

	public Main() {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(20, 20);
		plugins = readPlugins();
		initGUI();
		pack();
		this.setFocusable(true);
	     this.requestFocus();
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				textEditor.dispose();
				e.getWindow().dispose();
			}
		});
	}

	private void initGUI() {
		Container container = this.getContentPane();

		setLayout(new BorderLayout());
		MyComponent mc = new MyComponent();
		add(mc, BorderLayout.WEST);
		textEditor = new TextEditor(text);
		createAction(this);
		add(textEditor,BorderLayout.CENTER);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_SHIFT -> {
					textEditor.model.isSelected = false;
				}
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isIdentifierIgnorable(c) && (int)e.getKeyChar() != 65535 ) {
					textEditor.model.isSelected = false;
					textEditor.model.insert(c, true);
					return;
				}
				System.out.println(e.getKeyCode());


				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER -> {
					JFrame d = (JFrame)e.getSource();
					d.dispose();
				}
				case KeyEvent.VK_RIGHT -> textEditor.model.moveCursorRight();
				case KeyEvent.VK_LEFT -> textEditor.model.moveCursorLeft();
				case KeyEvent.VK_UP -> textEditor.model.moveCursorUp();
				case KeyEvent.VK_DOWN -> textEditor.model.moveCursorDown();
				case KeyEvent.VK_BACK_SPACE -> textEditor.model.deleteBefore();
				case KeyEvent.VK_DELETE -> textEditor.model.deleteAfter();
				case KeyEvent.VK_SHIFT -> {
					textEditor.model.isSelected = true;
				}
				case 67 -> textEditor.clipboardStack.push(textEditor.model.getSelectedText());
				case 86 -> {
					if(textEditor.model.isSelected == false) textEditor.clipboardStack.peek();
					else {
						textEditor.model.isSelected = false;
						textEditor.clipboardStack.pop();
						textEditor.model.isSelected = true;
					}
				}
				case 88 -> {
					textEditor.clipboardStack.push(textEditor.model.getSelectedText());
					textEditor.model.deleteSelected();
				}
				case 90 -> {
					if(textEditor.hasUndo) UndoManager.getInstance().undo();
				}
				case 89 -> {
					if(textEditor.hasRedo)UndoManager.getInstance().redo();
				}


				}
			}
		});
		createMenus();
		createStatusBar();
	}
	private void createStatusBar() {
		JLabel lengthLabel = new JLabel(textLabel(textEditor.model.lines.size(), 0, 0));
		lengthLabel.setBorder(BorderFactory.createMatteBorder(
				0, 1, 0, 0, Color.gray));
		add(lengthLabel, BorderLayout.SOUTH);
		textEditor.model.addCursorObserver((l)-> 
		lengthLabel.setText(textLabel(
				textEditor.model.lines.size(),
				textEditor.model.getCursorLocation().row,
				textEditor.model.getCursorLocation().column
				))
		);		
	}	
	String textLabel(int lines, int cursorRow, int cursorColumn) {
		return "      Broj linije: "+lines+", redak kursora: " + (cursorRow + 1) + ", stupac kursora:" + (cursorColumn +1);  
	}
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		fileMenu.add(new JMenuItem(openAction));
		fileMenu.add(new JMenuItem(saveAction));
		fileMenu.add(new JMenuItem(closeAction));
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		editMenu.add(new JMenuItem(undoAction));
		editMenu.add(new JMenuItem(redoAction));
		editMenu.add(new JMenuItem(cutAction));
		editMenu.add(new JMenuItem(copyAction));
		editMenu.add(new JMenuItem(pasteAction));
		editMenu.add(new JMenuItem(pasteAndTakeAction));
		editMenu.add(new JMenuItem(deleteSelectionAction));
		editMenu.add(new JMenuItem(clearDocumentAction));
		JMenu moveMenu = new JMenu("Move");
		menuBar.add(moveMenu);
		moveMenu.add(new JMenuItem(cursorStartAction));
		moveMenu.add(new JMenuItem(cursorEndAction));
		JMenu pluginMenu = new JMenu("Plugins");
		for (Plugin pl : plugins) {
			pluginMenu.add(new JMenuItem(pluginAction(pl)));
		}
		menuBar.add(pluginMenu);
		this.setJMenuBar(menuBar);
		JToolBar t = new JToolBar();
		t.add(new JButton(undoAction));
		t.add(new JButton(redoAction));
		t.add(new JButton(cutAction));
		t.add(new JButton(copyAction));
		t.add(new JButton(pasteAction));
		this.add(t, BorderLayout.PAGE_START);
	}
	private void createAction(Component parent) {
		openAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Path path = fileChoice(parent);
				if(path == null) return;
				else {
					if(!Files.isReadable(path)) throw new IllegalArgumentException("Datoteka: " + path.toAbsolutePath() + "ne postoji!");
					filePath = path;
					try {
						byte[] bytes = Files.readAllBytes(path);
						text = new String(bytes, StandardCharsets.UTF_8);
						textEditor.model.lines = new  ArrayList<String>(Arrays.asList(text.split("\n")));
						textEditor.model.setCursorLocation(new Location(0, 0));
						textEditor.repaint();
					} catch (IOException e1) {
						throw new IllegalArgumentException("Pogreška pri uèitavanju datoteke: " + path.toAbsolutePath());
					}
				}
			}
		};
		openAction.putValue(Action.NAME,"Open");
		saveAction = new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(filePath != null) {
					byte[] podatci = textEditor.model.lines.stream().collect(Collectors.joining("\n")).getBytes(StandardCharsets.UTF_8);
					try {
						Files.write(filePath, podatci);
					} catch (IOException e1) {
						throw new IllegalArgumentException("Nije moguæe spremiti datoteku na putanju: " + filePath);
					}
				} else {
					Path path = fileChoice(parent);
					if(path == null) return;
					try {
						byte[] podatci = textEditor.model.lines.stream().collect(Collectors.joining("\n")).getBytes(StandardCharsets.UTF_8);
						try {
							Files.write(path, podatci);
							filePath = path;
						} catch (IOException e1) {
							throw new IllegalArgumentException("Nije moguæe spremiti datoteku na putanju: " + path);
						}
					} catch (IllegalStateException e1) {
						JOptionPane.showMessageDialog(parent,
								"Datoteka na toj putanji je veæ otvorena",
								"Warning",
								JOptionPane.WARNING_MESSAGE  );
					}
				}			
			}
		};
		saveAction.putValue(Action.NAME,"Save");
		closeAction = new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame d = (JFrame)parent;
				d.dispose();			
			}
		};
		closeAction.putValue(Action.NAME,"Close");
		undoAction = new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textEditor.hasUndo) UndoManager.getInstance().undo();	
			}		
		};
		undoAction.setEnabled(false);
		UndoManager.getInstance().addListener(()-> {
			undoAction.setEnabled(!UndoManager.getInstance().undoStack.isEmpty());
		});
		undoAction.putValue(Action.NAME,"Undo");
		redoAction = new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textEditor.hasRedo) UndoManager.getInstance().redo();	
				parent.requestFocus();
			}		
		};
		UndoManager.getInstance().addListener(()-> {
			redoAction.setEnabled(!UndoManager.getInstance().redoStack.isEmpty());
		});
		redoAction.setEnabled(false);
		redoAction.putValue(Action.NAME,"Redo");
		cutAction = new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				textEditor.clipboardStack.push(textEditor.model.getSelectedText());
				textEditor.model.deleteSelected();
				parent.requestFocus();			
			}
		};
		cutAction.setEnabled(false);
		textEditor.model.addCursorObserver((l) -> cutAction.setEnabled(!textEditor.model.selectionRangle.isStartEndSame()));
		cutAction.putValue(Action.NAME,"Cut");	
		copyAction = new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				textEditor.clipboardStack.peek();
				parent.requestFocus();			
			}
		};
		copyAction.setEnabled(false);
		textEditor.model.addCursorObserver((l) -> copyAction.setEnabled(!textEditor.model.selectionRangle.isStartEndSame()));
		copyAction.putValue(Action.NAME,"Copy");
		pasteAction = new AbstractAction() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textEditor.model.isSelected == false) textEditor.clipboardStack.peek();
				parent.requestFocus();		
			}
		};
		pasteAction.setEnabled(false);
		textEditor.clipboardStack.addClipboardObserver(() -> pasteAction.setEnabled(!textEditor.clipboardStack.isEmpty()));
		pasteAction.putValue(Action.NAME,"Paste");
		pasteAndTakeAction = new AbstractAction() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textEditor.model.isSelected == false) textEditor.clipboardStack.pop();
				parent.requestFocus();			
			}
		};
		pasteAndTakeAction.setEnabled(false);
		textEditor.clipboardStack.addClipboardObserver(() -> pasteAndTakeAction.setEnabled(!textEditor.clipboardStack.isEmpty()));
		pasteAndTakeAction.putValue(Action.NAME,"Past & Take");
		deleteSelectionAction = new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				textEditor.model.deleteSelected();
				parent.requestFocus();			
			}
		};
		deleteSelectionAction.setEnabled(false);
		textEditor.model.addCursorObserver((l) -> deleteSelectionAction.setEnabled(!textEditor.model.selectionRangle.isStartEndSame()));
		deleteSelectionAction.putValue(Action.NAME,"Delete Section");
		clearDocumentAction = new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				textEditor.model.lines = new  ArrayList<String>();
				textEditor.model.lines.add("");
				textEditor.model.setCursorLocation(new Location(0, 0));
				textEditor.repaint();
				parent.requestFocus();				
			}
		};
		clearDocumentAction.putValue(Action.NAME, "Clear document");
		cursorStartAction = new AbstractAction("Cursor to document start") {			
			@Override
			public void actionPerformed(ActionEvent e) {
				textEditor.model.setCursorLocation(new Location(0, 0));
				textEditor.repaint();
				parent.requestFocus();				
			}
		};
		cursorEndAction = new AbstractAction("Cursor to document end") {			
			@Override
			public void actionPerformed(ActionEvent e) {
				textEditor.model.setCursorLocation(new Location(textEditor.model.lines.size() -1, textEditor.model.lines.get(textEditor.model.lines.size() -1).length()));
				textEditor.repaint();
				parent.requestFocus();				
			}
		};
	}	
	private Action pluginAction(Plugin pl) {
		Action action = new AbstractAction(pl.getName()) {		
			@Override
			public void actionPerformed(ActionEvent e) {
				pl.execute(textEditor.model, UndoManager.getInstance(), textEditor.clipboardStack);
				textEditor.repaint();			
			}			
		};
		action.putValue(Action.SHORT_DESCRIPTION, pl.getDescription());
		
		return action;
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new Main().setVisible(true);
		});
	}
	private static Path fileChoice(Component parent) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open file");
		if(fc.showOpenDialog(parent)!=JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File fileName = fc.getSelectedFile();
		Path filePath = fileName.toPath();
		return filePath;
	}	
	private List<Plugin> readPlugins() {
		List<Plugin> plugins = new ArrayList<Plugin>();
		String fullPluginsPath = new File("").getAbsolutePath() + PLUGINS_PATH;
		File folder = new File(fullPluginsPath);
		URLClassLoader newClassLoader;
		try {
			newClassLoader = new URLClassLoader(
					new URL[] {
					folder.toURI().toURL()
					});
			for(File f : folder.listFiles()) {
				String name = f.getName();
				if(name.endsWith(".class")) {
					String className = "hr.fer.ooup.lab3.zad2.plugins." +name.substring(0, name.length() - 6);
					Class<?> clasa = Class.forName(className ,true, newClassLoader);
					if(clasa.isInterface()) continue;
					try{
						Class<Plugin> classPlugin = (Class<Plugin>) clasa;
						Constructor<?> ctr = classPlugin.getConstructor();
						plugins.add((Plugin)ctr.newInstance());
					}catch (ClassCastException e) {
						continue;
					
					} catch (NoSuchMethodException e) {
					} catch (SecurityException e) {
					} catch (InstantiationException e) {
					} catch (IllegalAccessException e) {
					} catch (IllegalArgumentException e) {
					} catch (InvocationTargetException e) {
					}
				}
			}
		} catch (MalformedURLException e) {;
		} catch (ClassNotFoundException e) {
		}		
		return plugins;
	}
}
