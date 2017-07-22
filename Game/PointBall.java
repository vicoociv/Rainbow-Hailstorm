import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

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
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Determine whether this this point ball will intersect the same colored goal in the
     * next time step, assuming that both objects continue with their 
     * current velocity.
     * 
     * Intersection is determined by determining whether the bounding boxes of the 
     * two objects are overlapping 
     * 
     * @param obj : other object
     * @return whether an intersection will occur.
     */    
    public boolean willIntersectGoal(Goal obj){
        score++;
        boolean inBounds = (pos_x >= 5 && pos_x + width/2 <= 795);
        int next_x = pos_x + v_x;
        int next_y = pos_y + v_y;
        int next_obj_x = obj.pos_x + obj.v_x;
        int next_obj_y = obj.pos_y + obj.v_y;
        
        return (next_x + width >= next_obj_x
                && next_y + height >= next_obj_y
                && next_obj_x + obj.width >= next_x 
                && next_obj_y + obj.height >= next_y
                && inBounds);        
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
    

    
    public static PointBall createPointBall(int objectTime) {
        if (objectTime % (35 * 7 * 10) == 0) {
            Random gen = new Random();
            int x = gen.nextInt(500) + 150;

            // random speed of a factor of 5
            int speedY = gen.nextInt(6) + 10;
            while (speedY % 5 != 0) {
                speedY = gen.nextInt(6) + 10;
            }
            return new PointBall(x, speedY, randomColor(gen), 50);
        }
        return null;
    }
}
