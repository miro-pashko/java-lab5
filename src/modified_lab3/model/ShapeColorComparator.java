package model;

import java.util.Comparator;

/**
 * Orders shapes alphabetically by color (case-insensitive).
 */
public class ShapeColorComparator implements Comparator<Shape> {
    @Override
    public int compare(Shape a, Shape b) {
        return a.getShapeColor().compareToIgnoreCase(b.getShapeColor());
    }
}
