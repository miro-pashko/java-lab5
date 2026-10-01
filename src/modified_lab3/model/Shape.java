package model;

import java.io.Serializable;

/**
 * Common base for every shape in the dataset: holds the shape's color and
 * declares the area calculation every concrete shape must provide.
 *
 * Implements Serializable (with no custom writeObject/readObject) so that
 * a Shape[] dataset can be saved/loaded via default serialization; every
 * subclass (Rectangle, Triangle, Circle) becomes Serializable for free,
 * since their own fields are primitives and shapeColor is a String.
 */
public abstract class Shape implements Drawable, Serializable {

    private static final long serialVersionUID = 1L;

    protected final String shapeColor;

    protected Shape(String shapeColor) {
        this.shapeColor = shapeColor;
    }

    public String getShapeColor() {
        return shapeColor;
    }

    /** Computes this shape's area. Implemented differently by every subclass. */
    public abstract double calcArea();

    @Override
    public String toString() {
        return String.format("%s[color=%s, area=%.2f]", getClass().getSimpleName(), shapeColor, calcArea());
    }
}
