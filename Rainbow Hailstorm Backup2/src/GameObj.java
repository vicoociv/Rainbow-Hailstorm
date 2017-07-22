/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Graphics;
import java.awt.event.MouseEvent;

/** An object in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  move; their position should always be within their bounds.
 */
public class GameObj implements Comparable{

	/** Current position of the object (in terms of graphics coordinates)
	 *  
	 * Coordinates are given by the upper-left hand corner of the object.
	 * This position should always be within bounds.
	 *  0 <= pos_x <= max_x 
	 *  0 <= pos_y <= max_y 
	 */
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
	
	/**Declared here insdie the GameObj class so that you don't have to put as parameter each time
	 * when creating a new GameObj object*/
    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 700;

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
		//clip();
	}

	/**
	 * Prevents the object from going outside of the bounds of the area 
	 * designated for the object. (i.e. Object cannot go outside of the 
	 * active area the user defines for it).
	 */ 
	public void clip(){
		if (pos_x < 0) pos_x = 0;
		else if (pos_x > max_x) pos_x = max_x;

		if (pos_y < 0) pos_y = 0;
		else if (pos_y > max_y) pos_y = max_y;
	}
	
	 /**
     * Determine whether this game object is currently intersecting
     * another object.
     * 
     * @param obj : other object
     * @return whether this object intersects the other object.
     */
    public boolean intersects(GameObj obj){
        return (pos_x + width >= obj.pos_x
                && pos_y + height >= obj.pos_y
                && obj.pos_x + obj.width >= pos_x 
                && obj.pos_y + obj.height >= pos_y);
    }
    
    
    //use this for a general purpose will intersect method. Use helpers corresponding to different objects here
    //change this to intersect goal- maybe override in goal class
    //eep this the generic one for GameObj as a whole
	public boolean willIntersect(GameObj obj){ 
		int next_x = pos_x + v_x;
		int next_y = pos_y + v_y;
		int next_obj_x = obj.pos_x + obj.v_x;
		int next_obj_y = obj.pos_y + obj.v_y;
		return (next_x + width >= next_obj_x
				&& next_y + height >= next_obj_y
				&& next_obj_x + obj.width >= next_x 
				&& next_obj_y + obj.height >= next_y);
	}
	
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
	
	/** Update the velocity of the object in response to hitting
	 *  an obstacle in the given direction. If the direction is
	 *  null, this method has no effect on the object. */
	public void bounce(Direction d) {
		if (d == null) return;
		switch (d) {
		case UP:    v_y = Math.abs(v_y); break;  
		case DOWN:  v_y = -Math.abs(v_y); break;
		case LEFT:  v_x = Math.abs(v_x); break;
		case RIGHT: v_x = -Math.abs(v_x); break;
		}
	}
	
	/** Determine whether the game object will hit a 
	 *  wall in the next time step. If so, return the direction
	 *  of the wall in relation to this game object.
	 *  
	 * @return direction of impending wall, null if all clear.
	 */
	public Direction hitWall() {
		if (pos_x + v_x < 0)
			return Direction.LEFT;
		else if (pos_x + v_x > max_x)
			return Direction.RIGHT;
		if (pos_y + v_y < 0)
			return Direction.UP;
		else if (pos_y + v_y > max_y)
			return Direction.DOWN;
		else return null;
	}

	/** Determine whether the game object will hit another 
	 *  object in the next time step. If so, return the direction
	 *  of the other object in relation to this game object.
	 *  
	 * @return direction of impending object, null if all clear.
	 */
	
	public void hitObj(Paddle other) {
	    if (this.willIntersect(other)){
	        this.bounceAngle(other);
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


    @Override
    public int compareTo(Object o) {
        return 0;
    }
	
}
