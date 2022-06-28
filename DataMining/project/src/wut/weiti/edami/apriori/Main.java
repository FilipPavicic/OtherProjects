package wut.weiti.edami.apriori;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	static List<Set<String>> items = null;
	static Apriori apriori = null;

	public static void main(String[] args) {

		infoMessage();
		if (args.length > 0) {
			readFile(args);

		}
		

		try(Scanner sc = new Scanner(System.in)){
			while (true) {
				if (items == null) {
					System.out.println("Enter the file path and delimiter");
				}

				System.out.print("Enter command: > ");
				String command = sc.nextLine();
				if(command.equals("exit")) break;
				executeCommand(command);
			}
		}


	}

	private static void readFile(String[] args) {
		String path = args[0];
		String delimiter = args.length > 1 ? args[1].split("\"")[1] : " ";
		try {
			items = Utils.readCSVTransoaction(path, delimiter);
			System.out.println("Succesfuly loaded file: "+ path);
			
		}
		catch (Exception e) {
			System.out.println("Could not open file: " + path + ", delimiter: "+ delimiter);
			return;
		}
		apriori = new Apriori(items);
		System.out.println("Default parameters: ");
		System.out.println(apriori.printParam());

	}

	private static void executeCommand(String command) {
		String[] args = command.split(" ");
		if (items == null) {
			readFile(args);
			return;
		}
		switch (args[0]) {
		case "" : break;
		case "help" : {
			printHelp();
			return;
		}
		case "s" : {
			try {
				double sup = Double.parseDouble(args[1]);
				if (sup < 0 || sup > 1) {
					System.out.println("Support must be in range [0,1]");
					return;
				}
				apriori.setSupport(sup);
			}
			catch (NumberFormatException e) {
				System.out.println(args[1] + "is not number");
			}
			catch (Exception e) {
				System.out.println("No comlited command");
			}
			break;
		}
		case "c" : {
			try {
				double conf = Double.parseDouble(args[1]);
				if (conf < 0 || conf > 1) {
					System.out.println("Confidence must be in range [0,1]");
					return;
				}
				apriori.setConfidence(conf);
			}
			catch (NumberFormatException e) {
				System.out.println(args[1] + "is not number");
			}
			catch (Exception e) {
				System.out.println("No comlited command");
			}
			break;
		}
		case "l" : {
			if (args.length < 3) {
				System.out.println("No comlited command");
				return;
			}
			setLift(args);
			break;
		}
		case "minlen" : {
			try {
				int minlen = Integer.parseInt(args[1]);
				if (minlen < 0) {
					System.out.println("Minlen must be in range [0,inf]");
					return;
				}
				apriori.setMinlen(minlen);
			}
			catch (NumberFormatException e) {
				System.out.println(args[1] + "is not number");
			}
			catch (Exception e) {
				System.out.println("No comlited command");
			}
			break;
		}
		case "maxlen" : {
			try {
				int maxlen = Integer.parseInt(args[1]);
				if (maxlen < 0) {
					System.out.println("Maxlen must be in range [0,inf]");
					return;
				}
				apriori.setMaxlen(maxlen);
			}
			catch (NumberFormatException e) {
				System.out.println(args[1] + "is not number");
			}
			catch (Exception e) {
				System.out.println("No comlited command");
			}
			break;
		}
		case "left": {
			try {
				apriori.setFilterStringLeft(args[1]);
				apriori.setLeftSide(Stream.of(args).skip(2).collect(Collectors.toSet()));
			}
			catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			catch (Exception e) {
				System.out.println("No comlited command");
			}
			break;
		}
		case "right": {
			try {
				apriori.setFilterStringRight(args[1]);
				apriori.setRightSide(Stream.of(args).skip(2).collect(Collectors.toSet()));
			}
			catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			catch (Exception e) {
				System.out.println("No comlited command");
			}
			break;
		}
		case "fitems": {

			long start = System.currentTimeMillis();
			List<ItemSet> fitems = apriori.getFrequentItemSets();
			long end = System.currentTimeMillis();
			System.out.println("Execution time: "+ (end - start) / 1000.0);
			System.out.println(fitems.size() + " results found");
			String output = fitems.stream().map(ItemSet::toString).collect(Collectors.joining("\n"));
			if (args.length > 1) {
				try {
					File yourFile = new File(args[1]);
					yourFile.createNewFile(); // if file already exists will do nothing 
					try(FileOutputStream oFile = new FileOutputStream(yourFile, false)) {
						oFile.write(output.getBytes("UTF8"));
					}
					catch (Exception e) {
						System.out.println("Could not write into the file");
					}

				}
				catch (Exception e) {
					System.out.println("File could not be open");
				}
			}
			else {
				System.out.println(output);
			}
			break;
		}
		case "rules": {

			long start = System.currentTimeMillis();
			List<Rule> fitems = apriori.getRules();
			long end = System.currentTimeMillis();
			System.out.println("Execution time: "+ (end - start) / 1000.0);
			System.out.println("Founded " + fitems.size() + " results");
			String output = fitems.stream().map(Rule::toString).collect(Collectors.joining("\n"));
			if (args.length > 1) {
				try {
					File yourFile = new File(args[1]);
					yourFile.createNewFile(); // if file already exists will do nothing 
					try(FileOutputStream oFile = new FileOutputStream(yourFile, false)) {
						oFile.write(output.getBytes("UTF8"));
					}
					catch (Exception e) {
						System.out.println("Could not write into the file");
					}

				}
				catch (Exception e) {
					System.out.println("File could not be open");
				}
			}
			else {
				System.out.println(output);
			}
			break;
		}
		case "params" : {
			System.out.println(apriori.printParam());
			break;
		}
		default :
			System.out.println("Unexpected command: " + args[0]);
		}
	}

	private static void setLift(String[] args) {
		try {
			double lift = Double.parseDouble(args[2]);
			switch (args[1]) {
			case ">": {
				apriori.setLift(l -> l > lift, args[1] + " "+ lift);
				break;
			}
			case ">=": {
				apriori.setLift(l -> l >= lift, args[1] + " "+ lift);
				break;
			}
			case "<": {
				apriori.setLift(l -> l < lift, args[1] + " "+ lift);;
				break;
			}
			case "<=": {
				apriori.setLift(l -> l <= lift, args[1] + " "+ lift);
				break;
			}
			case "==": {
				apriori.setLift(l -> l == lift, args[1] + " "+ lift);
				break;
			}
			case "!=": {
				apriori.setLift(l -> l != lift, args[1] + " "+ lift);
				break;
			}
			default:
				System.out.println("Unexpected lift operator: " + args[1]);
			}
		}
		catch (NumberFormatException e) {
			System.out.println(args[2] + "is not number");
		}

	}

	private static void printHelp() {
		System.out.println("List of available commands:");
		System.out.println("s [double number [0,1]]\t\tset Support");
		System.out.println("c [double number [0,1]]\t\tset Confidence");
		System.out.println("l [>,<.>=,<=,==,!=] [double number]\t\tset Lift predicate");
		System.out.println("minlen [int number]\t\tset minlen");
		System.out.println("maxlen [int number]\t\tset maxlen");
		System.out.println("left [any,all,none] [list of items separate by space]\t\tset Left side");
		System.out.println("right [any,all,none] [list of items separate by space]\t\tset right side");
		System.out.println("fItems [path to file where to print output - OPTIONAL]\t\tget Frequent ItemSets");
		System.out.println("rules [path to file where to print output - OPTIONAL]\t\tget Rules");
		System.out.println("params\t\tPrint parameters of apriori algorithm");

	}

	private static void infoMessage() {
		System.out.println("Welcome to Program for discovering fruequent items and rules using Apriori algorithm");
		System.out.println("Created by: Dora Doljanin & Filip Pavièiæ");
		System.out.println("EDAMI course project");
		System.out.println("Faculty of Electronics and Information Technology");
		System.out.println("Warsaw University of Technology");
		System.out.println("[Note] For exit from program write: exit\n\n");
	}
}
