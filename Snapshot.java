import java.util.ArrayList;

public class Snapshot {

    private final Board3D board3d;

    private final Player currentPlayer;

    private final int currentTurn;

    private final ArrayList<Move> moveList;


    public Snapshot (Board3D board3d, Player player, int turn, ArrayList<Move> moveList){

        this.board3d = board3d.clone();
        this.currentPlayer = new Player(player);
        this.currentTurn = turn;
        this.moveList = new ArrayList<>(moveList);

    }

    public Board3D getBoard3d() {
        return board3d;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public ArrayList<Move> getMoveList() {
        return moveList;
    }
}
