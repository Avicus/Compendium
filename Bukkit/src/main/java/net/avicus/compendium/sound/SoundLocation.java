package net.avicus.compendium.sound;

import org.apache.commons.lang3.StringUtils;

public enum SoundLocation {
    CREDIT_GAIN,
    DEATH,
    KILL,
    MATCH_START,
    MATCH_DING,
    MATCH_WIN,
    MATCH_LOSE,
    MATCH_TIE,
    FRIEND_JOIN,
    FRIEND_LEAVE,
    PRIVATE_MESSAGE,
    TIP_MESSAGE,
    OBJECTIVE_TOUCH;

    SoundLocation() {
    }

    public String prettyName() {
        return StringUtils.capitalize(name().toLowerCase());
    }
}
