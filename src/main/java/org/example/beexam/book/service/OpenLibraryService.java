package org.example.beexam.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenLibraryService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String OPEN_LIBRARY_API_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:";

    public Map<String, Object> fetchBookMetadata(String isbn) {
        String url = OPEN_LIBRARY_API_URL + isbn + "&jscmd=data&format=json";

        try {
            String responseBody = restTemplate.getForObject(url, String.class);

            if (responseBody == null || responseBody.isBlank()) {
                throw new RuntimeException("Risposta vuota da OpenLibrary per l'ISBN: " + isbn);
            }

            JsonNode response = objectMapper.readTree(responseBody);
            String objectKey = "ISBN:" + isbn;

            if (response != null && response.has(objectKey)) {
                JsonNode bookData = response.get(objectKey);

                Map<String, Object> metadata = new HashMap<>();
                metadata.put("isbn", isbn);
                metadata.put("title", bookData.has("title") ? bookData.get("title").asText() : "Titolo non disponibile");

                if (bookData.has("publish_date")) {
                    String rawDate = bookData.get("publish_date").asText();
                    String year = rawDate.length() >= 4 ? rawDate.substring(rawDate.length() - 4) : rawDate;
                    metadata.put("publishYear", Integer.parseInt(year));
                }

                metadata.put("pages", bookData.has("number_of_pages") ? bookData.get("number_of_pages").asInt() : 0);

                if (bookData.has("authors") && bookData.get("authors").isArray()) {
                    metadata.put("suggestedAuthor", bookData.get("authors").get(0).get("name").asText());
                }

                return metadata;
            }

            throw new RuntimeException("Nessun libro trovato su OpenLibrary per l'ISBN: " + isbn);

        } catch (Exception e) {
            throw new RuntimeException("Errore durante il recupero dei metadati: " + e.getMessage());
        }
    }
}