=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an approprate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. I will use a Linked List to store the falling objects. There will be 5 different types of 
  objects: point balls, freeze traps, invisibility traps, long paddle traps, and finally short 
  paddle traps. Each of these objects will inherit from the GameObj class, so the Linked 
  List will store a list of GameObj's. I chose to use a Linked List because my program will have to 
  draw each of the objects stored in the list. This means the program will have to go through the 
  entire list one by one, so it doesn’t make sense for the program to use a Set or Map, where it 
  is more difficult to iterate through the entire set of objects. I can also more easily add or 
  remove objects from the start and end of the list. Also, I want to be able to have duplicates
  in my gameObjectList. A random generator randomly adds new objects to the list so I want the
  data structure I use to be able to handle duplicates in the case that two or more of the same 
  objects are created. The Linked List storing the order in which the objects appear is also 
  useful in that the program can more quickly remove objects from the beginning of the list 
  when they go out of bounds. 
  
  I used a LinkedList to store the goals because their are only four goals so traversing the 
  collection of goals would be really quick. The colors of the goals also randomly change each 
  time the player scores so I would have to check through the entire list to see if the correct 
  colored ball hits the correct colored goal. 
  

  2. I will use I/O to record the high scores of the player, the date, the number of incorrect 
  goal hits, and the number of effects hit when the game ends. This information is displayed at 
  the click of a button on the control panel labeled "high scores". 
  

  3. Each of the objects falling from the top of the canvas will inherit from the GameObj 
  superclass. This is because they all require similar methods such as move() and hitObj(). 
  They also have similar fields, such as x_pos, y_pos, width, and height. However, each of the 
  falling objects will have a different implementation of the willIntersectPaddle() method since 
  each object will interact differently with the paddle once they collide. For instance, some effect 
  objects will freeze the paddle, increase the paddle size, decrease the paddle size, and make the 
  paddle invisible. Another method that they differ in is the setEnabled method. This method 
  determines how long each effect will last. Finally, These objects will also have unique fields, 
  such as a color field in the PointBall class to store the color. This color will be used to 
  determine whether the PointBall has entered the correctly colored goal. 


  Therefore, the program will use dynamic dispatch to call the correct willIntersectPaddle() and the 
  setEnabled() methods for each object. Each object will also look different which means I can call 
  the different draw method for each object in a similar fashion. This way I can store a list of 
  different objects in a GameObj list and iterate through it calling the same methods, 
  allowing me to easily draw and set the interactivity of many different objects without worrying 
  about calling the correct subclass methods.

  4. I will write JUnit tests for the methods I wrote in the GameObj class, HighScoreReaderWriter 
  class, and the various GameObj subclasses. These methods deal primarily with the movement and 
  collision of the different game components. In these methods, I will be primarily calculating 
  the rebound angle of the point balls in relation to the angle of the paddle. I will also be 
  calculating the collision of a circle and angled surface. In the given code, the collision of 
  the circle with other objects uses a bounding box. However, in my own method, I will be detecting 
  the collisions between the curved edges of the circle with any angled surface. These methods 
  contains many edge cases so I will need to test them extensively. I will also test the methods 
  dealing with the adding and removing of objects from the collection.
  


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  GameCourt: This is where all the animation and logic of the game happens. It is also where the
  game objects interact and the scores are added up. 
  
  GameObj: This is where the fields and methods to be shared by the 7 types of objects are 
  written. Methods such as setEnable and willIntersect and fields such as pos_x and pos_y are 
  shared through this superclass. Additionally, the fields governing the special effects of the 
  paddle are shared here as well. 
  
  Paddle: This class creates a paddle. 
  
  Goal: This class creates a goal object.
  
  PointBall: This class creates a PointBall object.
  
  FreezeTrap: This class creates a FreezeTrap effect object.
  
  InvisibleTrap: This class creates a InvisibleTrap effect object.
  
  LongPaddle: This class creates a LongPaddle effect object.
  
  ShortPaddle: This class creates a ShortPaddle effect object.
  
  HighScoreReaderWriter: This class is responsible for reading and writing the 
  high score information to an external file. This class also refreshes the file whenever the 
  score is updated and makes sure they are in order.
  
  

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  Some significant stumbling blocks include writing the HighScoreReaderWriter class as well as 
  calculating collisions between a circular object and the angled surface of the paddle. For the 
  HighScoreReaderWriter class, I had to work through a number of challenged such as updating the 
  order of the scores based on top ten highest scores in the external text file. I also faced a bug
  where the program will overwrite the old text file every time a new score is recorded. As for 
  calculating the collisions, it was challenging calculating the x and y distance needed for the 
  paddle to contact the curved surfaces of the objects. 


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  Overall, there is a good separation of functionality. I created many helper methods, such as 
  drawStrings() which I was able to reuse. The GameObj class also has more broad methods which the
  subclasses provide further detail in their own implementations. The best example of separation
  of functionality is the HighScoreReaderWriter class that I wrote. This class is dedicated 
  to recording, updating, and returning the high score information. 
  
  I also made sure to encapsulate all private states. If a method was only used as a helper method
  in that class, I declared it private. Additionally, methods used outside the class, such as most 
  of the methods in the GameObj class, I declared public.
  
  If given more time, I would make an interface in place of the GameObj class in order to further 
  differentiate between the different objects. For instance although Paddle and FreezeTrap share 
  several methods and fields, they have completely different functionalities in the game. 


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  java.awt.geom.AffineTransform: I used this library to rotate the paddle object.
  java.awt.Component.addMouseMotionListener: I used this to make the paddle follow the mouse 
  position continuously.
  
  Aside from using this library to rotate the paddle, I did not use any external tutorials 
  or images.
  


