/* Milena Mijuskovic and Selena Zhang
 * ICS4UE-01/51
 * Mr. Benum
 * Jan 20th 2021
 * Program Summary: Class for handling how ocean, player, and platform interact with each other
 * Links and Resources Used:
 * https://docs.oracle.com/javase/7/docs/api/java/awt/Color.html
 * https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
 * Java Method A and B textbook
 * "Creating a Jar File that includes Resources" on Classroom - to embed images in jar file (use of Buffered Images)
 * "Pong Assignment Tips" on Classroom - to embed audio in jar file
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class DoodleJump extends Game
{
	//Fields 
	
	//general
	private int bottom;
	private int xMid;
	private Color background;
	private boolean gameOver;
	private boolean ending;
	
	//doodle related
	private final int DOODLE_SIZE = 30;
	private Player doodle;
	private int playerObjectSize;
	private String chosenDoodleLeft, chosenDoodleRight;
	private String allDoodles[] = { "DoodleOriginal_LEFT.png", "DoodleOriginal_RIGHT.png",
									"DoodleCarrot_LEFT.png", "DoodleCarrot_RIGHT.png",
									"DoodleHawaiian_LEFT.png", "DoodleHawaiian_RIGHT.png",
									"DoodleSport_LEFT.png", "DoodleSport_RIGHT.png",
									"DoodleSleepy_LEFT.png", "DoodleSleepy_RIGHT.png",
									"DoodleAngel_LEFT.png", "DoodleAngel_RIGHT.png"};
	
	//movement related
	private int speed;
	private int totDis; //total distance
	private int remDis; //remaining distance
	private boolean goUp = true, goDown = false;
	private int speeds[];
	private int count = 0;
	private boolean firstJump = true;
	private int total;
	
	//score related
	private int score; //is the total distance the platforms move
	private JLabel scoreLabel, startScreenLabel, choosePlayerScreenLabel, endScreenLabel, doodleLabel;
	
	//sound related
	private boolean playing = false; 
	private final String JUMP = "jump.wav";
	private final String BREAK_PLATFORM = "breakPlatform.wav";
	private final String LOSE = "lose.wav";
	
	//platform related
	private ArrayList<Platform> platforms;
	private int badPlatformChance;
	private Color goodC; 
	private Color badC; 
	private final int PLATFORM_HEIGHT = 5, PLATFORM_WIDTH = 30;
	
	//ocean related
	private int height, width;
	private Ocean ocean;
	
	//Methods
	
	/**
	 * This method runs once to set up the game
	 */
	@Override
	public void setup() 
	{	
		final int SCREEN_X = 384, SCREEN_Y = 400;
		
		//sets up the field and window
		super.setTitle("Doodle Jump");
		background = new Color(246, 241, 236);
		this.setBackground(background);
		this.setSize(SCREEN_X, SCREEN_Y);
		
		showStartScreen();
		choosePlayer();
		setDelay(70);
		initFields();
	}
	/**
	 * This method is called every tick of time
	 */
	@Override
	public void act() 
	{
		if(!ending) //when the main part of the game is happening
		{
			runGame();
		}
		if(ending) //is necessary so doodle falls into ocean
		{
			endingGame();
		}
		if(gameOver)
		{
			//display ending screen
			playSound(LOSE);
			removeEverything();
			showEndScreen();
		}
	}
	/**
	 * This method shows the start screen
	 */
	public void showStartScreen() 
	{
		final int IMAGE_X = 384, IMAGE_Y = 338;
		startScreenLabel = new JLabel();
		try 
		{
			BufferedImage image = ImageIO.read(getClass().getResourceAsStream("DoodleJumpHome.png"));
			startScreenLabel.setIcon(new ImageIcon(image));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		startScreenLabel.setBounds(0, 0, IMAGE_X, IMAGE_Y);
		add(startScreenLabel);
		repaint();
		while(!SKeyPressed()) 
		{
			setDelay(1); //delays 1 millisecond while while key is not pressed
		}
		remove(startScreenLabel);
	}
	
	/**
	 * This method shows the choose player screen
	 */
	public void showChoosePlayerScreen() 
	{
		final int IMAGE_X = 384, IMAGE_Y = 338;
		choosePlayerScreenLabel = new JLabel();
		try 
		{
			BufferedImage image = ImageIO.read(getClass().getResourceAsStream("ChoosePlayer.png"));
			choosePlayerScreenLabel.setIcon(new ImageIcon(image));		
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		choosePlayerScreenLabel.setBounds(0, 0, IMAGE_X, IMAGE_Y);
		add(choosePlayerScreenLabel);
		repaint();
	}
	/**
	 * This method chooses the player
	 */
	public void choosePlayer()
	{
		showChoosePlayerScreen();
		while(!key1Pressed() && !key2Pressed() && !key3Pressed() && !key4Pressed() && !key5Pressed() && !key6Pressed()) //while player is not chosen
		{
			setDelay(1);
			//assigns the player chosen
			if(key1Pressed())
			{
				chosenDoodleLeft = allDoodles[0];
				chosenDoodleRight = allDoodles[1];
			}
			else if(key2Pressed())
			{
				chosenDoodleLeft = allDoodles[2];
				chosenDoodleRight = allDoodles[3];
			}
			else if(key3Pressed())
			{
				chosenDoodleLeft = allDoodles[4];
				chosenDoodleRight = allDoodles[5];
			}
			else if(key4Pressed())
			{
				chosenDoodleLeft = allDoodles[6];
				chosenDoodleRight = allDoodles[7];
			}
			else if(key5Pressed())
			{
				chosenDoodleLeft = allDoodles[8];
				chosenDoodleRight = allDoodles[9];
			}
			else if(key6Pressed())
			{
				chosenDoodleLeft = allDoodles[10];
				chosenDoodleRight = allDoodles[11];
			}
		}
		remove(choosePlayerScreenLabel);
		repaint();
	}
	/**
	 * This method runs the actual game
	 */
	public void runGame()
	{
		scoreLabel.setText("" + score); //updates score
		movePlayer(); 
		if(goUp)
		{
			jumpUp();
			//movePlatformsDown(speed);
		}	
		if(goDown)
		{
			fall();
			if(checkCollision()) //if the doodle lands on a platform, move platforms down
				movePlatformsDown(speed);
		}
		if(count >= total) //makes sure that the index of speed stays within range
			count = 0;
	}
	/**
	 * This method displays the end screen
	 */
	public void showEndScreen()
	{
		final int IMAGE_X = 384, IMAGE_Y = 250;
		final int SCORE_X = xMid-10, SCORE_Y = 280, SCORE_WIDTH = 50, SCORE_HEIGHT = 20;
		endScreenLabel = new JLabel();
		try 
		{
			BufferedImage image = ImageIO.read(getClass().getResourceAsStream("Game Over.png"));
			endScreenLabel.setIcon(new ImageIcon(image));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		endScreenLabel.setBounds(0, 0, IMAGE_X, IMAGE_Y); 
		scoreLabel.setBounds(SCORE_X, SCORE_Y, SCORE_WIDTH, SCORE_HEIGHT); //relocates the score label to below the end screen label
		scoreLabel.setFont(new Font("Kristen ITC", Font.PLAIN, 22));
		add(endScreenLabel);
		gameOver = true;
		repaint();	
	}
	/**
	 * This method moves the player left and right
	 */
	public void movePlayer()
	{
		int rightBounds = getFieldWidth();
				
		//n key is left
		//m key is right
		if(NKeyPressed())
		{
			doodle.moveLeft();
			try //refer to comment at the top
			{
				BufferedImage image = ImageIO.read(getClass().getResourceAsStream(chosenDoodleLeft));
				doodleLabel.setIcon(new ImageIcon(image)); //set the right image based on direction
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			doodleLabel.setBounds(doodle.getX(), doodle.getY(), DOODLE_SIZE, DOODLE_SIZE);
		}
		if(MKeyPressed())
		{
			doodle.moveRight();
			try 
			{
				BufferedImage image = ImageIO.read(getClass().getResourceAsStream(chosenDoodleRight));
				doodleLabel.setIcon(new ImageIcon(image));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			doodleLabel.setBounds(doodle.getX(), doodle.getY(), DOODLE_SIZE, DOODLE_SIZE);
		}
		if(doodle.getX() > rightBounds)
		{
			doodle.setX(0);
			doodleLabel.setBounds(doodle.getX(), doodle.getY(), DOODLE_SIZE, DOODLE_SIZE);
		}
		if(doodle.getX() < 0)
		{
			doodle.setX(rightBounds);
			doodleLabel.setBounds(doodle.getX(), doodle.getY(), DOODLE_SIZE, DOODLE_SIZE);
		}
	}
	/**
	 * This method makes the player jump up
	 */
	public void jumpUp()
	{
		final double GRAVITY = 9.8;
		totDis = getFieldHeight()/2 + playerObjectSize; //total distance
		remDis = totDis - (getFieldHeight() - doodle.getY()); //remaining distance
		
		if(count == total) //make sure the index stays between 0 and 14
			count = 0;
		if(count == -1)
			count = 0;
		count++; 
		
		/*Since Selena's physics skills isn't good enough and can't get the same
		  values going up and down, she stored the first going up values to use for going down
		  so everything is uniform*/
		if(firstJump)
		{
			speeds[count] = (int)Math.sqrt(GRAVITY*remDis) - 10;
			if(speeds[total-1] != -1) //if the whole array has a value
				firstJump = false;
		}
		speed = speeds[count]; //gets current speed from array
		
		if(remDis > 0) //if there's distance left
		{
			doodle.setYSpeed(speed);
			doodleLabel.setBounds(doodle.getX(), doodle.getY(), DOODLE_SIZE, DOODLE_SIZE);
		}
		if(speed == 0) //reached top, time to go down
		{
			goUp = false;
			goDown = true;
		}
	}
	/**
	 * This method makes the player fall down
	 */
	public void fall()
	{
		int bottom = getFieldHeight() - playerObjectSize*3; 
		totDis = getFieldHeight()/2 + playerObjectSize;
		remDis = doodle.getY() + playerObjectSize;
		
		if(count == total) //make sure count stays in range
			count = 0;
		if(count == -1)
			count = 0;
		speed = speeds[count]; //get current speed
		count--;
		
		if(remDis >= 0)
		{
			doodle.setYSpeed(-speed);
			doodleLabel.setBounds(doodle.getX(), doodle.getY(), DOODLE_SIZE, DOODLE_SIZE);
		}
		if(remDis > bottom) //if the doodle reached the bottom
		{
			//if doodle is not colliding with a paddle then dead
			if(!checkCollision())
			{
				ending = true;
			}
			else //if it is colliding with a platform
			{
				goUp = true;
				goDown = false;
				playSound(JUMP);
			}
		}
	}
	/**
	 * This method ends the game
	 */
	public void endingGame()
	{
		final int DEAD_SPEED = -20;
		doodle.setYSpeed(DEAD_SPEED);
		doodleLabel.setBounds(doodle.getX(), doodle.getY(), DOODLE_SIZE, DOODLE_SIZE); 
		if(doodle.getY() > getFieldHeight()+50) //add 50 for good measure
		{
			gameOver = true;
			ending = false;
		}
	}
	/**
	 * This method removes everything off the field
	 */
	public void removeEverything()
	{
		remove(doodle);
		remove(doodleLabel);
		remove(ocean);
		for(int i = 0; i<platforms.size(); i++)
		{
			remove(platforms.get(i));
		}
		speed = 0;
		repaint();
	}
	/**
	 * This method checks for when the player lands on top of a platform
	 */
	public boolean checkCollision()
	{
		for(int i = 0; i<platforms.size(); i++)
		{
			//good platform
			if(doodle.collides(platforms.get(i)) && (doodle.getY()+playerObjectSize) <= (platforms.get(i).getY()+speed) && platforms.get(i).goodPlatform())
				return true;
			//bad platform
			else if(doodle.collides(platforms.get(i)) && (doodle.getY()+playerObjectSize) <= (platforms.get(i).getY()+speed) && !platforms.get(i).goodPlatform()) 
			{
				playSound(BREAK_PLATFORM);
				remove(platforms.get(i));
			}
		}
		return false;
	}
	/**
	 * This method initializes fields in setup
	 * @throws IOException 
	 */
	public void initFields() 
	{
		gameOver = false;
		ending = false;
		xMid = getFieldWidth()/2;
		playerObjectSize = 25;
		bottom = getFieldHeight();
		
		//creates doodle and image icon
		doodle = new Player(playerObjectSize, xMid, bottom-playerObjectSize);
		doodle.setColor(Color.BLACK);
		doodleLabel = new JLabel();
		try 
		{
			BufferedImage image = ImageIO.read(getClass().getResourceAsStream(chosenDoodleLeft));
			doodleLabel.setIcon(new ImageIcon(image));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		doodleLabel.setBounds(xMid, bottom-playerObjectSize, playerObjectSize, playerObjectSize);
		doodle.setVisible(false); //can't actually see the game object, the doodleLabel follows it
		add(doodle);
		add(doodleLabel);
		
		//speed
		total = 15;
		speeds = new int[total];
		Arrays.fill(speeds, -1); //-1 is essentially null
		
		//score label
		final int X = xMid-5, Y = 5, LENGTH = 50, WIDTH = 20;
		scoreLabel = new JLabel();
		scoreLabel.setBounds(X, Y, LENGTH, WIDTH); 
		scoreLabel.setText("" + score);
		scoreLabel.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
		add(scoreLabel);
		scoreLabel.repaint();
		
		//platforms
		platforms = new ArrayList<Platform>(18);
		badPlatformChance = 10;
		
		//colours
		goodC = new Color(113, 185, 69);
		badC = new Color(123, 90, 47);
		
		//ocean
		height = getFieldHeight()+20;
		width = getFieldWidth()+20;
		ocean = new Ocean(width, height-playerObjectSize*2+5);
		add(ocean);
		
		//platforms
		for(int i = height/6; i<height; i+= height/6) //fills up window with platforms
			newPlatforms(i);
	}
	/**
	 * This method tries to play sounds
	 * Referred to code in "Pong Assignment Tips"
	 * @param sound - sound file
	 */
	public void playSound(String sound) 
	{
		Clip clip = null;
		try 
		{
			InputStream is = getClass().getResourceAsStream(sound);
			BufferedInputStream bis = new BufferedInputStream(is); 
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(bis);
			clip = AudioSystem.getClip();
			clip.open(audioInput);
		} 
		catch (LineUnavailableException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (UnsupportedAudioFileException e) 
		{
			e.printStackTrace();
		}
		if(!playing && gameOver) //plays ending game sound
		{
			clip.start();
			playing = true;
		}
		else if(!gameOver)
			clip.start();
	}
	/**
	 * This method moves the platforms down
	 * @param y - speed
	 */
	public void movePlatformsDown(int y) 
	{
		int yChange; //how much a platform needs to move down by
		for(int i = 0; i<=platforms.size()-3; i += 3)
		{
			yChange = platforms.get(i).getY()+y;
			platforms.get(i).setY(yChange);
			platforms.get(i+1).setY(yChange);
			platforms.get(i+2).setY(yChange);
			score++; 
			if(score%300 == 0 && badPlatformChance != 3)//increases the chances of a bad platform appearing as score increases
				badPlatformChance--;
			if(yChange > height-playerObjectSize*2-5) //makes platforms disappear at ocean
			{
				remove(platforms.get(i));
				remove(platforms.get(i+1));
				remove(platforms.get(i+2));
			}
			if(yChange > height) //removes platforms from arraylist in descending order to not mess up index of these 3 platforms as they are being removed
			{
				platforms.remove(i+2);
				platforms.remove(i+1);
				platforms.remove(i);
				
				newPlatforms(yChange-height-y);
				i-=3; //goes back 3 in the index since 3 platforms were removed from the arraylist
			}
		}
	}
	/**
	 * This method makes new platforms
	 * @param y - starting y location of platforms
	 */
	public void newPlatforms(int y) 
	{
		Random random = new Random();
		Platform platform1, platform2, platform3;
		int randomX = random.nextInt(width-PLATFORM_WIDTH-10); //creates random x location for 1st platform
		int prevX1 = randomX;
		int randomBad = random.nextInt(badPlatformChance); //chooses a random bad platform
		if(randomBad == 0)
			platform1 = new Platform(randomX, y, PLATFORM_HEIGHT, PLATFORM_WIDTH, badC);
		else
			platform1 =  new Platform(randomX, y, PLATFORM_HEIGHT, PLATFORM_WIDTH, goodC);
		platforms.add(platform1);
		add(platform1);
		do
		{
			randomX = random.nextInt(width-PLATFORM_WIDTH-10);
		}while(Math.abs(randomX-prevX1) < PLATFORM_WIDTH*2); //creates random x location for the 2nd platform and makes sure the platforms are spaced out/do not overlap
		int prevX2 = randomX;
		if(randomBad == 1)
			platform2 = new Platform(randomX, y, PLATFORM_HEIGHT, PLATFORM_WIDTH, badC);
		else
			platform2 =  new Platform(randomX, y, PLATFORM_HEIGHT, PLATFORM_WIDTH, goodC);
		platforms.add(platform2);
		add(platform2);
		do
		{
			randomX = random.nextInt(width-PLATFORM_WIDTH-10); //creates random x location for the 3rd platform
		}while(Math.abs(randomX-prevX1) < PLATFORM_WIDTH*2 || Math.abs(randomX-prevX2) < PLATFORM_WIDTH*2);
		if(randomBad == 2)
			platform3 = new Platform(randomX, y, PLATFORM_HEIGHT, PLATFORM_WIDTH, badC);
		else
			platform3 =  new Platform(randomX, y, PLATFORM_HEIGHT, PLATFORM_WIDTH, goodC);
		platforms.add(platform3);
		add(platform3);
	}
	/**
	 * This is the main method
	 * @param args
	 */
	public static void main(String[] args) 
	{
		DoodleJump t = new DoodleJump();
		t.setResizable(false);
		t.setVisible(true);
		t.initComponents();
	}
}
