import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.*;

//subclass of Tile, shows the number of neighboring tiles with mines (if the number is 0, it is blank)
public class Number extends Tile implements Comparable<Tile> {

    //number of neighboring mines
    private int mineNumber;
    
    public Number(GameBoard board, Spot spot) {
        super(board, spot);
        this.mineNumber = board.neighborMines(spot);
    }
    
    /*uncovers the tile;
     * if the tile has 0 neighboring mines, each of the neighboring tiles will also be left clicked.
     * if any of the neighboring tiles has 0 neighboring mines, then each of its neighboring tiles will also be left clicked, and so on.
     * This process is done RECURSIVELY.
     * Finally, checks if the game is won (every tile that is not a mine has been uncovered). 
     */
    @Override
    public void handleLeftClick() {
        
        if (this.board.isOver() || this.getTileState() == TileState.UNCOVERED) {
            return;
        }
        
        this.uncover();
        int uncovered = this.board.getUncoveredCount();
        this.board.setUncoveredCount(uncovered + 1);
        
        if (this.mineNumber == 0) {
            Set<Spot> neighbors = this.board.neighborTiles(this.getSpot());
            for (Spot s : neighbors ) {
                Tile t = this.board.getTile(s);
                t.handleLeftClick();
            }
        }
        
        if (this.board.getUncoveredCount() + this.board.getMineCount() == this.board.boardRows() * this.board.boardCols()) {
            this.board.winGame();
        }
        
        this.paint();
        
    }
    
    public int getNum() {
        return this.mineNumber;
    }
    
    //returns the color associated with the number to be displayed on the tile
    public Color colorNum(int n) {
        Color c = Color.black;
        if (n == 1) {
            c = new Color(0,0,253);
        } else if (n == 2) {
            c = new Color(1,126,0);
        } else if (n == 3) {
            c = new Color(254,0,1);
        } else if (n == 4) {
            c = new Color(1,1,128);
        } else if (n == 5) {
            c = new Color(127,3,0);
        } else if (n == 6) {
            c = new Color(0,129,128);
        } else if (n == 7) {
            c = new Color(0,0,0);
        } else if (n == 8) {
            c = new Color(128,128,128);
        }
        return c;
    }

    //handle drawing of covered, flagged, and uncovered Number tiles
    @Override
    public void draw(Graphics g) {
        
        Spot spot = this.getSpot();
        int tileSize = board.getTileSize();
        int x = spot.getCol() * tileSize;
        int y = spot.getRow() * tileSize;
        
        if (this.getTileState() == TileState.COVERED) {
            g.setColor(new Color(130, 255, 80));
            if ((spot.getCol() + spot.getRow()) % 2 == 0) {
                g.setColor(new Color(51, 255, 51));
            }
            g.fillRect(x, y, tileSize, tileSize); 
        } else if (this.getTileState() == TileState.FLAGGED) {
            g.drawImage(this.getImage("images/flagbig.png"), x + 5, y + 5, tileSize - 10, tileSize - 10, null);
        } else {
            g.setColor(new Color(235, 194, 156));
            if ((spot.getCol() + spot.getRow()) % 2 == 0) {
                g.setColor(new Color(220, 184, 150));
            }
            g.fillRect(x, y, tileSize, tileSize); 
            g.setColor(colorNum(this.mineNumber));
            if (this.mineNumber != 0) {
                g.setFont(new Font("Monospaced", Font.BOLD, 25));
                
                int xShift = 17;
                int yShift = 33;
                
                if (tileSize == 40) {
                    xShift = 13;
                    yShift = 29;
                } else if (tileSize == 32) {
                    xShift = 9;
                    yShift = 25;
                }
                
                g.drawString("" + this.mineNumber, x + xShift, y + yShift);
            }
        }
    }
    
    public String toString() {
        return "n";
    }

}