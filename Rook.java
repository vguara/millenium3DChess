import java.awt.Color;
import java.util.ArrayList;

public class Rook extends ChessPiece {
    public Rook(Color color) {
        super(color, "R");
    }

    @Override
    public boolean isMoveValid(Tile fromTile, Tile toTile) {
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());

        // Rooks can move vertically or horizontally, but not diagonally
        return deltaX == 0 || deltaY == 0;
    }

    public boolean is3DMoveValid(int deltaBoards, Tile fromTile, Tile toTile, Move lastMove) {
        int deltaY = Math.abs(fromTile.getRow() - toTile.getRow());
        int deltaX = Math.abs(fromTile.getCol() - toTile.getCol());

        if(deltaBoards == 0){
            return isMoveValid(fromTile, toTile);
        } else {
            boolean vertical = (deltaX == 0 && deltaY == deltaBoards);
            boolean horizontal = (deltaY == 0 && deltaX == deltaBoards);
            boolean upwards = (deltaX == 0 && deltaY == 0);
            return vertical || horizontal || upwards;
        }
    }



    public String getLetter() {
        return letter;
    }
}


