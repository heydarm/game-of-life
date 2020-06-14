package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

public class GameOfLife extends JFrame {
    private final Timer timer;
    private Controller controller;
    private JPanel mainPanel;
    private JLabel generationLabel;
    private JLabel aliveLabel;

    public GameOfLife() {
        timer = new Timer(500, e -> {
            controller.nextGeneration();
            revalidate();
            repaint();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMainPanel();
        add(mainPanel);
    }

    public JLabel getGenerationLabel() {
        return generationLabel;
    }

    public JLabel getAliveLabel() {
        return aliveLabel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void setMainPanel() {
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        this.mainPanel = main;
    }

    public void main() {
        mainPanel.add(gamePanel(), BorderLayout.WEST);
        mainPanel.add(cellsField(), BorderLayout.CENTER);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private JPanel gamePanel() {
        JPanel game = new JPanel();

        game.setLayout(new BoxLayout(game, BoxLayout.Y_AXIS));
        game.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        setColorFor(game, ColorPalette.MAIN);

        game.add(btnPanel());
        game.add(infoPanel());
        game.add(speedSlider());
        game.add(sizeSetting());

        return game;
    }

    private JPanel btnPanel() {
        JPanel btnPanel = new JPanel();

        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setMaximumSize(new Dimension(150, 32));
        btnPanel.setOpaque(false);

        btnPanel.add(nextGenerationBtn());
        btnPanel.add(playToggleBtn());
        btnPanel.add(resetBtn());

        return btnPanel;
    }

    private JButton nextGenerationBtn() {
        JButton nextBtn = defaultBtn("resources/icons/next.png");

        nextBtn.addActionListener(e -> {
            controller.nextGeneration();
            revalidate();
            repaint();
        });

        return nextBtn;
    }

    private JButton playToggleBtn() {
        JButton playToggleBtn = defaultBtn("resources/icons/play.png");

        final boolean[] pause = {true};

        playToggleBtn.addActionListener(e -> {
            if (pause[0]) {
                timer.start();
                playToggleBtn.setIcon(scaleIcon("resources/icons/pause.png", 32, 32));
                pause[0] = false;
            } else {
                timer.stop();
                playToggleBtn.setIcon(scaleIcon("resources/icons/play.png", 32, 32));
                pause[0] = true;
            }
        });

        return playToggleBtn;
    }

    private JButton resetBtn() {
        JButton resetBtn = defaultBtn("resources/icons/reset.png");

        resetBtn.addActionListener(e -> {
            controller.generateInitialState();
            revalidate();
            repaint();
        });

        return resetBtn;
    }

    private JButton defaultBtn(String iconSrc) {
        JButton defaultBtn = new JButton();

        defaultBtn.setPreferredSize(new Dimension(32, 32));
        defaultBtn.setIcon(scaleIcon(iconSrc, 32, 32));
        defaultBtn.setBackground(Color.WHITE);
        defaultBtn.setBorder(null);

        return defaultBtn;
    }

    private JPanel infoPanel() {
        JPanel info = new JPanel();

        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        info.setOpaque(false);

        JLabel generationLabel = new JLabel();
        generationLabel.setText(String.format("Generation: #%d", controller.getUniverse().getGeneration()));
        this.generationLabel = generationLabel;

        JLabel aliveLabel = new JLabel();
        aliveLabel.setText(String.format("Alive: %d", controller.getUniverse().getAlive()));
        this.aliveLabel = aliveLabel;

        info.add(generationLabel);
        info.add(aliveLabel);

        return info;
    }

    private JSlider speedSlider() {
        JSlider speedSlider = new JSlider(1, 9, 5);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();

        labels.put(1, new JLabel("1"));
        labels.put(2, new JLabel("2"));
        labels.put(3, new JLabel("3"));
        labels.put(4, new JLabel("4"));
        labels.put(5, new JLabel("5"));
        labels.put(6, new JLabel("6"));
        labels.put(7, new JLabel("7"));
        labels.put(8, new JLabel("8"));
        labels.put(9, new JLabel("9"));

        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setLabelTable(labels);
        speedSlider.setPaintLabels(true);
        speedSlider.setOpaque(false);
        speedSlider.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        speedSlider.addChangeListener((e) -> {
            timer.setDelay(1000 - speedSlider.getValue() * 100);
            revalidate();
            repaint();
        });

        return speedSlider;
    }

    private JPanel sizeSetting() {
        JTextField sizeSettingTextField = new JTextField();
        sizeSettingTextField.setPreferredSize(new Dimension(50, 30));
        sizeSettingTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                sizeSettingTextField.setEditable(e.getKeyChar() >= '0'
                        && e.getKeyChar() <= '9'
                        || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                        || e.getKeyCode() == KeyEvent.VK_LEFT
                        || e.getKeyCode() == KeyEvent.VK_RIGHT);
            }
        });

        JButton sizeSettingBtn = new JButton("Enter");
        sizeSettingBtn.setBackground(Color.WHITE);
        sizeSettingBtn.addActionListener((e) -> {
            int size = Integer.parseInt(sizeSettingTextField.getText().equals("")
                    ? "1"
                    : sizeSettingTextField.getText());
            controller.setUniverse(new Universe(size));
            controller.generateInitialState();
            revalidate();
            repaint();
        });

        JPanel sizeSettingPanel = new JPanel();
        sizeSettingPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        sizeSettingPanel.setMaximumSize(new Dimension(150, 50));
        sizeSettingPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        sizeSettingPanel.setOpaque(false);

        sizeSettingPanel.add(sizeSettingTextField);
        sizeSettingPanel.add(sizeSettingBtn);

        return sizeSettingPanel;
    }

    private JPanel cellsField() {
        JPanel cellsField = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int numOfCells = controller.getUniverse().getCurrentState().length;
                int width = this.getSize().width / numOfCells;
                int height = this.getSize().height / numOfCells;

                for (int i = 0; i < numOfCells; i++) {
                    for (int j = 0; j < numOfCells; j++) {
                        g.setColor(new Color(ColorPalette.CELL.getR(),
                                ColorPalette.CELL.getG(),
                                ColorPalette.CELL.getB()));
                        if (controller.getUniverse().getCurrentState()[i][j]) {
                            g.fillRect(j * width, i * height, width, height);
                        }
                        g.setColor(Color.WHITE);
                        g.drawRect(j * width, i * height, width, height);
                    }
                }
            }
        };
        cellsField.setPreferredSize(new Dimension(500, 500));
        setColorFor(cellsField, ColorPalette.CELLS_FIELD);

        return cellsField;
    }

    private void setColorFor(JPanel panel, ColorPalette colorPalette) {
        panel.setBackground(new Color(colorPalette.getR(), colorPalette.getG(), colorPalette.getB()));
    }

    private ImageIcon scaleIcon(String src, int width, int height) {
        return new ImageIcon(new ImageIcon(src).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }
}