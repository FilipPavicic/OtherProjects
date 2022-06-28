package hr.fer.ooup.lab4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.ooup.lab4.models.CompositeShape;
import hr.fer.ooup.lab4.models.DocumentModel;
import hr.fer.ooup.lab4.models.GraphicalObject;
import hr.fer.ooup.lab4.models.LineSegment;
import hr.fer.ooup.lab4.models.Oval;
import hr.fer.ooup.lab4.models.SVGRendererImpl;
import hr.fer.ooup.lab4.states.AddShapeState;
import hr.fer.ooup.lab4.states.EraserState;
import hr.fer.ooup.lab4.states.IdleState;
import hr.fer.ooup.lab4.states.SelectShapeState;
import hr.fer.ooup.lab4.states.State;

public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;

	private  class Platno extends JComponent{
		private static final long serialVersionUID = 1L;
		DocumentModel dm;
		boolean shiftDown = false;
		boolean ctrlDown = false;

		public Platno(DocumentModel dm) {
			setFocusable(true);
			this.dm = dm;
			this.dm.addDocumentModelListener(this::repaint);
			keyboardAndMouseListeners();
		}

		private void keyboardAndMouseListeners() {
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					System.out.println("Usao");
					switch (e.getKeyCode()) {
					case KeyEvent.VK_SHIFT -> shiftDown = true;
					case KeyEvent.VK_CONTROL -> ctrlDown = true;
					case KeyEvent.VK_ESCAPE -> changeState(new IdleState());
					}
					currentState.keyPressed(e.getKeyCode());
				}
				@Override
				public void keyReleased(KeyEvent e) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_SHIFT -> shiftDown = false;
					case KeyEvent.VK_CONTROL -> ctrlDown = false;
					}
				}
			});
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					currentState.mouseDown(new Point(e.getPoint().x,e.getPoint().y), shiftDown, ctrlDown);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					currentState.mouseUp(new Point(e.getPoint().x,e.getPoint().y), shiftDown, ctrlDown);
				}
			});
			addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					currentState.mouseDragged(new Point(e.getPoint().x,e.getPoint().y));
				}	
			});

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(500,500);
		}
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, 500, 500);
			Renderer r = new G2DRendererImpl(g2d);
			for(GraphicalObject object : dm.list()) {
				object.render(r);
				currentState.afterDraw(r, object);
			}
			currentState.afterDraw(r);
		}
	}

	List<GraphicalObject> objects;
	DocumentModel dm;
	Platno platno;
	private State currentState;
	private static final Map<String,GraphicalObject> TAGS = new HashMap<>();
	static {
		GraphicalObject o = new CompositeShape(null);
		TAGS.put(o.getShapeID(), o);
	}

	public GUI(List<GraphicalObject> objects) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setFocusable(false);
		setLocation(20, 20);
		currentState = new IdleState();
		this.objects = objects;
		for (GraphicalObject graphicalObject : objects) {
			TAGS.put(graphicalObject.getShapeID(), graphicalObject);
		}
		dm = new DocumentModel();
		initGUI();
		pack();

	}

	private void initGUI() {
		setLayout(new BorderLayout());
		platno = new Platno(dm);
		add(platno,BorderLayout.CENTER);
		platno.requestFocusInWindow();
		JToolBar t = new JToolBar();

		t.add(new JButton(createAction("Uèitaj", ()->{
			try {
				Myimport();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		})));
		t.add(new JButton(createAction("Pohrani", ()->{
			try {
				Myexport();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		})));
		t.add(new JButton(createAction("SVG export", ()->{
			try {
				SVGexport();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		})));
		for(GraphicalObject obj: objects) {
			t.add(new JButton(createAction(obj.getShapeName(), ()->changeState(new AddShapeState(obj, dm)))));
		}
		t.add(new JButton(createAction("Selektiraj", ()->changeState(new SelectShapeState(dm)))));
		t.add(new JButton(createAction("Brisalo", ()->changeState(new EraserState(dm)))));
		this.add(t, BorderLayout.PAGE_START);

	}
	
	private void Myimport() throws IOException {
		Path filePath = getPath();
		byte[] bytes = Files.readAllBytes(filePath);
		String text = new String(bytes, StandardCharsets.UTF_8);
		List<String> rows = new  ArrayList<String>(Arrays.asList(text.split("\n")));
		Stack<GraphicalObject> stog = new Stack<GraphicalObject>();
		try {
			for(String row: rows) {
				row = row.trim();
				String[] parts = row.split(" ",2);
				if(parts.length != 2) throw new IllegalArgumentException("Nepoznati row");
				GraphicalObject go = TAGS.get(parts[0]);
				if(go == null) throw new IllegalArgumentException("TAG: "+ parts[0] +" nije pronaðen.");
				go.load(stog, parts[1]);
			}
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		}
		for (GraphicalObject graphicalObject : stog) {
			dm.addGraphicalObject(graphicalObject);
			dm.notifyListeners();
		}
		
	}

	private void Myexport() throws IOException {
		Path filePath = getPath();
		List<String> output = new ArrayList<String>();
		for(GraphicalObject o: dm.list()) {
			o.save(output);
		}
		byte[] podatci = output.stream().collect(Collectors.joining("\n")).getBytes(StandardCharsets.UTF_8);
		Files.write(filePath, podatci);
	}

	private void SVGexport() throws IOException {
		Path filePath = getPath();
		SVGRendererImpl r = new SVGRendererImpl(filePath);
		for(GraphicalObject o: dm.list()) {
			o.render(r);
		}
		r.close();
	}
	private Path getPath() throws IOException  {
		Path filePath = fileChoice(this);
		if(filePath == null) return null;
		if(!Files.exists(filePath)) Files.createFile(filePath);
		if(!Files.isReadable(filePath)) throw new IllegalArgumentException("Datoteka: " + filePath.toAbsolutePath() + "ne postoji!");
		return filePath;
	}

	private Action createAction(String name,Runnable onPress) {
		return new AbstractAction(name) {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				onPress.run();
				platno.requestFocusInWindow();
			}
		};
	}
	private void changeState(State newState) {
		this.currentState.onLeaving();
		this.currentState = newState;
		dm.notifyListeners();
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

	public static void main(String[] args) {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				List<GraphicalObject> objects = new ArrayList<>();

				objects.add(new LineSegment());
				objects.add(new Oval());

				GUI gui = new GUI(objects);
				gui.setVisible(true);
				gui.platno.requestFocusInWindow();

			}
		};
		SwingUtilities.invokeLater(r);
	}
}
