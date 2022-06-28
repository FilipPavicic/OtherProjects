package hr.fer.ooup.lab3.zad2.plugins;

import javax.swing.JOptionPane;

import hr.fer.ooup.lab3.zad2.ClipboardStack;
import hr.fer.ooup.lab3.zad2.TextEditorModel;
import hr.fer.ooup.lab3.zad2.UndoManager;

public class Statistika implements Plugin {
	@Override
	public String getName() {
		return "Statistika";
	}
	@Override
	public String getDescription() {
		return "plugin koji broji koliko ima redaka, rijeèi i slova u dokumentu i to prikazuje korisniku u dijalogu.";
	}
	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		int line = model.getLines().size();
		int words = model.getLines().stream().mapToInt((e) -> e.split(" ").length).sum();
		int letters = model.getLines().stream().mapToInt((e) -> e.length()).sum();
		JOptionPane.showMessageDialog(null, "Dokumnet sadrži "+line+" linija, "+words+" rijeèi i "+letters+" slova");

	}
}
