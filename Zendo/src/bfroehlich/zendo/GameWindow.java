package bfroehlich.zendo;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import bfroehlich.zendo.ValueRule.Value;

public class GameWindow extends JFrame {
	
	private ArrayList<Koan> koans;
	private Stack activestack;
	private JPanel main;
	private JScrollPane mainScroll;
	private Rule rule;

	private JPanel trophyPanel;
	private JPanel ruleDispHider;
	private JTextArea ruleDisp;
	private JPanel dialogPanel;
	private JTextArea output;
	private JButton newRule;
	private JButton testRule;
	private JButton allRules;
	private JButton playMaster;
	private JLabel submit;
	private JButton erase;
	private JButton enlarge;
	private JPanel priorGuesses;
	
	private Image trophy;
	
	//groups of inputs for user to specify a rule, the number is the group
	private JComboBox<String> selectProperties1;
	private JComboBox<String> selectComparator1;
	private JComboBox<String> selectValues1;

	private JComboBox<String> selectProperties2;
	private JComboBox<String> selectValues2;
	
	private JComboBox<String> selectItemOne3;
	private JComboBox<String> selectItemTwo3;
	
	private JComboBox<String> selectSize4;
	private JComboBox<String> selectColor4;
	
	private JComboBox<String> selectProperties5;

	private JComboBox<String> selectTouchingRestrictionOne6;
	private JComboBox<String> selectTouchingRestrictionTwo6;

	private JComboBox<String> selectTouchingRestrictionOne7;
	private JComboBox<String> selectTouchingRestrictionTwo7;
	
	private JComboBox<String> selectPointingAtRestrictionOne8;
	private JComboBox<String> selectPointingAtRestrictionTwo8;
	
	private JComboBox<String> selectPointingAtRestrictionOne9;
	private JComboBox<String> selectPointingAtRestrictionTwo9;
	
	private int koanSize;
	private int maxStackSize;
	
	private Master currentMaster;
	
	private ArrayList<Koan> simCity;
	
	private GameWindow thiss;
	
	
	private enum Master {
		Player, Computer
	}
	
	public GameWindow() {
		super("Zendo");
		thiss = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		koans = new ArrayList<Koan>();
		trophy = Main.loadImage("trophy.png", 50, 50, true);
		koanSize = 2;
		maxStackSize = 3;
		init();
	}
	
	private void init() {
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
		add(wrapper);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		main = new JPanel();
		mainScroll = new JScrollPane(main);
		mainScroll.setPreferredSize(new Dimension(Math.min(1000, screenSize.width-50), Math.min(400, screenSize.height*2/5)));
		mainScroll.getHorizontalScrollBar().setUnitIncrement(50);
		main.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 10));
		wrapper.add(mainScroll);
		
		JPanel belowMain = new JPanel();
		belowMain.setLayout(new GridBagLayout());
		wrapper.add(belowMain);
		
		trophyPanel = new JPanel();
		trophyPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));;
		trophyPanel.add(new JLabel(new ImageIcon(trophy)));
		GridBagConstraints trophyPanelConstraints = new GridBagConstraints();
		trophyPanelConstraints.anchor = GridBagConstraints.WEST;
		belowMain.add(trophyPanel, trophyPanelConstraints);
		
		JPanel belowMainEast = new JPanel();
		belowMainEast.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
		GridBagConstraints belowMainEastConstraints = new GridBagConstraints();
		belowMainEastConstraints.anchor = GridBagConstraints.EAST;
		belowMain.add(belowMainEast, belowMainEastConstraints);

		JButton addBlank = new JButton(new ImageIcon(Main.loadImage("blank2.png", 50, 50, false)));
		addBlank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Koan c = blankKoan(koanSize, koanSize);
				addKoan(c);
			}
		});
		belowMainEast.add(addBlank);

		JButton addRandom = new JButton(new ImageIcon(Main.loadImage("blankrandom2.png", 50, 50, false)));
		addRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Koan c = randomKoan(koanSize, koanSize, maxStackSize, true);
				addKoan(c);
			}
		});
		belowMainEast.add(addBlank);
		belowMainEast.add(addRandom);
		
		erase = new JButton();
		erase.setIcon(new ImageIcon(Main.loadImage("eraser3.png", 43, 30, false)));
		erase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(activestack != null) {
					boolean modified = activestack.removeTopPiece();
					if(modified && currentMaster == Master.Computer) {
						activestack.getKoan().setWorthy(null);
					}
					pack();
				}
			}
		});
		belowMainEast.add(erase);
		
		enlarge = new JButton();
		enlarge.setIcon(new ImageIcon(Main.loadImage("enlarge.png", 30, 30, false)));
		enlarge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(activestack != null) {
					enlargeKoan(activestack.getKoan());
					pack();
				}
			}
		});
		belowMainEast.add(enlarge);
		
		JLabel purge = new JLabel(new ImageIcon(Main.loadImage("nuke.png", 50, 50, false)));
		purge.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				if(JOptionPane.showConfirmDialog(thiss, new JLabel(new ImageIcon(Main.loadImage("nuke.png", 50, 50, false))), "Do it. Kill everyone.", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					purge();
				}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		belowMainEast.add(purge);
		
		submit = new JLabel(new ImageIcon(Main.loadImage("greenbutton.png", 50, 50, false)));
		submit.setEnabled(false);
		submit.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				if(submit.isEnabled()) {
					if(currentMaster == Master.Player) {
						computerGuessesRule();
					}
					else if(currentMaster == Master.Computer) {

						Rule guess = scrapeRule();
						if(guess != null) {
							ruleGuessed(guess);
						}
					}
				}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		belowMainEast.add(submit);
		
		JPanel control = new JPanel();
		wrapper.add(control);
		control.setLayout(new GridBagLayout());
		ArrayList<Piece> pieces = Piece.getAllPieces();
		for(int i = 0; i < pieces.size(); i++) {
			final Piece selectedPiece = pieces.get(i);
			JButton button = new JButton();
			button.setIcon(new ImageIcon(selectedPiece.getImage()));
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = selectedPiece.getColor().ordinal();
			constraints.gridy = selectedPiece.getSize().getPips() - 1;
			Insets inset = new Insets(5, 10, 5, 10);
			constraints.insets = inset;
			control.add(button, constraints);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(activestack != null) {
						ArrayList<Piece> pieces = activestack.getPieces();
						Piece.Direction stackDirection = Piece.Direction.Up;
						if(!pieces.isEmpty()) {
							stackDirection = pieces.get(0).getDirection();
						}
						boolean modified = activestack.addPiece(new Piece(selectedPiece.getColor(), selectedPiece.getSize(), stackDirection));
						if(modified && currentMaster == Master.Computer) {
							activestack.getKoan().setWorthy(null);
						}
						pack();
					}
				}
			});
		}
		
		GridBagConstraints dialogConstraints = new GridBagConstraints();
		dialogConstraints.anchor = GridBagConstraints.EAST;
		dialogConstraints.gridy = 0;
		dialogConstraints.gridheight = 3;
		
		control.add(Box.createRigidArea(new Dimension(50, 50)), dialogConstraints);
		
		dialogPanel = new JPanel();
		CardLayout dialogCardLayout = new CardLayout();
		dialogPanel.setLayout(dialogCardLayout);
		control.add(dialogPanel, dialogConstraints);
		
		output = new JTextArea();
		
		JPanel ruleFactoryWrapper = new JPanel();
		dialogPanel.add(ruleFactoryWrapper);
		
		JPanel ruleFactoryPanel = new JPanel();
		JScrollPane ruleFactoryScroll = new JScrollPane(ruleFactoryPanel);
		ruleFactoryScroll.getVerticalScrollBar().setUnitIncrement(16);
		ruleFactoryScroll.setPreferredSize(new Dimension(350, 250));
		ruleFactoryWrapper.add(ruleFactoryScroll);

		FlowLayout ruleCreationFlow = new FlowLayout(FlowLayout.LEADING, 5, 5);
		
		ruleFactoryPanel.setLayout(new BoxLayout(ruleFactoryPanel, BoxLayout.Y_AXIS));
		JPanel ruleFactoryTitlePanel = new JPanel();
		ruleFactoryTitlePanel.setLayout(ruleCreationFlow);
		ruleFactoryTitlePanel.add(new JLabel("Rule Factory"));
		ruleFactoryPanel.add(ruleFactoryTitlePanel);
		
		JButton clearFactory = new JButton("Empty");
		ruleFactoryTitlePanel.add(clearFactory);
		clearFactory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearRuleEntry();
			}
		});
		
		JPanel valueRuleEasyPanel = new JPanel();
		valueRuleEasyPanel.setLayout(ruleCreationFlow);
		ruleFactoryPanel.add(valueRuleEasyPanel);
		Vector<String> propertyOptions1 = new Vector<String>();
		propertyOptions1.add("");
		for(String option : ValueRule.EasyProperties) {
			propertyOptions1.add(option);
		}
		selectProperties1 = new JComboBox<String>(propertyOptions1);
		valueRuleEasyPanel.add(selectProperties1);
		Vector<String> comparatorOptions = new Vector<String>();
		comparatorOptions.add("");
		for(ValueRule.Comparator option : ValueRule.Comparator.values()) {
			comparatorOptions.add(option.toString());
		}
		selectComparator1 = new JComboBox<String>(comparatorOptions);
		valueRuleEasyPanel.add(selectComparator1);
		Vector<String> valueOptions1 = new Vector<String>();
		valueOptions1.add("");
		for(ValueRule.Value value : ValueRule.Value.values()) {
			valueOptions1.add(value.toString());
		}
		selectValues1 = new JComboBox<String>(valueOptions1);
		valueRuleEasyPanel.add(selectValues1);
		
		JPanel valueRuleHardPanel = new JPanel();
		valueRuleHardPanel.setLayout(ruleCreationFlow);
		ruleFactoryPanel.add(valueRuleHardPanel);
		Vector<String> propertyOptions2 = new Vector<String>();
		propertyOptions2.add("");
		for(String option : ValueRule.HardNumericalProperties) {
			propertyOptions2.add(option);
		}
		selectProperties2 = new JComboBox<String>(propertyOptions2);
		valueRuleHardPanel.add(selectProperties2);
		valueRuleHardPanel.add(new JLabel("Equal"));
		Vector<String> valueOptions2 = new Vector<String>();
		valueOptions2.add("");
		for(ValueRule.Value value : ValueRule.Value.values()) {
			if(!(value == Value.Even || value == Value.Odd || value == Value.Zero)) {
				valueOptions2.add(value.toString());
			}
		}
		selectValues2 = new JComboBox<String>(valueOptions2);
		valueRuleHardPanel.add(selectValues2);
		

		JPanel compoundContainsRulePanel = new JPanel();
		compoundContainsRulePanel.setLayout(ruleCreationFlow);
		ruleFactoryPanel.add(compoundContainsRulePanel);
		compoundContainsRulePanel.add(new JLabel("Contains "));
		Vector<String> itemOptionsOne3 = new Vector<String>();
		itemOptionsOne3.add("");
		for(String option : ContainsAndRule.Items) {
			itemOptionsOne3.add(option);
		}
		selectItemOne3 = new JComboBox<String>(itemOptionsOne3);
		compoundContainsRulePanel.add(selectItemOne3);
		compoundContainsRulePanel.add(new JLabel(" and "));
		Vector<String> itemOptionsTwo3 = new Vector<String>();
		itemOptionsTwo3.add("");
		for(String option : ContainsAndRule.Items) {
			itemOptionsTwo3.add(option);
		}
		selectItemTwo3 = new JComboBox<String>(itemOptionsTwo3);
		compoundContainsRulePanel.add(selectItemTwo3);
		

		JPanel specificPieceRulePanel = new JPanel();
		specificPieceRulePanel.setLayout(ruleCreationFlow);
		ruleFactoryPanel.add(specificPieceRulePanel);
		specificPieceRulePanel.add(new JLabel("Contains "));
		Vector<String> sizeOptions4 = new Vector<String>();
		sizeOptions4.add("");
		for(Piece.Size option : Piece.Size.values()) {
			sizeOptions4.add(option.toString());
		}
		selectSize4 = new JComboBox<String>(sizeOptions4);
		specificPieceRulePanel.add(selectSize4);
		Vector<String> colorOptions4 = new Vector<String>();
		colorOptions4.add("");
		for(Piece.Color option : Piece.Color.values()) {
			colorOptions4.add(option.toString());
		}
		selectColor4 = new JComboBox<String>(colorOptions4);
		specificPieceRulePanel.add(selectColor4);
		specificPieceRulePanel.add(new JLabel(" piece"));
		
		JPanel hardContiainsRulePanel = new JPanel();
		hardContiainsRulePanel.setLayout(ruleCreationFlow);
		ruleFactoryPanel.add(hardContiainsRulePanel);
		hardContiainsRulePanel.add(new JLabel("Contains "));
		Vector<String> selectOptions5 = new Vector<String>();
		selectOptions5.add("");
		for(String option : ValueRule.HardContainsProperties) {
			selectOptions5.add(option);
		}
		selectProperties5 = new JComboBox<String>(selectOptions5);
		hardContiainsRulePanel.add(selectProperties5);
		
		JPanel touchingSizeRulePanel = new JPanel();
		touchingSizeRulePanel.setLayout(ruleCreationFlow);
		ruleFactoryPanel.add(touchingSizeRulePanel);
		Vector<String> selectOptions6 = new Vector<String>();
		selectOptions6.add("");
		for(String option : Rule.SizeItems) {
			selectOptions6.add(option);
		}
		selectTouchingRestrictionOne6 = new JComboBox<String>(selectOptions6);
		touchingSizeRulePanel.add(selectTouchingRestrictionOne6);
		touchingSizeRulePanel.add(new JLabel(" Touching "));
		selectTouchingRestrictionTwo6 = new JComboBox<String>(selectOptions6);
		touchingSizeRulePanel.add(selectTouchingRestrictionTwo6);
		
		JPanel touchingColorRulePanel = new JPanel();
		touchingColorRulePanel.setLayout(ruleCreationFlow);
		ruleFactoryPanel.add(touchingColorRulePanel);
		Vector<String> selectOptions7 = new Vector<String>();
		selectOptions7.add("");
		for(String option : Rule.ColorItems) {
			selectOptions7.add(option);
		}
		selectTouchingRestrictionOne7 = new JComboBox<String>(selectOptions7);
		touchingColorRulePanel.add(selectTouchingRestrictionOne7);
		touchingColorRulePanel.add(new JLabel(" Touching "));
		selectTouchingRestrictionTwo7 = new JComboBox<String>(selectOptions7);
		touchingColorRulePanel.add(selectTouchingRestrictionTwo7);
		
		JPanel pointingAtSizeRulePanel = new JPanel();
		pointingAtSizeRulePanel.setLayout(ruleCreationFlow);
		ruleFactoryPanel.add(pointingAtSizeRulePanel);
		Vector<String> selectOptions8 = new Vector<String>();
		selectOptions8.add("");
		for(String option : Rule.SizeItems) {
			selectOptions8.add(option);
		}
		selectPointingAtRestrictionOne8 = new JComboBox<String>(selectOptions8);
		pointingAtSizeRulePanel.add(selectPointingAtRestrictionOne8);
		pointingAtSizeRulePanel.add(new JLabel(" Pointing at "));
		selectPointingAtRestrictionTwo8 = new JComboBox<String>(selectOptions8);
		pointingAtSizeRulePanel.add(selectPointingAtRestrictionTwo8);
		
		JPanel pointingAtColorRulePanel = new JPanel();
		pointingAtColorRulePanel.setLayout(ruleCreationFlow);
		ruleFactoryPanel.add(pointingAtColorRulePanel);
		Vector<String> selectOptions9 = new Vector<String>();
		selectOptions9.add("");
		for(String option : Rule.ColorItems) {
			selectOptions9.add(option);
		}
		selectPointingAtRestrictionOne9 = new JComboBox<String>(selectOptions9);
		pointingAtColorRulePanel.add(selectPointingAtRestrictionOne9);
		pointingAtColorRulePanel.add(new JLabel(" Pointing at "));
		selectPointingAtRestrictionTwo9 = new JComboBox<String>(selectOptions9);
		pointingAtColorRulePanel.add(selectPointingAtRestrictionTwo9);
				
		JScrollPane outputScroll = new JScrollPane(output);
		outputScroll.setPreferredSize(new Dimension(Math.min(250, screenSize.width/4), Math.min(200, screenSize.height/5)));
		output.setEditable(false);
		dialogPanel.add(outputScroll);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridBagLayout());
		wrapper.add(bottom);

		GridBagConstraints row0 = new GridBagConstraints();
		row0.gridy = 0;
		
		bottom.add(Box.createRigidArea(new Dimension(10, 50)), row0);
		
		GridBagConstraints row1 = new GridBagConstraints();
		row1.gridy = 1;
		
		JPanel buttonsRow1 = new JPanel();
		buttonsRow1.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 0));
		bottom.add(buttonsRow1, row1);
		
		JButton instructions = new JButton("Instructions");
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(thiss, "Click New Rule and the computer will devise a rule.\n"
						+ "Build new koans and click ? to see if they follow the rule.\n"
						+ "Then use the options at the bottom to try to guess the rule.");
			}
		});
		buttonsRow1.add(instructions);

		JButton techSupport = new JButton("Tech Support");
		techSupport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(thiss, "That's not a bug, it's a feature.");
			}
		});
		buttonsRow1.add(techSupport);
		
		GridBagConstraints row2 = new GridBagConstraints();
		row2.gridy = 2;
		
		JPanel buttonsRow2 = new JPanel();
		buttonsRow2.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
		bottom.add(buttonsRow2, row2);


		newRule = new JButton("New Rule");
		newRule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newRule(Rule.randomWeightedRule(maxStackSize));
				startGame(Master.Computer);
			}
		});
		buttonsRow2.add(newRule);
		
		testRule = new JButton("Test Rule");
		testRule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Rule created = scrapeRule();
				clearRuleEntry();
				if(created != null) {
					purge();
					newRule(created);
					startGame(Master.Computer);
				}
			}
		});
		buttonsRow2.add(testRule);
		
		allRules = new JButton("All Rules");
		allRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame allRulesFrame = new JFrame("The only rules that really matter are these: what a man can do, and what a man can't do");
				JTextArea allRulesText = new JTextArea();
				allRulesText.setEditable(false);
				ArrayList<Rule> rules = Rule.getAllRules(maxStackSize);
				HashMap<Rule, Double> satisfiedList = new HashMap<Rule, Double>();
				for(Rule rule : rules) {
					double satisfied = simulateRule(rule, koanSize, koanSize, maxStackSize);
					satisfiedList.put(rule, satisfied);
				}
//				rules.sort(new Comparator<Rule>() {
//					public int compare(Rule rule1, Rule rule2) {
//						double diff = satisfiedList.get(rule2) - satisfiedList.get(rule1);
//						if(diff > 0.0) {
//							return 1;
//						}
//						else if(diff < 0.0) {
//							return -1;
//						}
//						return 0;
//					}
//				});
				for(Rule rule : rules ) {
					allRulesText.setText(allRulesText.getText() + "  " + (int) (satisfiedList.get(rule)*100) + "%\t" + rule.toString() + "\n");
				}
				allRulesText.setText(allRulesText.getText() + "\n" + rules.size() + " possible rules");
				JScrollPane allRulesScroll = new JScrollPane(allRulesText);
				allRulesScroll.setPreferredSize(new Dimension(Math.min(500, screenSize.width/4), Math.min(400, screenSize.height/2)));
				allRulesFrame.add(allRulesScroll);
				allRulesFrame.setVisible(true);
				allRulesFrame.pack();
			}
		});
		buttonsRow2.add(allRules);
		
		playMaster = new JButton("Play Master");
		playMaster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame(Master.Player);
			}
		});
		buttonsRow2.add(playMaster);
		
		ruleDispHider = new JPanel();
		CardLayout ruleDispCardLayout = new CardLayout();
		ruleDispHider.setLayout(ruleDispCardLayout);
		
		ruleDisp = new JTextArea("rules are made to be broken");
		ruleDisp.setEditable(false);
		ruleDispHider.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				if(currentMaster == Master.Computer) {
					endGame();
				}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		ruleDispHider.add(ruleDisp);
		JLabel keyLabel = new JLabel();
		keyLabel.setIcon(new ImageIcon(Main.loadImage("key2.png", 150, 75, true)));
		ruleDispHider.add(keyLabel);
		
		GridBagConstraints ruleDispGBC = new GridBagConstraints();
		ruleDispGBC.anchor = GridBagConstraints.EAST;
		ruleDispGBC.gridheight = 3;
		bottom.add(Box.createRigidArea(new Dimension(100, 10)), ruleDispGBC);
		bottom.add(ruleDispHider, ruleDispGBC);
		ruleDispHider.setPreferredSize(new Dimension(150, 75));
		ruleDispHider.setBorder(new LineBorder(Color.BLACK, 3, true));
		
		priorGuesses = new JPanel();
		priorGuesses.setLayout(new BoxLayout(priorGuesses, BoxLayout.Y_AXIS));
		wrapper.add(priorGuesses);
		
		pack();
	}
	
	public class StackMouseListener implements MouseListener {
		
		private Stack stack;
		
		public StackMouseListener(Stack stack) {
			this.stack = stack;
		}
		
		public void mouseReleased(MouseEvent e) {
			if(activestack != null && activestack != stack) {
				activestack.setBorder(Stack.border);
			}
			if(activestack == stack) {
				stack.rotatePieces();
				if(currentMaster == Master.Computer) {
					stack.getKoan().setWorthy(null);
				}
			}
			else {
				stack.setBorder(Stack.activeBorder);
				activestack = stack;
			}
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
	}
	
	private Rule scrapeRule() {
		Rule scraped = null;
		if(!selectProperties1.getSelectedItem().equals("") && !selectComparator1.getSelectedItem().equals("") && !selectValues1.getSelectedItem().equals("")) {
			try {
				scraped = new ValueRule((String) selectProperties1.getSelectedItem(), ValueRule.Comparator.valueOf((String) selectComparator1.getSelectedItem()), ValueRule.Value.valueOf((String) selectValues1.getSelectedItem()), maxStackSize);
			}
			catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}
		else if(!selectProperties2.getSelectedItem().equals("") && !selectValues2.getSelectedItem().equals("")) {
			try {
				scraped = new ValueRule("" + selectProperties2.getSelectedItem(), ValueRule.Comparator.Equal, ValueRule.Value.valueOf("" + selectValues2.getSelectedItem()), maxStackSize);
			}
			catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}
		else if(!selectItemOne3.getSelectedItem().equals("") && !selectItemTwo3.getSelectedItem().equals("")) {
			try {
				scraped = new ContainsAndRule("" + selectItemOne3.getSelectedItem(), "" + selectItemTwo3.getSelectedItem(), maxStackSize);
			}
			catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}
		else if(!selectSize4.getSelectedItem().equals("") && !selectColor4.getSelectedItem().equals("")) {
			try {
				scraped = new SpecificPieceRule(Piece.Size.valueOf("" + selectSize4.getSelectedItem()), Piece.Color.valueOf("" + selectColor4.getSelectedItem()), maxStackSize);
			}
			catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}
		else if(!selectProperties5.getSelectedItem().equals("")) {
			try {
				scraped = new ValueRule("" + selectProperties5.getSelectedItem(), ValueRule.Comparator.GreaterEqual, ValueRule.Value.One, maxStackSize);
			}
			catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}
		else if(!selectTouchingRestrictionOne6.getSelectedItem().equals("") && !selectTouchingRestrictionTwo6.getSelectedItem().equals("")) {
			try {
				scraped = new TouchingRule("" + selectTouchingRestrictionOne6.getSelectedItem(), "" + selectTouchingRestrictionTwo6.getSelectedItem(), maxStackSize);
			}
			catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}
		else if(!selectTouchingRestrictionOne7.getSelectedItem().equals("") && !selectTouchingRestrictionTwo7.getSelectedItem().equals("")) {
			try {
				scraped = new TouchingRule("" + selectTouchingRestrictionOne7.getSelectedItem(), "" + selectTouchingRestrictionTwo7.getSelectedItem(), maxStackSize);
			}
			catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}
		else if(!selectPointingAtRestrictionOne8.getSelectedItem().equals("") && !selectPointingAtRestrictionTwo8.getSelectedItem().equals("")) {
			try {
				scraped = new PointingAtRule("" + selectPointingAtRestrictionOne8.getSelectedItem(), "" + selectPointingAtRestrictionTwo8.getSelectedItem(), maxStackSize);
			}
			catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}
		else if(!selectPointingAtRestrictionOne9.getSelectedItem().equals("") && !selectPointingAtRestrictionTwo9.getSelectedItem().equals("")) {
			try {
				scraped = new PointingAtRule("" + selectPointingAtRestrictionOne9.getSelectedItem(), "" + selectPointingAtRestrictionTwo9.getSelectedItem(), maxStackSize);
			}
			catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage());
			}
		}
		else {
			JOptionPane.showMessageDialog(this, "No rule entered");
		}
		return scraped;
	}
	
	private void purge() {

		main.removeAll();
		activestack = null;
		koans.clear();
		pack();
		repaint();
	}
	
	private void startGame(Master master) {
		endGame();
		output.setText("");
		currentMaster = master;
		if(master == Master.Computer) {
			setRuleHidden(true);
			priorGuesses.removeAll();
			submit.setEnabled(true);
			newRule.setEnabled(false);
			testRule.setEnabled(false);
			playMaster.setEnabled(false);
			CardLayout layout = (CardLayout) dialogPanel.getLayout();
			layout.first(dialogPanel);
		}
		else if(master == Master.Player) {
			JOptionPane.showMessageDialog(thiss, "Make two koans, one white and one black, then submit.");
			submit.setEnabled(true);
			CardLayout layout = (CardLayout) dialogPanel.getLayout();
			layout.last(dialogPanel);
			purge();
			priorGuesses.removeAll();
			newRule.setEnabled(false);
			testRule.setEnabled(false);
			playMaster.setEnabled(false);
			setRule(null);
			Koan ko = blankKoan(koanSize, koanSize);
			ko.setMarkerOfWorthEditable(true);
			addKoan(ko);
			Koan an = blankKoan(koanSize, koanSize);
			an.setMarkerOfWorthEditable(true);
			addKoan(an);
		}
		pack();
	}
	
	private void endGame() {
		setRuleHidden(false);
		newRule.setEnabled(true);
		testRule.setEnabled(true);
		playMaster.setEnabled(true);
		submit.setEnabled(false);
		clearRuleEntry();
	}
	
	private void clearRuleEntry() {
		selectProperties1.setSelectedIndex(0);
		selectComparator1.setSelectedIndex(0);
		selectValues1.setSelectedIndex(0);
		selectProperties2.setSelectedIndex(0);	
		selectValues2.setSelectedIndex(0);	
		selectItemOne3.setSelectedIndex(0);	
		selectItemTwo3.setSelectedIndex(0);
		selectColor4.setSelectedIndex(0);
		selectSize4.setSelectedIndex(0);
		selectProperties5.setSelectedIndex(0);
		selectTouchingRestrictionOne6.setSelectedIndex(0);
		selectTouchingRestrictionTwo6.setSelectedIndex(0);
		selectTouchingRestrictionOne7.setSelectedIndex(0);
		selectTouchingRestrictionTwo7.setSelectedIndex(0);
		selectPointingAtRestrictionOne8.setSelectedIndex(0);
		selectPointingAtRestrictionTwo8.setSelectedIndex(0);
		selectPointingAtRestrictionOne9.setSelectedIndex(0);
		selectPointingAtRestrictionTwo9.setSelectedIndex(0);
	}
	
	private void ruleGuessed(Rule guess) {
		if(guess.equals(rule)) {
			ArrayList<Rule> possibleRules = getPossibleRules();
			JTextArea text = new JTextArea();
			text.setEditable(false);
			text.setText(rule.toString() + "\n\n");
			for(Rule aRule : possibleRules) {
				if(!aRule.equals(rule)) {
					text.setText(text.getText() + aRule.toString() + "\n");
				}
			}
			String rulesReport = "";
			for(int i = 1; i <= koans.size(); i++) {
				ArrayList<Rule> possibleRulesProvisional = getPossibleRules(i);
				rulesReport += possibleRulesProvisional.size();
				if(i < koans.size()) {
					rulesReport += ", ";
				}
			}
			text.setText(text.getText() + "\n" + rulesReport);
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
			panel.add(new JLabel(new ImageIcon(trophy)));
			panel.add(text);
			JOptionPane.showMessageDialog(thiss, panel);
//			trophyPanel.add(new JLabel(new ImageIcon(trophy)));
			endGame();
		}
		else {
			//search for existing counterexamples
			boolean existsCounterexample = false;
			for(Koan koan : koans) {
				if(rule.statisfiesRule(koan) != guess.statisfiesRule(koan)) {
					Border existingBorder = koan.getBorder();
					koan.setBorder(new LineBorder(Color.RED, 15, true));
					main.scrollRectToVisible(koan.getBounds());
					JOptionPane.showMessageDialog(thiss, guess.toString() + "\nA counterexample exists");
					koan.setBorder(existingBorder);
					priorGuesses.add(new JLabel(guess.toString(), new ImageIcon(Main.loadImage("wrong.png", 20, 20, true)), JLabel.CENTER));
					pack();
					existsCounterexample = true;
					break;
				}
			}
			if(!existsCounterexample) {
				//generate a new counterexample
				try {
					Koan counterexample = addCounterExample(guess);
					Border existingBorder = counterexample.getBorder();
					counterexample.setBorder(new LineBorder(Color.RED, 15, true));
					priorGuesses.add(new JLabel(guess.toString(), new ImageIcon(Main.loadImage("wrong.png", 20, 20, true)), JLabel.CENTER));
					pack();
					JOptionPane.showMessageDialog(thiss, guess.toString() + "\nHere is a counterexample");
					counterexample.setBorder(existingBorder);
				}
				catch(InterruptedException exception) {
					//failed to generate a counterexample, rules may be equivalent
					JOptionPane.showMessageDialog(thiss, exception.getMessage());
					endGame();
				}
			}
		}
	}
	
	private ArrayList<Rule> getPossibleRules() {
		ArrayList<Rule> possibleRules = Rule.getAllRules(maxStackSize);
		
		Iterator<Rule> it = possibleRules.iterator();
		while(it.hasNext()) {
			Rule nextRule = it.next();
			for(Koan koan : koans) {
				if(koan.isWorthy() != null && koan.isWorthy() != nextRule.statisfiesRule(koan)) {
					it.remove();
					break;
				}
			}
		}
		return possibleRules;
	}
	
	private ArrayList<Rule> getPossibleRules(int numKoans) {
		ArrayList<Rule> possibleRules = Rule.getAllRules(maxStackSize);
		
		Iterator<Rule> it = possibleRules.iterator();
		while(it.hasNext()) {
			Rule nextRule = it.next();
			for(int i = 0; i < koans.size() && i < numKoans; i++) {
				Koan koan = koans.get(i);
				if(koan.isWorthy() != null && koan.isWorthy() != nextRule.statisfiesRule(koan)) {
					it.remove();
					break;
				}
			}
		}
		return possibleRules;
	}
	
	private void computerGuessesRule() {
		ArrayList<Rule> possibleRules = getPossibleRules();
		output.setText("");
		for(Rule rule : possibleRules) {
			output.setText(output.getText() + rule.toString() + "\n");
		}
		output.setText(output.getText() + possibleRules.size() + " possible rules");
		
		if(possibleRules.size() == 0) {
			JOptionPane.showMessageDialog(this, "No possible rules");
			endGame();
		}
		else if(possibleRules.size() == 1) {
			JOptionPane.showMessageDialog(this, "The rule is " + possibleRules.get(0).toString());
			endGame();
		}
		else {
			Koan testKoan = buildTestKoan(possibleRules);
			if(testKoan != null) {
				for(ArrayList<Stack> col : testKoan.getStacks()) {
					for(Stack stack : col) {
						stack.updateIcon();
					}
				}
				testKoan.setMarkerOfWorthEditable(true);
				addKoan(testKoan);
			}
			else {
				endGame();
			}
		}
	}
	
	private Koan buildTestKoan(ArrayList<Rule> possibleRules) {
		//devise a koan to test remaining possible rules;
		Koan testKoan = null;
		int attempts = 0;
		while(testKoan == null && attempts < 300) {
			attempts++;
			testKoan = randomKoan(koanSize, koanSize, maxStackSize, false);
			boolean havingTrouble = false;
			if(attempts > 150 ) {
				havingTrouble = true;
			}
			double agreementIndex = 0;
			for(Rule aRule : possibleRules) {
				if(aRule.statisfiesRule(testKoan)) {
					agreementIndex++;
				}
			}
			
			//ideally test koan will obey half of remaining rules and break other half
			//insist to get as close to 50% as is possible, else throw away test koan and try again
			double numRules = possibleRules.size();
			double ratio = agreementIndex/numRules;
			if(!havingTrouble) {
				if(numRules % 2 == 0) {
					if(ratio != 0.5) {
						testKoan = null;
					}
				}
				else {
					if(ratio < (numRules-1)/(2*numRules) || ratio > (numRules+1)/(2*numRules)) {
						testKoan = null;
					}
				}
			}
			else {
				//if it's taking too long, settle for a less efficient guess
				if(ratio <= 0.2 || ratio >= 0.8) {
					testKoan = null;
				}
			}
		}
		return testKoan;
	}
	
	private Koan addCounterExample(Rule guess) throws InterruptedException {
		Koan counterexample = null;
		int counter = 0;
		while(counterexample == null) {
			counter++;
			if(counter > 300) {
				throw new InterruptedException("can't find counterexample");
			}
			Koan trial = randomKoan(koanSize, koanSize, maxStackSize, true);
			if(rule.statisfiesRule(trial) != guess.statisfiesRule(trial)) {
				counterexample = trial;
			}
		}
		counterexample.setWorthy(rule.statisfiesRule(counterexample));
		addKoan(counterexample);
		return counterexample;
	}
	
	private void setRuleHidden(boolean hidden) {
		CardLayout cardLayout = (CardLayout) ruleDispHider.getLayout();
		if(hidden) {
			cardLayout.last(ruleDispHider);
		}
		else {
			cardLayout.first(ruleDispHider);
		}
		pack();
		repaint();
	}
	
	private void setRule(Rule aRule) {
		this.rule = aRule;
		if(rule != null) {
			ruleDisp.setText(rule.toStringFriendly());
		}
		else {
			ruleDisp.setText("");
		}
	}
	
	private void newRule(Rule newRule) {
		setRule(newRule);
		int counter = 0;
		purge();

		Koan firstExample = randomKoan(koanSize, koanSize, maxStackSize, true);
		firstExample.setWorthy(rule.statisfiesRule(firstExample));
		boolean firstExampleSatisfiesRule = rule.statisfiesRule(firstExample);
		
		Koan secondExample = null;
		while(secondExample == null) {
			counter++;
			if(counter > 300) {
				endGame();
				break;
			}
			Koan trial = randomKoan(koanSize, koanSize, maxStackSize, true);
			if(rule.statisfiesRule(trial) != firstExampleSatisfiesRule) {
				secondExample = trial;
				secondExample.setWorthy(rule.statisfiesRule(secondExample));
			}
		}
		
		if(firstExampleSatisfiesRule) {
			addKoan(firstExample);
			if(secondExample != null) {
				addKoan(secondExample);
			}
		}
		else {
			if(secondExample != null) {
				addKoan(secondExample);
			}
			addKoan(firstExample);
		}
		
		pack();
	}
	
	private double simulateRule(Rule rule, int width, int height, int maxStackSize) {
		double satisfied = 0;
		if(simCity == null) {
			simCity = new ArrayList<Koan>();
			for(int i = 0; i < 100; i++) {
				Koan test = randomKoan(width, height, maxStackSize, false);
				simCity.add(test);
			}
		}
		for(Koan koan : simCity) {
			if(rule.statisfiesRule(koan)) {
				satisfied++;
			}
		}
		return satisfied/100.0;
	}
	
	private void addKoan(Koan toAdd) {
		main.add(toAdd);
		koans.add(toAdd);
		if(activestack != null) {
			activestack.setBorder(Stack.border);
		}
		activestack = null;
		pack();
		JScrollBar horiz = mainScroll.getHorizontalScrollBar();
		horiz.setValue(horiz.getMaximum());
		repaint();
	}
	
	private Koan groundFloor(int width, int height) {

		ArrayList<ArrayList<Stack>> stacks2 = new ArrayList<ArrayList<Stack>>();
		for(int i = 0; i < width; i++) {
			ArrayList<Stack> row = new ArrayList<Stack>();
			for(int j = 0; j < height; j++) {
				ArrayList<Piece> pieces = new ArrayList<Piece>();
				pieces.add(Piece.randomPiece());
				Stack t = new Stack(pieces, true);
				t.setBorder(Stack.border);
				row.add(t);
				t.setBorder(Stack.border);
				t.addMouseListener(new StackMouseListener(t));
			}
			stacks2.add(row);
		}
		
		Koan c = new Koan(this, stacks2);
		for(ArrayList<Stack> row : stacks2) {
			for(Stack stack : row) {
				stack.setKoan(c);
			}
		}
		
		return c;
		
		//if(rule != null) {
		//	c.setWorthy(rule.statisfiesRule(c));
		//}
	}
	
	public void markerOfWorthClicked(Koan koan) {
		if(rule != null) {
			koan.setWorthy(rule.statisfiesRule(koan));
		}
	}
	
	public void enlargeKoan(Koan koan) {
		ArrayList<ArrayList<Stack>> stacks = koan.getStacks();
		int currentColSize = 0;
		if(!stacks.isEmpty()) {
			currentColSize = stacks.get(0).size();
		}
		for(int x = 0; x < stacks.size(); x++) {
			ArrayList<Stack> col = stacks.get(x);
			Stack newStack = new Stack(new ArrayList<Piece>(), true);
			newStack.setKoan(koan);
			newStack.addMouseListener(new StackMouseListener(newStack));
			col.add(newStack);
		}
		ArrayList<Stack> newCol = new ArrayList<Stack>();
		stacks.add(newCol);
		for(int y = 0; y < currentColSize+1; y++) {
			Stack newStack = new Stack(new ArrayList<Piece>(), true);
			newStack.setKoan(koan);
			newStack.addMouseListener(new StackMouseListener(newStack));
			newCol.add(newStack);
		}
		koan.setStacks(stacks);
	}
	
	private Koan blankKoan(int width, int height) {
		ArrayList<ArrayList<Stack>> stacks = new ArrayList<ArrayList<Stack>>();
		for(int i = 0; i < width; i++) {
			ArrayList<Stack> row = new ArrayList<Stack>();
			for(int j = 0; j < height; j++) {
				ArrayList<Piece> pieces = new ArrayList<Piece>();
				Stack stack = new Stack(pieces, true);
				stack.addMouseListener(new StackMouseListener(stack));
				row.add(stack);
			}
			stacks.add(row);
		}
		Koan koan = new Koan(this, stacks);
		for(ArrayList<Stack> row : stacks) {
			for(Stack stack : row) {
				stack.setKoan(koan);
			}
		}
		return koan;
	}
	
	private Koan randomKoan(int width, int height, int maxStackSize, boolean updateIconOnStartup) {
		ArrayList<ArrayList<Stack>> stacks = new ArrayList<ArrayList<Stack>>();
		for(int i = 0; i < width; i++) {
			ArrayList<Stack> row = new ArrayList<Stack>();
			for(int j = 0; j < height; j++) {
				ArrayList<Piece> pieces = new ArrayList<Piece>();
				Random rand = new Random();
				int numPieces = rand.nextInt(maxStackSize + 1);
				if(numPieces > 0) {
					Piece base = Piece.randomPiece();
					Piece.Direction dir = base.getDirection();
					pieces.add(base);
					while(pieces.size() < numPieces) {
						Piece.Color color = Piece.Color.values()[rand.nextInt(Piece.Color.values().length)];
						Piece.Size size = null;
						while(size == null) {
							Piece.Size newSize = Piece.Size.values()[rand.nextInt(Piece.Size.values().length)];
							if(newSize.getPips() <= base.getSize().getPips()) {
								size = newSize;
							}
						}
						Piece next = new Piece(color, size, dir);
						pieces.add(next);
						base = next;
					}
				}
				Stack stack = new Stack(pieces, updateIconOnStartup);
				stack.addMouseListener(new StackMouseListener(stack));
				row.add(stack);
			}
			stacks.add(row);
		}
		Koan koan = new Koan(this, stacks);
		for(ArrayList<Stack> row : stacks) {
			for(Stack stack : row) {
				stack.setKoan(koan);
			}
		}
		return koan;
	}
}