package net.avicus.compendium;

import org.javalite.http.Http;
import org.javalite.http.Post;

public class Paste {
    private final String title;
    private final String author;
    private final String text;

    public Paste(String title, String author, String text) {
        this.title = title;
        this.author = author;
        this.text = text;
    }

    public String upload() {
        String params = String.format("title=%s&name=%s&text=%s", this.title, this.author, this.text);
        Post post = Http.post("https://paste.keenant.com/api/create", params);
        post.header("User-Agent", "Java");
        return post.text().replace("\n", "").replace("\r", "");
    }
}
