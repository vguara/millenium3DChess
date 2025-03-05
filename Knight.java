import java.awt.Color;
import java.util.ArrayList;

public class Knight extends ChessPiece {

    public Knight (Color color){
        super(color, "N", "♞");
    }

    /**
     * Check if move is valid when both origin and destination tile are on the same board
     * @param fromTile Tile that the piece is moving from
     * @param toTile Tile that the piece is moving to
     * @return true if move is valid
     */
    public boolean isMoveValid(Tile fromTile, Tile toTile) {
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());

        // Knights can move in an "L" shape: two squares in one direction and one square in the other.
        return (deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2);
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

    @Override
    public ChessPiece clone() {
        return (Knight) super.clone();

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
