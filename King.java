import java.awt.Color;
public class King extends ChessPiece{

    public King (Color color){
        super(color, "K");
    }

    @Override
    public boolean isMoveValid(Tile fromTile, Tile toTile) {
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());

        // Kings can move one square in any direction (horizontally, vertically, or diagonally).
        return deltaX <= 1 && deltaY <= 1;
    }

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile) {
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        if(deltaBoards == 0){
            return isMoveValid(fromTile, toTile);
        } else {
            return (deltaX <= deltaBoards) && (deltaY <= deltaBoards) && (deltaBoards != 2);
        }
    }

    public String getLetter() {
        return letter;
    }
}
