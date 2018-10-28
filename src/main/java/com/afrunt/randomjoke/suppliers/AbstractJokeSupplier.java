package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * @author Andrii Frunt
 */
public abstract class AbstractJokeSupplier implements Supplier<Joke> {
    private HtmlCleaner cleaner = new HtmlCleaner();

    public abstract String getSource();

    protected String inputStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
        }

        return sb.toString();
    }

    protected JSONObject inputStreamToJsonObject(InputStream is) {
        return new JSONObject(new JSONTokener(is));
    }

    protected JSONObject jsonObjectFromUrl(String url, Map<String, String> headers) {
        return inputStreamToJsonObject(inputStreamFromUrl(url, headers));
    }

    protected TagNode tagNodeFromUrl(String url) {
        return tagNodeFromUrl(url, new HashMap<>());
    }

    protected TagNode tagNodeFromUrl(String url, Map<String, String> headers) {
        return cleaner.clean(stringFromUrl(url, headers));
    }

    private String stringFromUrl(String url, Map<String, String> headers) {
        return inputStreamToString(inputStreamFromUrl(url, headers));
    }

    protected String stringFromUrl(String url) {
        return stringFromUrl(url, new HashMap<>());
    }

    private InputStream inputStreamFromUrl(String url, Map<String, String> headers) {
        try {
            HttpURLConnection conn = openConnection(url);
            headers.forEach(conn::setRequestProperty);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return conn.getInputStream();
            } else {
                throw new RuntimeException("Cannot read from " + url + ". Response code is " + conn.getResponseCode());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected JSONObject jsonObjectFromUrl(String url) {
        return inputStreamToJsonObject(inputStreamFromUrl(url));
    }

    protected InputStream inputStreamFromUrl(String url) {
        try {
            HttpURLConnection conn = openConnection(url);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return conn.getInputStream();
            } else {
                throw new RuntimeException("Cannot read from " + url + ". Response code is " + conn.getResponseCode());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected HttpURLConnection openConnection(String url) {
        try {
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            HttpURLConnection conn;
            conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setRequestProperty("User-Agent", "random-joke");
            return conn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String removeSurroundingQuotes(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1);
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }
}
