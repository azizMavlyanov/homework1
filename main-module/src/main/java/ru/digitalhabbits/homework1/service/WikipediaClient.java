package ru.digitalhabbits.homework1.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WikipediaClient {
    public static final String WIKIPEDIA_SEARCH_URL = "https://en.wikipedia.org/w/api.php";

    @Nonnull
    public String search(@Nonnull String searchString) {
        final URI uri = prepareSearchUrl(searchString);
        final HttpGet request = new HttpGet(uri);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            final String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            final Gson gson = new Gson();
            final JsonObject wikipediaResponse = gson.fromJson(responseBody, JsonObject.class);
            final JsonObject pages = wikipediaResponse
                    .getAsJsonObject("query")
                    .getAsJsonObject("pages");
            final List<String> listOfKeys = new ArrayList<>(pages.keySet());
            final JsonElement extract = pages
                    .getAsJsonObject(listOfKeys.get(0))
                    .get("extract");

            return extract.getAsString().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    private URI prepareSearchUrl(@Nonnull String searchString) {
        try {
            return new URIBuilder(WIKIPEDIA_SEARCH_URL)
                    .addParameter("action", "query")
                    .addParameter("format", "json")
                    .addParameter("titles", searchString)
                    .addParameter("prop", "extracts")
                    .addParameter("explaintext", "")
                    .build();
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }
}
