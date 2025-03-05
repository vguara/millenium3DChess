import java.awt.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.BiFunction;

//@FunctionalInterface
//interface VarargsFunction<T, R> {
//    R apply(T obj, int i, int j, Object... extraParams);
//}

public class Board3D extends Board implements Cloneable{

    private Board[] board3d;

    private final Board topB;

    private final Board middleB;

    private final Board bottomB;

    private GameManager game;



    public Board3D(String boardName, GameManager gameManager){
        super(boardName);
        bottomB = new Board("Bottom");
        middleB = new Board("Middle");
        topB = new Board("Top");
        board3d = new Board[]{bottomB, middleB, topB};
        game = gameManager;
        resetBoards();
    }

    /**
     * reset boards to initial position
     */
    public void resetBoards(){
        clearPieces();
        bottomB.setupBoardOneSide("white");
        topB.setupBoardOneSide("black");
    }

    /**
     * Remove all piece from boards
     */
    public void clearPieces(){
        for (Board board : board3d){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    // iterate through every tile
                    board.getTile(i, j).setPiece(null);
                }
            }
        }
    }

    public Board getTopB () {
        return topB;
    }

    public Board getBottomB(){
        return bottomB;
    }

    public Board[] getBoards(){
        return board3d;
    }

    /**
     * Check if a specific tile is threatened by a player
     * @param tileBoard Board where the Tile to be tested for threats is
     * @param tile Tile to be tested for threats
     * @param playerColor Color of the player making the move - Tile is checked for threats from the OPPOSITE player
     * @param lastMove Last move made
     * @return true if tile is threatened
     */
    public boolean tileThreatened (Board tileBoard, Tile tile, Color playerColor, Move lastMove){

        Player player = game.getPlayerBasedOnColor(playerColor);

        return tileThreatened2(tileBoard, tile, player, lastMove);

//        for (Board board : board3d){
//            for (int i = 0; i < 8; i++){
//                for (int j = 0; j < 8; j++){
//                    // iterate through every tile
//                    Tile fromTile  = board.getTile(i, j);
//                    // if there is a piece and the piece is the opposite color of the current piece
//                    if (fromTile.getPiece() != null && fromTile.getPiece().getColor() != playerColor){
//                        boolean moveValid = validateMove(fromTile, tile, board, tileBoard, lastMove);
//                        if (moveValid && check3dPath(board, tileBoard, fromTile, tile)){
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
    }

    /**
     * Check if a specific tile is threatened by a player
     * @param tileBoard Board where the Tile to be tested for threats is
     * @param tile Tile to be tested for threats
     * @param player Player making the move - Tile is checked for threats from the OPPOSITE player
     * @param lastMove Last move made
     * @return true if tile is threatened
     */
    public boolean tileThreatened2 (Board tileBoard, Tile tile, Player player, Move lastMove){


        for (Board board : board3d){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    // iterate through every tile
                    Tile fromTile  = board.getTile(i, j);
                    Move move = new Move (player, fromTile, tile, board, tileBoard, this, lastMove);
                    // if there is a piece and the piece is the opposite color of the current piece
                    if (fromTile.getPiece() != null && fromTile.getPiece().getColor() != move.getPlayer().getColor()){
                        boolean moveValid = validateMove2(move);
                        if (moveValid && check3dPath2(move)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Tile get3DTile(Board clickedBoard, int row, int col){
        return clickedBoard.getTile(row, col);
    }

    public boolean sameBoard(Board board1, Board board2) {
        return board1 == board2;
    }

//    public boolean validateMove (Tile fromTile, Tile toTile, Board origin, Board dest, Move lastMove){
//        ChessPiece piece = fromTile.getPiece();
//
//        int deltaBoards = Math.abs((getBoardIndex(origin) - getBoardIndex(dest)));
//
//        boolean moveValid = piece.is3DMoveValid(deltaBoards, fromTile, toTile, lastMove);
//
//        boolean pathClear = check3dPath(origin, dest, fromTile, toTile);
//
//        boolean occupiedSameColor = false;
//
//        if (toTile.getPiece() !=null) {
//            occupiedSameColor = (fromTile.getPiece().getColor() == toTile.getPiece().getColor());
//        }
//
//        return (moveValid && pathClear && !occupiedSameColor);
//
//    }

    /**
     * Checks if the move is valid and if the path is clear
     * @param currentMove Move that is being checked
     * @return true if move is valid and path is clear
     */
    public boolean validateMove2 (Move currentMove){
        ChessPiece piece = currentMove.getMovedPiece();

        boolean moveValid = currentMove.validate();

        boolean pathClear = check3dPath2(currentMove);

        boolean occupiedSameColor = false;

        if (currentMove.getToTile().getPiece() !=null) {
            occupiedSameColor = (piece.getColor() == currentMove.getToTile().getPiece().getColor());
        }

        return (moveValid && pathClear && !occupiedSameColor);

    }

    /**
     * Sets a highlight on tiles to which moves from the selected piece are allowed
     * @param origin Board the piece is moving from
     * @param fromTile Tile the piece is moving from
     * @param lastMove last move made
     * @param player player making the move
     */
    public void highlightPossibleMoves(Board origin, Tile fromTile, Move lastMove, Player player){
        Tile checkedTile = null;
        for (Board board : board3d){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    checkedTile = board.getTile(i, j);
                    Move move = new Move(player, fromTile, checkedTile, origin, board, this, lastMove);
                    if (validateMove2(move)){
                        checkedTile.setHighlight(true);
                    }
                }
            }
        }
    }

    /**
     * Checks all moves possible next moves to verify if a move results in checkmate
     * @param player player making the move
     * @param lastMove last move made
     * @return true if checkmate
     */
    public boolean checkAllMovesForMate (Player player, Move lastMove){

        Color playerColor = player.getColor();
        for (Board board: getBoards()){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    // iterate through every tile
                    Tile fromTile  = board.getTile(i, j);
                    // if there is a piece and the piece is the opposite color of the current piece
                    if (fromTile.getPiece() !=null && fromTile.getPiece().getColor() == playerColor){
                        boolean tileStillThreatened = simulateMoves(player, fromTile, board, lastMove);
                        if(!tileStillThreatened) { return false;}
                    }
                }
            }
        }
        return true;
    }

    /**
     * Simulates all possible moves from a given state to check if a tile is still threatened regardless of next move.
     * @param player Player making the move
     * @param pieceTile Tile being threatened
     * @param pieceBoard Board where the tile is in
     * @param lastMove last move made
     * @return true if tile remains threatened regardless of next move.
     */
    public boolean simulateMoves(Player player, Tile pieceTile, Board pieceBoard, Move lastMove){
        Tile checkedTile;
        boolean tileThreatened = true;
        for (Board board : board3d){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    checkedTile = board.getTile(i, j);
                    Move testMove = null;
                    if (pieceTile.getPiece() != null ) {
                        testMove = new Move(player, pieceTile, checkedTile, pieceBoard, board, this, lastMove);
                    }
                    if (pieceTile.getPiece() != null && testMove.validate() && check3dPath2(testMove)){
                        testMove.execute();
                        tileThreatened = tileThreatened2(player.getKingBoard(), player.getKingTile(),player, lastMove);
                        testMove.undo();
                        if (!tileThreatened){ return tileThreatened;}
                    }
                }
            }
        }
        return tileThreatened;
    }



    public void removeAllHighlights(){
        for (Board board : board3d){
            board.removeHighlights();
        }
    }

//    public boolean check3dPath (Board origin, Board dest, Tile fromTile, Tile toTile){
//        if(sameBoard(origin, dest)){
//            return origin.checkPath(fromTile, toTile);
//        } else{
//            int originBoardIndex = getBoardIndex(origin);
//            int destBoardIndex = getBoardIndex(dest);
//
//            if (Math.abs(originBoardIndex - destBoardIndex) != 2) {
//                return true;
//            } else {
//                //the block can only be on the middle board.
//                // knight is the only piece that can move only one step when moving 2 boards up or down.
//                int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
//                int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
//                if (deltaX == 1 || deltaY == 1){
//                    return true; //this is a knight, cannot be blocked
//                }
//
//
//                // Calculate the middle board indices
//                int middleBoardRow = (fromTile.getRow() + toTile.getRow()) / 2;
//                int middleBoardCol = (fromTile.getCol() + toTile.getCol()) / 2;
//
//                // Get the middle board tile
//                Tile middleBoardTile = board3d[1].getTile(middleBoardRow, middleBoardCol);
//
//                return !middleBoardTile.isOccupied();
//
//            }
//        }
//    }

    public boolean check3dPath2 (Move move){
        Board origin = move.getFromBoard();
        Board dest = move.getToBoard();
        Tile fromTile = move.getFromTile();
        Tile toTile = move.getToTile();

        if(sameBoard(origin, dest)){
            return origin.checkPath(fromTile, toTile);
        } else{
            int originBoardIndex = getBoardIndex(origin);
            int destBoardIndex = getBoardIndex(dest);

            if (Math.abs(originBoardIndex - destBoardIndex) != 2) {
                return true;
            } else {
                //the block can only be on the middle board.
                // knight is the only piece that can move only one step when moving 2 boards up or down.
                int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
                int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
                if (deltaX == 1 || deltaY == 1){
                    return true; //this is a knight, cannot be blocked
                }


                // Calculate the middle board indices
                int middleBoardRow = (fromTile.getRow() + toTile.getRow()) / 2;
                int middleBoardCol = (fromTile.getCol() + toTile.getCol()) / 2;

                // Get the middle board tile
                Tile middleBoardTile = board3d[1].getTile(middleBoardRow, middleBoardCol);

                return !middleBoardTile.isOccupied();

            }
        }
    }

    public Board getBoardOfAPiece (ChessPiece piece){
        Tile checkedtile = null;
        for (Board board : board3d){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    checkedtile = board.getTile(i, j);
                    if (checkedtile.getPiece() == piece){
                        return board;
                    }
                }
            }
        }
        return null;

    }

    public int getBoardIndex(Board board) {
        for (int i = 0; i < board3d.length; i++) {
            if (board3d[i] == board) {
                return i;
            }
        }
        return -1; // Board not found
    }

    public Board getBoard(int i){
        return board3d[i];

    }

    public Board3D clone() {
        try {
            Board3D clonedBoard3D = (Board3D) super.clone();
            // Clone the boards array
            clonedBoard3D.board3d = new Board[board3d.length];
            for (int i = 0; i < board3d.length; i++) {
                clonedBoard3D.board3d[i] = board3d[i].clone();
            }
            return clonedBoard3D;
        } catch (Exception e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }


}
