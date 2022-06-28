package wut.weiti.edami.apriori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Apriori {
	List<Set<String>> items;
	double support;
	double confidence;
	Predicate<Double> lift;
	String liftString = "not set";
	int minlen;
	int maxlen;
	Set<String> rightSide;
	Set<String> leftSide;
	String filterStringLeft = "any";
	String filterStringRight= "any";

	Set<Rule> rules;
	Set<ItemSet> frequentItemSets;

	double frequentItemSetsSupport = 0.0;
	int frequentItemSetsMaxLen = 0;

	boolean rulesLiftChanged = false;
	double rulesConfidence = 0.0;
	boolean frequentItemSetsChanged = false;  
	int rulesMinLen = 0;
	boolean rulesConstrainsChanged = false;

	Comparator<ItemSet> frequentItemSetsComparator = (a, b) -> Double.compare(b.getSupport(), a.getSupport());
	Comparator<Rule> rulesComparator = (a, b) -> Double.compare(b.getConfidence(), a.getConfidence());

	
	public Apriori(List<Set<String>> items, double support, double confidence, Predicate<Double> lift, int minlen, int maxlen, Set<String> leftSide, Set<String> rightSide) {
		this.items = items;
		this.support = support;
		this.confidence = confidence;
		this.lift = lift;
		this.minlen = minlen;
		this.maxlen = maxlen;
		this.leftSide = leftSide;
		this.rightSide = rightSide;
	}

	/**
	 * Default constructor
	 */
	public Apriori(List<Set<String>> items) {
		this(items, 0.3, 0.2, (x) -> true, 1, 10,null,null);
	}
	

	public List<ItemSet> getFrequentItemSets() {
		if (frequentItemSets == null) {
			discoverFrequentItemSets();
			frequentItemSetsChanged = true;
			return filterFrequentItemSets();
		}

		if(support < frequentItemSetsSupport  || maxlen > frequentItemSetsMaxLen) {
			discoverFrequentItemSets();
			frequentItemSetsChanged = true;
			return filterFrequentItemSets();
		}

		return filterFrequentItemSets();
	}


	public List<Rule> getRules() {
		getFrequentItemSets();

		if(rules == null) {
			discoverRules();
			return filterRules();
		}

		if(frequentItemSetsChanged) {
			discoverRules();
			return filterRules();
		}

		if(confidence < rulesConfidence || rulesLiftChanged) {
			discoverRules();
			return filterRules();
		}

		if(rulesConstrainsChanged) {
			discoverRules();
			return filterRules();
		}

		if(rulesMinLen > minlen) {
			discoverRules();
			return filterRules();
		}

		return filterRules();
	}

	public double getSupport() {
		return support;
	}

	public void setSupport(double support) {
		this.support = support;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public Predicate<Double> getLift() {
		return lift;
	}

	public void setLift(Predicate<Double> lift) {
		this.rulesLiftChanged = true;
		this.lift = lift;
		this.liftString = "could not print";
	}
	
	public void setLift(Predicate<Double> lift, String liftString) {
		setLift(lift);
		this.liftString = liftString;
	}

	public int getMinlen() {
		return minlen;
	}

	public void setMinlen(int minlen) {
		this.minlen = minlen;
	}

	public int getMaxlen() {
		return maxlen;
	}

	public void setMaxlen(int maxlen) {
		this.maxlen = maxlen;
	}

	public Set<String> getRightSide() {
		return rightSide;
	}

	public void setRightSide(Set<String> rightSide) {
		this.rightSide = rightSide;
		rulesConstrainsChanged = true;
	}

	public Set<String> getLeftSide() {
		return leftSide;
	}

	public void setLeftSide(Set<String> leftSide) {
		this.leftSide = leftSide;
		rulesConstrainsChanged = true;
	}

	public String getFilterStringLeft() {
		return filterStringLeft;
	}

	public void setFilterStringLeft(String filterStringLeft) {
		checkFilterString(filterStringLeft);
		this.filterStringLeft = filterStringLeft;
		rulesConstrainsChanged = true;
	}

	public String getFilterStringRight() {
		return filterStringRight;
	}

	public void setFilterStringRight(String filterStringRight) {
		checkFilterString(filterStringRight);
		this.filterStringRight = filterStringRight;
		rulesConstrainsChanged = true;
	}


	public Comparator<ItemSet> getFrequentItemSetsComparator() {
		return frequentItemSetsComparator;
	}

	public void setFrequentItemSetsComparator(Comparator<ItemSet> frequentItemSetsComparator) {
		this.frequentItemSetsComparator = frequentItemSetsComparator;
	}

	public Comparator<Rule> getRulesComparator() {
		return rulesComparator;
	}

	public void setRulesComparator(Comparator<Rule> rulesComparator) {
		this.rulesComparator = rulesComparator;
	}

	private List<Rule> filterRules() {
		return rules
				.stream()
				.filter((x) -> x.getConfidence() >= confidence && x.getSupport() >= support)
				.filter((x) -> lift.test(x.getLift()))
				.sorted(rulesComparator)
				.collect(Collectors.toList());
	}

	private List<ItemSet> filterFrequentItemSets() {
		return frequentItemSets
				.stream()
				.filter(x -> x.getItemcount() >= minlen && x.getItemcount() <= maxlen)
				.filter(x -> x.getSupport() >= support)
				.sorted(frequentItemSetsComparator)
				.collect(Collectors.toList());
	}

	private Set<ItemSet> filterFrequentItemSetsForRules() {
		return frequentItemSets
				.stream()
				.filter(x -> x.getItemcount() > 1)
				.filter(x -> x.getItemcount() >= minlen && x.getItemcount() <= maxlen)
				.filter(x -> x.getSupport() >= support)
				.filter(x -> isSsatisfieLeft(x,true))
				.filter(x -> isSsatisfieRight(x,true))
				.collect(Collectors.toSet());
	}


	private void discoverFrequentItemSets() {
		int length = 2;
		Set<ItemSet> discovereditems = Utils.getStartItemSets(items);
		discovereditems.removeIf((e) -> e.getSupport() < support);
		while (length <= maxlen) {
			Set<Set<String>> candidates = Utils.getCandidates(discovereditems, length);
			candidates = Utils.pruning(candidates, discovereditems, length);
			Set<ItemSet> newItems = Utils.calculateSupport(candidates,items);
			newItems.removeIf((e) -> e.getSupport() < support);
			discovereditems.addAll(newItems);
			length++;
			if (newItems.isEmpty()) break;

		} 
		frequentItemSets = discovereditems;
		frequentItemSetsSupport = support;
		frequentItemSetsMaxLen = maxlen;
	}



	private void discoverRules() {
		Map<ItemSet,Double> frequentItemsSupports = frequentItemSets.stream().collect(Collectors.toMap(x -> x, ItemSet::getSupport));
		
		Set<ItemSet> frequentItems = filterFrequentItemSetsForRules();
		rules = new HashSet<Rule>();
		for (ItemSet itemSet : frequentItems) {
			Set<ItemSet> right = Utils.getEveryCombination(new ArrayList<>(itemSet.items), 1);
			for (ItemSet rightCandidate : right) {
				if (!isSsatisfieRight(rightCandidate, false)) continue;
				ItemSet leftCandidate = itemSet.copy();
				leftCandidate.removeAll(rightCandidate);
				if (!isSsatisfieLeft(leftCandidate, false)) continue;
				double candidateSupport = frequentItemsSupports.get(itemSet);
				double candidateConfidence = candidateSupport / frequentItemsSupports.get(leftCandidate);
				if (candidateConfidence <= confidence ) continue;
				double candidateLift = candidateConfidence /  frequentItemsSupports.get(rightCandidate);
				if (!lift.test(candidateLift) ) continue;
				rules.add(new Rule(leftCandidate,rightCandidate,candidateSupport,candidateConfidence,candidateLift));	
			}


		}
		rulesLiftChanged = false;
		rulesConfidence = confidence;
		rulesMinLen = minlen;
		frequentItemSetsChanged = false;
		rulesConstrainsChanged = false;
	}

	private boolean isSsatisfieLeft(ItemSet item, boolean entire) {
		return isSatisifie(item,leftSide, filterStringLeft,entire);
	}

	private boolean isSsatisfieRight(ItemSet item, boolean entire) {
		return isSatisifie(item,rightSide, filterStringRight,entire);
	}

	private boolean isSatisifie(ItemSet item, Set<String> side, String filterString, boolean entire) {
		if(side == null) return true;

		switch (filterString) {
		case "any": {	
			return side.stream().anyMatch(y -> item.contains(y));
		}
		case "all": {	
			return side.stream().allMatch(y -> item.contains(y));
		}
		case "none": {	
			if (entire) return true;				
			return  !side.stream().anyMatch(y -> item.contains(y));
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + filterString);
		}
	}
	
	private void checkFilterString(String s) {
		if (s.equals("all")) return;
		if (s.equals("any")) return;
		if (s.equals("none")) return;
		throw new IllegalArgumentException("Unexpected filter value: " + s);

		
	}
	
	public String printParam() {
		String out = "";
		out += "Support: " + support +", ";
		out += "Confidence: " + confidence +", ";
		out += "Lift: " + (lift == null ? "not set" : liftString) + ", ";
		out += "Minlen: " + minlen +", ";
		out += "MaxLen: " + maxlen + "\n";
		out += "Left Side: " +  (leftSide == null ? "not set" :  filterStringLeft +", items: " +leftSide) + "\n"; 
		out += "Right Side: " + (rightSide == null ? "not set" :  filterStringRight +", items: " +rightSide) + "\n"; 
		return out;
		
		
		
		
		
	}
	
	


}
