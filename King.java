import java.awt.Color;
import java.util.ArrayList;

public class King extends ChessPiece{

    public King (Color color){
        super(color, "K", "♚");
    }

    /**
     * Check if castling is possible
     * @param fromTile tile the King is moving from
     * @param toTile tile the King is moving to
     * @param lastMove last moved made - used to get the board the piece is in to find the rooks
     * @return if castling is possible.
     */
    public boolean castlingPossible (Tile fromTile, Tile toTile, Move lastMove){
        int deltaX = fromTile.getCol() - toTile.getCol();
        if (!this.firstMove || Math.abs(deltaX) != 2 || fromTile.getRow() != toTile.getRow() || lastMove == null){
            return false;
        }
        boolean kingSide = false;
        int rookDist = 0;


        if (deltaX == 2){
            kingSide = true;
            rookDist = 3;
        } else if (deltaX == -2){
            rookDist = -4;
        } else {
            return false;
        }

        Board board = lastMove.getMainBoard().getBoardOfAPiece(this);

        ChessPiece rook = board.getTile(fromTile.getRow(), fromTile.getCol()+rookDist).getPiece();


        if (!(rook instanceof Rook) || !rook.firstMove){
            return false;
        }

        //check for threatened tiles between king and rook.
        int step = kingSide ? 1 : -1;
        boolean tileThreatened = false;
        int row = fromTile.getRow();
        int initCol = fromTile.getCol();
        for (int i = initCol; (kingSide ? i <= 6 : i >= 2); i +=step){
            tileThreatened = lastMove.getMainBoard().tileThreatened(
                    board,
                    board.getTile(row, i),
                    this.getColor(),
                    lastMove);

            if (tileThreatened) return false;
        }
        //Extra check to see if
        if (!kingSide && board.getTile(row, toTile.getCol()-1).getPiece() != null){
            return false;
        }

        return true;


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


        // Kings can move one square in any direction (horizontally, vertically, or diagonally).
        return deltaX <= 1 && deltaY <= 1;
    }

    /**
     * Check if move is valid when origin and destination tiles are on different boards
     * @param deltaBoards int - Difference of boards between origin and destination tiles (0 to 2)
     * @param fromTile Tile that the piece is moving from
     * @param toTile Tile that the piece is moving to
     * @param lastMove last move made - used to check if castling is possible
     * @return true if move is valid
     */

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile, Move lastMove) {


        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        if(deltaBoards == 0){
            //castling check
            if (castlingPossible(fromTile, toTile, lastMove)) return true;
            return isMoveValid(fromTile, toTile);
        } else {
            return (deltaX <= deltaBoards) && (deltaY <= deltaBoards) && (deltaBoards != 2);
        }
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

    @Override
    public ChessPiece clone() {
        return (King) super.clone();
    }

    public String getLetter() {
        return letter;
    }
}
