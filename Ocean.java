/* Milena Mijuskovic and Selena Zhang
 * ICS4UE-01/51
 * Mr. Benum
 * Jan 20th 2021
 * Program Summary: Class for handling the creation of an ocean and its methods
 */
import java.awt.Color;
import java.awt.Graphics;

public class Ocean extends GameObject{
	//fields
	private int r = 0, g = 0, b = 255; //rgb values
	private Color c;
	/**
	 * This constructor makes an ocean
	 * @param x - x location
	 * @param y - y location
	 */
	public Ocean(int x, int y) {
		c = new Color(r, g, b);
		setColor(c);
		setSize(x, y);
		setX(0);
		setY(y);
	}
	/**
	 * This method is called every tick of time
	 */
	@Override
	public void act() {
		// TODO Auto-generated method stub
		oceanColour();
	}
	/**
	 * This method changes the colour of the ocean in an ombre effect
	 */
	private void oceanColour() {
		if(r == 0)
			g++;
		if(g == 200)
			r++;
		if(r == 75)
			g--;
		if(g == 0)
			r--;
		setColor(new Color(r, g, b));
		repaint();
	}
	
}
