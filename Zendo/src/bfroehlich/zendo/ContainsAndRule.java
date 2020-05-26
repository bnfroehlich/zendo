package bfroehlich.zendo;

import java.util.ArrayList;

public class ContainsAndRule extends Rule {
		
	private String item1;
	private String item2;
	
	public ContainsAndRule(String item1, String item2, int maxStackSize) {
		this.item1 = item1;
		this.item2 = item2;
		checkValidity(maxStackSize);
	}

	public void checkValidity(int maxStackSize) throws IllegalArgumentException {
		if(item1.equals(item2)) {
			throw new IllegalArgumentException("Items are same");
		}
		if(item1.equals("ungrounded piece") &&  (item2.equals("up piece") || item2.equals("pointing"))) {
			throw new IllegalArgumentException("Redundant contains");
		}
		if(item2.equals("ungrounded piece") &&  (item1.equals("up piece") || item1.equals("pointing"))) {
			throw new IllegalArgumentException("Redundant contains");
		}
	}
	
	public static ContainsAndRule fromString(String s , int maxStackSize) {
		String item1 = null;
		String item2 = null;
		try {
			String[] pieces = s.split(" : ");
			item1 = pieces[1];
			item2 = pieces[2];
		}
		catch(Exception e) {
			throw new IllegalArgumentException("Invalid items entry");
		}
		ContainsAndRule rule = new ContainsAndRule(item1, item2, maxStackSize);
		return rule;
	}
	
	public static ArrayList<ContainsAndRule> getAllCompoundContainsRules(int maxStackSize) {
		ArrayList<ContainsAndRule> rules = new ArrayList<ContainsAndRule>();
		for(String item1 : Items) {
			for(String item2 : Items) {
				try {
					ContainsAndRule rule = new ContainsAndRule(item1, item2, maxStackSize);
					if(!rules.contains(rule)) {
						rules.add(rule);
					}
				}
				catch(IllegalArgumentException e) {}
			}
		}
		return rules;
	}
	
	public String getItem1() {
		return item1;
	}

	public void setItem1(String item1) {
		this.item1 = item1;
	}

	public String getItem2() {
		return item2;
	}

	public void setItem2(String item2) {
		this.item2 = item2;
	}

	public String toString() {
		return "Contains : " + item1 + " : " + item2;
	}
	
	public boolean equals(Object rule) {
		if(!(rule instanceof ContainsAndRule)) {
			return false; 
		}
		ContainsAndRule ccr = (ContainsAndRule) rule;
		return (ccr.getItem1().equals(item1) && ccr.getItem2().equals(item2)) || (ccr.getItem2().equals(item1) && ccr.getItem1().equals(item2));
	}
	
	public int hashCode() {
		return item1.hashCode() + item2.hashCode();
	}

	public String toStringFriendly() {
		return "Contains\n" + item1 + "\n" + item2;
	}

	public boolean statisfiesRule(Koan koan) {
		int item1Value = evaluateProperty(item1, koan);
		int item2Value = evaluateProperty(item2, koan);
		return item1Value > 0 && item2Value > 0;
	}

}
