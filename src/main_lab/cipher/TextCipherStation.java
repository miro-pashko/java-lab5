package cipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

/**
 * Encapsulates file-level encryption/decryption through the shift-cipher
 * filter streams.
 */
public class TextCipherStation {

    public void lock(Path plainSource, Path cipherDestination, char key) throws IOException {
        try (Reader in = new BufferedReader(new FileReader(plainSource.toFile()));
             Writer out = new ShiftCipherWriter(new BufferedWriter(new FileWriter(cipherDestination.toFile())), key)) {
            relay(in, out);
        }
    }

    public void unlock(Path cipherSource, Path plainDestination, char key) throws IOException {
        try (Reader in = new ShiftCipherReader(new BufferedReader(new FileReader(cipherSource.toFile())), key);
             Writer out = new BufferedWriter(new FileWriter(plainDestination.toFile()))) {
            relay(in, out);
        }
    }

    private void relay(Reader in, Writer out) throws IOException {
        var buffer = new char[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
