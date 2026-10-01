package tags;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Downloads a page and tallies how often each HTML tag name appears
 * (opening tags only, to avoid double-counting a tag and its closing form).
 */
public class PageTagCounter {

    private static final Pattern OPENING_TAG = Pattern.compile("<\\s*([a-zA-Z][a-zA-Z0-9]*)");

    public String download(String pageUrl) throws IOException, InterruptedException {
        var client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        var request = HttpRequest.newBuilder(URI.create(pageUrl))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("HTTP " + response.statusCode() + " while fetching " + pageUrl);
        }
        return response.body();
    }

    public Map<String, Integer> tally(String html) {
        Map<String, Integer> counts = new HashMap<>();
        Matcher matcher = OPENING_TAG.matcher(html);
        while (matcher.find()) {
            counts.merge(matcher.group(1).toLowerCase(), 1, Integer::sum);
        }
        return counts;
    }

    public List<Map.Entry<String, Integer>> byNameAscending(Map<String, Integer> tally) {
        var entries = new ArrayList<>(tally.entrySet());
        entries.sort(Map.Entry.comparingByKey());
        return entries;
    }

    public List<Map.Entry<String, Integer>> byFrequencyAscending(Map<String, Integer> tally) {
        var entries = new ArrayList<>(tally.entrySet());
        entries.sort(Map.Entry.comparingByValue());
        return entries;
    }
}
