import java.awt.Color;
import java.util.ArrayList;

public class Pawn extends ChessPiece {

    public Pawn(Color color) {
        super(color, "P");

    }

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

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile, Move lastMove) {

        int deltaY = fromTile.getRow() - toTile.getRow(); //can't be abs because white and black move in opposite directions
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        //pawn can only move 2 steps in the first turn
        if (!firstMove && (deltaBoards == 2 || Math.abs(deltaY) == 2)){return false;}

        ChessPiece piece = fromTile.getPiece();

        Color pieceColor = piece.getColor();

        boolean opposingPiece = (toTile.getPiece() != null && toTile.getPiece().getColor() != pieceColor);

        System.out.println("Opposing piece? " + opposingPiece);

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

    public boolean verifyMove2 (Tile fromTile, Tile toTile){
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        return deltaY == 2;
    }

    @Override
    public String getLetter() {
        return letter;
    }
}
