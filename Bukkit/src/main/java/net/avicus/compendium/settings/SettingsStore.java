package net.avicus.compendium.settings;

import com.google.common.collect.ArrayListMultimap;

import java.util.*;
import java.util.Map.Entry;

/**
 * Stores any number of settings for any number of keys (such as users).
 * @param <K>
 */
public class SettingsStore<K> {
    private final ArrayListMultimap<K, SettingContext> settings;

    public SettingsStore() {
        this.settings = ArrayListMultimap.create();
    }

    /**
     * Store a value for a key and setting.
     * @param key
     * @param setting
     * @param value
     * @param <R>
     * @return The new value.
     */
    public <R> R set(K key, Setting<R> setting, R value) {
        List<SettingContext> list = new ArrayList<>(this.settings.values());
        for (SettingContext context : list) {
            if (context.getSetting().equals(setting)) {
                this.settings.values().remove(context);
                break;
            }
        }

        this.settings.put(key, new SettingContext<>(setting, setting.getType().parse(value)));
        return value;
    }

    /**
     * Parse and store a set of values based on a collection of available settings.
     * @param key
     * @param parse
     * @param settings
     */
    @SuppressWarnings("unchecked")
    public void set(K key, Map<String, String> parse, Collection<Setting> settings) {
        for (Entry<String, String> entry : parse.entrySet()) {
            String id = entry.getKey();
            String value = entry.getValue();

            for (Setting setting : settings) {
                if (setting.getId().equals(id)) {
                    Optional<SettingValue> parsed = setting.getType().parse(value);
                    Object raw = parsed.isPresent() ? parsed.get().raw() : setting.getDefaultValue();

                    this.set(key, setting, raw);
                }
            }
        }
    }

    /**
     * Parse and store a set of keys with values based on a collection of available settings.
     * @param parse
     * @param settings
     */
    public void set(Map<K, Map<String, String>> parse, Collection<Setting> settings) {
        for (Entry<K, Map<String, String>> entry : parse.entrySet())
            set(entry.getKey(), entry.getValue(), settings);
    }

    /**
     * Retrieve a stored value for a key and setting, or the setting's default.
     * @param key
     * @param setting
     * @param <R>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <R> R get(K key, Setting<R> setting) {
        List<SettingContext> set = this.settings.get(key);
        for (SettingContext context : set) {
            if (context.getSetting().equals(setting))
                return (R) context.getValue().raw();
        }
        return setting.getDefaultValue();
    }

    /**
     * Retrieve all settings for a key.
     * @param key
     * @return
     */
    public Map<String, String> get(K key) {
        Map<String, String> values = new LinkedHashMap<>();
        for (SettingContext context : this.settings.get(key)) {
            String id = context.getSetting().getId();
            String value = context.getValue().serialize();
            values.put(id, value);
        }
        return values;
    }

    /**
     * Retrieve all settings.
     * @return
     */
    public Map<K, Map<String, String>> get() {
        Map<K, Map<String, String>> values = new LinkedHashMap<>();
        for (K key : this.settings.keySet())
            values.put(key, get(key));
        return values;
    }

    /**
     * Toggle a toggleable setting between options.
     * @param key
     * @param setting
     * @param <R>
     * @return The new value if changed, otherwise empty (indicating the setting isn't toggleable).
     */
    @SuppressWarnings("unchecked")
    public <R> Optional<R> toggle(K key, Setting<R> setting) {
        R current = get(key, setting);

        SettingValue<R> value = setting.getType().parse(current);

        if (value instanceof SettingValueToggleable) {
            SettingValueToggleable toggle = (SettingValueToggleable) value;
            R next = (R) toggle.next();
            set(key, setting, next);
            return Optional.of(next);
        }

        return Optional.empty();
    }

    /**
     * Checks if the underlying data stored is equal.
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof SettingsStore) {
            SettingsStore store = (SettingsStore) object;
            return store.get().equals(get());
        }
        return false;
    }

    /**
     * An expensive hashCode() implementation.
     * @return
     */
    @Override
    public int hashCode() {
        return get().hashCode();
    }

    /**
     * A pretty Map string.
     * @return
     */
    @Override
    public String toString() {
        return this.get().toString();
    }
}
