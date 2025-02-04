import java.awt.*;
import java.util.ArrayList;

public class Board3D extends Board{

    private Board[] board3d;

    private final Board topB;

    private final Board middleB;

    private final Board bottomB;



    public Board3D(String boardName){
        super(boardName);
        bottomB = new Board("Bottom");
        bottomB.setupBoardOneSide("white");
        middleB = new Board("Middle");
        topB = new Board("Top");
        topB.setupBoardOneSide("black");
        board3d = new Board[]{bottomB, middleB, topB};

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

    public boolean tileThreatened (Board tileBoard, Tile tile, Color playerColor, Move lastMove){

        for (Board board : board3d){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    // iterate through every tile
                    Tile fromTile  = board.getTile(i, j);
                    // if there is a piece and the piece is the opposite color of the current piece
                    if (fromTile.getPiece() != null && fromTile.getPiece().getColor() != playerColor){
                        boolean moveValid = validateMove(fromTile, tile, tileBoard, board, lastMove);
                        if (moveValid){
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

    public boolean validateMove (Tile fromTile, Tile toTile, Board origin, Board dest, Move lastMove){
        ChessPiece piece = fromTile.getPiece();

        int deltaBoards = Math.abs((getBoardIndex(origin) - getBoardIndex(dest)));

        boolean moveValid = piece.is3DMoveValid(deltaBoards, fromTile, toTile, lastMove);

        boolean pathClear = check3dPath(origin, dest, fromTile, toTile);

        boolean occupiedSameColor = false;

        if (toTile.getPiece() !=null) {
            occupiedSameColor = (fromTile.getPiece().getColor() == toTile.getPiece().getColor());
        }

        return (moveValid && pathClear && !occupiedSameColor);

    }

    public void highlightPossibleMoves(Board origin, Tile fromTile, Move lastMove){
        Tile checkedTile = null;
        for (Board board : board3d){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    checkedTile = board.getTile(i, j);
                    if (validateMove(fromTile, checkedTile, origin, board, lastMove)){
                        checkedTile.setHighlight(true);
                    }
                }
            }
            board.debugPrintHighlightedTiles();
        }


    }

    public boolean checkAllMoves (Player player){

        Color playerColor = player.getColor();
        for (Board board: getBoards()){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    // iterate through every tile
                    Tile fromTile  = board.getTile(i, j);
                    // if there is a piece and the piece is the opposite color of the current piece



                }
            }

        }

        return true;
    }

    public void removeAllHighlights(){
        for (Board board : board3d){
            board.removeHighlights();
        }
    }

    public boolean check3dPath (Board origin, Board dest, Tile fromTile, Tile toTile){
        if(sameBoard(origin, dest)){
            return origin.checkPath(fromTile, toTile);
        } else{
            int originBoardIndex = getBoardIndex(origin);
            int destBoardIndex = getBoardIndex(dest);

            if (Math.abs(originBoardIndex - destBoardIndex) != 2) {
                return true;
            } else {
                //the block can only be on the middle board.
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


}
