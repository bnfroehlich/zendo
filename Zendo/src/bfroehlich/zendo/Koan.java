package bfroehlich.zendo;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Koan extends JPanel {

	private GameWindow window;
	private ArrayList<ArrayList<Stack>> stacks;
	private JLabel markerOfWorth;
	
	private boolean markerOfWorthEditable;
	private Boolean worthy;
	
	private static Image white;
	private static Image black;
	private static Image question;
	
	public Koan(GameWindow window, ArrayList<ArrayList<Stack>> stacks) {
		if(white == null) {
			white = Main.loadImage("white.png", 50, 50, true);
		}
		if(black == null) {
			black = Main.loadImage("black.png", 50, 50, true);
		}
		if(question == null) {
			question = Main.loadImage("question.png", 50, 50, true);
		}
		
		this.window = window;
		this.stacks = stacks;
		init();
	}
	
	private void init() {
		setBorder(new LineBorder(Color.BLUE, 3));
		setLayout(new GridBagLayout());
		for(int i = 0; i < stacks.size(); i++) {
			ArrayList<Stack> col = stacks.get(i);
			for(int j = 0; j < col.size(); j++) {
				addStackToGUI(col.get(j), i, j);
			}
		}
		
		markerOfWorth = new JLabel();
		markerOfWorth.setIcon(new ImageIcon(question));
		addMarkerOfWorth();

		final Koan myself = this;
		markerOfWorth.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				window.markerOfWorthClicked(myself);
				if(markerOfWorthEditable) {
					if(worthy == null) {
						setWorthy(true);
					}
					else {
						setWorthy(!worthy);
					}
				}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
	}

	public ArrayList<ArrayList<Stack>> getStacks() {
		return stacks;
	}

	public void setStacks(ArrayList<ArrayList<Stack>> stacks) {
		this.stacks = stacks;
		this.removeAll();
		for(int x = 0; x < stacks.size(); x++) {
			ArrayList<Stack> col = stacks.get(x);
			for(int y = 0; y < col.size(); y++) {
				addStackToGUI(col.get(y), x, y);
			}
		}
		addMarkerOfWorth();
	}
	
	public void addStackToGUI(Stack stack, int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.ipadx = 10;
		c.ipady = 10;
		add(stack, c);
	}
	
	private void addMarkerOfWorth() {
		int width = stacks.size();
		int height = 0;
		if(!stacks.isEmpty()) {
			height = stacks.get(0).size();
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = (int) (width-1)/2;
		if(width % 2 == 0) {
			c.gridwidth = 2;
		}
		c.ipady = height;
		add(markerOfWorth, c);
	}
	
	public void setWorthy(Boolean worthy) {
		this.worthy = worthy;
		if(worthy == null) {
			markerOfWorth.setIcon(new ImageIcon(question));
		}
		else if(worthy) {
			markerOfWorth.setIcon(new ImageIcon(white));
		}
		else if(!worthy) {
			markerOfWorth.setIcon(new ImageIcon(black));
		}
	}
	
	public Boolean isWorthy() {
		return worthy;
	}
	
	public void setMarkerOfWorthEditable(boolean editable) {
		this.markerOfWorthEditable = editable;
	}
	
	public boolean isMarkerOfWorthEditable() {
		return markerOfWorthEditable;
	}
}