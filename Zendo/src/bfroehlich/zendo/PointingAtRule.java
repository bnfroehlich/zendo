package bfroehlich.zendo;

import java.awt.Point;
import java.util.ArrayList;

public class PointingAtRule extends Rule {
	
	//public static String[] Restrictions = {"same color", "same size", "different color", "different size"};
	
	private String item1;
	private String item2;
	
	public PointingAtRule(String item1, String item2, int maxStackSize) {
		this.item1 = item1;
		this.item2 = item2;
		checkValidity(maxStackSize);
	}
	
	public static PointingAtRule fromString(String s, int maxStackSize) throws IllegalArgumentException {
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
		PointingAtRule rule = new PointingAtRule(item1, item2, maxStackSize);
		return rule;
	}
			
	public void checkValidity(int maxStackSize) throws IllegalArgumentException {
		
	}
	
	public static ArrayList<PointingAtRule> getAllPointingAtRules(int maxStackSize) {
		ArrayList<PointingAtRule> rules = new ArrayList<PointingAtRule>();
		for(String sizeItem1 : SizeItems) {
			for(String sizeItem2 : SizeItems) {
				try {
					PointingAtRule rule = new PointingAtRule(sizeItem1, sizeItem2, maxStackSize);
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
					PointingAtRule rule = new PointingAtRule(colorItem1, colorItem2, maxStackSize);
					if(!rules.contains(rule)) {
						rules.add(rule);
					}
				}
				catch(IllegalArgumentException e) {}
			}
		}
		return rules;
	}

	public boolean statisfiesRule(Koan koan) {
		ArrayList<ArrayList<Stack>> stacks = koan.getStacks();
		for(int x = 0; x < stacks.size(); x++) {
			ArrayList<Stack> column = stacks.get(x); 
			for(int y = 0; y < column.size(); y++) {
				Stack stack = column.get(y);
				ArrayList<Piece> pieces = stack.getPieces();
				for(int i = 0; i < pieces.size(); i++) {
					Piece indexPiece = pieces.get(i);
					ArrayList<Piece> pointingAtPieces = new ArrayList<Piece>();
					for(int j = i+1; j < pieces.size(); j++) {
						pointingAtPieces.add(pieces.get(j));
					}
					Piece.Direction dir = pieces.get(0).getDirection();
					if(!(dir == Piece.Direction.Up)) {
						Point pointer = dir.getPointer();
						try {
							int counter = 0;
							int currentX = x;
							int currentY = y;
							while(true) {
								counter++;
								if(counter > 10) {
									System.out.println("oops");
								}
								currentX += pointer.x;
								currentY += pointer.y;
								Stack nextPointedTo = stacks.get(currentX).get(currentY);
								pointingAtPieces.addAll(nextPointedTo.getPieces());
							}
						}
						catch(IndexOutOfBoundsException exception) {}
					}
					if(piecePointingAt(indexPiece, pointingAtPieces)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean piecePointingAt(Piece indexPiece, ArrayList<Piece> pointingAtPieces) {
		if(isItem(indexPiece, item1)) {
			for(Piece piece : pointingAtPieces) {
				if(isItem(piece, item2)) {
					return true;
				}
			}
		}
		return false;
	}

	public String toString() {
		return item1 + " : Pointing at : " + item2;
	}

	public String toStringFriendly() {
		return item1 + "\nPointing at\n" + item2;
	}
	
	public boolean equals(Object rule) {
		if(!(rule instanceof PointingAtRule)) {
			return false; 
		}
		PointingAtRule par = (PointingAtRule) rule;
		return par.getItem1().equals(item1) && par.getItem2().equals(item2);
	}
	
	public int hashCode() {
		return item1.hashCode() + item2.hashCode()*10;
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
