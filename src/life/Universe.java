package life;

class Universe {
    private boolean[][] currentState;
    private int alive;
    private int generation;

    public Universe(int size) {
        this.currentState = new boolean[size][size];
        alive = 0;
        generation = 0;
    }

    public boolean[][] getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(boolean[][] currentState) {
        this.currentState = currentState;
    }

    public int getAlive() {
        return this.alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public int getGeneration() {
        return this.generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }
}
