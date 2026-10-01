package io;

import model.Shape;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

/**
 * All file operations for the Shape[] dataset are encapsulated here, kept
 * separate from the model/view/controller layers. Uses
 * ObjectOutputStream/ObjectInputStream (object streams) and relies
 * entirely on Shape's default serialization (no custom
 * writeObject/readObject anywhere in the Shape hierarchy). The
 * destination/source path is a parameter, not hard-coded, so the caller
 * can specify the save location and filename.
 */
public class ShapeDatasetFileManager {

    public void save(Shape[] shapes, Path destination) throws IOException {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination.toFile())))) {
            oos.writeObject(shapes);
        }
    }

    public Shape[] load(Path source) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())))) {
            Object obj = ois.readObject();
            if (!(obj instanceof Shape[])) {
                throw new InvalidObjectException(source + " does not contain a Shape[] dataset.");
            }
            return (Shape[]) obj;
        }
    }
}
