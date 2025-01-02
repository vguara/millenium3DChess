import javax.swing.*;
import java.awt.*;

public class GameManager {
    private Board3D boards;

    private Tile selectedTile;

    private Board selectedBoard;

    private Displayer displayer;

    private Player p1;

    private Player p2;

    public GameManager() {
        boards = new Board3D();
        selectedTile = null;
        selectedBoard = null;
        p1 = new Player(Color.WHITE);
        p2 = new Player(Color.BLACK);
    }

    public void startGame() {
        // Initialize the game, set up the board, and potentially add players.
        Displayer displayer = new Displayer(boards, this);
        this.displayer = displayer;
    }

    public boolean isGameOver() {
        // Implement game over conditions
        // Return true if the game is over...
        return false; // Placeholder for now.
    }

    public Player getCurrentPlayer(){
        return p1.isCurrentTurn()? p1 : p2;
    }

    public void movePiece(int destRow, int destCol, Board clickedBoard) {
        Tile clickedTile = clickedBoard.getTile(destRow, destCol);

        // Check if there's a selectedTile
        if (selectedTile != null) {
            System.out.println("clicked with a tile selected");
            System.out.println("Selected tile on move: " + selectedTile.getRow() + ", " + selectedTile.getCol());
            System.out.println("Selected piece: " + selectedTile.getPiece().getLetter());

            // Check if the clicked tile is the same as the selected tile
            if (selectedTile == clickedTile) {
                // If the clicked tile is the same as the selected tile, unselect it
                selectedTile = null;
            } else {
                // If the clicked tile is different, proceed with the move logic
                // Check if the clicked tile is empty
                System.out.println("clicked on different tile");
                if (!clickedTile.isOccupied() || !clickedTile.getPiece().getColor().equals(selectedTile.getPiece().getColor())) {

                    //here we will add 3d move validation, first pass the difference between boards
                    int deltaBoards = Math.abs((boards.getBoardIndex(selectedBoard) - boards.getBoardIndex(clickedBoard)));

                    boolean validMove = selectedTile.getPiece().is3DMoveValid(deltaBoards, selectedTile, clickedTile);
                    System.out.println(validMove);
                    if(validMove){
                        //check path
                        if (boards.check3dPath(selectedBoard, clickedBoard, selectedTile, clickedTile)) {
                            // Move the piece from the selectedTile to the clickedTile
                            clickedTile.setPiece(selectedTile.getPiece());
                            clickedTile.getPiece().firstMove = false;
                            selectedTile.setPiece(null);
                            p1.flipTurn();
                            p2.flipTurn();
                            displayer.updateDisplay();
                        }
                    }
                }
                // Reset the selectedTile to null
                selectedTile = null;
            }
        } else {
            // If there's no selectedTile, check if the clicked tile is occupied

            if (clickedTile.isOccupied()) {
                if(clickedTile.getPiece().getColor() == getCurrentPlayer().getColor()) {

                    // Select the clicked tile
                    System.out.println("selected tile is occupied");
                    selectedTile = clickedTile;
                    selectedBoard = clickedBoard;
                    System.out.println("Selected tile: " + selectedTile.getRow() + ", " + selectedTile.getCol() + " on board "+ boards.getBoardIndex(selectedBoard));
                }
            }
        }
    }

//    public Board getBoard() {
//        return board;
//    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameManager game = new GameManager();
            game.startGame();
        });
    }
}
