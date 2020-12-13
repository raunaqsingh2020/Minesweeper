
//represents a spot in a 2D array; essentially fulfills the role of a (row, column) tuple.
public class Spot implements Comparable<Spot> {
    private final int row;
    private final int col;
    
    public Spot(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    @Override
    public int compareTo(Spot spot) {
        if (this.row == spot.getRow() && this.col == spot.getCol()) {
            return 0;
        } else if (this.col + this.row > spot.getRow() + spot.getCol()) {
            return 1;
        }
        return -1;
    }
    
    @Override
    public boolean equals(Object o) {
        boolean temp = false;
        if (o instanceof Spot) {
            Spot that = (Spot) o;
            temp = this.getRow() == that.getRow() && this.getCol() == that.getCol(); 
        }
        return temp;
    }
    
}
