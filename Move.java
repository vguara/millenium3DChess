public class Move implements Command {

    private final Player player;

    private final Tile fromTile;

    private final Tile toTile;

    private final ChessPiece movedPiece;

    private final Board fromBoard;

    private final Board toBoard;

    private final Board3D mainBoard;

    private ChessPiece takenPiece;

    private boolean check;

    private String castling;

    private boolean pieceFirstMove;

    private Move lastMove;

    private boolean promotion;

    public Move (Player movePlayer, Tile tileFrom, Tile tileTo, Board originBoard, Board destBoard, Board3D main, Move lastMoveMade){
        player = movePlayer;
        fromTile = tileFrom;
        toTile = tileTo;
        movedPiece = fromTile.getPiece();
        takenPiece = toTile.getPiece();
        fromBoard = originBoard;
        toBoard = destBoard;
        mainBoard = main;
        lastMove = lastMoveMade;
        pieceFirstMove = movedPiece != null && movedPiece.isFirstMove();
        check = false;
        castling = null;
        promotion = false;
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

    public void setCheck(boolean isCheck) {check = isCheck;}

    public void setCastling(boolean isCastling) {check = isCastling;}

    public Board3D getMainBoard() {return mainBoard;}

    public Board getFromBoard(){return fromBoard;}

    public Board getToBoard(){return toBoard;}

    public ChessPiece getTakenPiece() {return takenPiece;}


    public ChessPiece getMovedPiece() {
        return movedPiece;
    }

    @Override
    public String toString(){
//        String moveString = "Player " + player.getColor() + " moved piece " + movedPiece.getLetter() + " from " +
//                "tile " + fromTile.getRow() + " on board"  + fromBoard.getName() + " " +
//                fromTile.getCol() + " to tile " + toTile.getRow() + " " + toTile.getCol() + " on board " + toBoard.getName();

        String notation;
        String fromFile = String.valueOf(numberToLetter(fromTile.getCol()));
        String toFile = String.valueOf(numberToLetter(toTile.getCol()));
        int fromBoardInd = mainBoard.getBoardIndex(fromBoard) + 1 ;
        int toBoardInd = mainBoard.getBoardIndex(toBoard) + 1 ;
        int fromRank = fromTile.getRow()+1;
        int toRank = toTile.getRow()+1;
        String capture = (takenPiece != null) ? movedPiece.getLetter() + "x" : "";
        String isCheck = (check) ? "+" : "";

        if (castling != null){
            notation = fromBoardInd + "-" + castling;
        } else {
            notation = fromBoardInd + movedPiece.getLetter() + fromFile + fromRank + "-" +
                    toBoardInd + capture + toFile + toRank + isCheck;
        }

        return notation;
    }

    public void promote (){
        if (movedPiece.canPromote(toTile.getRow())){
            promotion = true;
            toTile.setPiece(new Queen(player.getColor()));
        }

    }


    public char numberToLetter(int num) {
        if (num < 0 || num > 7) {
            throw new IllegalArgumentException("Number must be between 0 and 7");
        }
        return (char) ('a' + num);
    }

    @Override
    public void execute() {

        if (movedPiece instanceof Pawn){
            enPassantExecute();
        } else if (movedPiece instanceof King){
            getPlayer().setKingLocation(toBoard, toTile);
            castlingExecute();
        }

        if (takenPiece != null){
            player.addTakenPiece(takenPiece);
        }
        toTile.setPiece(movedPiece);
        movedPiece.firstMove = false;
        fromTile.setPiece(null);
    }

    public void enPassantExecute(){

        if (lastMove != null){
            if (((Pawn) movedPiece).enPassantCondition(lastMove,fromTile, toTile)){
                takenPiece = lastMove.getMovedPiece();
                lastMove.getToTile().setPiece(null);
            }
        }
    }



    public void castlingExecute(){
        int deltaX = toTile.getCol()- fromTile.getCol();

        //Castling move
        if (Math.abs(deltaX) > 1) {
            int kingSide = 3;
            int queenSide = 4;
            int side = deltaX > 0 ? kingSide : queenSide;
            ChessPiece rook = fromBoard.getTile(fromTile.getRow(), fromTile.getCol() + side).getPiece();
            toBoard.getTile(toTile.getRow(), toTile.getCol() + (deltaX > 0 ? -1 : 1)).setPiece(rook);
            rook.firstMove = false;
            fromBoard.getTile(fromTile.getRow(), fromTile.getCol() + side).setPiece(null);
            castling = (deltaX > 0) ? "0-0" : "0-0-0";

        }

    }


    @Override
    public void undo() {
        boolean enPassant = false;
        if (movedPiece instanceof Pawn){
            enPassant = undoEnPassant();
        } else if (movedPiece instanceof King){
            getPlayer().setKingLocation(fromBoard, fromTile);
            undoCastling();
        }

        fromTile.setPiece(movedPiece);
        movedPiece.firstMove = pieceFirstMove;
        if (takenPiece != null) {
            player.removeLastTakenPiece();
        }
        if (!enPassant) {
            toTile.setPiece(takenPiece);
        }

    }

    public boolean undoEnPassant(){

        if (lastMove != null && takenPiece != null){
            if (((Pawn) movedPiece).enPassantCondition(lastMove,fromTile, toTile)){
                takenPiece = lastMove.getMovedPiece();
                lastMove.getToTile().setPiece(takenPiece);
                toTile.setPiece(null);
                return true;
            }
        }
        return false;
    }

    public void undoCastling(){
        int deltaX = toTile.getCol()- fromTile.getCol();

        //Castling move
        if (Math.abs(deltaX) > 1) {
            int kingSide = 3;
            int queenSide = -4;
            int side = deltaX > 0 ? kingSide : queenSide;
            ChessPiece rook = toBoard.getTile(fromTile.getRow(), fromTile.getCol() + (side > 0 ? 1 : -1)).getPiece();
            fromBoard.getTile(fromTile.getRow(), fromTile.getCol() + side).setPiece(rook);
            rook.firstMove = true;
            toBoard.getTile(fromTile.getRow(), fromTile.getCol() + (deltaX > 0 ? 1 : -1)).setPiece(null);
        }

    }

    public boolean moveAutoChecks(Move lastMove){

        execute();
        boolean tileThreatened = mainBoard.tileThreatened2(player.getKingBoard(), player.getKingTile(), player, lastMove);
        undo();

        return tileThreatened;
    }


    @Override
    public boolean validate() {
        // Validate the move using context (including last move)
        int deltaBoards = Math.abs((mainBoard.getBoardIndex(fromBoard) - mainBoard.getBoardIndex(toBoard)));
        if (movedPiece == null){
            return false;
        }

        return movedPiece.is3DMoveValid(deltaBoards, fromTile, toTile, lastMove);

    }

}
