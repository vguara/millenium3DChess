public class Board implements Cloneable {
    private Tile[][] tiles;

    private final String name;


    public Board(String boardName) {
        name = boardName;
        initializeBoard(boardName);
    }



    private void initializeBoard(String boardName) {
        tiles = new Tile[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col] = new Tile(row, col);
            }
        }
    }

    public String getName(){
        return name;
    }

    public Tile[][] getTiles() {return tiles;}

    void setupBoard(){

        //blacks
        tiles[0][0].setPiece(new Rook(ChessPiece.BLACK));
        tiles[0][1].setPiece(new Knight(ChessPiece.BLACK));
        tiles[0][2].setPiece(new Bishop(ChessPiece.BLACK));
        tiles[0][3].setPiece(new Queen(ChessPiece.BLACK));
        tiles[0][4].setPiece(new King(ChessPiece.BLACK));
        tiles[0][5].setPiece(new Bishop(ChessPiece.BLACK));
        tiles[0][6].setPiece(new Knight(ChessPiece.BLACK));
        tiles[0][7].setPiece(new Rook(ChessPiece.BLACK));

        //whites
        tiles[7][0].setPiece(new Rook(ChessPiece.WHITE));
        tiles[7][1].setPiece(new Knight(ChessPiece.WHITE));
        tiles[7][2].setPiece(new Bishop(ChessPiece.WHITE));
        tiles[7][3].setPiece(new Queen(ChessPiece.WHITE));
        tiles[7][4].setPiece(new King(ChessPiece.WHITE));
        tiles[7][5].setPiece(new Bishop(ChessPiece.WHITE));
        tiles[7][6].setPiece(new Knight(ChessPiece.WHITE));
        tiles[7][7].setPiece(new Rook(ChessPiece.WHITE));

        //pawns
        for (int col = 0; col < 8; col++) {
            tiles[1][col].setPiece(new Pawn(ChessPiece.BLACK));
            tiles[6][col].setPiece(new Pawn(ChessPiece.WHITE));
        }

    }

    void setupBoardOneSide (String color){
        if (color.equals("white")){
            tiles[7][0].setPiece(new Rook(ChessPiece.WHITE));
            tiles[7][1].setPiece(new Knight(ChessPiece.WHITE));
            tiles[7][2].setPiece(new Bishop(ChessPiece.WHITE));
            tiles[7][3].setPiece(new Queen(ChessPiece.WHITE));
            tiles[7][4].setPiece(new King(ChessPiece.WHITE));
            tiles[7][5].setPiece(new Bishop(ChessPiece.WHITE));
            tiles[7][6].setPiece(new Knight(ChessPiece.WHITE));
            tiles[7][7].setPiece(new Rook(ChessPiece.WHITE));
            for (int col = 0; col < 8; col++) {
                tiles[6][col].setPiece(new Pawn(ChessPiece.WHITE));
            }

        } else if (color.equals("black")){
            tiles[0][0].setPiece(new Rook(ChessPiece.BLACK));
            tiles[0][1].setPiece(new Knight(ChessPiece.BLACK));
            tiles[0][2].setPiece(new Bishop(ChessPiece.BLACK));
            tiles[0][3].setPiece(new Queen(ChessPiece.BLACK));
            tiles[0][4].setPiece(new King(ChessPiece.BLACK));
            tiles[0][5].setPiece(new Bishop(ChessPiece.BLACK));
            tiles[0][6].setPiece(new Knight(ChessPiece.BLACK));
            tiles[0][7].setPiece(new Rook(ChessPiece.BLACK));
            for (int col = 0; col < 8; col++) {
                tiles[1][col].setPiece(new Pawn(ChessPiece.BLACK));
            }

        } else {
            throw new IllegalArgumentException("color " + color + " is not a valid color ");
        }



    }

    public void removeHighlights(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                tiles[i][j].setHighlight(false);
            }
        }
    }



    public void debugPrintHighlightedTiles(){


        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (tiles[i][j].getHighlight()){
                    System.out.println("tile on row " + i + " and col " + j + " is highlighted" );
                }
            }
        }

    }


    public boolean checkPath (Tile fromTile, Tile toTile){

        int fromRow = fromTile.getRow();
        int fromCol = fromTile.getCol();
        int toRow = toTile.getRow();
        int toCol = toTile.getCol();


        int deltaX = Math.abs(fromCol - toCol);
        int deltaY = Math.abs(fromRow - toRow);


        //Check only for pieces that move in a straight line (all but the knight)
        if (deltaX == 0 || deltaY == 0 || deltaY == deltaX){

            int rowStep = Integer.compare((toRow - fromRow), 0);
            int colStep = Integer.compare((toCol - fromCol), 0);

            int currentRow = fromRow + rowStep;
            int currentCol = fromCol + colStep;

            while (currentRow != toRow || currentCol != toCol) {
                Tile currentTile = this.getTile(currentRow, currentCol);
                if (currentTile.isOccupied()) {
                    return false; // Path is blocked
                }
                currentRow += rowStep;
                currentCol += colStep;
            }
        }

        return true;
    }

    public Tile getTile(int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            return tiles[row][col];
        } else {
            return null;
        }
    }

    @Override
    public String toString(){
        return name +" board";
    }

    @Override
    public Board clone() {
        try {
            Board clonedBoard = (Board) super.clone();
            int rows = tiles.length;
            int cols = tiles[0].length;
            clonedBoard.tiles = new Tile[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    clonedBoard.tiles[i][j] = tiles[i][j].clone();
                }
            }
            return clonedBoard;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }

}
