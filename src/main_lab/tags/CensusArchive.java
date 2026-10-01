package tags;

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
 * All file operations for TagCensus are encapsulated here, using
 * ObjectOutputStream/ObjectInputStream with TagCensus's default
 * serialization. The path is always supplied by the caller, never
 * hard-coded.
 */
public class CensusArchive {

    public void store(TagCensus census, Path destination) throws IOException {
        try (var out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination.toFile())))) {
            out.writeObject(census);
        }
    }

    public TagCensus retrieve(Path source) throws IOException, ClassNotFoundException {
        try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())))) {
            var obj = in.readObject();
            if (!(obj instanceof TagCensus census)) {
                throw new InvalidObjectException(source + " does not hold a TagCensus.");
            }
            return census;
        }
    }
}
