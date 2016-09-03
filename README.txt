=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: slaouis
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. You may copy and paste from your proposal
  document if you did not change the features you are implementing.

  1. Concept 1: Modeling State Using 2D Arrays

- What specific feature of your game will be implemented using this concept?

  The Bricks will be displayed on the upper half of the game screen using a
  2D array.


- Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.
  
  The use of 2D arrays allows me to easily circulate through all of the bricks
  in order to evaluate their state, which is very useful when it comes to 
  assessing whether they should be displayed and whether the level is 
  completed. Last but not least, this tool allows for fancy displays with 
  elaborate structure.

  2. Concept 2: Object-Oriented Design

- What specific feature of your game will be implemented using this concept?

  The Bonus interface will be implemented by 3 different bonus objects which 
  display a specific image and share most of their methods, with some directly
  implemented in the Brick interface and others needing specific implementations
  by the subclasses.

- Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.
  
  Bonuses will affect the game state in different ways but they are dropped 
  by Bricks in the same way and have the same time duration; these 
  similarities are naturally grouped in an interface.

  3. Concept 3: Physics-based animations

- What specific feature of your game will be implemented using this concept?

  The released bonus will be under the effect of gravity and will undergo
  randomly generated motion.
  The ball-paddle interaction was revisited and now includes more complex
  physics.
  

  - Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.
  
  Implementing original motions for the bonuses' motion brings an original 
  feature to the well-known game, and it is intimately linked to the concept
  because it makes use of kinematics. It also increases the difficulty of 
  catching the bonuses. 
  Furthermore, I added a more subtle determination of the ball's trajectory
  following its impact on the paddle in order to make the game more 
  entertaining: by hitting the ball with the right side of the paddle, the ball
  will take on a trajectory to the right whose incline depends on the point of 
  impact on the paddle
  (see bouncePaddle method in Ball class) 

  4. Concept 4: Using I/O to parse a file format

  - What specific feature of your game will be implemented using this concept?

  A high-score screen will be maintained which is associated to a file listing
  the five highest scores and their associated user names.


  - Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.
  
  At the end of each game, the high-score file should be modified if necessary by 
  replacing the previous score by the new one. The file format is such that there
  are 5 lines containing the score and the associated user name, in descending order.
  The content of the file should be displayed using output techniques every time the
  user accesses the high-score screen, and a default display should be created to
  be shown when less than 5 games have been played. The user name should also be
  read from user input at the beginning of each game.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  - A Game class in charge of the general display of the main menu and of
    the level screen; the run method belongs to the game class.
  
  - A GameCourt class which synchronizes the motion of the objects on the 
    screen, keeps track of the status (score and lives), and maintains a 
    2D brick array
  
  - A Brick class which creates Bricks of varying resistance, color and
    value contributing to the score
    
  - A HighScores class which takes in a user name at every new game and 
    displays his score among the 5 current highest scores at the end of the 
    game, in a sorted order
    
  - A Ball class and a Paddle class, which both extend the provided
    GameObj class 
  
  - A Bonus interface, with 3 subclasses that modify the current state and
    share a duration and methods that control their behavior on the screen
    before they collide (possibly) with the paddle: LargePaddle, BigBall
    and QuickBall


- Revisit your proposal document. What components of your plan did you end up
  keeping? What did you have to change? Why?
  
  I added more physics! I realized that the game was dull in that the player had 
  almost no control over where the ball when through the collisions with the paddle,
  so I made a modification so that where in the paddle happened the collision with the
  ball determined the future direction of the ball.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  Yes, collisions were really delicate to implement. I had to play with the Area 
  class whose documentation I found in the Javados, and I considered several cases of 
  collision separately. 
  Figuring out how to control the flow from one screen to another was also a significant 
  challenge.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  I love my design! 


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  I used Javadocs to understand the Area class, and looked at stackoverflow to 
  figure out how to insert an image within a JPanel, manipulate a JDIalog and
  display formatted text into a JDialog window.
  I also referred to the example provided by the initial code whenever
  I had to deal with images.


