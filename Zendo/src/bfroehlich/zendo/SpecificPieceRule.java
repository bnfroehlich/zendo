package bfroehlich.zendo;

import java.util.ArrayList;

public class SpecificPieceRule extends Rule {

	private Piece.Size size;
	private Piece.Color color;
	
	public SpecificPieceRule(Piece.Size item1, Piece.Color item2, int maxStackSize) {
		this.size = item1;
		this.color = item2;
		checkValidity(maxStackSize);
	}
	
	public void checkValidity(int maxStackSize) throws IllegalArgumentException {
		
	}
	
	public static SpecificPieceRule fromString(String s , int maxStackSize) {
		Piece.Size aSize = null;
		Piece.Color aColor = null;
		try {
			String[] pieces = s.split(" : ");
			aSize = Piece.Size.valueOf(pieces[1]);
			aColor = Piece.Color.valueOf(pieces[2]);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("Invalid items entry");
		}
		SpecificPieceRule rule = new SpecificPieceRule(aSize, aColor, maxStackSize);
		return rule;
	}
	
	public static ArrayList<SpecificPieceRule> getAllSpecificPieceRules(int maxStackSize) {
		ArrayList<SpecificPieceRule> rules = new ArrayList<SpecificPieceRule>();
		for(Piece.Size aSize : Piece.Size.values()) {
			for(Piece.Color aColor : Piece.Color.values()) {
				try {
					SpecificPieceRule rule = new SpecificPieceRule(aSize, aColor, maxStackSize);
					if(!rules.contains(rule)) {
						rules.add(rule);
					}
				}
				catch(IllegalArgumentException e) {}
			}
		}
		return rules;
	}
	
	public Piece.Size getSize() {
		return size;
	}

	public void setSize(Piece.Size item1) {
		this.size = item1;
	}

	public Piece.Color getColor() {
		return color;
	}

	public void setColor(Piece.Color item2) {
		this.color = item2;
	}

	public String toString() {
		return "Contains : " + size + " : " + color + " : piece";
	}
	
	public boolean equals(Object rule) {
		if(!(rule instanceof SpecificPieceRule)) {
			return false; 
		}
		SpecificPieceRule spr = (SpecificPieceRule) rule;
		return (spr.getSize() == size && spr.getColor() == color);
	}
	
	public int hashCode() {
		return size.hashCode() + color.hashCode();
	}

	public String toStringFriendly() {
		return "Contains\n" + size + "\n" + color + "\npiece";
	}

	public boolean statisfiesRule(Koan koan) {
		ArrayList<ArrayList<Stack>> stacks = koan.getStacks();
		ArrayList<Piece> allPieces = new ArrayList<Piece>();
		for(ArrayList<Stack> row : stacks) {
			for(Stack stack : row) {
				allPieces.addAll(stack.getPieces());
			}
		}
		for(Piece piece : allPieces) {
			if(piece.getSize() == size && piece.getColor() == color) {
				return true;
			}
		}
		return false;
	}

}