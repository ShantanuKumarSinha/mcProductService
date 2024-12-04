package dev.shann.mcproductservice.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectionWrapper {
    private URL url;

    public HttpConnectionWrapper(URL url) {
        this.url = url;
    }

    public String makeRequest() throws IOException {
        HttpURLConnection connection = openConnection(url);
        connection.setRequestMethod("POST");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            InputStream inputStream = connection.getInputStream();
            // Read the response from the input stream
            // ...
            return "true";
        } else {
            return "false";
        }
    }

    protected HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }
}
