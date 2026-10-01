package model;

public class Rectangle extends Shape {

    private final double width;
    private final double height;

    public Rectangle(String shapeColor, double width, double height) {
        super(shapeColor);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double calcArea() {
        return width * height;
    }

    @Override
    public void draw() {
        System.out.printf("Drawing a %s rectangle, %.2f x %.2f.%n", shapeColor, width, height);
    }

    @Override
    public String toString() {
        return String.format("Rectangle[color=%s, width=%.2f, height=%.2f, area=%.2f]",
                shapeColor, width, height, calcArea());
    }
}
