import java.awt.Color;
import java.util.ArrayList;

public class Queen extends ChessPiece{

    public Queen (Color color){
        super(color, "Q", "♛");
    }

    /**
     * Check if move is valid when both origin and destination tile are on the same board
     * @param fromTile Tile that the piece is moving from
     * @param toTile Tile that the piece is moving to
     * @return true if move is valid
     */
    @Override
    public boolean isMoveValid(Tile fromTile, Tile toTile) {
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());

        // Queens can move horizontally, vertically, or diagonally in any direction.
        return deltaX == deltaY || deltaX == 0 || deltaY == 0;
    }

    /**
     * Check if move is valid when origin and destination tiles are on different boards
     * @param deltaBoards int - Difference of boards between origin and destination tiles (0 to 2)
     * @param fromTile Tile that the piece is moving from
     * @param toTile Tile that the piece is moving to
     * @param lastMove last move made - not used here
     * @return true if move is valid
     */
    @Override
    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile, Move lastMove) {
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        if(deltaBoards == 0){
            return isMoveValid(fromTile, toTile);
        } else {
            boolean diagonal = (deltaX == deltaY && deltaX == deltaBoards);
            boolean horOrVert = ((deltaX == 0 & deltaY == deltaBoards) || (deltaY == 0 && deltaX == deltaBoards));
            boolean upwards = (deltaX == 0 && deltaY == 0);
            return diagonal || horOrVert || upwards;
        }
    }

    @Override
    public ChessPiece clone() {
        return (Queen) super.clone();

    }

    /**
     * Check if the piece can be promoted
     * @param row the row the piece is in
     * @return if the piece can be promoted.
     */
    @Override
    public boolean canPromote(int row) {
        return false;
    }

    public String getLetter() {
        return letter;
    }
}
