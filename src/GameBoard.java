
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {
    
    private Tile[][] board;
    
    //number of mines on the board
    private int mineCount;
    
    //number of revealed tiles
    private int uncoveredCount;
    
    //difficulty level
    public enum Level {EASY, MEDIUM, EXPERT};
    private Level level;
    
    public enum GameState {READY, PLAYING, WON, LOST};
    private GameState gameState;
    
    //how many tiles are flagged
    private int flaggedCount;
    private JLabel flagStatus;
    
    //how much time has elapsed since the game started
    private int time;
    private JLabel timeStatus;
    
    //width/length of each tile (the tiles are squares)
    private int tileSize;
    
    private Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            updateTimer();
        }
    });
    
    public GameBoard(Level level, JLabel flagStatus, JLabel timeStatus) {
        
        if (level == Level.EASY) {
            this.board = new Tile[8][10];
            this.mineCount = 10;
            this.tileSize = 48;
        }
        else if (level == Level.MEDIUM) {
            this.board = new Tile[14][18];
            this.mineCount = 40;
            this.tileSize = 40;
        }
        else {
            this.board = new Tile[20][24];
            this.mineCount = 99;
            this.tileSize = 32;
        }
        
        this.level = level;
        this.uncoveredCount = 0;
        this.gameState = GameState.READY;
        
        this.time = 0;
        this.timeStatus = timeStatus;
        this.flaggedCount = 0;
        this.flagStatus = flagStatus;
        
        this.updateStatus();
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isOver())
                    return;
                
                Point p = e.getPoint();
                int row = (int) (p.y / tileSize);
                int col = (int) (p.x / tileSize);
                
                if (SwingUtilities.isLeftMouseButton(e)) {
                    handleLeftClick(row, col);
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    handleRightClick(row, col);
                }
                
            }
        });
    }

    //reset board given a difficulty level
    public void reset(Level l) {
        
        if (l == Level.EASY) {
            this.board = new Tile[8][10];
            this.mineCount = 10;
            this.tileSize = 48;
        }
        else if (l == Level.MEDIUM) {
            this.board = new Tile[14][18];
            this.mineCount = 40;
            this.tileSize = 40;
        }
        else {
            this.board = new Tile[20][24];
            this.mineCount = 99;
            this.tileSize = 32;
        }
        
        this.level = l;
        this.uncoveredCount = 0;
        this.gameState = GameState.READY;
        
        this.time = 0;
        this.timeStatus = timeStatus;
        this.flaggedCount = 0;
        this.flagStatus = flagStatus;
        
        this.timer.stop();
        this.updateStatus();
        
        repaint();

        requestFocusInWindow();
    }

    public void updateStatus() {
        this.flagStatus.setText("" + (this.mineCount - this.flaggedCount));
        this.timeStatus.setText("" + this.time);
    }
    
    public void updateTimer() {
        this.time++;
        updateStatus();
    }
    
    /*--------Getter and Setter Methods---------*/
    
    public int boardRows() {
        return this.board.length;
    }
    
    public int boardCols() {
        if (board[0] != null) {
            return board[0].length;
        }
        return 0;
    }
    
    public boolean isOver() {
        return this.gameState == GameState.WON || this.gameState == GameState.LOST; 
    }
    
    public int getFlaggedCount() {
        return this.flaggedCount;
    }
    
    public void setFlaggedCount(int c) {
        this.flaggedCount = c;
    }
    
    public int getUncoveredCount() {
        return this.uncoveredCount;
    }
    
    public void setUncoveredCount(int c) {
        this.uncoveredCount = c;
    }
    
    public int getMineCount() {
        return this.mineCount;
    }
    
    public Tile getTile(Spot s) {
        return this.board[s.getRow()][s.getCol()];
    }
    
    public int getTileSize() {
        return this.tileSize;
    }

    public Level getLevel() {
        return this.level;
    }
    

    //handle a win- send a winning message, check if there is a new high score, and save the board
    public void winGame() {
        this.gameState = GameState.WON;
        this.timer.stop();

        JFrame winFrame = new JFrame();
        winFrame.setLocation(550,350);
            
        JPanel panel = new JPanel();
        JLabel label1 = new JLabel("<html><b>You win!</b></html>");
        label1.setForeground(Color.blue);
        label1.setFont(new Font("Dialog", Font.BOLD, 20));
        label1.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel label2 = new JLabel("<html>Your skills are immaculate.</html>");
        label2.setFont(new Font("Dialog", Font.BOLD, 16));
        label2.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        panel.add(label1, BorderLayout.NORTH);
        panel.add(label2, BorderLayout.SOUTH);
        
        winFrame.add(panel, BorderLayout.CENTER);
        
        winFrame.pack();
        winFrame.setVisible(true);
        
        try {
            this.saveBoard();
            this.updateHighScores(this.level, this.time);
        } catch (IOException error) {
            System.out.println("Internal Error: " + error.getMessage());
        }
    }
    
    //handle a loss- send the losing message, save the board
    public void loseGame() {
        this.gameState = GameState.LOST;
        this.timer.stop();
        try {
            this.saveBoard();
        } catch (IOException error) {
            System.out.println("Internal Error: " + error.getMessage());
        }
        
        JFrame lossFrame = new JFrame();
        lossFrame.setLocation(550,350);
            
        JPanel panel = new JPanel();
        JLabel label1 = new JLabel("<html><b>You hit a mine!</b></html>");
        label1.setForeground(Color.red);
        label1.setFont(new Font("Dialog", Font.BOLD, 20));
        label1.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel label2 = new JLabel("<html>Better luck next time.</html>");
        label2.setFont(new Font("Dialog", Font.BOLD, 16));
        label2.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        panel.add(label1, BorderLayout.NORTH);
        panel.add(label2, BorderLayout.SOUTH);
        
        lossFrame.add(panel, BorderLayout.CENTER);
        
        lossFrame.pack();
        lossFrame.setVisible(true);
        
    }
    
    //sets up the board on the first left click, otherwise the Mine/Number class will respond appropriately
    public void handleLeftClick(int r, int c) {
        if (r >= 0 && r < this.boardRows() && c >= 0  && c < this.boardCols()) {
            if (this.board[r][c] == null) {
                this.setupBoard(r, c);
            }
            Tile clicked = this.board[r][c];
            clicked.handleLeftClick();
        }
    }
    
    //passes right click to Mine/Number class
    public void handleRightClick(int r, int c) {
        if (r >= 0 && r < this.boardRows() && c >= 0  && c < this.boardCols()) {
            Tile clicked = this.board[r][c];
            if (clicked != null) {
                clicked.handleRightClick();
            }
        }
    }
    
    //given a spot, returns a set of neighboring spots (not including itself) 
    public Set<Spot> neighborTiles(Spot s) {
        
        Set<Spot> neighbors = new TreeSet<Spot>();
        
        for (int r = s.getRow() - 1; r <= s.getRow() + 1; r++) {
            for (int c = s.getCol() - 1; c <= s.getCol() + 1; c++) {
                if (r >= 0 && r < this.boardRows() && c >= 0 && c < this.boardCols()) {
                    if (r != s.getRow() || c != s.getCol()) {
                        neighbors.add(new Spot(r, c));
                    }
                }
            }
        }
        
        return neighbors;
    }
    
    //returns the amount of neighboring mines (the number displayed on uncovered Number tiles)
    public int neighborMines(Spot s) {
        int count = 0;
        Set<Spot> neighbors = this.neighborTiles(s);
        for (Spot spot : neighbors) {
            if (this.board[spot.getRow()][spot.getCol()] instanceof Mine) {
                count++;
            }
        }
        return count;
    }
    
    //creates the board- ensures the spot that is clicked first has 0 neighboring mines, randomly places mines in other areas
    public void setupBoard(int r, int c) {
        
        this.gameState = GameState.PLAYING;
        this.timer.start();
        
        Spot firstClick = new Spot(r, c);
        Set<Spot> mineFreeArea = neighborTiles(firstClick);
        mineFreeArea.add(firstClick);
        
        int minesPlaced = 0;
        
        while (minesPlaced < this.mineCount) {
            int randRow = (int)(Math.random() * this.boardRows());
            int randCol = (int)(Math.random() * this.boardCols());
            Spot randSpot = new Spot(randRow, randCol);
            if (this.board[randRow][randCol] == null) {
                if (!mineFreeArea.contains(randSpot)) {
                    this.board[randRow][randCol] = new Mine(this, randSpot);
                    minesPlaced++;
                }
            }
        }
        
        for (int row = 0; row < this.boardRows(); row++) {
            for (int col = 0; col < this.boardCols(); col++) {
                if (this.board[row][col] == null) {
                    this.board[row][col] = new Number(this, new Spot(row, col));
                }
            }
        }

    }
    
    
    //stores the board in the 'recent.txt' file
    public void saveBoard() throws IOException {
        try{
            PrintWriter writer = new PrintWriter("files/recent.txt");
            for (int r = 0; r < this.boardRows(); r++) {
                for (int c = 0; c < this.boardCols(); c++) {
                    writer.print(this.board[r][c].toString());
                }
                writer.println();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
    }
    
    //reads the board in the 'recent.txt' file, and resets the game with that board
    public void readBoard() throws IOException {
        try {
            
            BufferedReader reader = new BufferedReader(new FileReader("files/recent.txt"));
            
            reader.mark(500);
            
            int rows = 0;
            while (reader.readLine() != null) {
                rows++;
            }
            reader.reset();
            
           
            String topRow = reader.readLine();
            int col = topRow.length();
            reader.reset();
            
            Tile[][] newBoard = new Tile[rows][col];
            
            boolean temp = false;
            int r = 0;
            while(!temp) {
                String line = reader.readLine();
                if (line == null)
                    temp = true;
                else {
                    for (int c = 0; c < line.length(); c++) {
                        if (line.charAt(c) == 'm') {
                            newBoard[r][c] = new Mine(this, new Spot(r, c));
                        }
                    }
                    r++;
                }
            }
            
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < col; k++) {
                    if (newBoard[j][k] == null) {
                        newBoard[j][k] = new Number(this, new Spot(j, k));
                    }
                }
            }
            
            reader.close();
            
            
            Level l = null;
            
            if (rows == 8) {
                l = Level.EASY;
            } else if (rows == 14) {
                l = Level.MEDIUM;
            } else if (rows == 20) {
                l = Level.EXPERT;
            }
            
            if (l != null) {
                this.reset(l);
                this.gameState = GameState.PLAYING;
                this.board = newBoard;
                this.timer.start();
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }

    }
    
    //returns an HTML formatted string of the high scores for a given difficulty
    public String highScoresToHTML(Level l) throws IOException {
        
        String filePath = "files/easyhighscores.txt";
        String levelString = "EASY";
        if (l == Level.MEDIUM) {
            filePath = "files/mediumhighscores.txt";
            levelString = "MEDIUM";
        } else if (l == Level.EXPERT) {
            filePath = "files/experthighscores.txt";
            levelString = "EXPERT";
        }
        
        try {
            
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            
            List<String> scorers = new ArrayList<String>();
            List<Integer> scores = new ArrayList<Integer>();
            
            String htmlString = "<html><b>" + levelString + "</b><br><br>";
            
            boolean temp = false;
            int r = 0;
            while(!temp) {
                String line = reader.readLine();
                if (line == null)
                    temp = true;
                else {
                    if (r % 2 == 0) {
                        htmlString += (r/2 + 1) + ". " + line + " - ";
                    } else {
                        htmlString += line + "<br>";
                    }
                    r++;
                }
            }
            htmlString += "</html>";
            
            reader.close();
            return htmlString;
            
        } catch (NoSuchFileException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
        
        return "";
    }
    
    //called when the game is won; determines if there is a new high score and calls the writeHighScores method if there is
    public void updateHighScores(Level l, int score) throws IOException {
        
        String filePath = "files/easyhighscores.txt";
        if (l == Level.MEDIUM) {
            filePath = "files/mediumhighscores.txt";
        } else if (l == Level.EXPERT) {
            filePath = "files/experthighscores.txt";
        }
        
        try {
            
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            
            List<String> scorers = new ArrayList<String>();
            List<Integer> scores = new ArrayList<Integer>();
            
            boolean temp = false;
            int r = 0;
            while(!temp) {
                String line = reader.readLine();
                if (line == null)
                    temp = true;
                else {
                    if (r % 2 == 0) {
                        scorers.add(line);
                    } else {
                        scores.add(Integer.parseInt(line));
                    }
                    r++;
                }
            }
            
            System.out.println(scorers);
            System.out.println(scores);
            
            reader.close();
            
            if (scores.size() < 5 || score < scores.get(scores.size()-1)) {
                String username = "Anonymous";
                
                JPanel highscorePanel = new JPanel(new BorderLayout());
                
                JLabel msg = new JLabel("You got a high score! Enter your name to join the leaderboard!");
                msg.setFont(new Font("Dialog", Font.BOLD, 16));
                
                JTextField textField = new JTextField(10);
                
                highscorePanel.add(msg, BorderLayout.NORTH);
                highscorePanel.add(textField, BorderLayout.SOUTH);
                
                int option = JOptionPane.showConfirmDialog(null, highscorePanel, "Enter Your Name!", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String name = textField.getText();
                        if (name == null)
                            throw new IllegalArgumentException();
                        else
                            username = name;
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(null, "There was an error adding your name.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                int index = 0;
                while (index < scores.size() && score > scores.get(index)) {
                    index++;
                }
                
                scorers.add(index, username);
                scores.add(index, score);
                
                if (scores.size() > 5) {
                    scorers.remove(scorers.size()-1);
                    scores.remove(scores.size()-1);
                }
                
                writeHighScores(filePath, scorers, scores);
                
            }
            
            
        } catch (NoSuchFileException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
        
    }
    
    //writes new high scores to the appropriate file ('easyhighscores.txt' for the Easy level, etc.)
    public void writeHighScores(String filePath, List scorers, List scores) {
        try{
            PrintWriter writer = new PrintWriter(filePath);
            
            for (int i = 0; i < scorers.size(); i++) {
                writer.print(scorers.get(i));
                writer.println();
                writer.print(scores.get(i));
                if (i != scorers.size()-1) {
                    writer.println();
                }
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int r = 0; r < this.boardRows(); r++) {
            for (int c = 0; c < this.boardCols(); c++) {
                Tile t = this.board[r][c];
                if (t == null) {
                    g.setColor(new Color(130, 255, 80));
                    if ((c + r) % 2 == 0) {
                        g.setColor(new Color(51, 255, 51));
                    }
                    g.fillRect(c * this.tileSize, r * this.tileSize, this.tileSize, this.tileSize); 
                } else {
                    t.draw(g);
                }
                
            }
        }
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.boardCols() * this.tileSize, this.boardRows() * this.tileSize);
    }
    
}