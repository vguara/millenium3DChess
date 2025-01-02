import java.awt.Color;

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

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile) {

        return isMoveValid(fromTile, toTile);

    }
    public String getLetter() {
        return letter;
    }

}
