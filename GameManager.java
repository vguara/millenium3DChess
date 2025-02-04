import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameManager {
    private Board3D boards;

    private Tile selectedTile;

    private Board selectedBoard;

    private Displayer displayer;

    private Player p1;

    private Player p2;

    private ArrayList<Move> moves;

    private Player playerInCheck;

    public GameManager() {
        p1 = new Player(Color.WHITE);
        p2 = new Player(Color.BLACK);
        boards = new Board3D("Main");
        setInitialKing();
        selectedTile = null;
        selectedBoard = null;
        moves = new ArrayList<>();
        playerInCheck = null;
    }

    private void setInitialKing(){
        p1.setKingLocation(boards.getBottomB(), boards.getBottomB().getTile(7,4));
        p2.setKingLocation(boards.getTopB(), boards.getTopB().getTile(0, 4));

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

    public void selectTile (Tile clickedTile, Board clickedBoard){

        if (selectedTile == clickedTile){
            boards.removeAllHighlights();
            selectedTile = null;
            selectedBoard = null;
        } else if (selectedTile == null && clickedTile.isOccupied()){
            // check if belongs to current player
            if (pieceBelongsToCurrentPlayer(clickedTile)){
                selectedTile = clickedTile;
                selectedBoard = clickedBoard;
                selectedTile.setHighlight(true);
                Move lastmove = moves.isEmpty() ? null: moves.get(moves.size()-1);
                boards.highlightPossibleMoves(selectedBoard, selectedTile, lastmove);
                System.out.println("Selected tile: " + selectedTile.getRow() + ", " + selectedTile.getCol() + " on board "+ boards.getBoardIndex(selectedBoard));
            }
        } else {
            System.out.println("clicked on an empty tile");
        }

    }



    public boolean pieceBelongsToCurrentPlayer(Tile tile){
        ChessPiece piece = tile.getPiece();
        Player currentPlayer = getCurrentPlayer();
        return currentPlayer.getColor().equals(piece.getColor());

    }

    public void checkTile(int destRow, int destCol, Board clickedBoard){
        Tile clickedTile = clickedBoard.getTile(destRow, destCol);

        if (selectedTile == null || clickedTile == selectedTile){
            selectTile(clickedTile, clickedBoard);
        } else {
            movePiece(clickedTile, clickedBoard);
        }

    }

    public boolean checkPath (Board clickedBoard, Tile clickedTile){

        return boards.check3dPath(selectedBoard, clickedBoard, selectedTile, clickedTile);

    }

    public boolean verifyCheck (Player player){

        Move lastMove = moves.isEmpty() ? null : moves.get(moves.size()-1);

        return boards.tileThreatened(player.getKingBoard(), player.getKingTile(), player.getColor(), lastMove);

    }

    public boolean verifyCheckMate(){

        if (playerInCheck != null){


        }

        return true;

    }




    public void makeMove (Board clickedBoard, Tile clickedTile){

        // remove taken pawn if en passant
        if (selectedTile.getPiece() instanceof Pawn && !moves.isEmpty()){
            if (((Pawn) selectedTile.getPiece()).enPassantCondition(moves.get(moves.size()-1),selectedTile, clickedTile)){
                moves.get(moves.size()-1).getToTile().setPiece(null);
            }
        }

        // Detect castling and update king position
        int deltaX = clickedTile.getCol()- selectedTile.getCol();
        if (selectedTile.getPiece() instanceof King) {

            //update king position
            getCurrentPlayer().setKingLocation(clickedBoard, clickedTile);

            //Castling move
            if (Math.abs(deltaX) > 1) {
                int side = deltaX > 0 ? 3 : -4;
                ChessPiece rook = selectedBoard.getTile(selectedTile.getRow(), selectedTile.getCol() + side).getPiece();
                clickedBoard.getTile(clickedTile.getRow(), clickedTile.getCol() + (deltaX > 0 ? -1 : 1)).setPiece(rook);
                rook.firstMove = false;
                System.out.println("Rook tile = " + clickedBoard.getTile(clickedTile.getRow(), clickedTile.getCol() + (deltaX > 0 ? 1 : -1)));
                selectedBoard.getTile(selectedTile.getRow(), selectedTile.getCol() + side).setPiece(null);
            }

        }
        clickedTile.setPiece(selectedTile.getPiece());
        clickedTile.getPiece().firstMove = false;
        selectedTile.setPiece(null);

        //store Move
        Move move = new Move(getCurrentPlayer(), selectedTile, clickedTile, clickedTile.getPiece(), selectedBoard, clickedBoard, boards);
        moves.add(move);

        p1.flipTurn();
        p2.flipTurn();
        playerInCheck = verifyCheck(getCurrentPlayer()) ? getCurrentPlayer() : null;
        System.out.println("Check " + playerInCheck);
        boards.removeAllHighlights();
        displayer.updateDisplay();

    }


    public void movePiece(Tile clickedTile, Board clickedBoard) {

        System.out.println("clicked with a tile selected");
        System.out.println("Selected tile on move: " + selectedTile.getRow() + ", " + selectedTile.getCol());
        System.out.println("Selected piece: " + selectedTile.getPiece().getLetter());

        // Check if the clicked tile is empty
        System.out.println("clicked on different tile");
        if (!clickedTile.isOccupied() || !clickedTile.getPiece().getColor().equals(selectedTile.getPiece().getColor())) {

            //here we will add 3d move validation, first pass the difference between boards
            int deltaBoards = Math.abs((boards.getBoardIndex(selectedBoard) - boards.getBoardIndex(clickedBoard)));

            Move lastMove = moves.isEmpty() ? null : moves.get(moves.size()-1);
            boolean validMove = selectedTile.getPiece().is3DMoveValid(deltaBoards, selectedTile, clickedTile, lastMove);
            System.out.println(validMove);
            if(validMove){
                //check path
                if(checkPath(clickedBoard, clickedTile)) {

                    makeMove(clickedBoard, clickedTile);
                }

            }
        }
        // Reset the selectedTile to null
        selectedTile = null;
        selectedBoard = null;
        boards.removeAllHighlights();

    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameManager game = new GameManager();
            game.startGame();
        });
    }
}
