import static org.junit.Assert.*;

import java.awt.Color;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;

import org.junit.Test;

public class Tester {

    GameCourt testGameCourt = new GameCourt();
    GameObj testObj = new GameObj(4, 5, 100, 100, 50, 50);
    Paddle testPaddle = new Paddle();
    
    //move test
    @Test
    public void testMoveInBounds() {
        testObj.move();
        assertEquals(104, testObj.pos_x);
        assertEquals(105, testObj.pos_y);
    }
    
    @Test
    public void testMoveOutOfBounds() {
        testObj.pos_x = 800;
        testObj.pos_y = 700;
        testObj.move();
        assertEquals(804, testObj.pos_x);
        assertEquals(705, testObj.pos_y);
    }
    
    @Test
    public void testWillIntersectMiddleFlatPaddle() {
        testObj.pos_x = testPaddle.pos_x - testObj.width/2;
        testObj.pos_y = testPaddle.pos_y - testObj.height + 1; //just barely inside paddle
        assertTrue(testObj.willIntersectPaddle(testPaddle));
    }
    
    @Test
    public void testWillIntersectRightFlatPaddle() {
        testObj.pos_x = testPaddle.pos_x + testPaddle.width/2 - testObj.width/2;
        testObj.pos_y = testPaddle.pos_y - testObj.height + 1;
        assertTrue(testObj.willIntersectPaddle(testPaddle));
    }
    
    @Test
    public void testWillIntersectLeftFlatPaddle() {
        testObj.pos_x = testPaddle.pos_x - testPaddle.width/2 - testObj.width/2;
        testObj.pos_y = testPaddle.pos_y - testObj.height + 2;
        assertTrue(testObj.willIntersectPaddle(testPaddle));
    }
    
    @Test
    public void testWillIntersectRightUpPaddle() {
        testPaddle.setAngle(30);
        testObj.pos_x = (int) Math.round(testPaddle.pos_x + testPaddle.width/2 * 
                Math.cos(Math.toRadians(30) - testObj.width/2));
        testObj.pos_y = (int) Math.round(testPaddle.pos_y - testPaddle.width/2 * 
                Math.sin(Math.toRadians(30) - testObj.height + testPaddle.height));
        assertTrue(testObj.willIntersectPaddle(testPaddle));
    }
    
    @Test
    public void testWillIntersectLeftDownPaddle() {
        testPaddle.setAngle(30);
        testObj.pos_x = (int) Math.round(testPaddle.pos_x - testPaddle.width/2 * 
                Math.cos(Math.toRadians(30) - testObj.width/2));
        testObj.pos_y = (int) Math.round(testPaddle.pos_y + testPaddle.width/2 * 
                Math.sin(Math.toRadians(30) - testObj.height + testPaddle.height + 1));
        assertTrue(testObj.willIntersectPaddle(testPaddle));
    }
    
//    @Test
//    public void testWillIntersectRightDownPaddle() {
//        testPaddle.setAngle(-30);
//        testObj.pos_x = (int) Math.round(testPaddle.pos_x + testPaddle.width/2 * 
//                Math.cos(Math.toRadians(30) - testObj.width/2));
//        testObj.pos_y = (int) Math.round(testPaddle.pos_y - testPaddle.width/2 * 
//                Math.sin(Math.toRadians(-30) - testObj.height - testPaddle.height + 5));
//        assertTrue(testObj.willIntersectPaddle(testPaddle));
//    }
    
    @Test
    public void testWillIntersectLeftUpPaddle() {
        testPaddle.setAngle(-30);
        testObj.pos_x = (int) Math.round(testPaddle.pos_x - testPaddle.width/2 * 
                Math.cos(Math.toRadians(30) - testObj.width/2));
        testObj.pos_y = (int) Math.round(testPaddle.pos_y - testPaddle.width/2 * 
                Math.sin(Math.toRadians(-30) - testObj.height - testPaddle.height + 5));
        assertTrue(testObj.willIntersectPaddle(testPaddle));
    }
    
    
    
    
    
    
    
    @Test
    public void testWillIntersect() {
        testObj.pos_x = testPaddle.pos_x - testObj.width/2;
        testObj.pos_y = testPaddle.pos_y - testObj.height + 2; //just barely inside paddle
        assertTrue(testObj.willIntersect(testPaddle));
    }
    
    @Test
    public void testOutOfBoundsFarOut() {
        testObj.pos_x = 890;
        testObj.pos_y = 790; //just barely inside paddle
        assertTrue(testObj.outOfBounds());
    }
    
    @Test
    public void testOutOfBoundsBackOut() {
        testObj.pos_x = -90;
        testObj.pos_y = -90; //just barely inside paddle
        assertTrue(testObj.outOfBounds());
    }
    
    @Test
    public void testInBounds() {
        testObj.pos_x = 100;
        testObj.pos_y = 100; //just barely inside paddle
        assertFalse(testObj.outOfBounds());
    }
    
    @Test
    public void testBounceAnglePositive() {
        testObj.v_x = 0;
        testObj.v_y = 10; //just barely inside paddle
        testPaddle.setAngle(30);
        testObj.bounceAngle(testPaddle);
        
        int reboundAngle = 2 * 30 - 90;
        int expectedXVel = (int) Math.round(10 * Math.cos(Math.toRadians(reboundAngle)));
        int expectedYVel = (int) Math.round(10 * Math.sin(Math.toRadians(reboundAngle)));
        assertEquals(testObj.v_x, expectedXVel);
        assertEquals(testObj.v_y, expectedYVel);
    }
    
    @Test
    public void testBounceAngleNegative() {
        testObj.v_x = 0;
        testObj.v_y = 10; //just barely inside paddle
        testPaddle.setAngle(-30);
        testObj.bounceAngle(testPaddle);
        
        int reboundAngle = 2 * -30 - 90;
        int expectedXVel = (int) Math.round(10 * Math.cos(Math.toRadians(reboundAngle)));
        int expectedYVel = (int) Math.round(10 * Math.sin(Math.toRadians(reboundAngle)));
        assertEquals(testObj.v_x, expectedXVel);
        assertEquals(testObj.v_y, expectedYVel);
    }
    
    @Test
    public void testIncrementTime() {
        int expectedTime = 70;
        testObj.incrementTime();
        testObj.incrementTime();
        assertEquals(testObj.time, expectedTime);
    }
    
    @Test
    public void testRandomColor() {
        Random gen = new Random();
        LinkedList<Color> tempColors = new LinkedList<Color>();
        tempColors.add(Color.RED);
        tempColors.add(Color.BLUE);
        tempColors.add(Color.GREEN);
        tempColors.add(Color.MAGENTA);
        
        for(int i = 0; i < 10; i++) {
            Color randomColor = GameObj.randomColor(gen);
            assertTrue(tempColors.contains(randomColor));
        }
    }
    
    @Test
    public void testWillIntersectGoalLeft() {
        PointBall tempPoint = PointBall.createPointBall(0);
        tempPoint.pos_x = 5;
        tempPoint.pos_y = 150;

        assertTrue(tempPoint.willIntersectGoal(new Goal(0, 100, Color.RED)));
    }
    
    @Test
    public void testWillIntersectGoalRight() {
        PointBall tempPoint = PointBall.createPointBall(0);
        tempPoint.pos_x = 745;
        tempPoint.pos_y = 150;

        assertTrue(tempPoint.willIntersectGoal(new Goal(800 - 15, 100, Color.RED)));
    }
    
    @Test
    public void testNoIntersectGoalRight() {
        PointBall tempPoint = PointBall.createPointBall(0);
        tempPoint.pos_x = 300;
        tempPoint.pos_y = 200;

        assertFalse(tempPoint.willIntersectGoal(new Goal(800 - 15, 100, Color.RED)));
    }
    
    @Test
    public void testNoIntersectGoalLeft() {
        PointBall tempPoint = PointBall.createPointBall(0);
        tempPoint.pos_x = 300;
        tempPoint.pos_y = 200;

        assertFalse(tempPoint.willIntersectGoal(new Goal(0, 100, Color.RED)));
    }
    
    @Test
    public void testCheckPointValid() {
        PointBall tempPoint = PointBall.createPointBall(0);
        tempPoint.pos_x = 745;
        tempPoint.pos_y = 150;
        tempPoint.setColor(Color.RED);

        assertTrue(tempPoint.willIntersectGoal(new Goal(800 - 15, 100, Color.RED)));
    }
    
    @Test
    public void testCheckPointNotValid() {
        PointBall tempPoint = PointBall.createPointBall(0);
        tempPoint.pos_x = 745;
        tempPoint.pos_y = 150;
        tempPoint.setColor(Color.BLUE);

        assertTrue(tempPoint.willIntersectGoal(new Goal(800 - 15, 100, Color.RED)));
    }
    
    @Test
    public void testSetEnabledFreezeTrap() {
        FreezeTrap tempFreeze = FreezeTrap.createFreezeTrap(0);
        tempFreeze.time = 1400;
        GameObj.frozen = true;
        tempFreeze.hitEffect = true;
        tempFreeze.setEnabled();

        assertFalse(GameObj.frozen);
        assertFalse(tempFreeze.hitEffect);
    }
    
    @Test
    public void testSetEnabledInvisibleTrap() {
        InvisibleTrap tempInvisible = InvisibleTrap.createInvisibleTrap(0);
        tempInvisible.time = 1750;
        tempInvisible.hitEffect = true;
        GameObj.paddleColor = Color.BLACK;
        tempInvisible.setEnabled();

        assertEquals(Color.WHITE, GameObj.paddleColor);
        assertFalse(tempInvisible.hitEffect);
    }
    
    @Test
    public void testSetEnabledLongPaddle() {
        LongPaddle tempLong = LongPaddle.createLongPaddle(0);
        tempLong.time = 3500;
        tempLong.hitEffect = true;
        GameObj.paddleWidth = 160;
        tempLong.setEnabled();

        assertEquals(120, GameObj.paddleWidth);
        assertFalse(tempLong.hitEffect);
    }
    
    @Test
    public void testSetEnabledShortPaddle() {
        ShortPaddle tempShort = ShortPaddle.createShortPaddle(0);
        tempShort.time = 2450;
        tempShort.hitEffect = true;
        GameObj.paddleWidth = 50;
        tempShort.setEnabled();

        assertEquals(120, GameObj.paddleWidth);
        assertFalse(tempShort.hitEffect);
    }
    
    
    /**HighScoreReaderWriter tests*/
    @Test
    public void testHighScoreReadrWriterUpdateScoreLists() {
        //the updateScoreLists method is called in constructor
        HighScoreReaderWriter highScores = new HighScoreReaderWriter(new File("high-scores-test.txt"));        
        LinkedList<int[]> result = highScores.getScores();
        
        assertEquals(result.get(0)[0], 3);
        assertEquals(result.get(0)[1], 1);
        assertEquals(result.get(0)[2], 0);
        
        assertEquals(result.get(1)[0], 1);
        assertEquals(result.get(1)[1], 0);
        assertEquals(result.get(1)[2], 0);
        
        assertEquals(result.get(2)[0], 1);
        assertEquals(result.get(2)[1], 2);
        assertEquals(result.get(2)[2], 5);
        
        assertEquals(result.get(3)[0], 1);
        assertEquals(result.get(3)[1], 2);
        assertEquals(result.get(3)[2], 5);
    }
    
    @Test
    public void testHighScoreReadrWriterUpdateScoreListsEmpty() {
        //the updateScoreLists method is called in constructor
        HighScoreReaderWriter highScores = new HighScoreReaderWriter(new File("high-scores-test-empty.txt"));        
        LinkedList<int[]> result = highScores.getScores();
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testHighScoreReadrWriterResetFile() {
        //the updateScoreLists method is called in constructor
        HighScoreReaderWriter highScores = new HighScoreReaderWriter(new File("high-scores-test3.txt"));        
        highScores.resetFileReaderWriter();
        
        HighScoreReaderWriter highScores2 = new HighScoreReaderWriter(new File("high-scores-test3.txt"));
        LinkedList<int[]> result = highScores2.getScores();
        
        assertEquals(0, result.size());
    }
    
    
    @Test
    public void testAddInfo() {
        //the updateScoreLists method is called in constructor
        HighScoreReaderWriter highScores = new HighScoreReaderWriter(new File("high-scores-test2.txt"));        
        highScores.addInfo(6, "6 7 8 15:50:47", 7, 8);
        HighScoreReaderWriter highScores2 = new HighScoreReaderWriter(new File("high-scores-test2.txt"));        

        
        LinkedList<int[]> result = highScores2.getScores();
        
        assertEquals(result.get(0)[0], 6);
        assertEquals(result.get(0)[1], 7);
        assertEquals(result.get(0)[2], 8);
    }
    
    
    
    
    
    
    
    
    

}
