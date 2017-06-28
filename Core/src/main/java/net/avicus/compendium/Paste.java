package net.avicus.compendium;

import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Paste {
    private static String pasteURL = "https://paste.avicus.net/";

    private final String title;
    private final String author;
    private final String text;
    private final boolean raw;

    public Paste(String title, String author, String text) {
        this(title, author, text, false);
    }

    public Paste(String title, String author, String text, boolean raw) {
        this.title = title;
        this.author = author;
        this.text = text;
        this.raw = raw;
    }

    public String upload() {
        StringBuilder text = new StringBuilder();
        text.append("Title: " + this.title);
        text.append("\n");
        text.append("Author: " + this.author);
        text.append("\n\n");
        text.append(this.text);

        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(pasteURL + "documents");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(text.toString());
            wr.flush();
            wr.close();

            //Get Response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return pasteURL + (this.raw? "raw/" : "") + new JsonParser().parse(rd.readLine()).getAsJsonObject().get("key").getAsString();

        } catch (IOException e) {
            return null;
        } finally {
            if (connection == null) return null;
            connection.disconnect();
        }
    }
}
