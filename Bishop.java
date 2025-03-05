import java.awt.Color;
import java.util.ArrayList;

public class Bishop extends ChessPiece {

    public Bishop (Color color){
        super(color, "B", "♝");
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

        // Bishops can move diagonally in any direction.
        return deltaX == deltaY;
    }

    /**
     * Check if move is valid when origin and destination tiles are on different boards
     * @param deltaBoards int - Difference of boards between origin and destination tiles (0 to 2)
     * @param fromTile Tile that the piece is moving from
     * @param toTile Tile that the piece is moving to
     * @param lastMove last move made - not used here
     * @return true if move is valid
     */

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile, Move lastMove) {
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        if(deltaBoards == 0){
            return isMoveValid(fromTile, toTile);
        } else {
            return (deltaX == deltaY) && (deltaX == deltaBoards);
        }
    }

    @Override
    public ChessPiece clone() {
        return (Bishop) super.clone();

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
