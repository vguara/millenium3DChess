import javax.swing.*;

public class Tile {
    private final int row;
    private final int col;
    //private ChessPiece piece;
    private ChessPiece piece;

    //highlight for moves
    private boolean highlight;


    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null; // Initially, the tile is empty.
        this.highlight = false;

    }

    public boolean isOccupied() {
        return piece != null;
    }

    public void setHighlight(boolean mode){
        this.highlight = mode;
    }

    public boolean getHighlight(){
        return this.highlight;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    @Override
    public String toString(){
        return "Tile row: " + this.row + " col: " + this.col;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }
    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

}
