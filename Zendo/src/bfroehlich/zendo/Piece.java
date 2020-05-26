package bfroehlich.zendo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Piece {
	
	public enum Direction {
		North, East, South, West, Up;
		
		public Point getPointer() {
			if(this == North) {
				return new Point(0, -1);
			}
			else if(this == South) {
				return new Point(0, 1);
			}
			else if(this == East) {
				return new Point(1, 0);
			}
			else if(this == West) {
				return new Point(-1, 0);
			}
			else if(this == Up) {
				return new Point(0, -1);
			}
			return null;
		}
		
		public Direction rotate() {
			try {
				return values()[this.ordinal() + 1];
			}
			catch(ArrayIndexOutOfBoundsException e) {
				return values()[0];
			}
		}
	}
	
	public enum Color {
		Blue, Yellow, Green, Red
	}
	
	public enum Size {
		Small, Medium, Large;
		
		public int getSizeMultiplier() {
			if(this == Size.Small) {
				return 3;
			}
			else if(this == Size.Medium) {
				return 4;
			}
			else if(this == Size.Large) {
				return 5;
			}
			return 0;
		}
		
		public int getPips() {
			if(this == Size.Small) {
				return 1;
			}
			else if(this == Size.Medium) {
				return 2;
			}
			else if(this == Size.Large) {
				return 3;
			}
			return 0;
		}
	}
	
	private Color color;
	private Size size;
	private Direction direction;
	private Image image;
	
	public Piece(Color color, Size size, Direction direction) {
		this.color = color;
		this.size = size;
		this.direction = direction;
		Image masterImage = Main.loadImage("pyramids2.png", 649, 365, false);
		//(3, 4, 5) --> (60, 85, 105) (18, 9, 5), (290, 150, 0)
		//size 3: 20+72x, 290, 
		//size 4: 13+72x, 146 
		//size 5: 5+72x, 0 
		Point p = new Point(4 + 8*(3-size.getPips()) + 72*color.ordinal(), 145*(3-size.getPips()));
		Dimension[] dimensions = {new Dimension(37, 60), new Dimension(53, 87), new Dimension(65, 105)};
		Dimension d = dimensions[size.getPips()-1];
		
		if(direction == Direction.Up) {
			p = new Point(297 + 82*color.ordinal(), 31+142*(3-size.getPips()));
			if(size == Size.Medium) {
				p.y += 4;
			}
			d = new Dimension(32+23*(size.getPips()-1), 32+23*(size.getPips()-1));
		}
		
		BufferedImage img = Main.toBufferedImage(masterImage).getSubimage(p.x, p.y, d.width, d.height); //fill in the corners of the desired crop location here
		BufferedImage subImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = subImage.createGraphics();
		g.drawImage(img, 0, 0, null);
		Image rotated = Main.rotate(subImage, 90*direction.ordinal());
		
		rotated = Main.screenScale(rotated);
		image = rotated;
	}
	
	public Piece(Color color, Size size) {
		this(color, size, Direction.Up);
	}
	
	public Piece(Piece piece) {
		this(piece.getColor(), piece.getSize(), piece.getDirection());
	}
	
	public static Piece randomPiece() {
		Random rand = new Random();
		Color c = Color.values()[rand.nextInt(4)];
		Size size = Size.values()[rand.nextInt(Size.values().length)];
		Direction[] directions =  Direction.values();
		Direction dir = directions[rand.nextInt(directions.length)];
		return new Piece(c, size, dir);
	}
	
	public static ArrayList<Piece> getAllPieces() {
		Size[] sizes = Size.values();
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for(int j = 0; j < sizes.length; j++) {
			for(Color c : Color.values()) {
				pieces.add(new Piece(c, sizes[j]));
			}
		}
		return pieces;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public Direction getDirection() {
		return direction;
	}

//	public void setDirection(Direction newDirection) {
//		
//		System.out.println("set direction: " + this.direction + " to " + newDirection);
//		Direction oldDirection = this.direction;
//		this.direction = newDirection;
//		Image rotated = Main.rotate(image, 90*newDirection.ordinal() - 90*oldDirection.ordinal());
//		image = rotated;
//	}
	
	public Image getImage() {
		return image;
	}
	
	public String toString() {
		return color + " " + size + " " + direction;
	}
	
//	public void paint(Graphics g) {
//		g.setColor(color);
//		if(direction == Direction.East) {
//			int[] xPoints = {0, 20*size, 0};
//			int[] yPoints = {0, 10*size, 20*size};
//			g.fillPolygon(xPoints, yPoints, 3);
//		}
//		else if(direction == Direction.West) {
//			int[] xPoints = {20*size, 20*size, 0};
//			int[] yPoints = {0, 20*size, 10*size};
//			g.fillPolygon(xPoints, yPoints, 3);
//		}
//		else if(direction == Direction.South) {
//			int[] xPoints = {0, 20*size, 10*size};
//			int[] yPoints = {0, 0, 20*size};
//			g.fillPolygon(xPoints, yPoints, 3);
//		}
//		else if(direction == Direction.North) {
//			int[] xPoints = {0, 20*size, 10*size};
//			int[] yPoints = {0, 0, 20*size};
//			g.fillPolygon(xPoints, yPoints, 3);
//		}
//		else if(direction == Direction.Up) {
//			int[] xPoints = {0, 20*size, 10*size};
//			int[] yPoints = {20*size, 20*size, 10*size};
//			g.fillPolygon(xPoints, yPoints, 3);
//		}
//	}
}