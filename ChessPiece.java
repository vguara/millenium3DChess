import java.awt.Color;

public abstract class ChessPiece {

    public static final Color BLACK = Color.BLACK;
    public static final Color WHITE = Color.WHITE;
    private Color color;
    protected boolean firstMove;

    protected String letter;

    public ChessPiece(Color color, String letterName) {
        this.color = color;
        firstMove = true;
        letter = letterName;
    }

    // Abstract method to validate if a move is legal for this piece
    public abstract boolean isMoveValid(Tile fromTile, Tile toTile);

    public abstract boolean is3DMoveValid (int deltaBoards, Tile fromTile, Tile toTile);

    public abstract String getLetter();

    // Getter methods for color and name
    public Color getColor() {
        return color;
    }

}
