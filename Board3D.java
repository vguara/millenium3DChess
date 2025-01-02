public class Board3D extends Board{

    Board[] board3d;

    public Board3D(){
        Board bottomB = new Board();
        bottomB.setupBoard();
        Board middleB = new Board();
        Board topB = new Board();

        board3d = new Board[]{bottomB, middleB, topB};
    }

    public Tile get3DTile(Board clickedBoard, int row, int col){
        return clickedBoard.getTile(row, col);
    }

    public boolean sameBoard(Board board1, Board board2) {
        return board1 == board2;
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
