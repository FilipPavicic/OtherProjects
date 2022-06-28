package wut.weiti.edami.apriori;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Utils {
	public static List<Set<String>> readCSVTransoaction(String path, String delimiter) throws FileNotFoundException, IOException {
		List<Set<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(delimiter);
		        records.add(Set.of(values));
		    }
		};
		return records;
	}
	public static List<Set<String>> readCSVTransoactionErrorHandle(String path, String delimiter) {
		try {
			return readCSVTransoaction(path,delimiter);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	
	public static Set<ItemSet> calculateSupport(Set<Set<String>> candidates, List<Set<String>> items) {
		Set<ItemSet> newItems = new HashSet<ItemSet>();
		for (Set<String> candidate : candidates) {
			int counter = 0;
			for (Set<String> item : items) {
				if (item.containsAll(candidate)) counter++;
			}
			newItems.add(new ItemSet(candidate, counter * 1.0 / items.size() ));		
		}
		return newItems;
	}
	static Map<Set<String>,List<Set<String>>> tmp_map = new HashMap<Set<String>, List<Set<String>>>();
	static Map<String,Long> codes = new HashMap<>();
	static Map<Set<String>,List<Set<String>>>  tmp_map_new = new HashMap<Set<String>, List<Set<String>>>();
	static boolean firstTime = true;
	static int side = 0;
	
	public static Set<ItemSet> calculateSupport1(Set<Set<String>> candidates, List<Set<String>> items) {
		var map = side == 0 ? tmp_map : tmp_map_new;
		var map_new = side == 1 ? tmp_map : tmp_map_new;
		Set<ItemSet> newItems = new HashSet<ItemSet>();
		for (Set<String> candidate : candidates) {
			if (!firstTime) {
				Set<String> mincode = candidate.stream()
						.map(e -> {
							var a = new HashSet<String>(candidate);
							a.remove(e);
							return a;
						})
						.findAny().get();
				items = map.get(mincode);
			} 
			int counter = 0;
			List<Set<String>> new_list = new ArrayList<Set<String>>();
			for (Set<String> item : items) {
				if (item.containsAll(candidate)) {
						new_list.add(item);
						counter++;
					}
			}
			map_new.put(candidate, new_list);
			newItems.add(new ItemSet(candidate, counter * 1.0 / items.size() ));		
		}
		side = 1 - side;
		map.clear();
		firstTime = false;
		return newItems;
	}

	public static Set<Set<String>> pruning(Set<Set<String>> candidates, Set<ItemSet> items, int length) {
		if (length < 3) {
			return candidates;
		}
		Set<Set<String>> candidates_tmp = new HashSet<Set<String>>(candidates);
		for (Set<String> candidate : candidates) {
			for (int i = 2; i < length; i++) {
				boolean isOkay = checkEveryCombination(new ArrayList<String>(candidate),items, i);
				if (!isOkay) {
					candidates_tmp.remove(candidate);
					break;
				}
			}
		}
		return candidates_tmp;
	}

	

	public static boolean checkEveryCombination(List<String> candidate, Set<ItemSet> items, int len) {
		return checkEveryCombinationRecursion(candidate,items,len,0,new String[len]);
		
	}



	private static boolean checkEveryCombinationRecursion(List<String> candidate, Set<ItemSet> items, int len, int index,
			String[] result) {
		if(len == 0) {
			if(items.contains(new ItemSet(result))) {
				return true;
			}
			return false;
		
		}
		for(int i = index; i <= candidate.size() - len; i++) {
			result[result.length - len] = candidate.get(i); 
			boolean isOkay = checkEveryCombinationRecursion(candidate, items, len -1, i +1, result);
			if(!isOkay) return false;
		}
		return true;
	}
	
	public static Set<ItemSet> getEveryCombination(List<String> candidate, int len) {
		Set<ItemSet> set = new HashSet<ItemSet>();
		getEveryCombinationRecursion(candidate,len,0,new String[len],set);
		return set;
		
	}
	
	private static void	getEveryCombinationRecursion(List<String> candidate, int len, int index,
			String[] result, Set<ItemSet> foundCombinations) {
		if(len == 0) {
			foundCombinations.add(new ItemSet(result));
			return;
		
		}
		for(int i = index; i <= candidate.size() - len; i++) {
			result[result.length - len] = candidate.get(i); 
			getEveryCombinationRecursion(candidate, len -1, i +1, result, foundCombinations);
		}
		return;
	}

	public static Set<Set<String>> getCandidates(Set<ItemSet> items, int length) {
		Set<Set<String>> tmp = new HashSet<>();
		Set<ItemSet> items1 = new HashSet<>(items);
		for (ItemSet x : items) {
			items1.remove(x);
			for (ItemSet y : items1) {
				Set<String> union = new HashSet<String>(x.getItems());
				union.addAll(y.getItems());
				if (union.size() == length) {
					tmp.add(union);
				}
			}
		}
		return tmp;
	}

	public static Set<ItemSet> getStartItemSets(List<Set<String>> items) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Set<String> set : items) {
			for (String item : set) {
				if (!map.containsKey(item)) {
					map.put(item, 1);
				}
				else {
					map.replace(item, map.get(item) + 1);
				}
			}
		}
		return map.entrySet().stream().map(e -> new ItemSet(e.getValue()* 1.0 / items.size(), e.getKey())).collect(Collectors.toSet());
	}
	
	public static String format(double n) {
		return String.format("%.5f", n).replace(',', '.');
	}
	
	public static List<String> printSetLimited(Set<String> set, int length) {
		List<String> ls = new ArrayList<String>();
		ls.add("[");
		set.stream().forEachOrdered(new Consumer<String>() {
			public void accept(String e) {
				if (ls.get(ls.size()-1).length() + e.length() > length) {
					ls.add(e);
				}
				else {
					 ls.set(ls.size()-1, ls.get(ls.size()-1) + e);
				}
			};
		});
		ls.set(ls.size()-1, ls.get(ls.size()-1) + "]");
		return ls;
	}


	
}

