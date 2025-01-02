import javax.swing.*;

public class Tile {
    private int row;
    private int col;
    //private ChessPiece piece;
    private ChessPiece piece;


    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null; // Initially, the tile is empty.
    }

    public boolean isOccupied() {
        return piece != null;
    }

    public ChessPiece getPiece() {
        return piece;
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
