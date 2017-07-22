import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class FreezeTrap extends GameObj {
        
    public FreezeTrap(int x, int speedY, int size) {
        super(0, speedY, x, 0, size, size);
        super.maxTime = 1400;
    }

    @Override
    public void draw(Graphics g) {
        if (drawObject) {
            g.setColor(Color.WHITE);
            g.fillOval(pos_x, pos_y, width, height);
            g.setColor(Color.CYAN);
            g.fillOval(pos_x + 5, pos_y + 5, width - 10, height - 10);
        }
    }
    
    @Override
    public void setEnabled() {
        if (time % 1400 == 0 && hitEffect) {
            frozen = false;
            hitEffect = false;
        } 
    }

    @Override
    public boolean willIntersectPaddle(Paddle obj){
        if (super.willIntersectPaddle(obj)) {
            hitEffect = true;
            drawObject = false;
            frozen = true;
            return true;
        } else {
            return false;
        }
    }
    
    public static FreezeTrap createFreezeTrap(int objectTime) {
        if (objectTime % (35 * 42 * 10) == 0) {
            Random gen = new Random();
            int x = gen.nextInt(500) + 150;

            // random speed of a factor of 5
            int speedY = gen.nextInt(6) + 10;
            while (speedY % 5 != 0) {
                speedY = gen.nextInt(6) + 10;
            }
            return new FreezeTrap(x, speedY, 30);
        }
        return null;
    }
}
