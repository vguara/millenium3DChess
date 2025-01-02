import java.awt.Color;

public class Queen extends ChessPiece{

    public Queen (Color color){
        super(color, "Q");
    }

    @Override
    public boolean isMoveValid(Tile fromTile, Tile toTile) {
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());

        // Queens can move horizontally, vertically, or diagonally in any direction.
        return deltaX == deltaY || deltaX == 0 || deltaY == 0;
    }

    @Override
    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile) {
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        if(deltaBoards == 0){
            return isMoveValid(fromTile, toTile);
        } else {
            boolean diagonal = (deltaX == deltaY && deltaX == deltaBoards);
            boolean horOrVert = ((deltaX == 0 & deltaY == deltaBoards) || (deltaY == 0 && deltaX == deltaBoards));
            return diagonal || horOrVert;
        }
    }

    public String getLetter() {
        return letter;
    }
}
