/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument.Iterator;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	private Paddle paddle; // the paddle used to hit the balls

	public boolean playing = true; // whether the game is running
	private JLabel status; // Current status text (i.e. Running...)

	// Game constants
	public static final int PADDLE_ANGLE_VELOCITY = 5;

	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 35;
	private int time;
	
	//Game data structures
	TreeSet<GameObj> gameObjectList = new TreeSet<GameObj>();
	LinkedList<Goal> goalList = new LinkedList<Goal>();
	
	//Game information
	private int score = 0;
	private int strikes = 0;
	


	public GameCourt(JLabel status) {
	    time = 0;
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//adds the goals
		refreshGoal();
		
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });

		
		//DO START/Instrucitons SCREEN HERE!!!
		if (!playing) {
		    
		    
		    
		    
		} else {
		    timer.start(); // MAKE SURE TO START THE TIMER!
		}
		
		

		addKeyListener(new KeyAdapter() {
		    //changes the angle of the paddle
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
					paddle.setAngleVel(-PADDLE_ANGLE_VELOCITY);
				else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
                    paddle.setAngleVel(PADDLE_ANGLE_VELOCITY);
			}
			//stops the paddle from turning
			public void keyReleased(KeyEvent e) {
                paddle.setAngleVel(0);
			}
		});
		
	    // This mouse motion listener allows obstacles to follow the mouse
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                paddle.pos_x = e.getX();
            }
        });

        setFocusable(true);
		this.status = status;
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {
		paddle = new Paddle();

		playing = true;
		strikes = 0;
		score = 0;
		status.setText("Running...");

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
		    addPointBall(10);
		    updateScores();
	        animateGameObjects();		    
		    
		    //moves paddle radially 
			paddle.move();

			// check for the game end conditions
			//do I/O here
			if (strikes > 2) {
			    playing = false; 
			}
			
			// update the display
			repaint();
	        time += 35;
		}
	}
	
	
	//HANDLING OF GOALS:
	//can randomize locations of goals later
	private void refreshGoal(){
	    goalList.clear();
	    Random gen = new Random();

	    //generates random goal colors
	    LinkedList<Color> tempList = new LinkedList<Color>();
	    for (int i = 0; i < 4; i++) {
	        Color tempCol = randomColor(gen);
	        if (!tempList.contains(tempCol)) {
	            tempList.add(tempCol);
	        } else {
	            i--;
	        }
	    }
	    goalList.add(new Goal(0, 50, tempList.get(0)));
	    goalList.add(new Goal(0, 450, tempList.get(1)));
	    goalList.add(new Goal(800 - 15, 50, tempList.get(2)));
	    goalList.add(new Goal(800 - 15, 450, tempList.get(3)));	
	}
	
	private void drawGoals(Graphics g){
	    //draws all the goals
        for (int i = 0; i < goalList.size(); i++){
            goalList.get(i).draw(g);
        }
	}
	
	
	//HANDLING OF GAME ELEMENTS
	private void addGameElement(GameObj obj){
	    gameObjectList.add(obj);
	}
	
    private void removeGameElement(GameObj obj){
        gameObjectList.remove(obj);
    }
    
    //can add time keeping parameter to determine when to add the ball. This method below will just be called repeatedly under tick()
    //Adds random objects with random properties to the list at a certain time. 
    private void addPointBall(int interval){
        if (time % (35 * interval * 10) == 0){
            Random gen = new Random();
            int x = gen.nextInt(500) + 150;
        
            //random speed of a factor of 5
            int speedY = gen.nextInt(15) + 5;
            while (speedY % 5 != 0){
                speedY = gen.nextInt(15) + 10;
            }
        
            Color randCol = randomColor(gen);
            gameObjectList.add(new PointBall(x, speedY, randCol, 50));
        }
    }
    
    //generates random color from four options
    private Color randomColor(Random gen){
        int colIndex = gen.nextInt(4);
        if (colIndex == 0){
            return Color.RED;
        } else if (colIndex == 1){
            return Color.BLUE;
        } else if (colIndex == 2){
            return Color.GREEN;
        } else {
            return Color.MAGENTA;
        }
    }

    //draws any object stored in the gameObjectsList via dynamic dispatch
    private void drawGameObjects(Graphics g){
        
        for (GameObj obj: gameObjectList){
            obj.draw(g);
        }
    }
    
    //moves, bounces, removes game Objects. Also calculates strikes
    private void animateGameObjects(){
        for (GameObj obj: gameObjectList){
            obj.move();
            obj.hitObj(paddle);
            
            //if the game object goes out of bounds it is removed
            if (obj.outOfBounds()){
                //if point ball goes past paddle it counts as a strike
                if (obj.pos_y > obj.max_y + obj.height * 2 && obj instanceof PointBall){
                    strikes++;
                }
                gameObjectList.remove(obj);
            }
        }
    }
  
    
    //Updates the score when a PointBall hits the correctly colored goal
    private void updateScores(){
        for (GameObj obj: gameObjectList){
            if (obj instanceof PointBall){
                PointBall temp = (PointBall) obj;
                for (int j = 0; j < goalList.size(); j++){
                    //if touches correct goal
                    if (temp.willIntersectGoal(goalList.get(j)) 
                            && temp.checkPointValid(goalList.get(j))){
                        score++;
                        refreshGoal();
                        gameObjectList.remove(temp);
                    //if touches any goal
                    } else if (temp.willIntersectGoal(goalList.get(j))) {
                        gameObjectList.remove(temp);
                    }
                }
            }
        }
    }

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//draw the score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.BOLD, 80));
        g.drawString(score + "", 400 - g.getFontMetrics().stringWidth(score + "")/2, 120);
        //draw strikes
        g.setFont(new Font("Serif", Font.BOLD, 40));
        g.drawString(strikes + "", 400 - g.getFontMetrics().stringWidth(strikes + "")/2, 180);
        
        //draw game objects, goals, and paddles
		drawGameObjects(g);
		drawGoals(g);
		paddle.draw(g);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 700);
	}
}
