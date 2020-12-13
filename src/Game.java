
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Game implements Runnable {
    
    String currentLevel = "Easy";
    
    public void run() {
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(500, 200);
        
        Font font = new Font("Courier", Font.BOLD, 20);
        final JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 126, 30));
        topPanel.setLayout(new BorderLayout());
        
        final JPanel topWestPanel = new JPanel();
        topWestPanel.setBackground(new Color(30, 126, 30));
        topWestPanel.setLayout(new BorderLayout());
        
        //dropdown for levels
        String levels[] = {"Easy", "Medium", "Expert"};
        final JComboBox level = new JComboBox(levels);
        level.setBorder(new EmptyBorder(10, 5, 10, 5));
        level.setFont(new Font("Dialog", Font.BOLD, 16));
        topWestPanel.add(level, BorderLayout.WEST);
        
        final JPanel topCenterPanel = new JPanel();
        topCenterPanel.setBackground(new Color(30, 126, 30));
        
        //label showing how many flags left
        final JLabel flagStatus = new JLabel("10");
        flagStatus.setBorder(new EmptyBorder(10, 5, 10, 5));
        flagStatus.setFont(font);
        flagStatus.setForeground(Color.white);        
        flagStatus.setIcon(new ImageIcon(getImage("images/flag.png")));
        topCenterPanel.add(flagStatus);

        //label showing how much time has elapsed since the game started
        final JLabel timeStatus = new JLabel("0");
        timeStatus.setBorder(new EmptyBorder(10, 5, 10, 5));
        timeStatus.setFont(font);
        timeStatus.setForeground(Color.white);
        timeStatus.setIcon(new ImageIcon(getImage("images/timer.png")));
        topCenterPanel.add(timeStatus);
        
        final JPanel boardPanel = new JPanel();
        boardPanel.setBackground(new Color(30, 30, 30));
        final GameBoard board = new GameBoard(GameBoard.Level.EASY, flagStatus, timeStatus);
        boardPanel.add(board);
        
        //change the board whenever the levels dropdown is changed
        level.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!level.getSelectedItem().equals(currentLevel)) {
                    if (level.getSelectedItem().equals("Easy")) {
                        board.reset(GameBoard.Level.EASY);
                        frame.setLocation(500, 200);
                        currentLevel = "Easy";
                    } else if (level.getSelectedItem().equals("Medium")) {
                        board.reset(GameBoard.Level.MEDIUM);
                        frame.setLocation(400, 150);
                        currentLevel = "Medium";
                    } else if (level.getSelectedItem().equals("Expert")) {
                        board.reset(GameBoard.Level.EXPERT);
                        frame.setLocation(350, 100);
                        currentLevel = "Expert";
                    }
                }
                frame.pack();
            }
        });
        
        final JPanel topEastPanel = new JPanel();
        topEastPanel.setBackground(new Color(30, 126, 30));
        
        //button to create a new board
        final JButton resetButton = new JButton();
        resetButton.setIcon(new ImageIcon(getImage("images/reset.png")));
        resetButton.setBorderPainted(false);
        resetButton.setBorder(new EmptyBorder(10, 3, 10, 3));
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentLevel.equals("Easy")) {
                    board.reset(GameBoard.Level.EASY);
                } else if (currentLevel.equals("Medium")) {
                    board.reset(GameBoard.Level.MEDIUM);
                } else if (currentLevel.equals("Expert")) {
                    board.reset(GameBoard.Level.EXPERT);
                }
                frame.pack();
            }
        });
        topEastPanel.add(resetButton);
        
        //button to replay the same board as the last loss/win
        final JButton replayButton = new JButton();
        replayButton.setIcon(new ImageIcon(getImage("images/replay.png")));
        replayButton.setBorderPainted(false);
        replayButton.setBorder(new EmptyBorder(10, 3, 10, 3));
        replayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    board.readBoard();
                    if (board.getLevel() == GameBoard.Level.EASY) {
                        currentLevel = "Easy";
                        level.setSelectedIndex(0);
                    } else if (board.getLevel() == GameBoard.Level.MEDIUM) {
                        currentLevel = "Medium";
                        level.setSelectedIndex(1);
                    } else if (board.getLevel() == GameBoard.Level.EXPERT) {
                        currentLevel = "Expert";
                        level.setSelectedIndex(2);
                    }
                } catch (IOException error) {
                    System.out.println("Internal Error: " + error.getMessage());
                }
                frame.pack();
            }
        });
        topEastPanel.add(replayButton);
        
        //button to show leaderboard
        final JButton highscoreButton = new JButton();
        highscoreButton.setIcon(new ImageIcon(getImage("images/highscore.png")));
        highscoreButton.setBorderPainted(false);
        highscoreButton.setBorder(new EmptyBorder(10, 3, 10, 3));
        highscoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFrame highscoresFrame = new JFrame("Highscores");
                highscoresFrame.setLocation(350, 200);
                
                final JPanel panel = new JPanel();
                final JPanel easyPanel = new JPanel();
                final JPanel mediumPanel = new JPanel();
                final JPanel expertPanel = new JPanel();
                
                panel.setLayout(new BorderLayout());
                easyPanel.setLayout(new BorderLayout());
                mediumPanel.setLayout(new BorderLayout());
                expertPanel.setLayout(new BorderLayout());
                
                JLabel easyHighscores, mediumHighscores, expertHighscores;
                try {
                    easyHighscores = new JLabel(board.highScoresToHTML(GameBoard.Level.EASY));
                    mediumHighscores = new JLabel(board.highScoresToHTML(GameBoard.Level.MEDIUM));
                    expertHighscores = new JLabel(board.highScoresToHTML(GameBoard.Level.EXPERT));
                    
                    easyHighscores.setFont(font);
                    mediumHighscores.setFont(font);
                    expertHighscores.setFont(font);
                    
                    easyHighscores.setBorder(new EmptyBorder(5, 10, 5, 10));
                    mediumHighscores.setBorder(new EmptyBorder(5, 10, 5, 10));
                    expertHighscores.setBorder(new EmptyBorder(5, 10, 5, 10));
                    
                    easyPanel.add(easyHighscores, BorderLayout.NORTH);
                    mediumPanel.add(mediumHighscores, BorderLayout.NORTH);
                    expertPanel.add(expertHighscores, BorderLayout.NORTH);
                } catch (IOException error) {
                    System.out.println("Internal Error: " + error.getMessage());
                }
                
                panel.add(easyPanel, BorderLayout.WEST);
                panel.add(mediumPanel, BorderLayout.CENTER);
                panel.add(expertPanel, BorderLayout.EAST);
                
                highscoresFrame.add(panel, BorderLayout.CENTER);
                
                highscoresFrame.pack();
                highscoresFrame.setVisible(true);
            }
        });
        topEastPanel.add(highscoreButton);
        
        //button to see instructions
        final JButton instructionsButton = new JButton();
        instructionsButton.setIcon(new ImageIcon(getImage("images/instructions.png")));
        instructionsButton.setBorderPainted(false);
        instructionsButton.setBorder(new EmptyBorder(10, 3, 10, 3));
        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFrame instructionFrame = new JFrame("Instructions");
                instructionFrame.setLocation(400,75);
                
                final JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                
                final JLabel instructions = new JLabel("<html>"
                        + "<b>MINESWEEPER INSTRUCTIONS</b><br><br><br>"
                        + "The goal of the game is to uncover every tile that is NOT a mine as fast as possible. "
                        + "When you uncover a tile, it will show a number- or a mine :(. "
                        + "This number represents the amount of mines in the neighboring tiles. "
                        + "If there is no number, there are no mines in the neighboring tiles. "
                        + "Use this information in order to deduce which tiles are hiding mines and which are not. "
                        + "If you suspect a tile is hiding a mine, make sure to flag it so you don't forget! "
                        + "<br><br>"
                        + "<b>Controls:</b><br>"
                        + "Left-click: Uncover a tile.<br>"
                        + "Right-click: Flag a covered tile."
                        + "<br><br>"
                        + "<b>Features:</b><br>"
                        + "In the top left of the window, there is a dropdown for you to select a level (the size of the minefield). "
                        + "If you aren't familiar with the game, perhaps start on the Easy level. <br>"
                        + "In the center of the top of the window, you can see how much time you have taken and how many mines you have flagged. "
                        + "The amount of flags is the same as the amount of mines. <br>"
                        + "In the top right of the window, there is a reset button (which will create a new board) and "
                        + "a rewind button (which will replay the same board as your last loss/win"
                        + " in case you want to give it another shot or challenge a friend to beat your time). "
                        + " There is also a button to see the high scores (the trophy button). "
                        + "And, of course, all the way on the right is the Instructions button, but you knew that :). "
                        + "<br><br><b>Good luck! I'll see you on the leaderboards!</b>"
                        + "</html>");
                        
                instructions.setBorder(new EmptyBorder(10,10,10,10));
                instructions.setFont(new Font("Courier", Font.PLAIN, 20));
                
                panel.add(instructions, BorderLayout.CENTER);
                instructionFrame.add(panel, BorderLayout.CENTER);
                
                instructionFrame.pack();
                
                instructionFrame.setSize(800, 750);;
                
                instructionFrame.setVisible(true);
            }
        });
        topEastPanel.add(instructionsButton);
        
        topPanel.add(topEastPanel, BorderLayout.EAST);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        topPanel.add(topWestPanel, BorderLayout.WEST);
        
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
    
    public Image getImage (String imageName) {
        Image image = null;
        try {
            if (image == null) {
                image = ImageIO.read(new File(imageName));
            }
        } catch (IOException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
        return image;
    }
    
}

