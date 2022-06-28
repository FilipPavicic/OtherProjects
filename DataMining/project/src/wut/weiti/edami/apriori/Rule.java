package wut.weiti.edami.apriori;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Rule {
	double support;
	double confidence;
	double lift;
	ItemSet leftSide;
	ItemSet rightSide;
	
	public Rule(ItemSet leftSide, ItemSet rightSide, double support, double confidence, double lift) {
		super();
		this.support = support;
		this.confidence = confidence;
		this.lift = lift;
		this.leftSide = leftSide;
		this.rightSide = rightSide;
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

	public double getLift() {
		return lift;
	}

	public void setLift(double lift) {
		this.lift = lift;
	}

	public ItemSet getLeftSide() {
		return leftSide;
	}

	public void setLeftSide(ItemSet leftSide) {
		this.leftSide = leftSide;
	}

	public ItemSet getRightSide() {
		return rightSide;
	}

	public void setRightSide(ItemSet rightSide) {
		this.rightSide = rightSide;
	}
	
	@Override
	public String toString() {
		int size = 50;
		List<String> ls = Utils.printSetLimited(leftSide.items, size);
		List<String> rs = Utils.printSetLimited(rightSide.items, size);
		String out = "";
		for (int i = 0; i < Math.max(ls.size(), rs.size()); i++) {
			String a = i < ls.size() ? ls.get(i) : "";
			String b = i == 0 ? "-->" : "";
			String c = i < rs.size() ? rs.get(i) : "";
			String d = i == 0 ? "support=" + Utils.format(support)
					+ ", confidence=" + Utils.format(confidence)
					+ ", lift=" + Utils.format(lift) : "";
			out += String.format("%-"+(size+2)+"s%-4s%-"+(size+2)+"s%s\n", a,b,c,d);
		}
		return out;
	}
	

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftSide == null) ? 0 : leftSide.hashCode());
		result = prime * result + ((rightSide == null) ? 0 : rightSide.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (Math.abs(confidence - other.confidence) > 10e-4) 
			return false;
		if (leftSide == null) {
			if (other.leftSide != null)
				return false;
		} else if (!leftSide.equals(other.leftSide)) 
			return false;
		if (Math.abs(lift - other.lift) > 10e-4)
			return false;
		if (rightSide == null) {
			if (other.rightSide != null)
				return false;
		} else if (!rightSide.equals(other.rightSide))
			return false;
		if (Math.abs(support - other.support) > 10e-4) 
			return false;
		return true;
	}
	
	
	
	
}
