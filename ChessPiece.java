import java.awt.*;
import java.util.ArrayList;

public abstract class ChessPiece implements Cloneable {

    public static final Color BLACK = Color.BLACK;
    public static final Color WHITE = Color.WHITE;
    private Color color;
    protected boolean firstMove;

    protected String letter;

    protected String pieceChar;

    public ChessPiece(Color color, String letterName, String pieceChar) {
        this.color = color;
        firstMove = true;
        letter = letterName;
        this.pieceChar = pieceChar;
    }

    @Override
    public ChessPiece clone() {
        try {
            ChessPiece clonedPiece = (ChessPiece) super.clone();
            return clonedPiece;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }

        // Abstract method to validate if a move is legal for this piece
    public abstract boolean isMoveValid(Tile fromTile, Tile toTile);

    public abstract boolean is3DMoveValid (int deltaBoards, Tile fromTile, Tile toTile, Move lastMove);

    public boolean isFirstMove() {return firstMove;}

    public void flipFirstMove() {firstMove = !firstMove;}

    public abstract String getLetter();

    public String getPieceChar(){
        return pieceChar;
    };

    public abstract boolean canPromote(int row);

    @Override
    public String toString(){
        return (getColor() == WHITE ? " Wh " : " Bl ") + getLetter();
    }

    // Getter methods for color and name
    public Color getColor() {
        return color;
    }

}
