package life;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Universe(10));
        controller.run();
    }
}
