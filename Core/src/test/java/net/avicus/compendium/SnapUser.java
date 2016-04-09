package net.avicus.compendium;

import lombok.ToString;

@ToString
public class SnapUser {
    private final int id;
    private final String name;

    public SnapUser(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
