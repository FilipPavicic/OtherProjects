package wut.weiti.edami.apriori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemSet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Set<String> items;
	double support;
	
	public ItemSet(Set<String> items, double support)  {
		super();
		this.items = items;
		this.support = support;
	}
	
	public ItemSet(double support, String... items)  {
		super();
		this.items = new HashSet<>(Arrays.asList(items));
		this.support = support;
	}
	
	public ItemSet(String... items)  {
		this(-1,items);
	}
	
	

	public Set<String> getItems() {
		return items;
	}



	public void setItems(Set<String> items) {
		this.items = items;
	}



	public double getSupport() {
		return support;
	}



	public void setSupport(double support) {
		this.support = support;
	}
	
	public int getItemcount() {
		return items.size();
	}



	

	@Override
	public int hashCode() {
		return items.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		ItemSet other = (ItemSet) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  items + ", support=" + Utils.format(support) ;
	}
	
	public String[] itemsOutput() {
		return  (String[]) items
			.stream()
			.map(s -> s.replaceAll("\"", ""))
			.sorted()
			.collect(Collectors.toList()).toArray(new String[1]);
	}
	 public int size() {
		 return items.size();
	 }
	 
	 public boolean contains(Object o) {
		return  items.contains(o);
	 }

	public ItemSet copy() {
		return new ItemSet(new HashSet<>(items), support);
	}

	public void removeAll(ItemSet o) {
		this.items.removeAll(o.items);
		
	}
	
	
}
