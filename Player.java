import java.awt.*;
import java.util.ArrayList;

public class Player {

    public static final Color BLACK = Color.BLACK;
    public static final Color WHITE = Color.WHITE;

    private Board kingBoard;

    private Tile kingTile;

    private final Color color;

    private boolean currentTurn;

    private ArrayList<ChessPiece> piecesTaken;

    //Constructor for the player defines the color.
    public Player(Color playerColor){
        color = playerColor;
        currentTurn = (color == WHITE);
        piecesTaken = new ArrayList<>();
    }

    public Player (Player original){
        this.color = original.getColor();
        currentTurn = original.isCurrentTurn();
        piecesTaken = original.getPiecesTaken();

    }

    public void addTakenPiece (ChessPiece pieceTaken){
        piecesTaken.add(pieceTaken);
    }

    public ArrayList<ChessPiece> getPiecesTaken(){
        return piecesTaken;
    }

    public void resetTakenPieces(){
        piecesTaken.clear();
    }

    public void removeLastTakenPiece(){
        piecesTaken.removeLast();
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

    private String colorString(Color colorToString) {
        if (colorToString == WHITE){
            return "White";
        } else {
            return "Black";
        }
    }

    @Override
    public String toString(){
        return "Player " + colorString(getColor());
    }



    //returns player's color
    public Color getColor() {
        return color;
    }
}
