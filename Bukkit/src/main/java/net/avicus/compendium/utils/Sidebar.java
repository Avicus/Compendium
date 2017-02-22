package net.avicus.compendium.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

@ToString
public class Sidebar {
    @Getter private final Scoreboard scoreboard;
    @Getter private final Multimap<Integer, SidebarTeam> teams;
    @Getter private final Objective objective;

    /**
     * Creates a new sidebar with a new scoreboard attachment.
     */
    public Sidebar() {
        this(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    /**
     * Creates a new titled sidebar with a new scoreboard attachment.
     *
     * @param title
     */
    public Sidebar(String title) {
        this(Bukkit.getScoreboardManager().getNewScoreboard(), title);
    }

    /**
     * Creates a new sidebar with the given scoreboard.
     */
    public Sidebar(Scoreboard scoreboard) {
        this(scoreboard, UUID.randomUUID().toString().substring(0, 6));
    }

    /**
     * Creates a new titles sidebar with the given scoreboard.
     *
     * @param title
     */
    public Sidebar(Scoreboard scoreboard, String title) {
        Preconditions.checkNotNull(scoreboard, "Sidebar scoreboard must not be null");
        Preconditions.checkNotNull(title, "Sidebar title must not be null");

        this.scoreboard = scoreboard;
        this.teams = ArrayListMultimap.create();

        this.objective = scoreboard.registerNewObjective(title, "dummy");
        this.objective.setDisplayName(title);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Retrieve the title of the sidebar.
     *
     * @return
     */
    public String getTitle() {
        return this.objective.getDisplayName();
    }

    /**
     * Changes the title of the sidebar.
     *
     * @param text
     */
    public void setTitle(String text) {
        if (!this.objective.getDisplayName().equals(text))
            this.objective.setDisplayName(text);
    }

    /**
     * Adds a line to the sidebar at the given line (score) number.
     * Appends spaces to the text to ensure it will be seen on the board.
     *
     * @param line
     * @param text
     */
    public void add(String text, int line) {
        set(text, line, true);
    }

    /**
     * Adds a line to the sidebar at the given line (score) number.
     *
     * @param line
     * @param text
     */
    private void set(String text, int line) {
        set(text, line, false);
    }

    private void set(String text, int line, boolean safely) {
        SidebarTeam sidebarTeam = createSidebarTeam(text, line, safely);
        Team team = sidebarTeam.getTeam();
        team.addEntry(sidebarTeam.getLineText());
        this.objective.getScore(sidebarTeam.getLineText()).setScore(line);
        this.teams.put(line, sidebarTeam);
    }

    /**
     * Get all text at the given line (score) number.
     *
     * @param line
     * @return
     */
    public List<String> get(int line) {
        List<String> lines = this.teams.get(line)
                .stream()
                .map(SidebarTeam::getFullText)
                .collect(Collectors.toList());
        return lines;
    }

    /**
     * Get the line (score) number of the given text.
     *
     * @param text
     * @return
     * @throws IllegalArgumentException If the text cannot be found in the sidebar.
     */
    public int getLine(String text) throws IllegalArgumentException {
        for (Entry<Integer, SidebarTeam> entry : this.teams.entries())
            if (entry.getValue().getFullText().equals(text))
                return entry.getKey();
        throw new IllegalArgumentException("line not found");
    }

    /**
     * Removes all entries with the given line (score) number and sets new text.
     *
     * @param line
     * @param text
     */
    public void replace(int line, String text) {
        // Only replace if necessary
        boolean doReplace = false;

        // If there isn't a single line there, it's needed
        if (get(line).size() != 1) {
            doReplace = true;
        } else {
            // Otherwise, we replace only if the text does not match what is there currently.
            String current = get(line).get(0);
            if (!current.equals(text)) {
                doReplace = true;
            }
        }

        if (doReplace) {
            clear(line);
            add(text, line);
        }

    }

    /**
     * Removes all entries with the given text.
     *
     * @param text
     */
    public void clear(String text) {
        remove(text, null);
    }

    /**
     * Removes all entries with the given line (score) number.
     *
     * @param line
     */
    public void clear(int line) {
        remove(null, line);
    }

    /**
     * Removes all entries in the sidebar.
     */
    public void reset() {
        remove(null, null);
    }

    /**
     * Removes all entries with the given text and line (score) number.
     *
     * @param text
     */
    public void remove(String text, Integer line) {
        Iterator<Entry<Integer, SidebarTeam>> iterator = this.teams.entries().iterator();
        while (iterator.hasNext()) {
            Entry<Integer, SidebarTeam> entry = iterator.next();
            if (text != null && !entry.getValue().getFullText().equals(text))
                continue;
            if (line != null && !entry.getKey().equals(line))
                continue;
            this.scoreboard.resetScores(entry.getValue().getLineText());
            entry.getValue().getTeam().unregister();
            iterator.remove();
        }
    }

    private SidebarTeam createSidebarTeam(String text, int lineNum, boolean fixDuplicates) {
        String[] split = getSplitText(text, 0);

        if (fixDuplicates) {
            int fix = 1;
            boolean unique = false;
            while (!unique) {
                unique = true;
                for (SidebarTeam team : this.teams.values()) {
                    if (team.getLineText().equals(split[1])) {
                        text += ChatColor.RESET;
                        split = getSplitText(text, fix);
                        fix++;
                        unique = false;
                        break;
                    }
                }
            }
        }

        String prefix = split[0];
        String line = split[1];
        String suffix = split[2];

        Team team = this.scoreboard.registerNewTeam("sb-" + lineNum + "-" + UUID.randomUUID().toString().substring(0, 6));

        if (prefix != null)
            team.setPrefix(prefix);

        if (suffix != null)
            team.setSuffix(suffix);

        return new SidebarTeam(team, line, text);
    }

    private String[] getSplitText(String text, int fix) {
        int count = 16 - fix;
        List<String> split;
        while (true) {
            Splitter splitter = Splitter.fixedLength(count);
            split = splitter.splitToList(text);
            if (text.length() == 0)
                break;

            if (split.get(0).charAt(split.get(0).length() - 1) != '§')
                break;
            count--;
        }

        String prefix = null;
        String line = split.get(0);
        String suffix = null;

        if (split.size() > 1) {
            prefix = split.get(0);
            line = split.get(1);
        }
        if (split.size() > 2)
            suffix = split.get(2);

        return new String[]{prefix, line, suffix};
    }

    @ToString
    @Data
    private class SidebarTeam {
        private final Team team;
        private final String lineText;
        private final String fullText;
    }
}
