package net.avicus.compendium.plugin;

import net.avicus.compendium.locale.text.LocalizedFormat;

public class Messages {
    public static LocalizedFormat GENERIC_SETTINGS = get("generic.settings");
    public static LocalizedFormat GENERIC_CLICK_ME = get("generic.click-me");
    public static LocalizedFormat GENERIC_SUMMARY = get("generic.summary");
    public static LocalizedFormat GENERIC_DESCRIPTION = get("generic.description");
    public static LocalizedFormat GENERIC_DEFAULT = get("generic.default");
    public static LocalizedFormat GENERIC_CURRENT = get("generic.current");
    public static LocalizedFormat GENERIC_TOGGLE = get("generic.toggle");
    public static LocalizedFormat GENERIC_SETTING_SET = get("generic.setting-set");

    public static LocalizedFormat ERRORS_NOT_PLAYER = get("errors.not-player");
    public static LocalizedFormat ERRORS_SETTINGS_HELP = get("errors.settings-help");
    public static LocalizedFormat ERRORS_NO_SETTING = get("errors.no-setting");
    public static LocalizedFormat ERRORS_NOT_TOGGLE = get("errors.not-toggle");
    public static LocalizedFormat ERRORS_INVALID_VALUE = get("errors.invalid-value");

    public static final LocalizedFormat GENERIC_RESTARTING = get("generic.restarting");
    public static final LocalizedFormat GENERIC_RESTARTING_PLURAL = get("generic.restarting-plural");

    public static LocalizedFormat get(String path) {
        return CompendiumPlugin.getLocaleBundle().getFormat(path);
    }
}
