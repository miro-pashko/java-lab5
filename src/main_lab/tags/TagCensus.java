package tags;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

/**
 * The dataset saved/loaded via object streams. No writeObject/readObject
 * overrides here - serialization is entirely the JVM's default mechanism.
 */
public class TagCensus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String pageUrl;
    private final LocalDateTime takenAt;
    private final Map<String, Integer> tally;

    public TagCensus(String pageUrl, Map<String, Integer> tally) {
        this.pageUrl = pageUrl;
        this.takenAt = LocalDateTime.now();
        this.tally = new TreeMap<>(tally);
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public Map<String, Integer> getTally() {
        return tally;
    }

    @Override
    public String toString() {
        return "TagCensus{url=%s, takenAt=%s, distinctTags=%d}".formatted(pageUrl, takenAt, tally.size());
    }
}
