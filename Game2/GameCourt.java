
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;

import javax.swing.*;

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

    private Paddle paddle;

    // Game states
    public boolean playing = false;
    public boolean showInstruction = true;
    public boolean showHighScores = false;
    public boolean gameOver = false;

    // Game constants
    public static final int PADDLE_ANGLE_VELOCITY = 5;

    // Time constants
    public static final int INTERVAL = 35;
    private int objectTime;
    
    // Game data structures
    LinkedList<GameObj> gameObjectList = new LinkedList<GameObj>();
    LinkedList<Goal> goalList = new LinkedList<Goal>();

    // Game information to record
    private int score = 0;
    private int strikes = 0;
    private int incorrectHit = 0;
    private int effectsHit = 0;
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat timeFormat;
    private HighScoreReaderWriter highScores;
        
    
    public GameCourt() {
        objectTime = 3500;
        requestFocusInWindow();

        timeFormat = new SimpleDateFormat("HH:mm:ss");
        highScores = new HighScoreReaderWriter(new File("high-scores.txt"));

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        refreshGoal();

        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            // changes the angle of the paddle
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    paddle.setAngleVel(-PADDLE_ANGLE_VELOCITY);
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    paddle.setAngleVel(PADDLE_ANGLE_VELOCITY);
            }

            // stop paddle from turning
            public void keyReleased(KeyEvent e) {
                paddle.setAngleVel(0);
            }
        });

        // paddle follows mouse movement
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (!GameObj.frozen){
                    paddle.pos_x = e.getX();
                }
            }
        });
        setFocusable(true);
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        paddle = new Paddle();
        strikes = 0;
        score = 0;
        incorrectHit = 0;
        effectsHit = 0;
        gameObjectList.clear();
        GameObj.paddleColor = Color.WHITE;
        GameObj.paddleWidth = 120;
        GameObj.frozen = false;

        if (gameOver) {
            playing = true;
            showInstruction = false;
            showHighScores = false;
            gameOver = false;
        }
        requestFocusInWindow();
    }

    /**Starts the game by setting all the game fields*/
    public void startGame() {
        playing = true;
        gameOver = false;
        showInstruction = false;
        showHighScores = false;
        requestFocusInWindow();
    }

    /**Shows the instructions by turning off non relevant fields and turning on 
     * field governing instructions display*/
    public void showInstructions() {
        showInstruction = true;
        playing = false;
        showHighScores = false;
        gameOver = false;

        repaint();
        requestFocusInWindow();
    }

    /**Shows the high scores by turning off non relevant fields and turning on 
     * field governing high score display*/    
    public void showHighScores() {
        showHighScores = true;
        playing = false;
        showInstruction = false;
        gameOver = false;

        repaint();
        requestFocusInWindow();
    }

    /**Exits the program*/
    public void exit() {
        System.exit(0);
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            addGameObject();
            updateScores();
            animateGameObjects();

            // moves paddle radially
            paddle.move();

            // check for the game end conditions
            if (strikes == 5) {
                gameOver = true;
                playing = false;

                // writes score and date information to the high score file.
                String date = timeFormat.format(cal.getTime());
                String infoToStore = score + " " + incorrectHit + " " + effectsHit + " " + date;
                highScores.resetFileReaderWriter();
                highScores.addInfo(score, infoToStore, incorrectHit, effectsHit);
            }

            // update the display
            repaint();
            objectTime += 35;
        }
    }

    /**Clears current goals and replaces them with new randomized goals with different colors*/
    private void refreshGoal() {
        goalList.clear();
        goalList = Goal.getNewGoals();      
    }

    /**Goes through the goalList to draw each goal*/
    private void drawGoals(Graphics g) {
        for (int i = 0; i < goalList.size(); i++) {
            goalList.get(i).draw(g);
        }
    }
    
    /**Adds the five different game objects to the gameObjectList*/
    private void addGameObject() {
        PointBall tempPoint = PointBall.createPointBall(objectTime);
        if (tempPoint != null) {
            gameObjectList.add(tempPoint);
        }
        
        FreezeTrap tempFreeze = FreezeTrap.createFreezeTrap(objectTime);
        if (tempFreeze != null) {
            gameObjectList.add(tempFreeze);
        }
        
        InvisibleTrap tempInvisible = InvisibleTrap.createInvisibleTrap(objectTime);
        if (tempInvisible != null) {
            gameObjectList.add(tempInvisible);
        }
        
        LongPaddle tempLong = LongPaddle.createLongPaddle(objectTime);
        if (tempLong != null) {
            gameObjectList.add(tempLong);
        }
        
        ShortPaddle tempShort = ShortPaddle.createShortPaddle(objectTime);
        if (tempShort != null) {
            gameObjectList.add(tempShort);
        }
    }
    

    /**Draws all the game objects in a list
     * 
     * @param list the list of Game objects to be drawn
     * @param g the graphics component*/
    private void drawObjects(LinkedList<GameObj> list, Graphics g) {
        for (GameObj obj : gameObjectList) {
            obj.draw(g);
        }
    }

    /**Updates the conditions of the screen*/
    private void animateGameObjects() {
        for (int i = 0; i < gameObjectList.size(); i++) {
            GameObj tempObject = gameObjectList.get(i);
            tempObject.move();
            tempObject.hitObj(paddle);
            
            //increments time counter for each effect to determine how long they will last
            if (tempObject.returnHitEffect()) {
                tempObject.incrementTime();
            }
            
            tempObject.setEnabled();

            if (!(tempObject instanceof PointBall) && tempObject.willIntersect(paddle)) {
                effectsHit++;
            }
            
            // if the game object goes out of bounds it is removed
            if (tempObject.outOfBounds()) {
                // if point ball goes past paddle it counts as a strike
                if (tempObject.pos_y > tempObject.max_y + tempObject.height * 2) {
                    if (tempObject instanceof PointBall) {
                        strikes++;
                    } 
                }
                
                //makes sure the effect of the goal runs out before the effect object is removed
                if (!(tempObject instanceof PointBall)) {
                    if (tempObject.maxTime <= tempObject.time) {
                        gameObjectList.remove(tempObject);
                    }
                } else {
                    gameObjectList.remove(tempObject);
                }
            }
        }
    }

    /**Updates the score depending on if the point ball hits the correct goal*/
    private void updateScores() {
        for (int i = 0; i < gameObjectList.size(); i++) {
            GameObj obj = gameObjectList.get(i);
            if (obj instanceof PointBall) {
                PointBall temp = (PointBall) obj;
                for (int j = 0; j < goalList.size(); j++) {
                    // if touches correct goal
                    if (temp.willIntersectGoal(goalList.get(j))
                            && temp.checkPointValid(goalList.get(j))) {
                        score++;
                        refreshGoal();
                        gameObjectList.remove(temp);
                        // if touches wrong goal decreases score
                    } else if (temp.willIntersectGoal(goalList.get(j))) {
                        gameObjectList.remove(temp);
                        incorrectHit++;
                        if (score > 0) {
                            score--;
                        }
                    }
                }
            } 
        }
    }
    
    /**Draws a given list of strings
     * 
     * @param g the graphic component
     * @param message the list of strings
     * @param x the x position of the strings
     * @param y the y position of the strings
     * @param size the size of the strings
     * @param spacing the spacing in between the lines*/
    public void drawStrings(Graphics g, LinkedList<String> message, int x, int y, int size, int spacing) {
        g.setFont(new Font("Serif", Font.BOLD, size));
        int spacer = 0;
        for (int i = 0; i < message.size(); i++) {
            String tempText = message.get(i);
            g.drawString(tempText, x, y + spacer);
            spacer += spacing;
        }
    }

    /**Paints the instruction page
     * 
     * @param g the graphics component*/
    public void paintInstructions(Graphics g) {
        super.paintComponent(g);
        String title = "Rainbow Hailstorm";

        String wordLengthStandard = "the falling colored balls into the same colored goals on "
                + "either side of the \n";
        int width = 400 - g.getFontMetrics().stringWidth(wordLengthStandard) / 2;

        LinkedList<String> tempInstructionsList = new LinkedList<String>();
        tempInstructionsList.add(
                "Welcome to Rainbow Hailstorm. The objective of this game is to bounce \n");
        tempInstructionsList.add(
                "the falling colored balls into the same colored goals on either side of the \n");
        tempInstructionsList.add(
                "screen. To do this, you will use the paddle on the bottom of the screen. Move \n");
        tempInstructionsList.add(
                "the mouse left and right to move the paddle and press the left and right \n");
        tempInstructionsList.add(
                "keys to angle the paddle. Angle the paddle to the correct angle to bounce the \n");
        tempInstructionsList
                .add("balls into the goals. Each ball is worth 1 point. \n");
        tempInstructionsList.add(
                "Warning! You will have to avoid the traps that fall down. Hitting a ball into \n");
        tempInstructionsList.add(
                "the wrong colored goal will deduct 1 point. Letting a ball go pass the paddle\n");
        tempInstructionsList.add(
                "and hit the ground will result in a strike. Five strikes and the game ends");

        g.setColor(Color.WHITE);
        drawStrings(g, tempInstructionsList, width, 120, 15, 20);
        g.setFont(new Font(title, Font.PLAIN, 30));
        g.drawString(title, 400 - g.getFontMetrics().stringWidth(title) / 2, 50);

        // draw example game objects, goals, and paddles
        int objX = width + 30;
        int objY = 320;
        int spacerObj = 0;
        
        LinkedList<String> descriptionList = new LinkedList<String>();
        descriptionList.add("Point Ball: Worth one point");
        descriptionList.add("Freeze Trap: Freezes you paddle");
        descriptionList.add("Invisible Trap: Makes the paddle invisible");
        descriptionList.add("Long Paddle: Makes the paddle longer");
        descriptionList.add("Short Paddle: Makes the paddle shorter");
        
        drawStrings(g, descriptionList, objX + 50, objY + 25, 20, 45);

        // linked list since have to go through each one to call draw method
        LinkedList<GameObj> imageList = new LinkedList<GameObj>();
        imageList.add(new PointBall(0, 0, Color.BLUE, 30));
        imageList.add(new FreezeTrap(0, 0, 30));
        imageList.add(new InvisibleTrap(0, 0, 30));
        imageList.add(new LongPaddle(0, 0, 30));
        imageList.add(new ShortPaddle(0, 0, 30));
        
        for (GameObj obj : imageList) {
            obj.pos_x = objX;
            obj.pos_y = objY + spacerObj;
            obj.draw(g);
            spacerObj += obj.height + 15;
        }
    }

    /**Paints the various pages of the game such as instructions, high scores, and game over
     * 
     * @param g graphics component*/
    @Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        if (showInstruction) {
            paintInstructions(g);
        } else if (gameOver) {
            g.setFont(new Font("Serif", Font.BOLD, 60));
            int x = 400 - g.getFontMetrics().stringWidth("GAME OVER")/2;
            g.drawString("GAME OVER", x, 120);
            g.setFont(new Font("Serif", Font.BOLD, 45));
            g.drawString("Score: " + score, x + 120, 220);
        } else if (showHighScores) {
            //high scores on the game over page or individually
            g.setFont(new Font("Serif", Font.BOLD, 30));
            drawStrings(g, highScores.getHighScoreInfo(), 120, 120, 25, 40);
            g.drawString("High scores: ", 350, 80);
        } else if (playing){
	        //draw the score
	        g.setFont(new Font("Serif", Font.BOLD, 80));
	        g.drawString("Score: " + score, 400 - g.getFontMetrics().stringWidth("Score: " + score)/2, 120);
	        //draw strikes
	        g.setFont(new Font("Serif", Font.BOLD, 40));
	        g.drawString("Strikes: " + strikes, 400 - g.getFontMetrics().stringWidth("Strikes: " + strikes)/2, 180);
	        drawObjects(gameObjectList, g);
        }
        drawGoals(g);
        paddle.draw(g);
	}
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 700);
    }
}
