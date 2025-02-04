import java.awt.Color;
import java.util.ArrayList;

public class King extends ChessPiece{

    public King (Color color){
        super(color, "K");
    }

    public boolean castlingPossible (Tile fromTile, Tile toTile, Move lastMove){
        int deltaX = fromTile.getCol() - toTile.getCol();
        if (!this.firstMove || Math.abs(deltaX) != 2 || fromTile.getRow() != toTile.getRow() || lastMove == null){
            return false;
        }
        boolean kingSide = false;
        int rookDist = 0;


        if (deltaX == 2){
            kingSide = true;
            rookDist = 3;
        } else if (deltaX == -2){
            rookDist = -4;
        } else {
            return false;
        }

        Board board = lastMove.getMainBoard().getBoardOfAPiece(this);

        ChessPiece rook = board.getTile(fromTile.getRow(), fromTile.getCol()+rookDist).getPiece();

        //debug checking if rook
        System.out.println("Row " + fromTile.getRow() + " and col " + (fromTile.getCol()+rookDist) );

        System.out.println("Found rook? " + rook.getLetter());


        if (!(rook instanceof Rook) || !rook.firstMove){
            return false;
        }

        //check for threatened tiles between king and rook.
        int step = kingSide ? 1 : -1;
        boolean tileThreatened = false;
        int row = fromTile.getRow();
        int initCol = fromTile.getCol();
        for (int i = initCol; (kingSide ? i <= 6 : i >= 2); i +=step){
            tileThreatened = lastMove.getMainBoard().tileThreatened(
                    board,
                    board.getTile(row, i),
                    this.getColor(),
                    lastMove);

            if (tileThreatened) return false;
        }
        //Extra check to see if
        if (!kingSide && board.getTile(row, toTile.getCol()-1).getPiece() != null){
            return false;
        }

        return true;


    }

    @Override
    public boolean isMoveValid(Tile fromTile, Tile toTile) {
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());


        // Kings can move one square in any direction (horizontally, vertically, or diagonally).
        return deltaX <= 1 && deltaY <= 1;
    }

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile, Move lastMove) {


        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        if(deltaBoards == 0){
            //castling check
            if (castlingPossible(fromTile, toTile, lastMove)) return true;
            return isMoveValid(fromTile, toTile);
        } else {
            return (deltaX <= deltaBoards) && (deltaY <= deltaBoards) && (deltaBoards != 2);
        }
    }

    public String getLetter() {
        return letter;
    }
}
