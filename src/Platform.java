/* Selena Zhang and Milena Mijuskovic
 * ICS4UE-01/51
 * Mr. Benum
 * Jan 20th 2021
 * Program Summary: This class defines the Platform object for Doodle Jump
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Platform extends GameObject
{
	//fields
	private Color c;
	private final int GOOD_RED = 113, GOOD_GREEN = 185, GOOD_BLUE = 69; //rgb values of a good platform
	
	/**
	 * This constructor makes a platform
	 * @param x - x location
	 * @param y - y location
	 * @param height - height of a platform
	 * @param width - width of a platform
	 * @param colour - colour
	 */
	public Platform(int x, int y, int height, int width, Color colour) 
	{
		c = colour;
		setColor(c);
		setSize(width, height);
		setX(x);
		setY(y);
	}
	/**
	 * This method is called every tick of time
	 */
	@Override
	public void act() 
	{
		
	}
	/**
	 * This method checks if the current platform is a good (green) or a bad (brown) platform
	 * @return true or false
	 */
	public boolean goodPlatform() {
		if(c.getRed() == GOOD_RED && c.getGreen() == GOOD_GREEN && c.getBlue() == GOOD_BLUE)
			return true;
		return false;
	}
	
}