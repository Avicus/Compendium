package net.avicus.compendium.config;

import lombok.Getter;
import net.avicus.compendium.config.inject.ConfigInjector;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Config {
    protected static final Yaml yaml = new Yaml();

    @Getter private final Map<String, Object> data;

    public Config(Map<String, Object> data) {
        this.data = data;
    }

    public Config() {
        this.data = new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    private Config(Object object) {
        this((Map<String, Object>) object);
    }

    public Config(String raw) {
        this(yaml.load(raw));
    }

    public Config(InputStream stream) {
        this(yaml.load(stream));
    }

    public Config(Reader reader) {
        this(yaml.load(reader));
    }

    @Override
    public String toString() {
        return yaml.dump(this.data);
    }

    public void save(File file) {
        try {
            yaml.dump(this.data, new FileWriter(file));
        } catch (IOException e) {
            throw new ConfigException("unable to write to file", e);
        }
    }

    public boolean absent(String key) {
        return !exists(key);
    }

    public boolean contains(String key) {
        return exists(key);
    }

    public boolean exists(String key) {
        return this.data.containsKey(key);
    }

    public void set(Map<String, ?> values) {
        this.data.putAll(values);
    }

    public void set(String key, Object value) {
        this.data.put(key, value);
    }

    public List<String> getStringList(String key) throws ClassCastException {
        return getList(key, String.class);
    }

    public List<Integer> getIntList(String key) throws ClassCastException {
        return getList(key, Integer.class);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> cast) throws ClassCastException {
        List list = get(key, List.class);
        for (Object object : list) {
            if (!cast.isAssignableFrom(object.getClass()))
                throw new ClassCastException("List element '" + object + "' of type " + object.getClass() + " cannot be casted to " + cast);
        }
        return (List<T>) list;
    }

    public int getInt(String key) throws ClassCastException {
        return get(key, Number.class).intValue();
    }

    public long getLong(String key) throws ClassCastException {
        return get(key, Number.class).longValue();
    }

    public double getDouble(String key) throws ClassCastException {
        return get(key, Number.class).doubleValue();
    }

    public String getString(String key) throws ClassCastException {
        return get(key, String.class);
    }

    public String getAsString(String key) throws ClassCastException {
        return contains(key) ? get(key).toString() : null;
    }

    @SuppressWarnings("unchecked")
    public Config getConfig(String key) throws ClassCastException {
        Map<String, Object> map = (Map<String, Object>) get(key, Map.class);
        return new Config(map);
    }

    public Object get(String key) {
        return get(key, Object.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> cast) throws ClassCastException {
        return (T) this.data.get(key);
    }

    public ConfigInjector injector(Class clazz) {
        return new ConfigInjector(clazz, this);
    }
}
