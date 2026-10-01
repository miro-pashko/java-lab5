package controller;

import io.ShapeDatasetFileManager;
import model.Circle;
import model.Rectangle;
import model.Shape;
import model.ShapeAreaComparator;
import model.ShapeColorComparator;
import model.ShapeDataGenerator;
import model.Triangle;
import view.ShapeView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Coordinates the Shape[] model with the ShapeView: reads menu choices
 * from the view, performs the requested operation on the dataset, and
 * hands the results back to the view to display.
 */
public class ShapeController {

    private final ShapeView view;
    private final ShapeDatasetFileManager fileManager = new ShapeDatasetFileManager();
    private Shape[] shapes;

    public ShapeController(ShapeView view, int datasetSize, boolean randomData) {
        this.view = view;
        this.shapes = ShapeDataGenerator.generate(datasetSize, randomData);
    }

    public void run() {
        boolean running = true;
        while (running) {
            view.printMenu();
            String choice = view.readMenuChoice();
            switch (choice) {
                case "1":
                    view.displayShapes(shapes);
                    break;
                case "2":
                    view.displayTotalArea(totalArea(shapes));
                    break;
                case "3":
                    totalAreaBySpecifiedType();
                    break;
                case "4":
                    sortByArea();
                    break;
                case "5":
                    sortByColor();
                    break;
                case "6":
                    saveDataset();
                    break;
                case "7":
                    loadDataset();
                    break;
                case "0":
                    running = false;
                    view.displayMessage("Exiting. Goodbye!");
                    break;
                default:
                    view.displayMessage("Unknown option.\n");
            }
        }
        view.close();
    }

    private double totalArea(Shape[] source) {
        double total = 0.0;
        for (Shape shape : source) {
            total += shape.calcArea();
        }
        return total;
    }

    private void totalAreaBySpecifiedType() {
        int choice = view.promptShapeTypeChoice();
        Class<? extends Shape> type;
        String typeName;
        switch (choice) {
            case 1:
                type = Rectangle.class;
                typeName = "Rectangle";
                break;
            case 2:
                type = Triangle.class;
                typeName = "Triangle";
                break;
            default:
                type = Circle.class;
                typeName = "Circle";
                break;
        }

        double total = 0.0;
        int matchCount = 0;
        for (Shape shape : shapes) {
            if (type.isInstance(shape)) {
                total += shape.calcArea();
                matchCount++;
            }
        }
        view.displayTotalAreaByType(typeName, total, matchCount);
    }

    private void sortByArea() {
        Arrays.sort(shapes, new ShapeAreaComparator());
        view.displaySortResult("increasing area");
        view.displayShapes(shapes);
    }

    private void sortByColor() {
        Arrays.sort(shapes, new ShapeColorComparator());
        view.displaySortResult("color");
        view.displayShapes(shapes);
    }

    private void saveDataset() {
        Path destination = view.promptOutputFilePath("\nEnter the path to save the dataset to: ");
        try {
            fileManager.save(shapes, destination);
            view.displayMessage("Saved " + shapes.length + " shapes to " + destination + "\n");
        } catch (IOException e) {
            view.displayMessage("  Error saving dataset: " + e.getMessage() + "\n");
        }
    }

    private void loadDataset() {
        Path source = view.promptExistingFilePath("\nEnter the path of a saved dataset file: ");
        try {
            Shape[] loaded = fileManager.load(source);
            this.shapes = loaded;
            view.displayMessage("Loaded " + loaded.length + " shapes from " + source + "\n");
        } catch (IOException | ClassNotFoundException e) {
            view.displayMessage("  Error loading dataset: " + e.getMessage() + "\n");
        }
    }
}
