package bfroehlich.zendo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public abstract class Rule {
	
	public static String[] ColorItems = {"blue piece", "yellow piece", "green piece", "red piece"};
	public static String[] SizeItems = {"small piece", "medium piece", "large piece"};
	public static String[] Items = {"blue piece", "yellow piece", "green piece", "red piece", "small piece", "medium piece", "large piece"};
	
	public Rule() {
		
	}
	
	public static Rule randomRule(int maxStackSize) {
		Random rand = new Random();
		ArrayList<Rule> rules = getAllRules(maxStackSize);
		return rules.get(rand.nextInt(rules.size()));
	}
	
	public static Rule randomWeightedRule(int maxStackSize) {
		ArrayList<ArrayList<Rule>> rules = new ArrayList<ArrayList<Rule>>();
		
		ArrayList<Rule> easyValueRules = new ArrayList<Rule>(ValueRule.getEasyValueRules(maxStackSize));
		rules.add(easyValueRules);
		rules.add(easyValueRules);
		rules.add(easyValueRules);
		rules.add(easyValueRules);
		rules.add(easyValueRules);
		rules.add(easyValueRules);
		rules.add(easyValueRules);
		rules.add(easyValueRules);
		rules.add(easyValueRules);
		
		ArrayList<Rule> hardNumericalRules = new ArrayList<Rule>(ValueRule.getHardNumericalValueRules(maxStackSize));
		rules.add(hardNumericalRules);
		rules.add(hardNumericalRules);
		rules.add(hardNumericalRules);
		
		ArrayList<Rule> hardContainsRules = new ArrayList<Rule>(ValueRule.getHardContainsValueRules(maxStackSize));
		rules.add(hardContainsRules);
		
		ArrayList<Rule> containsAndRules = new ArrayList<Rule>(ContainsAndRule.getAllCompoundContainsRules(maxStackSize));
		rules.add(containsAndRules);
		rules.add(containsAndRules);
		rules.add(containsAndRules);
		
		ArrayList<Rule> specificPieceRules = new ArrayList<Rule>(SpecificPieceRule.getAllSpecificPieceRules(maxStackSize));
		rules.add(specificPieceRules);
		rules.add(specificPieceRules);
		rules.add(specificPieceRules);
		
		ArrayList<Rule> touchingRules = new ArrayList<Rule>(TouchingRule.getAllTouchingRules(maxStackSize));
		rules.add(touchingRules);
		rules.add(touchingRules);
		rules.add(touchingRules);
		
		ArrayList<Rule> pointingAtRules = new ArrayList<Rule>(PointingAtRule.getAllPointingAtRules(maxStackSize));
		rules.add(pointingAtRules);
		rules.add(pointingAtRules);
		rules.add(pointingAtRules);
		
		
		Random rand = new Random();
		int ruleClassIndex = rand.nextInt(rules.size());
		ArrayList<Rule> ruleClass = rules.get(ruleClassIndex);
		Rule rule = ruleClass.get(rand.nextInt(ruleClass.size()));
		return rule;
	}
	
	public static ArrayList<Rule> getAllRules(int maxStackSize) {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		ArrayList<ValueRule> valueRules = ValueRule.getAllValueRules(maxStackSize);
		rules.addAll(valueRules);
		ArrayList<ContainsAndRule> containsAndRules = ContainsAndRule.getAllCompoundContainsRules(maxStackSize);
		rules.addAll(containsAndRules);
		ArrayList<SpecificPieceRule> specificPieceRules = SpecificPieceRule.getAllSpecificPieceRules(maxStackSize);
		rules.addAll(specificPieceRules);
		ArrayList<TouchingRule> touchingRules = TouchingRule.getAllTouchingRules(maxStackSize);
		rules.addAll(touchingRules);
		ArrayList<PointingAtRule> pointingAtRules = PointingAtRule.getAllPointingAtRules(maxStackSize);
		rules.addAll(pointingAtRules);
		

//		System.out.println("value : " + valueRules.size() + "/" + rules.size() + ": " + valueRules.size()/rules.size());
//		System.out.println("containsAnd : " + containsAndRules.size() + "/" + rules.size() + ": " + containsAndRules.size()/rules.size());
//		System.out.println("touching : " + touchingRules.size() + "/" + rules.size() + ": " + touchingRules.size()/rules.size());
//		System.out.println("pointingAt : " + pointingAtRules.size() + "/" + rules.size() + ": " + pointingAtRules.size()/rules.size());
//		for(Rule rule : rules) {
//			System.out.println(rule);
//		}
//		System.out.println(rules.size());
		return rules;
	}
	
	public abstract void checkValidity(int maxStackSize) throws IllegalArgumentException;
	
	public abstract String toStringFriendly();

	public abstract boolean statisfiesRule(Koan koan);
	
	public boolean isItem(Piece piece, String item) {
		switch(item) {
		case("blue piece") :
			return piece.getColor() == Piece.Color.Blue;
		case("red piece") :
			return piece.getColor() == Piece.Color.Red;
		case("green piece") :
			return piece.getColor() == Piece.Color.Green;
		case("yellow piece") :
			return piece.getColor() == Piece.Color.Yellow;
		case("small piece") :
			return piece.getSize() == Piece.Size.Small;
		case("medium piece") :
			return piece.getSize() == Piece.Size.Medium;
		case("large piece") :
			return piece.getSize() == Piece.Size.Large;
		}
		return false;
	}
	
	public int evaluateProperty(String property, Koan koan) {
		ArrayList<ArrayList<Stack>> stacks = koan.getStacks();
		ArrayList<Piece> allPieces = new ArrayList<Piece>();
		for(ArrayList<Stack> row : stacks) {
			for(Stack stack : row) {
				allPieces.addAll(stack.getPieces());
			}
		}
		switch(property) {
		case("colors") :
			ArrayList<Piece.Color> colors = new ArrayList<Piece.Color>(Arrays.asList(Piece.Color.values()));
			for(Piece piece : allPieces) {
				if(colors.contains(piece.getColor())) {
					colors.remove(piece.getColor());
				}
			}
			return 4-colors.size();
		case("sizes") :
			boolean[] hasSizes = {false, false, false};
			for(Piece piece : allPieces) {
				if(piece.getSize() == Piece.Size.Small) {
					hasSizes[0] = true;
				}
				else if(piece.getSize() == Piece.Size.Medium) {
					hasSizes[1] = true;
				}
				else if(piece.getSize() == Piece.Size.Large) {
					hasSizes[2] = true;
				}
			}
			int numSizes = 0;
			for(boolean has : hasSizes) {
				if(has) numSizes++;
			}
			return numSizes;
		case("pieces") :
			return allPieces.size();
		case("blue piece") :
		case("blue pieces") :
			int blue = 0;
			for(Piece piece : allPieces) {
				if(piece.getColor() == Piece.Color.Blue) {
					blue++;
				}
			}
			return blue;
		case("yellow piece") :
		case("yellow pieces") :
			int yellow = 0;
			for(Piece piece : allPieces) {
				if(piece.getColor() == Piece.Color.Yellow) {
					yellow++;
				}
			}
			return yellow;
		case("green piece") :
		case("green pieces") :
			int green = 0;
			for(Piece piece : allPieces) {
				if(piece.getColor() == Piece.Color.Green) {
					green++;
				}
			}
			return green;
		case("red piece") :
		case("red pieces") :
			int red = 0;
			for(Piece piece : allPieces) {
				if(piece.getColor() == Piece.Color.Red) {
					red++;
				}
			}
			return red;
		case("small piece") :
		case("small pieces") :
			int small = 0;
			for(Piece piece : allPieces) {
				if(piece.getSize() == Piece.Size.Small) {
					small++;
				}
			}
			return small;
		case("medium piece") :
		case("medium pieces") :
			int medium = 0;
			for(Piece piece : allPieces) {
				if(piece.getSize() == Piece.Size.Medium) {
					medium++;
				}
			}
			return medium;
		case("large piece") :
		case("large pieces") :
			int large = 0;
			for(Piece piece : allPieces) {
				if(piece.getSize() == Piece.Size.Large) {
					large++;
				}
			}
			return large;
		case("grounded piece") :
		case("grounded pieces") :
			int grounded = 0;
			for(ArrayList<Stack> row : stacks) {
				for(Stack stack : row) {
					ArrayList<Piece> pieces = stack.getPieces();
					if(!pieces.isEmpty()) {
						if(pieces.get(0).getDirection() == Piece.Direction.Up) {
							grounded++; //each Up stack contains 1 grounded piece
						}
						else {
							grounded+= pieces.size(); //all non-Up pieces are grounded
						}
					}
				}
			}
			return grounded;
		case("ungrounded piece") :
		case("ungrounded pieces") :
			int ungrounded = 0;
			for(ArrayList<Stack> row : stacks) {
				for(Stack stack : row) {
					if(stack.getPieces().size() > 1 && stack.getPieces().get(0).getDirection() == Piece.Direction.Up) {
						ungrounded+= stack.getPieces().size() - 1; //each Up stack contains 1 grounded piece; the rest are ungrounded
					}
				}
			}
			return ungrounded;
		case("up piece") :
		case("up pieces") :
			int up = 0;
			for(Piece piece : allPieces) {
				if(piece.getDirection() == Piece.Direction.Up) {
					up++;
				}
			}
			return up;
		case("stacks") :
			int numStacks = 0;
			for(ArrayList<Stack> row : stacks) {
				for(Stack stack : row) {
					if(!stack.getPieces().isEmpty()) {
						numStacks++; //each occupied stack contains 1 grounded piece
					}
				}
			}
			return numStacks;
		case("largest stack") :
			int largest = 0;
			for(ArrayList<Stack> row : stacks) {
				for(Stack stack : row) {
					if(stack.getPieces().size() > largest) {
						largest = stack.getPieces().size(); //each occupied stack contains 1 grounded piece
					}
				}
			}
			return largest;
		case("greatest color") :
			int redPieces = evaluateProperty("red pieces", koan);
			int bluePieces = evaluateProperty("blue pieces", koan);
			int greenPieces = evaluateProperty("green pieces", koan);
			int yellowPieces = evaluateProperty("yellow pieces", koan);
			int max1 = Math.max(redPieces, bluePieces);
			int max2 = Math.max(greenPieces, yellowPieces);
			return Math.max(max1, max2);
		case("pointing") :
			int pointing = 0;
			for(int x = 0; x < stacks.size(); x++) {
				ArrayList<Stack> row = stacks.get(x);
				for(int y = 0; y < row.size(); y++) {
					Stack aStack = row.get(y);
					ArrayList<Piece> pieces = aStack.getPieces();
					if(!pieces.isEmpty()) {
						pointing += pieces.size()-1; //each piece points to the one above it in a stack regardless of stack orientation
						Piece.Direction dir = pieces.get(0).getDirection();
						if(!(dir == Piece.Direction.Up)) {
							Point pointer = dir.getPointer();
							int currentX = x;
							int currentY = y;
							try {
								while(true) {
									currentX += pointer.x;
									currentY += pointer.y;
									Stack nextPointedTo = stacks.get(currentX).get(currentY);
									pointing += nextPointedTo.getPieces().size();
								}
							}
							catch(IndexOutOfBoundsException exception) {}
						}
					}
				}
			}
			return pointing;
		case("smallest piece") :
			int smallestPiecePips = 0;
			for(Piece piece : allPieces) {
				if(smallestPiecePips == 0) {
					smallestPiecePips = piece.getSize().getPips();
				}
				else if(piece.getSize().getPips() < smallestPiecePips) {
					smallestPiecePips = piece.getSize().getPips();
				}
			}
			return smallestPiecePips;
		case("largest piece") :
			int largestPiecePips = 0;
			for(Piece piece : allPieces) {
				if(piece.getSize().getPips() > largestPiecePips) {
					largestPiecePips = piece.getSize().getPips();
				}
			}
			return largestPiecePips;
		}
		return 0;
	}
}