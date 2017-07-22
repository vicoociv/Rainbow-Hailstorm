import java.awt.Color;
import java.awt.Graphics;

public class PointBall extends GameObj {

    private Color color;

    public PointBall(int x, int speedY, Color color, int size) {
        super(0, speedY, x, 0, size, size);
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(pos_x, pos_y, width, height);
    }
    
    public Color getColor(){
        return color;
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
        
          //angled paddle
          if (obj.getAngle() != 0){
            double xShift = width/2 * Math.cos(Math.toRadians(90 - obj.getAngle()));
            double yShift = width/2 * Math.sin(Math.toRadians(90 - obj.getAngle()));
            double trueY = pos_y + height/2 + yShift;
            double angleMultiplier = Math.tan(Math.toRadians(obj.getAngle()));

            //left side paddle contact
            if (pos_x + width - xShift >= obj.pos_x - obj.width/2 && pos_x + xShift <= obj.pos_x) {
                if (obj.getAngle() > 0) {
                    double paddleHeight = obj.pos_y - (obj.pos_x - pos_x) * angleMultiplier;
                    result = (trueY >= paddleHeight && trueY <= paddleHeight + 20);
                } else {
                    double paddleHeight = obj.pos_y - (obj.pos_x - (pos_x + width)) * angleMultiplier;
                    result = (trueY >= paddleHeight && trueY <= paddleHeight + 20);
                }
                
            //right side paddle contact
            } else if (pos_x <= obj.pos_x + obj.width/2 && pos_x + width - xShift > obj.pos_x) {
                if (obj.getAngle() > 0) {
                    double paddleHeight = obj.pos_y + (pos_x - obj.pos_x) * angleMultiplier;
                    result = (trueY >= paddleHeight && trueY <= paddleHeight + 20);
                } else {
                    double paddleHeight = obj.pos_y + ((pos_x + width) - obj.pos_x) * angleMultiplier;
                    result = (trueY >= paddleHeight && trueY <= paddleHeight + 20);
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
    
    /**
     * Determine whether this game object will intersect its corresspondingly colored goal in the
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
    public boolean willIntersectGoal(Goal obj){
        int next_x = pos_x + v_x;
        int next_y = pos_y + v_y;
        int next_obj_x = obj.pos_x + obj.v_x;
        int next_obj_y = obj.pos_y + obj.v_y;
        return (next_x + width >= next_obj_x
                && next_y + height >= next_obj_y
                && next_obj_x + obj.width >= next_x 
                && next_obj_y + obj.height >= next_y);        
    }
    
    public boolean checkPointValid(Goal obj){
        return (color == obj.getColor());
    }

    @Override
    //Sees if point ball intersects paddle or goal
    public boolean willIntersect(GameObj obj){
        boolean result = false; 
        if (obj instanceof Paddle){
            Paddle tempPaddle = (Paddle) obj;
            result = willIntersectPaddle(tempPaddle);
        }
        return result;
    }
    

}
