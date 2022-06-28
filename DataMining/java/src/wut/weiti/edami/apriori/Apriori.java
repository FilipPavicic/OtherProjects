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
	boolean rulesConstrainsChanged = false
	
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
	public Set<Rule> getRules() {
		getFrequentItemSets();
		
		if(rules == null) {
			discoverRules();
			return filterRules();
		}
		
		if(frequentItemSetsChanged) {
			discoverRules();
			return filterRules();
		}
		
		if(confidence > rulesConfidence || rulesLiftChanged) {
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
	}

	public Set<String> getLeftSide() {
		return leftSide;
	}

	public void setLeftSide(Set<String> leftSide) {
		this.leftSide = leftSide;
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

	private Set<Rule> filterRules() {
		return rules
			.stream()
			.filter((x) -> x.getConfidence() >= confidence && x.getSupport() >= support)
			.filter((x) -> lift.test(x.getLift()))
			.sorted(rulesComparator)
			.collect(Collectors.toSet());
	}
	
	private List<ItemSet> filterFrequentItemSets() {
		return frequentItemSets
			.stream()
			.filter(x -> x.getItemcount() >= minlen && x.getItemcount() <= maxlen)
			.filter(x -> x.getSupport() >= support)
			.sorted(frequentItemSetsComparator)
			.collect(Collectors.toList());
	}
	
	private Map<ItemSet,Double> filterFrequentItemSetsForRules() {
		return frequentItemSets
			.stream()
			.filter(x -> x.getItemcount() > 1)
			.filter(x -> x.getItemcount() >= minlen && x.getItemcount() <= maxlen)
			.filter(x -> x.getSupport() >= support)
			.filter(x -> isSsatisfieLeft(x))
			.filter(x -> isSsatisfieRight(x))
			.collect(Collectors.toMap(x -> x, ItemSet::getSupport));
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
		Map<ItemSet,Double> frequentItems = filterFrequentItemSetsForRules();
		for (ItemSet itemSet : frequentItems.keySet()) {
			for (int i = 2; i <= itemSet.size(); i++) {
				Set<ItemSet> right = Utils.getEveryCombination(new ArrayList<>(itemSet.items), i);
				for (ItemSet rightCandidate : right) {
					if (isSsatisfieRight(rightCandidate)) continue;
					ItemSet leftCandidate = itemSet.copy();
					leftCandidate.removeAll(rightCandidate);
					if (isSsatisfieLeft(leftCandidate)) continue;
					double candidateSupport = frequentItems.get(itemSet);
					double candidateConfidence = candidateSupport / frequentItems.get(leftCandidate);
					if (candidateConfidence < confidence ) continue;
					double candidateLift = candidateConfidence /  frequentItems.get(rightCandidate);
					if (lift.test(candidateLift) ) continue;
					rules.add(new Rule(leftCandidate,rightCandidate,candidateSupport,candidateConfidence,candidateLift));						
				}
			}
			
		}
		rulesLiftChanged = false;
		rulesConfidence = confidence;
		rulesMinLen = minlen;
		frequentItemSetsChanged = false;
		rulesLeftSide = new HashSet<>(leftSide);
		rulesRightSide = new HashSet<>(rightSide);
 	}
	
	private boolean isSsatisfieLeft(ItemSet item) {
		return isSatisifie(item,leftSide, filterStringLeft);
	}
	
	private boolean isSsatisfieRight(ItemSet item) {
		return isSatisifie(item,rightSide, filterStringRight);
	}
	private boolean isSatisifie(ItemSet item, Set<String> side, String filterString) {
		if (side == null) return true;
		switch(filterString) {
		case
		}
	}
	
	
	
}

