package hr.fer.ooup.lab3.zad2.plugins;

import hr.fer.ooup.lab3.zad2.ClipboardStack;
import hr.fer.ooup.lab3.zad2.TextEditorModel;
import hr.fer.ooup.lab3.zad2.UndoManager;

public interface Plugin {
	  String getName(); // ime plugina (za izbornicku stavku)
	  String getDescription(); // kratki opis
	  void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}