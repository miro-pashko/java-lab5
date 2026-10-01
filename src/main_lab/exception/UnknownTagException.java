package exception;

/**
 * Thrown when the tag name entered as a search criterion is not present
 * in the current TagCensus.
 */
public class UnknownTagException extends Exception {
    public UnknownTagException(String message) {
        super(message);
    }
}
