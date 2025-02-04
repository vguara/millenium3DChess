import java.awt.Color;
import java.util.ArrayList;

public class Knight extends ChessPiece {

    public Knight (Color color){
        super(color, "H");
    }

    public boolean isMoveValid(Tile fromTile, Tile toTile) {
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());

        // Knights can move in an "L" shape: two squares in one direction and one square in the other.
        return (deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2);
    }

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile, Move lastMove) {
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());

        if (deltaBoards == 0) {
            return isMoveValid(fromTile, toTile);
        } else if (Math.abs(deltaBoards) == 1){
            boolean horizontal = (deltaX == 2 && deltaY == 0);
            boolean vertical = (deltaX == 0 & deltaY == 2);
            return horizontal || vertical;

        } else {
            boolean horizontal = (deltaX == 1 && deltaY == 0);
            boolean vertical = (deltaX == 0 & deltaY == 1);
            return horizontal || vertical;
        }
    }
    public String getLetter() {
        return letter;
    }

}
