package cipher;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * A FilterWriter that shifts every character's code point upward by the
 * code value of a single key character before it reaches the wrapped
 * stream.
 *
 * All three write(...) overloads are overridden deliberately:
 * FilterWriter's default write(char[],int,int) and write(String,int,int)
 * delegate straight to the wrapped Writer without going through
 * write(int), so overriding only write(int) would silently skip
 * encryption for bulk writes (which is what BufferedWriter normally does).
 */
public class ShiftCipherWriter extends FilterWriter {

    private final int shiftAmount;

    public ShiftCipherWriter(Writer destination, char key) {
        super(destination);
        this.shiftAmount = key;
    }

    @Override
    public void write(int codePoint) throws IOException {
        out.write(encode(codePoint));
    }

    @Override
    public void write(char[] buffer, int offset, int length) throws IOException {
        var encoded = new char[length];
        for (int i = 0; i < length; i++) {
            encoded[i] = encode(buffer[offset + i]);
        }
        out.write(encoded, 0, length);
    }

    @Override
    public void write(String text, int offset, int length) throws IOException {
        var encoded = new char[length];
        for (int i = 0; i < length; i++) {
            encoded[i] = encode(text.charAt(offset + i));
        }
        out.write(encoded, 0, length);
    }

    private char encode(int codePoint) {
        return (char) (codePoint + shiftAmount);
    }
}
