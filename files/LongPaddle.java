import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class LongPaddle extends GameObj {
    
    public LongPaddle(int x, int speedY, int size) {
        super(0, speedY, x, 0, size, size);
        super.maxTime = 5250;
    }

    @Override
    public void draw(Graphics g) {
        if (drawObject) {
            g.setColor(Color.GREEN);
            g.fillOval(pos_x, pos_y, width, height);
            g.setColor(Color.BLACK);
            g.fillOval(pos_x + 2, pos_y + 2, width - 4, height - 4);
            g.setColor(Color.GREEN);
            g.fillRect(pos_x + width/2 - 2, pos_y, 4, height);
            g.fillRect(pos_x, pos_y + height/2 - 2, width, 4);
        }
    }

    @Override
    //disables the trap after a certain period of time
    public void setEnabled() {
        if (time >= maxTime && paddleWidth == 160) {
            paddleWidth = 120;
            hitEffect = false;
        } 
    }
    
    @Override
    //Sees if point ball intersects paddle or goal
    public boolean willIntersectPaddle(Paddle obj){        
        if (super.willIntersectPaddle(obj)) {
            hitEffect = true;
            drawObject = false;
            paddleWidth = 160;
            return true;
        } else {
            return false;
        }
    }
    
    /**Creates a LongPaddle object*/
    public static LongPaddle createLongPaddle(int objectTime) {
        if (objectTime % (35 * 32 * 10) == 0) {
            Random gen = new Random();
            int x = gen.nextInt(500) + 150;

            // random speed of a factor of 5
            int speedY = gen.nextInt(6) + 10;
            while (speedY % 5 != 0) {
                speedY = gen.nextInt(6) + 10;
            }
            return new LongPaddle(x, speedY, 50);
        }
        return null;
    }
}
