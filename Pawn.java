import java.awt.Color;

public class Pawn extends ChessPiece {


    public Pawn(Color color) {
        super(color, "P");
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

        if (pieceColor == Color.WHITE) {
            // White pawns can move forward by one square or two squares on their first move
            if (firstMove) {
                return (deltaX == 0 && deltaY == 1) || (deltaX == 0 && deltaY == 2);
            } else {
                // On subsequent moves, white pawns can only move forward by one square
                return deltaX == 0 && deltaY == 1;
            }
        } else if (pieceColor == Color.BLACK) {
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

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile) {

        int deltaY = fromTile.getRow() - toTile.getRow(); //can't be abs because white and black move in opposite directions
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        //pawn can only move 2 steps in the first turn
        if (!firstMove && deltaBoards == 2){return false;}

        ChessPiece piece = fromTile.getPiece();

        Color pieceColor = piece.getColor();

        if(deltaBoards == 0){
            return isMoveValid(fromTile, toTile);
        } else {
            if (pieceColor == Color.WHITE){
                return deltaX == 0 && deltaY == deltaBoards;
            } else if(pieceColor == Color.BLACK) {
                return deltaX == 0 && deltaY == -deltaBoards;
            }
        }
        return false;
    }




    @Override
    public String getLetter() {
        return letter;
    }
}
