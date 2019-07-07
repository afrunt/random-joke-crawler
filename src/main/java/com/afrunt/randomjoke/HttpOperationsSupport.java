package com.afrunt.randomjoke;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @author Andrii Frunt
 */
public interface HttpOperationsSupport {
    default HttpURLConnection openConnection(String url) {
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

    default InputStream inputStreamFromUrl(String url) {
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

    default InputStream inputStreamFromUrl(String url, Map<String, String> headers) {
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

    default List<String> stringsFromUrl(String url) {
        Scanner sc = new Scanner(inputStreamFromUrl(url));
        List<String> lines = new ArrayList<>();

        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        return lines;
    }

    default JSONObject jsonObjectFromUrl(String url) {
        return inputStreamToJsonObject(inputStreamFromUrl(url));
    }

    default String inputStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
        }

        return sb.toString();
    }

    default JSONObject inputStreamToJsonObject(InputStream is) {
        return new JSONObject(new JSONTokener(is));
    }

    default JSONObject jsonObjectFromUrl(String url, Map<String, String> headers) {
        return inputStreamToJsonObject(inputStreamFromUrl(url, headers));
    }

    default TagNode tagNodeFromUrl(String url) {
        return tagNodeFromUrl(url, new HashMap<>());
    }

    default TagNode tagNodeFromUrl(String url, Map<String, String> headers) {
        return getCleaner().clean(stringFromUrl(url, headers));
    }

    default String stringFromUrl(String url, Map<String, String> headers) {
        return inputStreamToString(inputStreamFromUrl(url, headers));
    }

    default String stringFromUrl(String url) {
        return stringFromUrl(url, new HashMap<>());
    }

    HtmlCleaner getCleaner();
}
