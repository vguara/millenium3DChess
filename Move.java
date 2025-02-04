public class Move {

    private final Player player;

    private final Tile fromTile;

    private final Tile toTile;

    private final ChessPiece movedPiece;

    private final Board fromBoard;

    private final Board toBoard;

    private final Board3D mainBoard;

    public Move (Player movePlayer, Tile tileFrom, Tile tileTo, ChessPiece piece, Board originBoard, Board destBoard, Board3D main){
        player = movePlayer;
        fromTile = tileFrom;
        toTile = tileTo;
        movedPiece = piece;
        fromBoard = originBoard;
        toBoard = destBoard;
        mainBoard = main;
    }

    public Player getPlayer() {
        return player;
    }

    public Tile getFromTile() {
        return fromTile;
    }

    public Tile getToTile() {
        return toTile;
    }

    public Board3D getMainBoard() {return mainBoard;}

    public Board getFromBoard(){return fromBoard;}

    public Board getToBoard(){return toBoard;}

    public ChessPiece getMovedPiece() {
        return movedPiece;
    }

    @Override
    public String toString(){
        String moveString = "Player " + player.getColor() + " moved piece " + movedPiece.getLetter() + " from " +
                "tile " + fromTile.getRow() + " on board"  + fromBoard.getName() + " " +
                fromTile.getCol() + " to tile " + toTile.getRow() + " " + toTile.getCol() + " on board " + toBoard.getName();

        return moveString;

    }

}
