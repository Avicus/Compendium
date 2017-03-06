package net.avicus.compendium.settings;

import com.google.common.collect.ArrayListMultimap;
import net.avicus.compendium.plugin.CompendiumPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

/**
 * Stores any number of settings for UUIDs.
 */
public class SettingStore {
    private final ArrayListMultimap<UUID, SettingContext> settings;

    public SettingStore() {
        this.settings = ArrayListMultimap.create();
    }

    /**
     * Store a value for a key and setting.
     *
     * @param key
     * @param setting
     * @param value
     * @param <R>
     * @return The new value.
     */
    public <R> R set(UUID key, Setting<R> setting, R value) {
        return set(key, setting, value, true);
    }

    public <R> R set(UUID key, Setting<R> setting, R value, boolean callEvent) {
        List<SettingContext> list = new ArrayList<>(this.settings.values());
        for (SettingContext context : list) {
            if (context.getSetting().equals(setting)) {
                Bukkit.getLogger().info(String.format("[Settings] Removing %s from the store for %s (WAS: %s)", setting.getId(), key, context.getValue().raw()));
                this.settings.values().remove(context);
                break;
            }
        }

        SettingContext context = new SettingContext<>(setting, setting.getType().value(value));
        this.settings.put(key, context);
        Player player = Bukkit.getPlayer(key);
        if (player != null && callEvent) {
            Bukkit.getPluginManager().callEvent(new SettingModifyEvent(CompendiumPlugin.getInstance(), context, player));
            Bukkit.getLogger().info(String.format("[Settings] Adding '%s' to the store for %s (IS: %s)", setting.getId(), player.getName(), value));
        } else
            Bukkit.getLogger().info(String.format("[Settings] Adding '%s' to the store for %s (IS: %s)", setting.getId(), key, value));
        return value;
    }

    public void set(UUID key, Map<String, String> parse, Collection<Setting> settings) {
        set(key, parse, settings, false);
    }

    /**
     * Parse and store a set of values based on a collection of available settings.
     *
     * @param key
     * @param parse
     * @param settings
     */
    @SuppressWarnings("unchecked")
    public void set(UUID key, Map<String, String> parse, Collection<Setting> settings, boolean callEvent) {
        for (Entry<String, String> entry : parse.entrySet()) {
            String id = entry.getKey();
            String value = entry.getValue();

            settings.stream()
                    .filter(setting -> setting.getId()
                            .equals(id))
                    .forEach(setting -> {
                        Optional<SettingValue> parsed = setting.getType()
                                .parse(value);
                        Object raw = parsed.isPresent() ? parsed.get()
                                .raw() : setting.getDefaultValue();

                        this.set(key, setting, raw, callEvent);
                    });
        }
    }

    /**
     * Parse and store a set of keys with values based on a collection of available settings.
     *
     * @param parse
     * @param settings
     */
    public void set(Map<UUID, Map<String, String>> parse, Collection<Setting> settings) {
        for (Entry<UUID, Map<String, String>> entry : parse.entrySet())
            set(entry.getKey(), entry.getValue(), settings);
    }

    /**
     * Retrieve a stored value for a key and setting, or the setting's default.
     *
     * @param key
     * @param setting
     * @param <R>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <R> R get(UUID key, Setting<R> setting) {
        List<SettingContext> set = this.settings.get(key);
        for (SettingContext context : set) {
            if (context.getSetting().getId().equals(setting.getId()))
                return (R) context.getValue().raw();
        }
        Bukkit.getLogger().info(String.format("[Settings] Retrieving default value for '%s' (%s) for %s", setting.getId(), setting.getDefaultValue(), key));
        set(key, setting, setting.getDefaultValue());
        return setting.getDefaultValue();
    }

    /**
     * Retrieve all settings for a key.
     *
     * @param key
     * @return
     */
    public Map<String, String> get(UUID key) {
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
     *
     * @return
     */
    public Map<UUID, Map<String, String>> get() {
        Map<UUID, Map<String, String>> values = new LinkedHashMap<>();
        for (UUID key : this.settings.keySet())
            values.put(key, get(key));
        return values;
    }

    /**
     * Toggle a toggleable setting between options.
     *
     * @param key
     * @param setting
     * @param <R>
     * @return The new value if changed, otherwise empty (indicating the setting isn't toggleable).
     */
    @SuppressWarnings("unchecked")
    public <R> Optional<R> toggle(UUID key, Setting<R> setting) {
        R current = get(key, setting);

        SettingValue<R> value = setting.getType().value(current);

        if (value instanceof SettingValueToggleable) {
            SettingValueToggleable toggle = (SettingValueToggleable) value;
            R next = (R) toggle.next();
            Bukkit.getLogger().info(String.format("[Settings] Toggling '%s' from '%s' to '%s' for %s", setting.getId(), toggle.raw(), toggle.next(), key));
            set(key, setting, next);
            return Optional.of(next);
        }

        return Optional.empty();
    }

    /**
     * Checks if the underlying data stored is equal.
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof SettingStore) {
            SettingStore store = (SettingStore) object;
            return store.get().equals(get());
        }
        return false;
    }

    /**
     * An expensive hashCode() implementation.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return get().hashCode();
    }

    /**
     * A pretty Map string.
     *
     * @return
     */
    @Override
    public String toString() {
        return this.get().toString();
    }
}
