import java.awt.Color;
import java.util.ArrayList;

public class Bishop extends ChessPiece {

    public Bishop (Color color){
        super(color, "B");
    }


    @Override
    public boolean isMoveValid(Tile fromTile, Tile toTile) {
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());

        // Bishops can move diagonally in any direction.
        return deltaX == deltaY;
    }

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile, Move lastMove) {
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        if(deltaBoards == 0){
            return isMoveValid(fromTile, toTile);
        } else {
            return (deltaX == deltaY) && (deltaX == deltaBoards);
        }
    }

    public String getLetter() {
        return letter;
    }
}
