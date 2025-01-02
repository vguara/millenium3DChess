public class Board {
    private Tile[][] tiles;

    public Board() {

        initializeBoard();
    }

    private void initializeBoard() {
        tiles = new Tile[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col] = new Tile(row, col);
            }
        }

        //setupBoard();
    }

    void setupBoard(){

        //whites
        tiles[0][0].setPiece(new Rook(ChessPiece.BLACK));
        tiles[0][1].setPiece(new Knight(ChessPiece.BLACK));
        tiles[0][2].setPiece(new Bishop(ChessPiece.BLACK));
        tiles[0][3].setPiece(new Queen(ChessPiece.BLACK));
        tiles[0][4].setPiece(new King(ChessPiece.BLACK));
        tiles[0][5].setPiece(new Bishop(ChessPiece.BLACK));
        tiles[0][6].setPiece(new Knight(ChessPiece.BLACK));
        tiles[0][7].setPiece(new Rook(ChessPiece.BLACK));

        //blacks
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

    public boolean checkPath (Tile fromTile, Tile toTile){

        int fromRow = fromTile.getRow();
        int fromCol = fromTile.getCol();
        int toRow = toTile.getRow();
        int toCol = toTile.getCol();

        System.out.println("Starting position row: " + fromRow + " " + fromCol);
        System.out.println("Final position row: " + toRow + " " + toCol);

        int deltaX = Math.abs(fromCol - toCol);
        int deltaY = Math.abs(fromRow - toRow);


        //Check only for pieces that move in a straight line (all but the knight)
        if (deltaX == 0 || deltaY == 0 || deltaY == deltaX){

            int rowStep = Integer.compare((toRow - fromRow), 0);
            int colStep = Integer.compare((toCol - fromCol), 0);

            int currentRow = fromRow + rowStep;
            int currentCol = fromCol + colStep;

            while (currentRow != toRow || currentCol != toCol) {
                System.out.println("Checking tile row: " + currentRow + " col: " + currentCol);
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
}
