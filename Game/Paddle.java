import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Paddle extends GameObj{

    private int angle;
    private int angleVel;

    public Paddle() { //paddleWidth is only updated once here
        super(0,0, 350, 650, 120, 15);
        angle = 0;
        angleVel = 0;
    }
    
    public void draw(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setColor(paddleColor);

        Rectangle2D rect = new Rectangle2D.Double(-paddleWidth/2., -height/2., paddleWidth, height);

        //rotating the rectangle
        AffineTransform transform = new AffineTransform();
        transform.translate(pos_x, pos_y); 
        trim();
        transform.rotate(Math.toRadians(angle));
        Shape rotatedRect = transform.createTransformedShape(rect);

        graphics.fill(rotatedRect);
    }
    
    private void trim(){
        if (angle > 45){
            angle = 45;
        } else if (angle < -45){
            angle = -45;
        }
    }

    public int getAngle(){
        return angle;
    }
    
    public void setAngle(int angle){
        this.angle = angle;
    }
    
    public void setAngleVel(int angleVel){
        this.angleVel = angleVel;
    }
    
    @Override
    public void move(){
        angle += angleVel;
    }
}
