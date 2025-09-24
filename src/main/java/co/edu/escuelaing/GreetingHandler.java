package co.edu.escuelaing;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GreetingHandler implements HttpHandler {

    private static final String TEMPLATE = "Hello, %s!";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        URI requestURI = exchange.getRequestURI();
        Map<String, String> queryParams = splitQuery(requestURI.getRawQuery());
        String name = queryParams.getOrDefault("name", "World");
        String response = String.format(TEMPLATE, name);

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // parse query string into map
    private Map<String, String> splitQuery(String rawQuery) {
        Map<String, String> result = new HashMap<>();
        if (rawQuery == null || rawQuery.isEmpty()) return result;
        String[] pairs = rawQuery.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8.name());
                    String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8.name());
                    result.put(key, value);
                } else {
                    String key = URLDecoder.decode(pair, StandardCharsets.UTF_8.name());
                    result.put(key, "");
                }
            } catch (UnsupportedEncodingException e) {
                // won't happen - UTF-8 is supported
            }
        }
        return result;
    }
}
