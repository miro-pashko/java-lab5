package model;

import java.util.Comparator;

/**
 * Orders shapes by increasing area.
 */
public class ShapeAreaComparator implements Comparator<Shape> {
    @Override
    public int compare(Shape a, Shape b) {
        return Double.compare(a.calcArea(), b.calcArea());
    }
}
