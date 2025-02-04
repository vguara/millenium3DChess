import java.awt.*;

public class Player {

    public static final Color BLACK = Color.BLACK;
    public static final Color WHITE = Color.WHITE;

    private Board kingBoard;

    private Tile kingTile;

    private Color color;

    private boolean currentTurn;

    //Constructor for the player defines the color.
    public Player(Color color){
        this.color = color;
        this.currentTurn = (color == WHITE);
    }

    //returns if it is this players current turn
    public boolean isCurrentTurn() {
        return currentTurn;
    }

    public void setKingLocation(Board board, Tile tile) {
        kingBoard = board;
        kingTile = tile;
    }

    public Board getKingBoard() {
        return kingBoard;
    }

    public Tile getKingTile() {
        return kingTile;
    }

    //changes turn
    public void flipTurn(){
        currentTurn = !currentTurn;
    }



    //returns player's color
    public Color getColor() {
        return color;
    }
}
