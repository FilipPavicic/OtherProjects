package hr.fer.zemris.ooup.lab3;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import hr.fer.zemris.ooup.lab3.model.Animal;
import hr.fer.zemris.ooup.lab3.model.AnimalFactory;

public class Main {
	public static void main(String[] args) {
		Map<String, String> životinje = new HashMap<String, String>();
			životinje.put("Miško","Parrot");
			for (Entry<String, String> entry : životinje.entrySet()) {
				Animal a = AnimalFactory.newInstance(entry.getValue(), entry.getKey());
				a.animalPrintGreeting();
				a.animalPrintMenu();
		}
	}
}
