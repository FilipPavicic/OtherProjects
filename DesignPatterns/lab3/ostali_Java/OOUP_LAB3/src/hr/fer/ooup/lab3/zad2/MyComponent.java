package hr.fer.ooup.lab3.zad2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class MyComponent extends JComponent {
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200,100);
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.red);
		g.drawLine(20, 20, 80, 20);
		g.drawLine(20, 40, 20, 100);
		g.setColor(Color.black);
		g.drawString("Ovo je prvi redak teksta", 40, 40);
		g.drawString("Ovo je drugi redak teksta", 40, 40 + g.getFontMetrics().getHeight());
	}
}