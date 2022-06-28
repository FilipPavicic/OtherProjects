package hr.fer.zemris.ooup.lab3.model;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class AnimalFactory {
	static Map<String,Class<Animal>> loaderi = new HashMap<>();
	@SuppressWarnings("unchecked")
	public static Animal newInstance(String animalKind, String name) {
		Class<Animal> clazz = null;
		Animal animal = null;
		try {
			ClassLoader parent = AnimalFactory.class.getClassLoader();

			URLClassLoader newClassLoader = new URLClassLoader(
				new URL[] {
					// Dodaj jedan direktorij (završava s /)
					new File("D:/java/plugins/").toURI().toURL(),
					// Dodaj jedan konkretan JAR (ne završava s /)
					new File("D:/java/plugins-jarovi/zivotinje.jar").toURI().toURL()
				}, parent);
			if(!loaderi.containsKey(animalKind)) 
				loaderi.put(animalKind, (Class<Animal>) Class.forName("hr.fer.zemris.ooup.lab3.model.plugins."+animalKind, true, newClassLoader));
			clazz = loaderi.get(animalKind);
			Constructor<?> ctr = clazz.getConstructor(String.class);
			animal = (Animal)ctr.newInstance(name);
		} catch (ClassNotFoundException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (MalformedURLException e) {
		}
		return animal;
	}
}
