import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Displayer extends JFrame {
    private Board3D board3D;
    private JPanel chessboardPanel;

    private GameManager game;

    public Displayer(Board3D board3d, GameManager game) {
        this.board3D = board3d;
        this.game = game;
        this.chessboardPanel = new JPanel(new GridLayout(8, 24));
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);

        add(chessboardPanel, BorderLayout.CENTER);

        updateDisplay();

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle starting a new game if needed
            }
        });
        add(newGameButton, BorderLayout.SOUTH);

        // You can add other UI components as needed.

        setVisible(true);

    }

    //update display

    public void updateDisplay(){
        // Clear the chessboardPanel by removing all components.
        chessboardPanel.removeAll();

        for (int row = 0; row < 8; row++) {
            for (int boardIndex = 0; boardIndex < 3; boardIndex++) {
                Board board = board3D.getBoard(boardIndex);
                for (int col = 0; col < 8; col++) {
                    Tile tile = board.getTile(row, col);
                    JButton tileButton = new JButton();

                    if ((row + col) % 2 == 0) {
                        tileButton.setBackground(Color.LIGHT_GRAY);
                    } else {
                        tileButton.setBackground(Color.DARK_GRAY);
                    }

                    if (tile.isOccupied()) {
                        ChessPiece piece = tile.getPiece();
                        String pieceName = piece.getLetter();
                        Font font = new Font("Arial", Font.BOLD, 24);
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
        // Revalidate and repaint the panel to reflect the changes.
        chessboardPanel.revalidate();
        chessboardPanel.repaint();

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

        game.movePiece(row, col, board3D.getBoard(boardIndex));

        updateDisplay();

    }

}
