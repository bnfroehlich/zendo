package bfroehlich.zendo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Stack extends JPanel {

	private ArrayList<Piece> pieces;
	private JLabel label;
	private Koan koan;
	
	public static Border border = new LineBorder(Color.WHITE, 3, true);
	public static Border activeBorder = new LineBorder(Color.RED, 3, true);

	public Stack(ArrayList<Piece> pieces, boolean updateIconOnStartup) {
		this.pieces = pieces;
		label = new JLabel();
		add(label);
		if(pieces != null && !pieces.isEmpty() && updateIconOnStartup) {
			updateIcon();
		}
		setBorder(border);
//		addMouseListener(new MouseListener() {
//			public void mouseReleased(MouseEvent e) {
//				//t.setOpaque(true);
//				//t.setForeground(Color.RED);
//				//t.setBackground(Color.RED);
//				setBorder(BorderFactory.createLineBorder(Color.RED, 10));
//			}
//			
//			public void mousePressed(MouseEvent e) {}
//			
//			public void mouseExited(MouseEvent e) {}
//			
//			public void mouseEntered(MouseEvent e) {}
//			
//			public void mouseClicked(MouseEvent e) {}
//		});
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}
	
	public Koan getKoan() {
		return koan;
	}

	public void setKoan(Koan koan) {
		this.koan = koan;
	}
	
	public void rotatePieces() {
		for(int i = 0; i < pieces.size(); i++) {
			Piece old = pieces.get(i);
			Piece replacement = new Piece(old.getColor(), old.getSize(), old.getDirection().rotate());
			pieces.remove(i);
			pieces.add(i, replacement);
		}
		updateIcon();
	}

	public boolean addPiece(Piece newPiece) {
		if(!pieces.isEmpty()) {
			Piece lastPiece = pieces.get(pieces.size()-1);
			if(newPiece.getSize().getPips() > lastPiece.getSize().getPips()) {
				return false;
			}
			newPiece = new Piece(newPiece.getColor(), newPiece.getSize(), lastPiece.getDirection());
			pieces.add(newPiece);
			
		}
		else {
			pieces.add(newPiece);
		}
		updateIcon();
		return true;
	}
	
	public boolean removeTopPiece() {
		if(!pieces.isEmpty()) {
			pieces.remove(pieces.size()-1);
			updateIcon();
			return true;
		}
		else {
			return false;	
		}
	}
	
	public void updateIcon() {
		if(pieces.isEmpty()) {
			label.setIcon(null);
			return;
		}
		Piece.Direction direction = pieces.get(0).getDirection();
		
		int offset = 15;
		
		final BufferedImage firstImage = Main.toBufferedImage(pieces.get(0).getImage());
	    int w = firstImage.getWidth(null);
	    int h = firstImage.getHeight(null);
	    
	    if(direction == Piece.Direction.East || direction == Piece.Direction.West) {
	    	w += offset*(pieces.size());
	    }
	    else if(direction == Piece.Direction.North || direction == Piece.Direction.South || direction == Piece.Direction.Up) {
	    	h += offset*(pieces.size());
	    }

	    final BufferedImage finalImage = new BufferedImage(w, h,
	        BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = finalImage.createGraphics();
	    
	    //backtrack to the starting point
	    if(direction == Piece.Direction.North || direction == Piece.Direction.West || direction == Piece.Direction.Up) {
	    	g.translate(direction.getPointer().x*offset*(pieces.size()-1)*(-1), direction.getPointer().y*offset*(pieces.size()-1)*(-1));
	    }
	    
	    for(int i = 0; i < pieces.size(); i++) {
	    	Piece piece = pieces.get(i);
		    g.drawImage(piece.getImage(), 0, 0, null);
		    
		    if(i < pieces.size() - 1) {
		    	Piece nextPiece = pieces.get(i+1);
		    	
		    	//centering
		    	if(piece.getDirection() == Piece.Direction.North || piece.getDirection() == Piece.Direction.South || piece.getDirection() == Piece.Direction.Up) {
		    		g.translate((piece.getImage().getWidth(this) - nextPiece.getImage().getWidth(this))/2, 0);
	    		}
		    	else if(piece.getDirection() == Piece.Direction.East || piece.getDirection() == Piece.Direction.West) {
		    		g.translate(0, (piece.getImage().getHeight(this) - nextPiece.getImage().getHeight(this))/2);
		    	}
		    	
		    	if(piece.getDirection() == Piece.Direction.East) {
	    			g.translate(piece.getImage().getWidth(this) - nextPiece.getImage().getWidth(this), 0);
	    		}
	    		else if(piece.getDirection() == Piece.Direction.South) {
	    			g.translate(0, piece.getImage().getHeight(this) - nextPiece.getImage().getHeight(this));
	    		}
		    	
		    	//tightly group Up stacks
		    	if(piece.getDirection() == Piece.Direction.Up) {
		    		g.translate(0, piece.getImage().getHeight(this) - nextPiece.getImage().getHeight(this));
		    	}
		    	
		    	//offset for stacking
			    g.translate(piece.getDirection().getPointer().x*offset, piece.getDirection().getPointer().y*offset);
		    }
	    }
	    //g.drawImage(newPiece.getImage(), 0, 0, null);
	    g.dispose();
	    
	    label.setIcon(new ImageIcon(finalImage));
	}
	
//	public void paint(Graphics g) {
//		for(int i = 0; i < pieces.size(); i++) {
//			Piece piece = pieces.get(i);
//			Graphics graphics = g.create();
//			piece.paint(graphics);
//			if(piece.getDirection() == Direction.East) {
//				graphics.translate(10, 0);
//			}
//			else if(piece.getDirection() == Direction.South) {
//				graphics.translate(0, 10);
//			}
//			else if(piece.getDirection() == Direction.West) {
//				graphics.translate(-10, 0);
//			}
//			else if(piece.getDirection() == Direction.North || piece.getDirection() == Direction.Up) {
//				graphics.translate(0, -10);
//			}
//		}
//	}
}