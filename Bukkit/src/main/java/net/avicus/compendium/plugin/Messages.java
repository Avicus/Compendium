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
    public static final LocalizedFormat GENERIC_COUNTDOWN_COMMAND_HEADER = get("generic.countdown.command.header");
    public static final LocalizedFormat GENERIC_COUNTDOWN_COMMAND_CANCEL_SINGULAR = get("generic.countdown.command.cancel.singular");
    public static final LocalizedFormat GENERIC_COUNTDOWN_COMMAND_CANCEL_ALL_SINGULAR = get("generic.countdown.command.cancel.all-singular");
    public static final LocalizedFormat GENERIC_COUNTDOWN_COMMAND_CANCEL_ALL_PLURAL = get("generic.countdown.command.cancel.all-plural");
    public static final LocalizedFormat GENERIC_COUNTDOWN_COMMAND_CANCEL_BUTTON = get("generic.countdown.command.cancel.button");
    public static final LocalizedFormat GENERIC_COUNTDOWN_TYPE_RESTARTING_NAME = get("generic.countdown.type.restarting.name");
    public static final LocalizedFormat GENERIC_COUNTDOWN_TYPE_RESTARTING_TIME_SINGULAR = get("generic.countdown.type.restarting.time-singular");
    public static final LocalizedFormat GENERIC_COUNTDOWN_TYPE_RESTARTING_TIME_PLURAL = get("generic.countdown.type.restarting.time-plural");

    public static final LocalizedFormat ERRORS_COMMAND_INTERNAL_ERROR = get("errors.command.internal-error");
    public static final LocalizedFormat ERRORS_COMMAND_NUMBER_EXPECTED = get("errors.command.number-expected");
    public static final LocalizedFormat ERRORS_COMMAND_NO_PERMISSION = get("errors.command.no-permission");
    public static final LocalizedFormat ERRORS_COMMAND_INVALID_USAGE = get("errors.command.invalid-usage");
    public static final LocalizedFormat ERRORS_COMMAND_MUST_BE_PLAYER = get("errors.command.must-be-player");
    public static LocalizedFormat ERRORS_SETTINGS_HELP = get("errors.settings-help");
    public static LocalizedFormat ERRORS_NO_SETTING = get("errors.no-setting");
    public static LocalizedFormat ERRORS_NOT_TOGGLE = get("errors.not-toggle");
    public static LocalizedFormat ERRORS_INVALID_VALUE = get("errors.invalid-value");
    public static final LocalizedFormat ERRORS_INVALID_PAGE = get("errors.invalid-page");
    public static final LocalizedFormat ERRORS_COUNTDOWN_COMMAND_CANCEL_NONE_ACTIVE = get("errors.countdown.command.cancel.none-active");
    public static final LocalizedFormat ERRORS_COUNTDOWN_COMMAND_CANCEL_NO_SUCH_ID = get("errors.countdown.command.cancel.no-such-id");

    public static LocalizedFormat get(String path) {
        return CompendiumPlugin.getLocaleBundle().getFormat(path);
    }
}
