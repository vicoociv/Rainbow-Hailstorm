import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class InvisibleTrap extends GameObj {
    
    public InvisibleTrap(int x, int speedY, int size) {
        super(0, speedY, x, 0, size, size);
        super.maxTime = 1750;
    }

    @Override
    public void draw(Graphics g) {
        if (drawObject) {
            g.setColor(Color.WHITE);
            g.fillOval(pos_x, pos_y, width, height);
            g.setColor(Color.LIGHT_GRAY);
            g.fillOval(pos_x + 5, pos_y + 5, width - 10, height - 10);
        }
    }

    @Override
    //disables the trap after a certain period of time
    public void setEnabled() {
        if (time >= 1750 && GameObj.paddleColor == Color.BLACK) {
            paddleColor = Color.WHITE;
            hitEffect = false;
        } 
    }
    
    @Override
    //Sees if point ball intersects paddle or goal
    public boolean willIntersectPaddle(Paddle obj){        
        if (super.willIntersectPaddle(obj)) {
            hitEffect = true;
            drawObject = false;
            GameObj.paddleColor = Color.BLACK;
            return true;
        } else {
            return false;
        }
    }
    
    /**Creates a InvisibleTrap object*/
    public static InvisibleTrap createInvisibleTrap(int objectTime) {
        if (objectTime % (35 * 53 * 10) == 0) {
            Random gen = new Random();
            int x = gen.nextInt(500) + 150;

            // random speed of a factor of 5
            int speedY = gen.nextInt(6) + 10;
            while (speedY % 5 != 0) {
                speedY = gen.nextInt(6) + 10;
            }
            return new InvisibleTrap(x, speedY, 30);
        }
        return null;
    }
}
