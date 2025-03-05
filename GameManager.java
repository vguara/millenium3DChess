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

    private ArrayList<Snapshot> snapshots;

    private int turn;

    private boolean isCheckMate;

    public GameManager() {

    }

    private void setInitialKing(){
        p1.setKingLocation(boards.getBottomB(), boards.getBottomB().getTile(7,4));
        p2.setKingLocation(boards.getTopB(), boards.getTopB().getTile(0, 4));

    }

    public ArrayList<Move> getMoves(){
        return moves;
    }


    private void increaseTurn() {turn++;}

    private void decreaseTurn() {
        if (turn > 0){
            turn--;
        }
    }

    public void initialState(){
        selectedTile = null;
        selectedBoard = null;
        moves = new ArrayList<>();
        playerInCheck = null;
        isCheckMate = false;
        turn = 0;
        snapshots = new ArrayList<>();
        setInitialKing();
    }

    public void startGame() {

        p1 = new Player(Color.WHITE);
        p2 = new Player(Color.BLACK);
        boards = new Board3D("Main", this);
        initialState();
        displayer = new Displayer(boards, this);
    }
    public void resetGame(){
        initialState();
        boards.resetBoards();
        p1.resetTakenPieces();
        p2.resetTakenPieces();
        if (getCurrentPlayer().getColor() != Color.WHITE) {
            p1.flipTurn();
            p2.flipTurn();
        }
        displayer.updateDisplay();
    }



    public boolean isGameOver() {
        // Implement game over conditions
        // Return true if the game is over...
        return false; // Placeholder for now.
    }

    public Player getCurrentPlayer(){
        return p1.isCurrentTurn()? p1 : p2;
    }

    public Player getOpposingPlayer() { return p1.isCurrentTurn()? p2 : p1; }

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
                boards.highlightPossibleMoves(selectedBoard, selectedTile, lastmove, getCurrentPlayer());
            }
        } else {
            System.out.println("clicked on an empty tile");
        }

    }

    public void createSnapshot(){
        Snapshot snapshot = new Snapshot(boards, getCurrentPlayer(), turn, moves);
        snapshots.add(snapshot);
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

//    public boolean checkPath2 (Board clickedBoard, Tile clickedTile){
//
//        return boards.check3dPath(selectedBoard, clickedBoard, selectedTile, clickedTile);
//
//    }

    public boolean checkPath (Move move){

        return boards.check3dPath2(move);

    }

    public boolean verifyCheck (Player player){

        Move lastMove = moves.isEmpty() ? null : moves.get(moves.size()-1);

        return boards.tileThreatened2(player.getKingBoard(), player.getKingTile(), player, lastMove);

    }

    public void verifyCheckMate(){

        if (playerInCheck == null){
            return;
        }

        //create a snapshot to test moves
        Snapshot current = new Snapshot(boards, playerInCheck, turn, moves);

        if (current.getBoard3d().checkAllMovesForMate(playerInCheck, moves.get(moves.size()-1))){
            isCheckMate = true;
        }


    }

    public void undoLastMove(){
        if (!moves.isEmpty()){
            moves.getLast().undo();
            moves.removeLast();
            Move previousMove = null;
            if (!moves.isEmpty()) {
                previousMove = moves.getLast();
            }
            p1.flipTurn();
            p2.flipTurn();
            playerInCheck = verifyCheck(getCurrentPlayer()) ? getCurrentPlayer() : null;
            System.out.println("Check " + playerInCheck);
            verifyCheckMate();
            if (playerInCheck != null && previousMove != null){
                previousMove.setCheck(true);
            }
            decreaseTurn();
            displayer.updateDisplay();
        }
    }


    public void movePiece(Tile clickedTile, Board clickedBoard) {


        // Check if the clicked tile is empty
        System.out.println("clicked on different tile");
        if (!clickedTile.isOccupied() || !clickedTile.getPiece().getColor().equals(selectedTile.getPiece().getColor())) {

            //here we will add 3d move validation, first pass the difference between boards
//            int deltaBoards = Math.abs((boards.getBoardIndex(selectedBoard) - boards.getBoardIndex(clickedBoard)));

            Move lastMove = moves.isEmpty() ? null : moves.get(moves.size()-1);
            Move currentMove = new Move(getCurrentPlayer(), selectedTile, clickedTile, selectedBoard, clickedBoard, boards, lastMove);
            boolean validMove = currentMove.validate();
            boolean moveAutoChecks = currentMove.moveAutoChecks(lastMove);
            if (moveAutoChecks){
                System.out.println("Move not allowed as it puts own king in danger");
            }
            //validate move and check path
            if(validMove && checkPath(currentMove) && !moveAutoChecks){

                currentMove.execute();
                endTurn(currentMove);

            }
        }
        // Reset the selectedTile to null
        selectedTile = null;
        selectedBoard = null;
        boards.removeAllHighlights();

    }

    public ArrayList<ChessPiece> getPiecesTakenFromPlayer(Color playerColor){
        if (playerColor == Color.white){
            return p1.getPiecesTaken();
        } else if (playerColor == Color.black){
            return p2.getPiecesTaken();
        } else {
            throw new IllegalArgumentException("Color must be black or white");
        }

    }

    public void endTurn (Move madeMove){


        p1.flipTurn();
        p2.flipTurn();
        playerInCheck = verifyCheck(getCurrentPlayer()) ? getCurrentPlayer() : null;
        System.out.println("Check " + playerInCheck);
        verifyCheckMate();
        if (playerInCheck != null){
            madeMove.setCheck(true);
        }

        moves.add(madeMove);
        if (isCheckMate){
            System.out.println("Checkmate, game is over");
        }

        madeMove.promote();

        //Temp print taken pieces.
        System.out.println("White player taken pieces " + p1.getPiecesTaken());
        System.out.println("Black player taken pieces " + p2.getPiecesTaken());
        increaseTurn();
        boards.removeAllHighlights();
        displayer.updateDisplay();
    }

    public Player getPlayerInCheck(){
        return playerInCheck;
    }

    public boolean getCheckMate(){
        return isCheckMate;
    }

    public Player getPlayerBasedOnColor(Color color){
        return (p1.getColor() == color) ? p1 : p2;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameManager game = new GameManager();
            game.startGame();
        });
    }
}
