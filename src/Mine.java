
import java.awt.Color;
import java.awt.Graphics;

//subclass of Tile, unique draw function and response to left click
public class Mine extends Tile implements Comparable<Tile> {
    
    public Mine(GameBoard board, Spot spot) {
        super(board, spot);
    }
    
    //uncovers the mine and ends the game on a loss
    @Override
    public void handleLeftClick() {
        
        if (this.board.isOver())
            return;
        
        this.uncover();
        this.board.loseGame();
        
        this.paint();
        
    }
    
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
            g.setColor(Color.red);
            g.fillRect(x, y, tileSize, tileSize);
            g.setColor(Color.black);
            g.fillOval(x + tileSize/4, y + tileSize/4, tileSize/2, tileSize/2);
            g.setColor(Color.white);
            g.fillOval(x + tileSize/4 + tileSize/8, y + tileSize/4 + tileSize/8, 4, 4);
        }
    }
    
    public String toString() {
        return "m";
    }

}
