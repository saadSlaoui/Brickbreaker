This breackbreaker game is the result of the independent final project of a Computer Science class I took in the Spring of 2016. The implementation makes use of concepts such as the modeling of state using 2D arrays, object-oriented design, physics-based animation and I/O interactions to maintain a high-scores file. 

Breakdown of the role of each class:
- The Game class in charge of the general display of the main menu and of the level screen
- The GameCourt class synchronizes the motion of the objects on the screen, keeps track of the status (score and lives), and maintains a 2D brick array
- The GameObj class defines general patterns for different game objects
- The Brick class creates Bricks of varying resistance, color and
value contributing to the score
- The HighScores class takes in a user name at every new game and displays his score among the 5 current highest scores at the end of the game, in a sorted order
- The Ball class and the Paddle class both extend the GameObj class 
- The Bonus interface, with 3 subclasses that modify the current state and share a duration and methods that control their behavior on the screen before they collide (possibly) with the paddle: LargePaddle, BigBall and QuickBall





