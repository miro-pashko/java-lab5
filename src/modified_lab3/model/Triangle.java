package model;

public class Triangle extends Shape {

    private final double base;
    private final double height;

    public Triangle(String shapeColor, double base, double height) {
        super(shapeColor);
        this.base = base;
        this.height = height;
    }

    public double getBase() {
        return base;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double calcArea() {
        return 0.5 * base * height;
    }

    @Override
    public void draw() {
        System.out.printf("Drawing a %s triangle, base %.2f, height %.2f.%n", shapeColor, base, height);
    }

    @Override
    public String toString() {
        return String.format("Triangle[color=%s, base=%.2f, height=%.2f, area=%.2f]",
                shapeColor, base, height, calcArea());
    }
}
