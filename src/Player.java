/* Selena Zhang and Milena Mijuskovic
 * ICS4UE-01/51
 * Mr. Benum
 * Jan 20th 2021
 * Program Summary: This class defines the Player object for Doodle Jump
 */
public class Player extends GameObject
{
	//Fields
	int dx = 6;
	
	//Constructor
	/**
	 * This constructor makes a Player object
	 * @param s - size
	 * @param x - x location
	 * @param y - y location
	 */
	public Player(int s, int x, int y)
	{
		setSize(s, s);
		setX(x);
		setY(y);
	}
	
	//Methods
	/**
	 * This method is called every tick of time
	 */
	@Override
	public void act() 
	{
		
	}
	/**
	 * This method moves the player left
	 */
	public void moveLeft()
	{
		setX(getX()-dx);
	}
	/**
	 * This method moves the player right
	 */
	public void moveRight()
	{
		setX(getX()+dx);
	}
	/**
	 * This method sets the y speed of the player
	 */
	public void setYSpeed(int speed)
	{
		setY(getY()-speed);
	}
}