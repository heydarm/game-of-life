package life;

import java.util.Random;

class Controller {
    private Universe universe;
    private GameOfLife gui;

    public Controller(Universe universe) {
        this.universe = universe;
    }

    public Universe getUniverse() {
        return universe;
    }

    public void setUniverse(Universe universe) {
        this.universe = universe;
    }

    public void run() {
        gui = new GameOfLife();
        gui.setController(this);
        gui.main();
        generateInitialState();
    }

    public void nextGeneration() {
        int len = universe.getCurrentState().length;
        boolean[][] nextState = new boolean[len][len];

        for (int i = 0; i < len; ++i) {
            for (int j = 0; j < len; ++j) {
                if (!universe.getCurrentState()[i][j]) {
                    nextState[i][j] = countNeighbors(i, j) == 3;
                } else {
                    nextState[i][j] = countNeighbors(i, j) == 3 || countNeighbors(i, j) == 2;
                }
            }
        }

        universe.setCurrentState(nextState);
        universe.setAlive(countAlive());
        universe.setGeneration(universe.getGeneration() + 1);
        sendInfoToGui();
    }

    private boolean findNeighbor(Neighbor neighbor, int i, int j) {
        int xAxis = j + neighbor.getX();
        int yAxis = i + neighbor.getY();
        int len = this.universe.getCurrentState().length;

        xAxis = xAxis < 0 ? len + xAxis % len : xAxis % len;
        yAxis = yAxis < 0 ? len + yAxis % len : yAxis % len;

        return this.universe.getCurrentState()[yAxis][xAxis];
    }

    private int countNeighbors(int i, int j) {
        int count = 0;

        for (Neighbor n : Neighbor.values()) {
            if (findNeighbor(n, i, j)) {
                ++count;
            }
        }

        return count;
    }

    public void generateInitialState() {
        Random random = new Random();
        int len = universe.getCurrentState().length;

        for (int i = 0; i < len; ++i) {
            for (int j = 0; j < len; ++j) {
                universe.getCurrentState()[i][j] = random.nextBoolean();
            }
        }

        universe.setGeneration(1);
        universe.setAlive(countAlive());
        sendInfoToGui();
    }

    public int countAlive() {
        int count = 0;

        for (boolean[] cells : universe.getCurrentState()) {
            for (boolean cell : cells) {
                if (cell) {
                    ++count;
                }
            }
        }

        return count;
    }

    private void sendInfoToGui() {
        gui.getGenerationLabel().setText(String.format("Generation: #%d", universe.getGeneration()));
        gui.getAliveLabel().setText(String.format("Alive: %d", universe.getAlive()));
    }
}
