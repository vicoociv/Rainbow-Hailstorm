/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Random;

/** An object in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  move; their position should always be within their bounds.
 */
public class GameObj implements Comparable{

	/** Current position of the object (in terms of graphics coordinates)*/
	public int pos_x; 
	public int pos_y;

	/** Size of object, in pixels */
	public int width;
	public int height;
	
	/** Velocity: number of pixels to move every time move() is called */
	public int v_x;
	public int v_y;

	/** Upper bounds of the area in which the object can be positioned.  
	 *    Maximum permissible x, y positions for the upper-left 
	 *    hand corner of the object
	 */
	public int max_x;
	public int max_y;
	
	/** makes sure the falling objects only bounce once on the paddle. 
	 * Protects against glitching*/
	public boolean contact;
	
    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 700;

    
    // Game information to record
    protected static int score = 0;
    protected static int incorrectHit = 0;
    protected static int trapsHit = 0;
    
    //Goal state    
    protected static int paddleWidth = 120;
    protected static Color paddleColor = Color.WHITE;
    protected static boolean frozen = false;
    
    //trap fields
    protected boolean hitEffect = false;
    protected boolean drawObject = true;
    protected int time = 0;
    protected int maxTime = 0;


        
	/**
	 * Constructor
	 */
	public GameObj(int v_x, int v_y, int pos_x, int pos_y, 
		int width, int height){
		this.v_x = v_x;
		this.v_y = v_y;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.width = width;
		this.height = height;
		contact = false;
		
		// take the width and height into account when setting the 
		// bounds for the upper left corner of the object.
		this.max_x = COURT_WIDTH - width;
		this.max_y = COURT_HEIGHT - height;
	}


	/**
	 * Moves the object by its velocity.  Ensures that the object does
	 * not go outside its bounds by clipping.
	 */
	public void move(){
		pos_x += v_x;
		pos_y += v_y;
	}

    /**
     * Determine whether this game object will intersect the Paddle in the
     * next time step, assuming that both objects continue with their 
     * current velocity.
     * 
     * Intersection is determined by calculating the vertical and horizontal 
     * distances to the edge of the curved falling objects. 
     * intersection is considered to occur.
     * 
     * @param obj : other object
     * @return whether an intersection will occur.
     */
     public boolean willIntersectPaddle(Paddle obj){
        boolean result = false; 
        int angle = obj.getAngle();
        double xShift = width/2 * Math.cos(Math.toRadians(90 - angle));
        double yShift = width/2 * Math.sin(Math.toRadians(90 - angle));
        
        //angled paddle
        if (obj.getAngle() != 0){
          double trueY = pos_y + height/2 + yShift;
          double angleMultiplier = Math.tan(Math.toRadians(obj.getAngle()));
          double trueWidth = (paddleWidth) * Math.cos(Math.toRadians(angle));

          //left side paddle contact
          if (pos_x + width - xShift >= obj.pos_x - trueWidth/2 && pos_x + xShift <= obj.pos_x) {
              if (obj.getAngle() > 0) {
                  double paddleHeight = obj.pos_y - (obj.pos_x - pos_x) * angleMultiplier;
                  result = (trueY >= paddleHeight && trueY <= paddleHeight + 35);
              } else {
                  double paddleHeight = obj.pos_y - (obj.pos_x - (pos_x + width)) * angleMultiplier;
                  result = (trueY >= paddleHeight && trueY <= paddleHeight + 35);
              }
              
          //right side paddle contact
          } if (pos_x - xShift <= obj.pos_x + trueWidth/2 && pos_x + xShift > obj.pos_x) {
              if (obj.getAngle() > 0) {
                  double paddleHeight = obj.pos_y + (pos_x - obj.pos_x) * angleMultiplier;
                  result = (trueY >= paddleHeight && trueY <= paddleHeight + 35);
              } else {
                  double paddleHeight = obj.pos_y + ((pos_x + width) - obj.pos_x) * angleMultiplier;
                  result = (trueY >= paddleHeight && trueY <= paddleHeight + 35);
              }
          } 
          //flat paddle
        } else {
            if (pos_x + xShift <= obj.pos_x + paddleWidth/2 
                    && pos_x + width - xShift >= obj.pos_x - paddleWidth/2) {
                result = (pos_y + height >= obj.pos_y - obj.height/2 
                        && pos_y + height <= obj.pos_y + obj.height);
            }
          }
      return result;
    }
        

    /**General method to see if two objects intersect via bounding box*/ 
	public boolean willIntersect(GameObj obj){ 
		int next_x = pos_x + v_x;
		int next_y = pos_y + v_y;
		int next_obj_x = obj.pos_x + obj.v_x;
		int next_obj_y = obj.pos_y + obj.v_y;
		return (next_x + width >= next_obj_x
				&& next_y + height >= next_obj_y
				&& next_obj_x + paddleWidth >= next_x 
				&& next_obj_y + obj.height >= next_y);
	}
	
	/**checks if an object id out of bounds*/
	public boolean outOfBounds(){
	    return (pos_x > max_x + width * 2 || pos_x < -width 
	            || pos_y > max_y + height * 2 || pos_y < -height);
	}
	
	
    /** Update the x and y velocity of the object in response to hitting
     *  an obstacle in the given angle. If the angle is
     *  0, this method has no effect on the object. */
    public void bounceAngle(Paddle paddle) {
        if (paddle == null) return;
        if (!contact){
            int reboundAngle = 2 * paddle.getAngle() - 90;
            v_x = (int) Math.round(v_y * Math.cos(Math.toRadians(reboundAngle)));
            v_y = (int) Math.round(v_y * Math.sin(Math.toRadians(reboundAngle)));
            this.contact = true;
        }
    }	
    
	
	/** Determine whether the game object will hit another 
	 *  object in the next time step. If so, return the direction
	 *  of the other object in relation to this game object.
	 *  
	 * @return direction of impending object, null if all clear.
	 */
	public void hitObj(Paddle other) {
	    if (this.willIntersectPaddle(other)){
	        this.bounceAngle(other);
	    }
	}
	
	/**Determines how long the efects of each effect object lasts*/
	public void setEnabled() {}
	
	/**increments the time of the objects effect*/
	public void incrementTime() {
	    time += 35;
	}
	
    /**Generates a random color from four choices: RED, BLUE, GREEN, MAGENTA
     * 
     * @param gen the random generator
     * @return the random color*/
    public static Color randomColor(Random gen) {
        int colIndex = gen.nextInt(4);
        if (colIndex == 0) {
            return Color.RED;
        } else if (colIndex == 1) {
            return Color.BLUE;
        } else if (colIndex == 2) {
            return Color.GREEN;
        } else {
            return Color.MAGENTA;
        }
    }
	
	
	/**
	 * Default draw method that provides how the object should be drawn 
	 * in the GUI. This method does not draw anything. Subclass should 
	 * override this method based on how their object should appear.
	 * 
	 * @param g 
	 *	The <code>Graphics</code> context used for drawing the object.
	 * 	Remember graphics contexts that we used in OCaml, it gives the 
	 *  context in which the object should be drawn (a canvas, a frame, 
	 *  etc.)
	 */
	public void draw(Graphics g) {
	}
	
	/**Returns whether an effect object has been avtivated*/
	public boolean returnHitEffect() {
	    return hitEffect;
	}

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
