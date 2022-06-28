package hr.fer.ooup.lab3.zad2.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;


import hr.fer.ooup.lab3.zad2.ClipboardStack;
import hr.fer.ooup.lab3.zad2.EditAction;
import hr.fer.ooup.lab3.zad2.TextEditorModel;
import hr.fer.ooup.lab3.zad2.UndoManager;

public class VelikoSlovo implements Plugin {

	public VelikoSlovo() {
		super();
	}
	@Override
	public String getName() {
		return "Veliko slovo";
	}
	@Override
	public String getDescription() {
		return "prolazi kroz dokument i svako prvo slovo rijeèi mijenja u veliko";
	}
	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		var prevLines = new ArrayList(model.getLines());
		model.setLines(
			model.getLines().stream().map(
				(e)-> Arrays.stream(e.split(" ")).map(
					(e1) -> e1.substring(0, 1).toUpperCase() + e1.substring(1,e1.length())
					).
				collect(Collectors.joining(" "))
				).toList()
			);
		var afterLines = new ArrayList<String>(model.getLines()); 
		undoManager.getInstance().push(new EditAction() {
			@Override
			public void execute_undo() {
				model.setLines(prevLines);
				model.notifyAllTextObserver();
			}
			@Override
			public void execute_do() {
				model.setLines(afterLines);
				model.notifyAllTextObserver();	
			}
		});
	}

}
