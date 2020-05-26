package bfroehlich.zendo;

import java.util.ArrayList;

public class TouchingRule extends Rule {

	//public static String[] Restrictions = {"same color", "same size", "different color", "different size"};
	
	private String item1;
	private String item2;
	
	public TouchingRule(String item1, String item2, int maxStackSize) {
		this.item1 = item1;
		this.item2 = item2;
		checkValidity(maxStackSize);
	}
	
	public static TouchingRule fromString(String s, int maxStackSize) throws IllegalArgumentException {
		String item1 = null;
		String item2 = null;
		try {
			String[] pieces = s.split(" : ");
			item1 = pieces[0];
			item2 = pieces[2];
		}
		catch(Exception e) {
			throw new IllegalArgumentException("Invalid items entry");
		}
		TouchingRule rule = new TouchingRule(item1, item2, maxStackSize);
		return rule;
	}
			
	public void checkValidity(int maxStackSize) throws IllegalArgumentException {
		
	}
	
	public static ArrayList<TouchingRule> getAllTouchingRules(int maxStackSize) {
		ArrayList<TouchingRule> rules = new ArrayList<TouchingRule>();
		for(String sizeItem1 : SizeItems) {
			for(String sizeItem2 : SizeItems) {
				try {
					TouchingRule rule = new TouchingRule(sizeItem1, sizeItem2, maxStackSize);
					if(!rules.contains(rule)) {
						rules.add(rule);
					}
				}
				catch(IllegalArgumentException e) {}
			}
		}
		for(String colorItem1 : ColorItems) {
			for(String colorItem2 : ColorItems) {
				try {
					TouchingRule rule = new TouchingRule(colorItem1, colorItem2, maxStackSize);
					if(!rules.contains(rule)) {
						rules.add(rule);
					}
				}
				catch(IllegalArgumentException e) {}
			}
		}
		return rules;
	}

	public String toStringFriendly() {
		return item1 + "\nTouching\n" + item2;
	}

	public boolean statisfiesRule(Koan koan) {
		ArrayList<ArrayList<Stack>> stacks = koan.getStacks();
		for(ArrayList<Stack> row : stacks) {
			for(Stack stack : row) {
				ArrayList<Piece> pieces = stack.getPieces();
				for(int i = 0; i < pieces.size(); i++) {
					if(i < pieces.size()-1) {
						if(piecesAreTouching(pieces.get(i), pieces.get(i+1))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean piecesAreTouching(Piece piece1, Piece piece2) {
		if(isItem(piece1, item1) && isItem(piece2, item2)) {
			return true;
		}
		if(isItem(piece1, item2) && isItem(piece2, item1)) {
			return true;
		}
		return false;
	}

	public String toString() {
		return item1 + " : Touching : " + item2;
	}
	
	public boolean equals(Object rule) {
		if(!(rule instanceof TouchingRule)) {
			return false; 
		}
		TouchingRule tr = (TouchingRule) rule;
		return (tr.getItem1().equals(item1) && tr.getItem2().equals(item2)) || (tr.getItem1().equals(item2) && tr.getItem2().equals(item1));
	}
	
	public int hashCode() {
		return item1.hashCode() + item2.hashCode();
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
}