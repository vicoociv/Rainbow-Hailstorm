import java.awt.Color;
import java.awt.Graphics;

public class Goal extends GameObj{

    private Color color;

    public Goal(int x, int y, Color color) {
        super(0, 0, x, y, 15, 200);
        this.color = color;
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(pos_x, pos_y, width, height);
    }
    
    public Color getColor(){
        return color;
    }
    
    
    //use to randomly change position of the goal after it is hit. Only change one at a time. Whichever one is hit first
    public void changeY(int y){
        pos_y = y;
    }
    

}
