import model.*;
import controller.ShapeController;
import view.ShapeView;

/**
 * Entry point: wires up the View and Controller (Model is created inside
 * the Controller) and starts the menu loop.
 *
 * Dataset size and generation mode (random vs. sequential) are set here;
 * change RANDOM_DATA to false for repeatable, deterministic output.
 */
public class Main {

    private static final int DATASET_SIZE = 12;
    private static final boolean RANDOM_DATA = true;

    public static void main(String[] args) {
        ShapeView view = new ShapeView();
        ShapeController controller = new ShapeController(view, DATASET_SIZE, RANDOM_DATA);
        controller.run();
    }
}
