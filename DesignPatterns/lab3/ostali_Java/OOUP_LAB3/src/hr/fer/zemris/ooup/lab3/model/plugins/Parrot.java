package hr.fer.zemris.ooup.lab3.model.plugins;
import hr.fer.zemris.ooup.lab3.model.Animal;

public class Parrot extends Animal {
	String name;
	
	public Parrot(String name) {
		super();
		this.name = name;
	}
	@Override
	public String name() {		
		return name;
	}
	@Override
	public String greet() {
		return "pipi";
	}
	@Override
	public String menu() {
		// TODO Auto-generated method stub
		return "jabuku";
	}
}
