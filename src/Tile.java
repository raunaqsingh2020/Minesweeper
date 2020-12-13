import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


//abstract class, superclass of 'Mine' and 'Number'; represents each spot on the board
public abstract class Tile implements Comparable<Tile> {
    
    protected GameBoard board;
    private Spot spot;
    
    public enum TileState {COVERED, UNCOVERED, FLAGGED};
    private TileState tileState;
    
    public Tile(GameBoard board, Spot spot) {
        this.tileState = TileState.COVERED;
        this.board = board;
        this.spot = spot;
    }
    
    //implemented by subclasses
    public abstract void handleLeftClick();
    
    //flags or removes flag from tile
    public void handleRightClick() {
        if (this.board.isOver() || this.tileState == TileState.UNCOVERED)
            return;
        
        if (this.tileState == TileState.COVERED) {
            this.tileState = TileState.FLAGGED;
            int flagged = this.board.getFlaggedCount();
            this.board.setFlaggedCount(flagged + 1);
            this.board.updateStatus();
        }
        else if (this.tileState == TileState.FLAGGED) {
            this.tileState = TileState.COVERED;
            int flagged = this.board.getFlaggedCount();
            this.board.setFlaggedCount(flagged - 1);
            this.board.updateStatus();
        }
        
        this.paint();
    }
    
    public Image getImage(String imageName) {
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
    
    public Spot getSpot() {
        return this.spot;
    }
    
    public TileState getTileState() {
        return this.tileState;
    }
    
    public void uncover() {
        this.tileState = TileState.UNCOVERED;
    }
    
    public int compareTo(Tile t) {
        return (this.spot).compareTo(t.getSpot());
    }

    abstract public void draw(Graphics g);
  
    public void paint() {
      Graphics g = board.getGraphics();
      this.draw(g);
    }
    
    
}