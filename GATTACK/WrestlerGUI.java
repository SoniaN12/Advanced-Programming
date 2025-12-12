import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class WrestlerGUI extends JFrame {
        private PlayerWrestler player;
        private CPUWrestler cpu;
        private JTextArea statusArea;
        private CanvasPanel canvas;
        private Timer animationTimer;
        private int playerOffset = 0, cpuOffset = 0;// Animation offsets
        private boolean playerHit = false;
        private boolean cpuHit = false;


    public WrestlerGUI() {
            player = new PlayerWrestler();
            cpu = new CPUWrestler();

            setTitle("Gattack");
            setSize(800, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // STATUS PANEL
            statusArea = new JTextArea();
            statusArea.setEditable(false);
            statusArea.setPreferredSize(new Dimension(300, 0));
            add(new JScrollPane(statusArea), BorderLayout.EAST);

            // CANVAS FOR ANIMATION
            canvas = new CanvasPanel();
            add(canvas, BorderLayout.CENTER);

            // BUTTON PANEL
            JPanel buttonPanel = new JPanel();
            JButton light = new JButton("Light Attack");
            JButton heavy = new JButton("Heavy Attack");
            JButton block = new JButton("Block");
            JButton recover = new JButton("Recover");

            buttonPanel.add(light);
            buttonPanel.add(heavy);
            buttonPanel.add(block);
            buttonPanel.add(recover);
            add(buttonPanel, BorderLayout.SOUTH);

            // BUTTON ACTIONS
            light.addActionListener(e -> playerAction(Wrestler.Move.LIGHT_ATTACK, true));
            heavy.addActionListener(e -> playerAction(Wrestler.Move.HEAVY_ATTACK, true));
            block.addActionListener(e -> playerAction(Wrestler.Move.BLOCK, true));
            recover.addActionListener(e -> playerAction(Wrestler.Move.RECOVER, true));

            updateStatus();
            setVisible(true);
        }

    private void declareWinnerByStamina() {
        if (player.stamina > cpu.stamina) {
            JOptionPane.showMessageDialog(this, "You win by stamina!");
        } else if (cpu.stamina > player.stamina) {
            JOptionPane.showMessageDialog(this, "CPU wins by stamina!");
        } else {
            JOptionPane.showMessageDialog(this, "Both wrestlers ran out of stamina! It's a draw.");
        }

        // End the game
        player.stamina = 0;
        cpu.stamina = 0;
        playerOffset = cpuOffset = 0;
        canvas.repaint();
    }


    /** Handles player moves and optional animation */
        private void playerAction(Wrestler.Move move, boolean animate) {
            if (!player.isAlive() || !cpu.isAlive()) return;

            // Check if player or CPU can make any move
            boolean playerCanMove = player.canPerformAnyMove();
            boolean cpuCanMove = cpu.canPerformAnyMove();

            if (!playerCanMove || !cpuCanMove) {
                declareWinnerByStamina();
                return;
            }

            // Execute player move
            MoveHandler.executeMove(player, cpu, move);

            if (animate) animateAttack(true, () -> {
                player.endTurn();

                if (cpu.isAlive()) {
                    // CPU chooses a random move it can perform
                    Wrestler.Move cpuMove = getRandomAvailableMove(cpu);
                    if (cpuMove != null) {
                        MoveHandler.executeMove(cpu, player, cpuMove);
                        animateAttack(false, () -> {
                            cpu.endTurn();
                            updateStatus();
                            checkGameEnd();
                        });
                    } else {
                        declareWinnerByStamina();
                    }
                } else {
                    updateStatus();
                    checkGameEnd();
                }
            });

            updateStatus();

            System.out.println("DEBUG: Player stamina = " + player.stamina);
            System.out.println("DEBUG: CPU stamina = " + cpu.stamina);

        }

    private Wrestler.Move getRandomAvailableMove(Wrestler cpu) {
        // Filter only moves CPU has enough stamina for
        Wrestler.Move[] moves = Wrestler.Move.values();
        java.util.List<Wrestler.Move> available = new ArrayList<>();

        for (Wrestler.Move move : moves) {
            int cost = switch (move) {
                case LIGHT_ATTACK -> 10;
                case HEAVY_ATTACK -> 25;
                default -> 0;
            };
            if (cpu.stamina >= cost) available.add(move);
        }

        if (available.isEmpty()) return null;
        return available.get(new Random().nextInt(available.size()));
    }



    public void flashPlayer() {
        playerHit = true;
        canvas.repaint();
        new Timer(200, e -> {
            playerHit = false;
            canvas.repaint();
            ((Timer) e.getSource()).stop();
        }).start();
    }

    public void flashCPU() {
        cpuHit = true;
        canvas.repaint();
        new Timer(200, e -> {
            cpuHit = false;
            canvas.repaint();
            ((Timer) e.getSource()).stop();
        }).start();
    }


    /** Simple forward/back animation */
    private void animateAttack(boolean isPlayer, Runnable afterAnimation) {
        int frames[] = {0};
        animationTimer = new Timer(20, e -> {
            frames[0]++;

            if (isPlayer) playerOffset += 4;
            else cpuOffset -= 4;

            canvas.repaint();

            if (frames[0] == 10) {
                if (isPlayer) playerOffset -= 8;
                else cpuOffset += 8;
            }

            if (frames[0] > 20) {
                playerOffset = cpuOffset = 0;
                canvas.repaint();
                ((Timer) e.getSource()).stop();

                if (afterAnimation != null) afterAnimation.run(); // chain CPU response
            }
        });
        animationTimer.start();
    }


    /** Updates the status panel */
        private void updateStatus() {
            statusArea.setText(player + "\n" + cpu);
        }

        /** Checks if game ended */
        private void checkGameEnd() {
            if (!cpu.isAlive()) JOptionPane.showMessageDialog(this, "You win!");
            else if (!player.isAlive()) JOptionPane.showMessageDialog(this, "You lost!");
        }

        /** Canvas to draw triangle and rectangle wrestlers */
        private class CanvasPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                // Draw Player Triangle
                int px = 100 + playerOffset;
                int[] xPoints = {px, px - 30, px + 30};
                int[] yPoints = {300, 350, 350};
                g2.setColor(playerHit ? Color.BLACK : Color.GREEN);
                g2.fillPolygon(xPoints, yPoints, 3);

                // Draw CPU Rectangle
                int cx = 500 + cpuOffset;
                g2.setColor(cpuHit ? Color.BLACK : Color.YELLOW);
                g2.fillRect(cx - 30, 300, 60, 50);

            }
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(WrestlerGUI::new);
        }
    }

