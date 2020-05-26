package bfroehlich.zendo;

import java.util.ArrayList;
import java.util.Arrays;

public class ValueRule extends Rule {

	public static String[] EasyProperties = {"colors", "pieces", "blue pieces", "yellow pieces", "green pieces", "red pieces", "small pieces", "medium pieces", "large pieces"};
	
	public static String[] HardNumericalProperties = {"sizes", "stacks", "largest stack", "greatest color", "smallest piece", "largest piece"};
	
	public static String[] HardContainsProperties = {"up piece", "ungrounded piece"};

	public enum Comparator {
		Equal, LessEqual, GreaterEqual;
		
		public static String[] labels() {
			String[] labels = {"Equal", "LessEqual", "GreaterEqual"};
			return labels;
		}
	}
	
	public enum Value {
		Zero, One, Two, Three, Even, Odd
	}
	
	private String property;
	private Value value;
	private Comparator comparator;
	
	
	public ValueRule(String property, Comparator comparator, Value value, int maxStackSize) throws IllegalArgumentException {
		this.property = property;
		this.value = value;
		this.comparator = comparator;
		checkValidity(maxStackSize);
	}
	
	public static ArrayList<ValueRule> getAllValueRules(int maxStackSize) {
		ArrayList<ValueRule> rules = new ArrayList<ValueRule>();
		for(String property : EasyProperties) {
			for(Comparator comparator : Comparator.values()) {
				for(Value value : Value.values()) {
					try {
						ValueRule rule = new ValueRule(property, comparator, value, maxStackSize);
						rules.add(rule);
					}
					catch(IllegalArgumentException e) {}
				}
			}
		}
		for(String property : HardNumericalProperties) {
			Comparator comparator = Comparator.Equal;
			for(Value value : Value.values()) {
				if(!(value == Value.Even || value == Value.Odd)) {
					try {
						ValueRule rule = new ValueRule(property, comparator, value, maxStackSize);
						rules.add(rule);
					}
					catch(IllegalArgumentException e) {}
				}
			}
		}
		for(String property : HardContainsProperties) {
			Comparator comparator = Comparator.GreaterEqual;
			Value value = Value.One;
			try {
				ValueRule rule = new ValueRule(property, comparator, value, maxStackSize);
				rules.add(rule);
			}
			catch(IllegalArgumentException e) {}
		}
		return rules;
	}
	
	public static ArrayList<ValueRule> getEasyValueRules(int maxStackSize) {
		ArrayList<ValueRule> rules = new ArrayList<ValueRule>();
		for(String property : EasyProperties) {
			for(Comparator comparator : Comparator.values()) {
				for(Value value : Value.values()) {
					try {
						ValueRule rule = new ValueRule(property, comparator, value, maxStackSize);
						rules.add(rule);
					}
					catch(IllegalArgumentException e) {}
				}
			}
		}
		return rules;
	}
	
	public static ArrayList<ValueRule> getHardNumericalValueRules(int maxStackSize) {
		ArrayList<ValueRule> rules = new ArrayList<ValueRule>();
		for(String property : HardNumericalProperties) {
			Comparator comparator = Comparator.Equal;
			for(Value value : Value.values()) {
				if(!(value == Value.Even || value == Value.Odd)) {
					try {
						ValueRule rule = new ValueRule(property, comparator, value, maxStackSize);
						rules.add(rule);
					}
					catch(IllegalArgumentException e) {}
				}
			}
		}
		return rules;
	}
	
	public static ArrayList<ValueRule> getHardContainsValueRules(int maxStackSize) {
		ArrayList<ValueRule> rules = new ArrayList<ValueRule>();
		for(String property : HardContainsProperties) {
			Comparator comparator = Comparator.GreaterEqual;
			Value value = Value.One;
			try {
				ValueRule rule = new ValueRule(property, comparator, value, maxStackSize);
				rules.add(rule);
			}
			catch(IllegalArgumentException e) {}
		}
		return rules;
	}
	
	public static ValueRule fromString(String s, int maxStackSize) {
		String property = null;
		Comparator comparator = null;
		Value value = null;
		try {
			String[] pieces = s.split(" : ");
			property = pieces[0];
			comparator = Comparator.valueOf(pieces[1]);
			value = Value.valueOf(pieces[2]);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("Invalid rule component entry");
		}
		ValueRule rule = new ValueRule(property, comparator, value, maxStackSize);
		return rule;
	}
	
	public void checkValidity(int maxStackSize)  throws IllegalArgumentException {
		if(property == null || value == null || comparator == null) {
			throw new IllegalArgumentException("Invalid rule components missing");
		}
		if((value == Value.Even || value == Value.Odd) && comparator != Comparator.Equal) {
			throw new IllegalArgumentException("Invalid use of even/odd");
		}
		String[] nullPropsArr = {"colors", "sizes", "stacks", "grounded pieces", "largest stack", "greatest color", "smallest piece", "largest piece"};
		ArrayList<String> nullProps = new ArrayList<String>(Arrays.asList(nullPropsArr));
		if(value == Value.Zero && nullProps.contains(property)) {
			//no null rules allowed except 1: pieces equal zero
			throw new IllegalArgumentException("Invalid null rule");
		}
		if(value == Value.One && comparator == Comparator.GreaterEqual && nullProps.contains(property)) {
			throw new IllegalArgumentException("Invalid null rule");
		}
		if(value == Value.Zero && comparator != Comparator.Equal) {
			throw new IllegalArgumentException("Invalid zero use");
		}
		if(property.equals("sizes") && value == Value.Three && comparator != Comparator.Equal) {
			throw new IllegalArgumentException("Invalid use of sizes");
		}
		if(property.equals("largest stack")) {
			if(value.ordinal() <= 3) {
				//if value is 0, 1, 2, or 3
				if(value.ordinal() > maxStackSize || (value.ordinal() == maxStackSize && comparator != Comparator.Equal)) {
					throw new IllegalArgumentException("Invalid largest stack use");
				}
			}
		}
		if(property.equals("smallest piece")) {
			if(value == Value.One) {
				throw new IllegalArgumentException("Bad use of smallest piece : One");
			}
			if(value == Value.Three && comparator != Comparator.Equal) {
				throw new IllegalArgumentException("Bad use of smallest piece : Three");
			}
		}
		if(property.equals("largest piece")) {
			if(value == Value.Three) {
				throw new IllegalArgumentException("Bad use of largest piece : Three");
			}
		}
	}
	
	public String toString() {
		return property + " : " + comparator + " : " + value;
	}
	
	public String toStringFriendly() {
		return property + "\n" + comparator + "\n" + value;
	}
	
	public boolean equals(Object rule) {
		if(!(rule instanceof ValueRule)) {
			return false; 
		}
		ValueRule vr = (ValueRule) rule;
		return vr.getProperty().equals(this.property) && vr.getComparator() == this.comparator && vr.getValue() == this.value;
	}
	
	public int hashCode() {
		return property.hashCode() + comparator.hashCode() + value.hashCode();
	}
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
	public Comparator getComparator() {
		return comparator;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public boolean statisfiesRule(Koan koan) {
		int koanValue = evaluateProperty(property, koan);
		int valueInt = -1;
		if(value == Value.Zero || value == Value.One || value == Value.Two || value == Value.Three) {
			valueInt = value.ordinal();
			switch(comparator) {
			case Equal :
				return koanValue == valueInt;
			case LessEqual :
				return koanValue <= valueInt;
			case GreaterEqual :
				return koanValue >= valueInt;
			}
		}
		else if(value == Value.Even) {
			return koanValue % 2 == 0;
		}
		else if(value == Value.Odd) {
			return koanValue % 2 == 1;
		}
		return false;
	}

	
}