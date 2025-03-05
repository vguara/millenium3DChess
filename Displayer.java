import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class Displayer extends JFrame {
    private Board3D board3D;
    private JPanel chessboardPanel;

    private JPanel infoPanel; // Add this panel for displaying information

    final static private Color lightTile = Color.LIGHT_GRAY;
    final static private Color darkTile = Color.DARK_GRAY;

    final static private Color highlightedLightTile = new Color(51,204,255);

    final static private Color highlightedDarkTile = new Color(51,153,204);

    private GameManager game;

    public Displayer(Board3D board3d, GameManager game) {
        this.board3D = board3d;
        this.game = game;
        this.chessboardPanel = new JPanel(new GridLayout(8, 24));
        this.infoPanel = new JPanel(); // Initialize the info panel
        infoPanel.setLayout(new GridLayout(1, 2)); // Set a layout for the info panel

        setTitle("Millennium 3D Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);

        add(chessboardPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);

        infoPanel.setVisible(true);


        JLabel moveListLabel = new JLabel("Move List: ") ; //for creation no move made
        JLabel whitePiecesTakenLabel = new JLabel("Pieces taken by white: " ); //for creation no pieces taken
        JLabel blackPiecesTakenLabel = new JLabel("Pieces taken by black: " ); //for creation no pieces taken
        JLabel playerInCheckLabel = new JLabel("");
        infoPanel.add(moveListLabel);
        infoPanel.add(whitePiecesTakenLabel);
        infoPanel.add(blackPiecesTakenLabel);
        infoPanel.add(playerInCheckLabel);

        updateDisplay();

        JPanel buttonPanel = new JPanel();


//        JButton newGameButton = new JButton("New Game");
//        newGameButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                game.resetGame();
//            }
//        });
//        add(newGameButton, BorderLayout.PAGE_END);
//        JButton undoMoveButton = new JButton("Undo");
//        undoMoveButton.addActionListener(e -> game.undoLastMove());
//        add(undoMoveButton, BorderLayout.PAGE_END);

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.resetGame();
            }
        });
        buttonPanel.add(newGameButton, BorderLayout.NORTH);
        JButton undoMoveButton = new JButton("Undo");
        undoMoveButton.addActionListener(e -> game.undoLastMove());
        buttonPanel.add(undoMoveButton, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.SOUTH);


        setVisible(true);

    }



    //update display
    public void updateDisplay(){
        // Clear the chessboardPanel by removing all components.
        chessboardPanel.removeAll();
        boolean highlighted = false;

        for (int row = 0; row < 8; row++) {
            for (int boardIndex = 0; boardIndex < 3; boardIndex++) {
                Board board = board3D.getBoard(boardIndex);
                for (int col = 0; col < 8; col++) {
                    Tile tile = board.getTile(row, col);
                    JButton tileButton = new JButton();
                    tileButton.setMargin(new Insets(0, 0, 0, 0));
                    highlighted = tile.getHighlight();

                    if ((row + col) % 2 == 0) {
                        if (highlighted){
                            tileButton.setBackground(highlightedLightTile);
                        } else {
                            tileButton.setBackground(lightTile);
                        }
                    } else {
                        if (highlighted){
                            tileButton.setBackground(highlightedDarkTile);
                        } else {
                            tileButton.setBackground(darkTile);
                        }
                    }
                    if (tile.isOccupied()) {
                        ChessPiece piece = tile.getPiece();
                        String pieceName = piece.getPieceChar();
                        Font font = new Font("Arial Unicode MS", Font.BOLD, 42);
                        tileButton.setForeground(piece.getColor()); // Set the text color
                        tileButton.setFont(font);

                        tileButton.setText(pieceName);
                    } else {
                        tileButton.setText(""); // Clear the text if the tile is not occupied
                    }

                    tileButton.setActionCommand(row + "," + col + "," + boardIndex);

                    // Add an ActionListener to the tile button
                    tileButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectTile(tileButton);
                        }
                    });

                    // You can add logic to display the piece on the tile if it's occupied.

                    chessboardPanel.add(tileButton);


                }
                // Add a vertical line between boards
                if (boardIndex < 2) {
                    JPanel separator = new JPanel();
                    separator.setBackground(Color.BLACK);
                    separator.setPreferredSize(new Dimension(1, separator.getPreferredSize().height));
                    chessboardPanel.add(separator);
                }
            }
        }

        updateInfoPanel();

        // Revalidate and repaint the panel to reflect the changes.
        chessboardPanel.revalidate();
        chessboardPanel.repaint();
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    // update the panel
    private void updateInfoPanel(){
        infoPanel.removeAll();
        ArrayList<ChessPiece> whitePiecesTaken = null;
        ArrayList<ChessPiece> blackPiecesTaken = null;

        try {
            whitePiecesTaken = game.getPiecesTakenFromPlayer(Color.white);
            blackPiecesTaken = game.getPiecesTakenFromPlayer(Color.black);

        } catch (IllegalArgumentException e){
            System.out.println("Invalid color");
            System.exit(1);
        }
        ArrayList<Move> moveList = game.getMoves();

        JLabel moveListLabel = new JLabel("Move List: " + moveList);
        JLabel whitePiecesTakenLabel = new JLabel("Pieces taken by white: " + whitePiecesTaken);
        JLabel blackPiecesTakenLabel = new JLabel("Pieces taken by black: " + blackPiecesTaken);
        JLabel playerInCheckLabel = new JLabel("");
        Player playerInCheck = game.getPlayerInCheck();
        if (playerInCheck != null){
            playerInCheckLabel.setText(game.getOpposingPlayer()  + " check");
            if (game.getCheckMate()){
                playerInCheckLabel.setText(game.getOpposingPlayer()  + " checkmate");
            }
        }

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(moveListLabel);
        infoPanel.add(whitePiecesTakenLabel);
        infoPanel.add(blackPiecesTakenLabel);
        infoPanel.add(playerInCheckLabel);

        for (int i = 0; i < infoPanel.getComponentCount(); i++) {
            infoPanel.getComponent(i).setMaximumSize(new Dimension(Integer.MAX_VALUE, infoPanel.getComponent(i).getPreferredSize().height));
        }

    }




    // Highlight a tile by changing its background color
    private void selectTile(JButton tileButton) {

        String actionCommand = tileButton.getActionCommand();
        String[] coordinates = actionCommand.split(",");
        int row = Integer.parseInt(coordinates[0]);
        int col = Integer.parseInt(coordinates[1]);
        int boardIndex = Integer.parseInt(coordinates[2]);

        Color originalColor = tileButton.getBackground();
        tileButton.setBackground(Color.yellow);

        // Use a Timer to revert the background color after a delay
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileButton.setBackground(originalColor);
            }
        });
        timer.setRepeats(false); // Run the timer only once
        timer.start();

        game.checkTile(row, col, board3D.getBoard(boardIndex));

        updateDisplay();

    }

}
