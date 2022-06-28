package wut.weiti.edcs.ludo;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Tools {

	public static void log(boolean debug, Codes code, String... args) {
		if (debug) 
			System.out.println(
					"[CODE] ==> [" +
							code.toString().toUpperCase() +
							"] " +
							Stream.of(args).collect(Collectors.joining(", "))
					);
	}
	public static void gamePrint(String text) {
		System.out.println("[GAME INTERFACE] ==> "+ text);
	}



	public static String str1(Map<String, Integer> map) {
		return map.entrySet().stream().
				map(e -> {
					return "'"+e.getKey()+"': "+ e.getValue();
				}).
				collect(Collectors.joining(", ","{", "}"));
	}

	public static String str(Map<String, Map<String,Integer>> map) {
		return map.entrySet().stream().
				map(e ->{
					return "'"+e.getKey()+"': "+ str1(e.getValue());

				}).
				collect(Collectors.joining(", ","{", "}"));

	}
	public static Map<String, Integer> eval1(String text) {
		return 
				Stream.of(text.substring(1, text.length()-1).split(", ")).
				map(s -> {
					String[] ss = s.split(": ");
					String k = ss[0].substring(1, ss[0].length()-1);
					int v = Integer.parseInt(ss[1]);
					return new AbstractMap.SimpleEntry<String, Integer>(k,v);
				}).
				collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}

	public static Map<String, Map<String, Integer>> eval(String text) {
		text = text.replace("{", "#1#").replace("}", "###");
		//System.out.println(text);
		return 
				Stream.of(text.substring(3, text.length()-6).split("###, ")).
				map(s -> {
					String[] ss = s.split(": #1#");
					String k = ss[0].substring(1, ss[0].length()-1);
					Map<String, Integer> v = eval1("{"+ss[1] +"}");
					return new AbstractMap.SimpleEntry<String, Map<String, Integer>>(k,v);
				}).
				collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}

	public static List<String> eval2(String text) {
		return Stream.of(text.substring(1, text.length()-1).split(", ")).
				map(s -> s.substring(1, s.length()-1)).
				collect(Collectors.toList());
	}

	public static String str2(List<String> list) {
		return list.stream().
				map(s -> "'"+s+"'").
				collect(Collectors.joining(", ", "[", "]"));
	}

}
