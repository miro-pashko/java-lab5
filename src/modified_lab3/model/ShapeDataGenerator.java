package model;

import java.util.Random;

/**
 * Builds the Shape[] dataset. Values are drawn from small pre-prepared
 * pools (not free-form user input): either sequentially, cycling through
 * shape type/color/dimension pools in a fixed rotation, or randomly.
 */
public final class ShapeDataGenerator {

    private static final String[] COLORS =
            {"Red", "Green", "Blue", "Yellow", "Black", "White", "Orange", "Purple"};

    private static final double[] RECT_WIDTHS = {2.0, 3.5, 4.0, 5.5, 6.0};
    private static final double[] RECT_HEIGHTS = {1.5, 2.5, 3.0, 4.5, 5.0};

    private static final double[] TRIANGLE_BASES = {3.0, 4.0, 5.0, 6.0, 7.0};
    private static final double[] TRIANGLE_HEIGHTS = {2.0, 3.0, 4.0, 5.0, 6.0};

    private static final double[] CIRCLE_RADII = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5};

    private ShapeDataGenerator() {
        // utility class
    }

    /**
     * @param count  number of shapes to generate (dataset size)
     * @param random if true, values are picked at random from the pools
     *               above; if false, they are picked in sequential
     *               (round-robin) order for repeatable output
     */
    public static Shape[] generate(int count, boolean random) {
        Shape[] shapes = new Shape[count];
        Random rnd = random ? new Random() : null;

        for (int i = 0; i < count; i++) {
            int typeIndex = random ? rnd.nextInt(3) : i % 3;
            String color = random ? pick(rnd, COLORS) : COLORS[i % COLORS.length];

            switch (typeIndex) {
                case 0:
                    double w = random ? pick(rnd, RECT_WIDTHS) : RECT_WIDTHS[i % RECT_WIDTHS.length];
                    double h = random ? pick(rnd, RECT_HEIGHTS) : RECT_HEIGHTS[i % RECT_HEIGHTS.length];
                    shapes[i] = new Rectangle(color, w, h);
                    break;
                case 1:
                    double base = random ? pick(rnd, TRIANGLE_BASES) : TRIANGLE_BASES[i % TRIANGLE_BASES.length];
                    double height = random ? pick(rnd, TRIANGLE_HEIGHTS) : TRIANGLE_HEIGHTS[i % TRIANGLE_HEIGHTS.length];
                    shapes[i] = new Triangle(color, base, height);
                    break;
                default:
                    double radius = random ? pick(rnd, CIRCLE_RADII) : CIRCLE_RADII[i % CIRCLE_RADII.length];
                    shapes[i] = new Circle(color, radius);
                    break;
            }
        }
        return shapes;
    }

    private static String pick(Random rnd, String[] pool) {
        return pool[rnd.nextInt(pool.length)];
    }

    private static double pick(Random rnd, double[] pool) {
        return pool[rnd.nextInt(pool.length)];
    }
}
