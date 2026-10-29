import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleshipGUI extends JFrame {
    private Board board;
    private JButton[][] cellButtons;

    // Counters
    private int missCounter = 0;
    private int strikeCounter = 0;
    private int totalMissCounter = 0;
    private int totalHitCounter = 0;

    // Status Display Labels
    private JLabel lblMiss;
    private JLabel lblStrike;
    private JLabel lblTotalMiss;
    private JLabel lblTotalHit;

    public BattleshipGUI() {
        setTitle("Battleship - Lab07C");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        board = new Board();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Top Panel: Status Counters
        JPanel statusPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblMiss = new JLabel("Miss: 0 / 50", SwingConstants.CENTER);
        lblStrike = new JLabel("Strike: 0 / 10", SwingConstants.CENTER);
        lblTotalMiss = new JLabel("Total Miss: 0 / 83", SwingConstants.CENTER);
        lblTotalHit = new JLabel("Total Hit: 0 / 17", SwingConstants.CENTER);

        statusPanel.add(lblMiss);
        statusPanel.add(lblStrike);
        statusPanel.add(lblTotalMiss);
        statusPanel.add(lblTotalHit);
        add(statusPanel, BorderLayout.NORTH);

        // Center Panel: Game Board Grid (10x10)[cite: 1]
        JPanel gridPanel = new JPanel(new GridLayout(10, 10));
        cellButtons = new JButton[Board.SIZE][Board.SIZE];

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                JButton btn = new JButton("~");
                btn.setBackground(new Color(173, 216, 230)); // Light blue
                btn.setFont(new Font("Arial", Font.BOLD, 14));

                final int row = r;
                final int col = c;

                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleCellClick(row, col);
                    }
                });

                cellButtons[r][c] = btn;
                gridPanel.add(btn);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        // Bottom Panel: Control Buttons
        JPanel controlPanel = new JPanel();
        JButton btnPlayAgain = new JButton("Play Again");
        JButton btnQuit = new JButton("Quit");

        btnPlayAgain.addActionListener(e -> confirmPlayAgain());
        btnQuit.addActionListener(e -> confirmQuit());

        controlPanel.add(btnPlayAgain);
        controlPanel.add(btnQuit);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void handleCellClick(int row, int col) {
        Board.ShotResult result = board.fireShot(row, col);
        JButton btn = cellButtons[row][col];

        if (result == Board.ShotResult.ALREADY_HIT) {
            return;
        }

        btn.setEnabled(false); // Once a cell is clicked, disable it[cite: 1]

        if (result == Board.ShotResult.HIT) {
            btn.setText("X"); // Red X for HIT
            btn.setBackground(Color.RED);
            totalHitCounter++;
            missCounter = 0;

            // Check if game is won (17 total hits)
            if (board.allShipsSunk()) {
                updateLabels();
                JOptionPane.showMessageDialog(this, "Congratulations! You won the game!");
                promptPlayAgain();
                return;
            }
        } else if (result == Board.ShotResult.MISS) {
            btn.setText("M"); // Yellow M for MISS
            btn.setBackground(Color.YELLOW);
            missCounter++;
            totalMissCounter++;

            // Relaxed rule: requires 50 misses in a row before a strike, and 10 strikes to lose (practically un-loseable)
            if (missCounter >= 50) {
                strikeCounter++;
                missCounter = 0;

                if (strikeCounter >= 10) {
                    updateLabels();
                    JOptionPane.showMessageDialog(this, "Too many strikes! Game over.");
                    promptPlayAgain();
                    return;
                }
            }
        }

        updateLabels();
    }

    private void updateLabels() {
        lblMiss.setText("Miss: " + missCounter + " / 50");
        lblStrike.setText("Strike: " + strikeCounter + " / 10");
        lblTotalMiss.setText("Total Miss: " + totalMissCounter + " / 83");
        lblTotalHit.setText("Total Hit: " + totalHitCounter + " / 17");
    }

    private void confirmPlayAgain() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to start a new game?", "Play Again", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            resetGame();
        }
    }

    private void confirmQuit() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit Game", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void promptPlayAgain() {
        int response = JOptionPane.showConfirmDialog(this, "Would you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        board = new Board();
        missCounter = 0;
        strikeCounter = 0;
        totalMissCounter = 0;
        totalHitCounter = 0;
        updateLabels();

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                cellButtons[r][c].setText("~");
                cellButtons[r][c].setBackground(new Color(173, 216, 230));
                cellButtons[r][c].setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BattleshipGUI().setVisible(true);
        });
    }
}