package cipher;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reverses ShiftCipherWriter's transformation: subtracts the same key
 * character's code value from every character read.
 *
 * Both read() and read(char[],int,int) are overridden deliberately:
 * FilterReader's default read(char[],int,int) delegates straight to the
 * wrapped Reader without going through read(), so overriding only read()
 * would silently skip decryption for bulk reads (which is what
 * BufferedReader normally does internally).
 */
public class ShiftCipherReader extends FilterReader {

    private final int shiftAmount;

    public ShiftCipherReader(Reader source, char key) {
        super(source);
        this.shiftAmount = key;
    }

    @Override
    public int read() throws IOException {
        int codePoint = in.read();
        return codePoint == -1 ? -1 : decode(codePoint);
    }

    @Override
    public int read(char[] buffer, int offset, int length) throws IOException {
        int n = in.read(buffer, offset, length);
        for (int i = 0; i < n; i++) {
            buffer[offset + i] = (char) decode(buffer[offset + i]);
        }
        return n;
    }

    private int decode(int codePoint) {
        return (char) (codePoint - shiftAmount);
    }
}
