/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

/**
 * A basic game object displayed as a yellow circle, starting in the upper left
 * corner of the game court.
 * 
 */
public class Circle extends GameObj {

	public static final int SIZE = 100;
	public static final int INIT_POS_X = 400;
	public static final int INIT_POS_Y = 0;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 5;

	public Circle(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(pos_x, pos_y, width, height);
	}
	
    public boolean willIntersectPaddle(Paddle obj){
        boolean result = false; 
        
          //angled paddle
          if (obj.getAngle() != 0){
            double xShift = width/2 * Math.cos(Math.toRadians(90 - obj.getAngle()));
            double yShift = width/2 * Math.sin(Math.toRadians(90 - obj.getAngle()));
            double trueY = pos_y + height/2 + yShift;
            double angleMultiplier = Math.tan(Math.toRadians(obj.getAngle()));

            //left side paddle contact
            if (pos_x + width - xShift >= obj.pos_x - obj.width/2 && pos_x + xShift <= obj.pos_x) {
                if (obj.getAngle() > 0) {
                    result = (trueY >= obj.pos_y - (obj.pos_x - pos_x) * angleMultiplier);
                    if (result)
                        System.out.println("left up");
                } else {
                    result = (trueY >= obj.pos_y - (obj.pos_x - (pos_x + width)) * angleMultiplier);
                    if (result)
                        System.out.println("left down");
                }
                
            //right side paddle contact
            } else if (pos_x <= obj.pos_x + obj.width/2 && pos_x + width - xShift > obj.pos_x) {
                if (obj.getAngle() > 0) {
                    result = (trueY >= obj.pos_y + (pos_x - obj.pos_x) * angleMultiplier);
                    if (result)
                        System.out.println("right down");
                } else {
                    result = (trueY >= obj.pos_y + ((pos_x + width) - obj.pos_x) * angleMultiplier);
                    if (result)
                        System.out.println("right up");
                }
            } 
            //flat paddle
          } else {
              if (pos_x + width/2 <= obj.pos_x + obj.width/2 
                      && pos_x + width/2 >= obj.pos_x - obj.width/2) {
                  result = (pos_y + height >= obj.pos_y - obj.height/2);
              }
            }
        return result;
    }
	
	@Override
    //Sees if point ball intersects paddle or goal
    public boolean willIntersect(GameObj obj){
        boolean result = false; 
        if (obj instanceof Paddle){
            System.out.println("Hello");
            Paddle tempPaddle = (Paddle) obj;
            result = willIntersectPaddle(tempPaddle);
        } else if (obj.getClass().equals("Goal")){
            Goal tempGoal = (Goal) obj;
            //result = willIntersectGoal(tempGoal);

        }
        
        return result;
    }

}
