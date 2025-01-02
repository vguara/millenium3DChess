import java.awt.*;

public class Player {

    public static final Color BLACK = Color.BLACK;
    public static final Color WHITE = Color.WHITE;

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

    //changes turn
    public void flipTurn(){
        currentTurn = !currentTurn;
    }

    //returns player's color
    public Color getColor() {
        return color;
    }
}
