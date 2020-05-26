package bfroehlich.zendo;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Main {

	
	public static void main(String[] args) {
		ArrayList<String> ruleTypes = new ArrayList<String>();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		for(int i = 0; i < 10000; i++) {
			Rule rule = Rule.randomWeightedRule(3);
			String ruleType = rule.getClass().getSimpleName();
			if(!ruleTypes.contains(ruleType)) {
				ruleTypes.add(ruleType);
				counts.add(0);
			}
			int index = ruleTypes.indexOf(ruleType);
			counts.set(index, counts.get(index) + 1);
		}
		System.out.println(ruleTypes);
		System.out.println(counts);
		
		new GameWindow().setVisible(true);
	}
	
	public static Image loadImage(String path, int width, int height, boolean allowScreenScaling) {
		if(path == null) {
			return null;
		}
		Image image = null;
		try {
			image = ImageIO.read(Main.class.getResource("/" + path));
		}
		catch(IOException e) {
			System.err.println(e.getClass());
			System.err.println(e.getMessage());
		}
        image = resize(image, width, height, allowScreenScaling);
        return image;
	}
	
	public static BufferedImage loadBufferedImage(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Main.class.getResource("/" + path));
		}
		catch(IOException e) {
			System.err.println(e.getClass());
			System.err.println(e.getMessage());
		}
		return image;
	}
	
	public static Image resize(Image image, int width, int height, boolean allowScreenScaling) {
		//assume default screen height of 1000 and shrink images if screen is smaller
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        if(screenSize.height < 1000 && allowScreenScaling) {
        	width = (width*screenSize.height)/1000;
        	height = (height*screenSize.height)/1000;
        }
		
        if(width > 0 && height > 0) {
        	image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        }
        return image;
	}
	
	public static Image screenScale(Image image) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		//assume default screen height of 1000 and shrink images if screen is smaller
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        if(screenSize.height < 1000) {
        	width = (width*screenSize.height)/1000;
        	height = (height*screenSize.height)/1000;
        }
        if(width > 0 && height > 0) {
        	image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        }
        return image;
	}
	
	public static BufferedImage rotate(Image img, double angle)
	{
	    double sin = Math.abs(Math.sin(Math.toRadians(angle))),
	           cos = Math.abs(Math.cos(Math.toRadians(angle)));

	    int w = img.getWidth(null), h = img.getHeight(null);

	    int neww = (int) Math.floor(w*cos + h*sin),
	        newh = (int) Math.floor(h*cos + w*sin);

	    BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = bimg.createGraphics();

	    g.translate((neww-w)/2, (newh-h)/2);
	    g.rotate(Math.toRadians(angle), w/2, h/2);
	    g.drawRenderedImage(toBufferedImage(img), null);
	    g.dispose();

	    return bimg;
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
}