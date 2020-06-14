package life;

public enum ColorPalette {
    MAIN(255, 255, 255),       // White
    CELLS_FIELD(249, 220, 92), // Naples Yellow
    CELL(1, 25, 54);           // Oxford Blue

    private final int r;
    private final int g;
    private final int b;

    ColorPalette(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}
