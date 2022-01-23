package net.avicus.compendium.utils;

import com.google.common.collect.Maps;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * A key->value representation of objects which can be parsed from .properties and .txt files.
 *
 * @author Austin Mayes
 * @param <V> type of value being stored for each key
 */
public abstract class FileBackedKVSet<V> {

  private final Map<String, V> data;

  /** Constructor. */
  public FileBackedKVSet() {
    this(new HashMap<>());
  }

  /**
   * Constructor.
   *
   * @param data that makes up this collection
   */
  public FileBackedKVSet(Map<String, V> data) {
    this.data = data;
  }

  protected abstract V parse(String raw);

  /**
   * Load in strings from a collection of .properties and .txt files and add them to the collection.
   *
   * @param root path of the files
   * @throws IOException if a file fails to load
   */
  public void load(Path root) throws IOException {
    Files.walk(root)
        .filter(Files::isRegularFile)
        .forEach(
            (f) -> {
              String file = f.toString();
              if (file.endsWith(".properties")) {
                try (InputStream stream = new FileInputStream(file)) {
                  Properties prop = new Properties();
                  prop.load(stream);
                  for (Entry<Object, Object> entry : prop.entrySet()) {
                    Object k = entry.getKey();
                    Object v = entry.getValue();
                    V parsed = parse(v.toString());
                    if (parsed != null) add(k.toString(), parsed);
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              } else if (file.endsWith(".txt")) {
                try {
                  byte[] bytes = Files.readAllBytes(f.toAbsolutePath());
                  String fileName = f.getFileName().toString();
                  String key = fileName.substring(0, fileName.length() - 4);
                  V parsed = parse(new String(bytes, StandardCharsets.UTF_8));
                  if (parsed != null) add(key, parsed);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              } else if (file.endsWith(".xml")) {
                SAXBuilder sax = new SAXBuilder();
                try (InputStream stream = new FileInputStream(file)) {
                  Document doc = sax.build(stream);
                  addAll(fromXml(doc.getRootElement()));
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
  }

  /**
   * Create from an XML element.
   *
   * @param el The &lt;locale lang="en" country="us"/&gt;
   */
  public Map<String, V> fromXml(Element el) {
    Map<String, V> data = Maps.newHashMap();
    for (Content content : el.getDescendants()) {
      if (content instanceof Element) {
        Element child = (Element) content;

        if (child.getChildren().size() > 0) {
          continue;
        }

        String path = getPath(el, child);
        data.put(path, parse(child.getTextTrim().replaceAll(" +", " ")));
      }
    }

    return data;
  }

  private static String getPath(Element exclude, Element nested) {
    String result = nested.getName();
    Element curr = nested.getParentElement();
    while (!curr.equals(exclude)) {
      result = curr.getName() + "." + result;
      curr = curr.getParentElement();
    }
    return result;
  }

  public void addAll(Map<String, V> toAdd) {
    data.putAll(toAdd);
  }

  /**
   * Add a value represented by a key to the collection of parsed data
   *
   * @param key identifying the value
   * @param value to add to the collection
   */
  public void add(String key, V value) {
    this.data.put(key, value);
  }

  /**
   * Get a value from the collection corresponding to the given key, if one exists.
   *
   * @param key to search for
   * @return a value from the collection corresponding to the given key
   */
  public Optional<V> get(String key) {
    return Optional.ofNullable(data.get(key));
  }

  @Override
  public String toString() {
    return "FileBackedKVSet{" + "data=" + data + '}';
  }
}
