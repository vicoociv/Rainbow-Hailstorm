import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

public class Goal extends GameObj{

    private Color color;

    public Goal(int x, int y, Color color) {
        super(0, 0, x, y, 20, 150);
        this.color = color;
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(pos_x, pos_y, width, height);
    }
    
    public Color getColor(){
        return color;
    }
    
    
    public void changeY(int y){
        pos_y = y;
    }
    
    // generates random goal colors
    public static LinkedList<Goal> getNewGoals() {
        Random gen = new Random();
        LinkedList<Goal> goalListTemp = new LinkedList<Goal>();
        LinkedList<Color> tempList = new LinkedList<Color>();
        for (int i = 0; i < 4; i++) {
            Color tempCol = randomColor(gen);
            if (!tempList.contains(tempCol)) {
                tempList.add(tempCol);
            } else {
                i--;
            }
        }
        goalListTemp.add(new Goal(0, 100, tempList.get(0)));
        goalListTemp.add(new Goal(0, 400, tempList.get(1)));
        goalListTemp.add(new Goal(800 - 15, 100, tempList.get(2)));
        goalListTemp.add(new Goal(800 - 15, 400, tempList.get(3)));
        
        return goalListTemp;
    }
    

}
