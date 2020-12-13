=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: raunaqs
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
  
  I used a 2D Array of the abstract class Tile. This array served as the board 
  for my Minesweeper game. The Tile class has the subclasses Mine and Number. 
  The board (or array) has randomly placed Mines across it except for where the
  player first clicks. Every other spot is a Number, a Tile that is not a
  Mine that shows how many neighboring Tiles are Mines. My Minesweeper 
  implementation has three difficulty levels (Easy, Medium, and Expert), each 
  of which has a different size board. This is very easy to implement with a
  2D Array. The 2D Array was also convenient for reading and writing the board
  to files. 

  2. File I/O
  
  My implementation of Minesweeper utilizes File I/O multiple times. First, it 
  is used to read in images (like the image of the flag that the user can use to
  mark a certain suspicious Tile). Second, the game uses File I/O to keep track
  of high scores. Whenever the user wins, the program reads a file of the high 
  scores for the difficulty the user played at and if there are not 5 high 
  scores or the user has a better high score, they will replace the lowest of 
  the previous high scorers. These scores are also sorted. Lastly, the program 
  stores the board of the most recent loss or win using File I/O. Characters 
  representing the 2D Array serving as the game board are stored in the 
  'recent.txt' file. There is a 'rewind' button which allows users to play 
  this board, in case they accidentally clicked a mine and lost and would 
  like to try that same board again, or if they won and would like to see if 
  someone else can match them. The data (the high scores and most recent 
  win/loss board) is persistent. The program also catches and handles 
  IOExceptions (FileNotFoundException and NoSuchFileException).

  3. Inheritance/Subtyping for Dynamic Dispatch
  
  I used an abstract class, 'Tile,' to represent each location in the 2D array 
  board. This class had two subclasses, 'Mine' and 'Number.' The use of the 
  abstract class was justified as the subclasses both inherit the 
  'handleRightClick()' method. If the player right clicks a covered Mine or
  Number, the Tile will be flagged. The subclasses also inherit other useful
  methods, such as a method to get an image from its file path. The Mine and 
  Number classes are different as well. Most importantly, they have different
  implementations of the 'handleLeftClick()' method. When a Mine is left-clicked,
  the player loses and the game ends. However, if they left-click on a Number,
  it is uncovered and the player becomes one step closer to winning. 

  4. Recursion
  
  I used recursion in order to clear the areas of the board where there are no 
  mines.When the player left-clicks a Number Tile that has no neighboring mines, 
  the neighboring Tiles are uncovered. This process is repeated for those 
  neighboring Number Tiles with 0 neighboring mines. This process lends itself
  to a recursive solution. The base case of the recursion is when none of the 
  neighboring Number Tiles have 0 surrounding mines. 

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Spot Class:
  Functions as a (row, column) tuple. Made it easier to use collections of 
  locations in the 2D Array (ex. using Set<Spot> instead of two coordinated 
  Sets, one for the row and one for the column). 
  
  Tile Class:
  Abstract class that represented what was at each spot in the 2D board array.
  Each Tile is either a Number of Mine. This class implemented a function that
  handled a player right clicking a tile by flagging the tile. The subclasses
  handled the player left clicking the tiles, as they had different 
  implementations. 
  
  Number Class:
  Subclass of Tile class. The Number class represents a spot in the board where
  there is no Mine. This tile displays the number of adjacent mines (it is blank
  if there are none). If implements a function that handles a left-click, 
  which will uncover the tile and reveal the number of adjacent mines. This 
  function is recursively called on tiles with 0 adjacent mines in order to 
  clear areas without mines.
  
  Mine Class:
  Subclass of Tile class. The Mine class represents a mine, and implements a 
  function that handles a left-click by ending the game with a loss. 
  
  GameBoard Class:
  The GameBoard class contains the 2D Array of Tiles. It directs the clicks of
  the player to the Mine/Number class that was clicked. It contains the logic
  of the game. It generates the board, updates the timer, keeps track of 
  the flags used, updates high scores, and saves and reads the board of the 
  previous win/loss.  
  
  Game Class:
  The Game class organizes the board and other widgets (the level dropdown, 
  the flag and timer labels, and the reset, rewind, high score, and instructions
  buttons) on the game window.
  

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  My largest mistake came from an extraneous readLine() call, which completely
  messed up my File I/O results and took me very long to find. My design process
  took some extra time as I decided to make the sizes of my tiles different for
  the different board sizes (so that the expert level board was not
  ridiculously large or the easy level board was very small). I ultimately had 
  to account for this when drawing mines in the Mine class, and drawing the 
  numbers on the Number tiles. I also made a HighScore class to store both the
  name of the user and their score, but ultimately found that this was 
  unnecessary. 

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  I think I did a generally good job with the overall design of the game. For the 
  most part, the functionality is in the proper place (i.e. my Mine class handles
  the explosion of the Mine, etc.). I also did not directly alter the private 2D 
  Array storing the board in the GameBoard class. Instead, I used getter and 
  setter methods diligently. If I were to refactor, I would probably add more
  getter/setter methods so that my Tile class did not have to have the 2D 
  board array as a protected variable (for the sake of my Number and Mine classes),
  but instead as a private variable. 


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  Images:
  http://pixelartmaker.com/art/07b1e1ff07180bb
  
  https://icon-library.com/icon/sand-timer-icon-3.html
  
  http://www.myiconfinder.com/icon/next-pause-previous-rewind-forward-stop-play-
  back-eject-controls-music-button/4923
  
  https://www.marshall.edu/it/keep-learning/question-mark-circle-icon/
  
  https://hotemoji.com/triangular-flag-on-post-emoji.html
  
  https://www.freeiconspng.com/img/32264
  
  
  
  
  
