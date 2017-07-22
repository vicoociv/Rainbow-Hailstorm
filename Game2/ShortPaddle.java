import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class ShortPaddle extends GameObj {
    
    public ShortPaddle(int x, int speedY, int size) {
        super(0, speedY, x, 0, size, size);
        super.maxTime = 2450;
    }

    @Override
    public void draw(Graphics g) {
        if (drawObject) {
            g.setColor(Color.YELLOW);
            g.fillOval(pos_x, pos_y, width, height);
            g.setColor(Color.BLACK);
            g.fillOval(pos_x + 2, pos_y + 2, width - 4, height - 4);
            g.setColor(Color.YELLOW);
            g.fillRect(pos_x, pos_y + height/2 - 2, width, 4);
        }
    }

    @Override
    
    //ERATIC LENGTHS OF EFFECT!!!!!!!!
    //disables the trap after a certain period of time
    public void setEnabled() {
        if (time % 2450 == 0 && (paddleWidth == 50)) {
            GameObj.paddleWidth = 120;
            hitEffect = false;
        } 
    }
    
    @Override
    //Sees if point ball intersects paddle or goal
    public boolean willIntersectPaddle(Paddle obj){        
        if (super.willIntersectPaddle(obj)) {
            hitEffect = true;
            drawObject = false;
            GameObj.paddleWidth = 50;
            return true;
        } else {
            return false;
        }
    }
    
    public static ShortPaddle createShortPaddle(int objectTime) {
        if (objectTime % (35 * 63 * 10) == 0) {
            Random gen = new Random();
            int x = gen.nextInt(500) + 150;

            // random speed of a factor of 5
            int speedY = gen.nextInt(6) + 10;
            while (speedY % 5 != 0) {
                speedY = gen.nextInt(6) + 10;
            }
            return new ShortPaddle(x, speedY, 50);
        }
        return null;
    }
    
    
}
