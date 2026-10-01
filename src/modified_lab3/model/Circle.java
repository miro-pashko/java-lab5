package model;

public class Circle extends Shape {

    private final double radius;

    public Circle(String shapeColor, double radius) {
        super(shapeColor);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public double calcArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public void draw() {
        System.out.printf("Drawing a %s circle, radius %.2f.%n", shapeColor, radius);
    }

    @Override
    public String toString() {
        return String.format("Circle[color=%s, radius=%.2f, area=%.2f]", shapeColor, radius, calcArea());
    }
}
