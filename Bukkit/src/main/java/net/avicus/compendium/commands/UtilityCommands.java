package net.avicus.compendium.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import net.avicus.compendium.plugin.CompendiumPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class UtilityCommands {
    @Command(aliases = {"exec"}, desc = "Run a command N times.", usage = "<count> <command>", min = 2)
    public static void exec(CommandContext args, CommandSender sender) throws CommandException {
        if (!sender.isOp()) {
            sender.sendMessage("no");
            for (int i = 0; i < 10; i++) {
                sender.sendMessage("");
            }
            sender.sendMessage("just no");
            return;
        }

        int count = args.getInteger(0);
        for (int i = 1; i <= count; i++) {
            Bukkit.getServer().dispatchCommand(sender, args.getJoinedStrings(1));
        }
    }

    private static final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .appendDays().appendSuffix("d")
            .appendHours().appendSuffix("h")
            .appendMinutes().appendSuffix("m")
            .appendSecondsWithOptionalMillis().appendSuffix("s")
            .appendSeconds()
            .toFormatter();

    @Command(aliases = {"execdelay", "ed"}, desc = "Run a command N times with a delay.", usage = "<count> <delay> <command>", min = 2)
    public static void execDelayed(CommandContext args, CommandSender sender) throws CommandException {
        if (!sender.isOp()) {
            sender.sendMessage("bye");
            for (int i = 0; i < 10; i++) {
                sender.sendMessage("");
            }
            sender.sendMessage("have a nice day");
            return;
        }

        int count = args.getInteger(0);
        String delayString = args.getString(1);
        Duration delay;
        try {
            delay = periodFormatter.parsePeriod(delayString).toStandardDuration();
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid duration format.");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(CompendiumPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= count; i++) {
                    try {
                        Thread.sleep(delay.getMillis());
                    } catch (InterruptedException e) {
                        // uh
                        e.printStackTrace();
                    }
                    // Bukkit throws an error for async commands
                    Bukkit.getScheduler().runTask(CompendiumPlugin.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.getServer().dispatchCommand(sender, args.getJoinedStrings(2));
                        }
                    });
                }
            }
        });
    }

    @Command(aliases = {"vel", "velocity"}, desc = "Apply velocity to yourself.", min = 3, max = 3, help = "x y z")
    @CommandPermissions("compendium.vel")
    public static void velocity(CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player))
            throw new CommandException("You must be a player to use this command.");

        String xString = cmd.getString(0);
        String yString = cmd.getString(1);
        String zString = cmd.getString(2);
        try {
            ((Player) sender).setVelocity(new Vector(Float.parseFloat(xString), Float.parseFloat(yString), Float.parseFloat(zString)));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Error in number syntax");
        }
    }

    @Command(aliases = {"safe-stop", "sast"}, desc = "Stop a server only if it is empty.", max = 0)
    @CommandPermissions("compendium.safestop")
    public static void safeStop(CommandContext cmd, CommandSender sender) throws CommandException {
        int offset = sender instanceof Player ? 1 : 0;
        if (Bukkit.getOnlinePlayers().size() - offset == 0)
            Bukkit.shutdown();
        else
            sender.sendMessage(ChatColor.RED + "The server cannot be stopped because there are players on it.");
    }
}
