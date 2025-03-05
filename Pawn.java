import java.awt.Color;
import java.util.ArrayList;

public class Pawn extends ChessPiece {

    public Pawn(Color color) {
        super(color, "P", "♟");

    }

    /**
     * Checks if the all en passant conditions are filled to allow en Passant move
     * @param lastMove last move made - There are conditions related to last move
     * @param fromTile Tile the Pawn is moving from
     * @param toTile Tile the Pawn is moving to
     * @return true if all en passant conditions are valid
     */
    public boolean enPassantCondition(Move lastMove, Tile fromTile, Tile toTile){
        boolean pawnMove = lastMove.getMovedPiece() instanceof Pawn;
        boolean oppositeColor = lastMove.getMovedPiece().getColor() != super.getColor();
        boolean moved2 = verifyMove2(lastMove.getFromTile(), lastMove.getToTile());
        boolean sameRow = fromTile.getRow() == lastMove.getToTile().getRow();
        boolean nextCol = Math.abs(fromTile.getCol() - lastMove.getToTile().getCol()) == 1;
        boolean goingToNextCol = toTile.getCol() == lastMove.getToTile().getCol();
        boolean isWhite = lastMove.getPlayer().getColor() == Color.WHITE;
        boolean correctRow;
        if (isWhite){
            correctRow = (fromTile.getRow() - toTile.getRow()) == -1;
        } else {
            correctRow = (fromTile.getRow() - toTile.getRow()) == 1;
        }
        return pawnMove && oppositeColor && moved2 && sameRow && nextCol && goingToNextCol && correctRow;
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
        int deltaY = fromTile.getRow() - toTile.getRow(); // Positive for white, negative for black

        // Check if there is a piece on the fromTile
        ChessPiece piece = fromTile.getPiece();
        if (piece == null) {
            return false;
        }

        // Check the color of the pawn
        Color pieceColor = piece.getColor();

        // check if there is a piece of the opposite color on toTile
        boolean opposingPiece = (toTile.getPiece() != null && toTile.getPiece().getColor() != pieceColor);

        if (pieceColor == Color.WHITE) {
            // check if piece is being taken
            if (opposingPiece){
                return (deltaX == 1 && deltaY == 1);
            }
            // White pawns can move forward by one square or two squares on their first move
            if (firstMove) {
                return (deltaX == 0 && deltaY == 1) || (deltaX == 0 && deltaY == 2);
            } else {
                // On subsequent moves, white pawns can only move forward by one square
                return deltaX == 0 && deltaY == 1;
            }
        } else if (pieceColor == Color.BLACK) {

            if (opposingPiece){
                return (deltaX == 1 && deltaY == -1);
            }
            // Black pawns can move forward by one square or two squares on their first move
            if (firstMove) {
                return (deltaX == 0 && deltaY == -1) || (deltaX == 0 && deltaY == -2);
            } else {
                // On subsequent moves, black pawns can only move forward by one square
                return deltaX == 0 && deltaY == -1;
            }
        }

        return false; // Unknown color, not a valid move
    }


    boolean checkEnPassant(Tile fromTile, Tile toTile, Move lastMove){
        if (lastMove == null){
            return false;
        }
        return enPassantCondition(lastMove, fromTile, toTile);

    }


    /**
     * Check if move is valid when origin and destination tiles are on different boards
     * @param deltaBoards int - Difference of boards between origin and destination tiles (0 to 2)
     * @param fromTile Tile that the piece is moving from
     * @param toTile Tile that the piece is moving to
     * @param lastMove last move made - used to check for En Passant condition
     * @return true if move is valid
     */
    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile, Move lastMove) {

        int deltaY = fromTile.getRow() - toTile.getRow(); //can't be abs because white and black move in opposite directions
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        //pawn can only move 2 steps in the first turn
        if (!firstMove && (deltaBoards == 2 || Math.abs(deltaY) == 2)){return false;}

        ChessPiece piece = fromTile.getPiece();

        Color pieceColor = piece.getColor();

        boolean opposingPiece = (toTile.getPiece() != null && toTile.getPiece().getColor() != pieceColor);

        if(deltaBoards == 0){
            boolean possibleEnPassant = checkEnPassant(fromTile, toTile, lastMove);
            if (possibleEnPassant) {
                return true;
            }
            return isMoveValid(fromTile, toTile);
        } else {
            if (pieceColor == Color.WHITE){
                if (opposingPiece){
                  return (Math.abs(deltaX) == 1 && deltaY == 1 && Math.abs(deltaBoards) == 1);
                }
                boolean vertical = (deltaX == 0 && deltaY == deltaBoards);
                boolean upwards = (deltaX == 0 && deltaY == 0);

                return vertical || upwards;
            } else if(pieceColor == Color.BLACK) {
                if (opposingPiece){
                    return (Math.abs(deltaX) == 1 && deltaY == -1 && Math.abs(deltaBoards) == 1);
                }
                boolean vertical = (deltaX == 0 && deltaY == -deltaBoards);
                boolean upwards = (deltaX == 0 && deltaY == 0);

                return vertical || upwards;
            }
        }
        return false;
    }

    /**
     * Checks if the pawn moved 2 tiles, used to check for en passant.
     * Only allowed if it is the pawn first move, but it is optional.
     * @param fromTile origin tile
     * @param toTile destination tile
     * @return true if pawn moved 2 tiles
     */

    public boolean verifyMove2 (Tile fromTile, Tile toTile){
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        return deltaY == 2;
    }

    @Override
    public ChessPiece clone() {
        return (Pawn) super.clone();

    }

    /**
     * Check if the piece can be promoted
     * @param row the row the piece is in
     * @return if the piece can be promoted.
     */
    @Override
    public boolean canPromote(int row) {
        if (super.getColor() == WHITE && row == 0){
            return true;
        } else return super.getColor() == BLACK && row == 7;
    }

    @Override
    public String getLetter() {
        return letter;
    }
}
