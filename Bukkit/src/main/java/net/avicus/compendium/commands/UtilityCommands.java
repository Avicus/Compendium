package net.avicus.compendium.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import net.avicus.compendium.commands.exception.TranslatableCommandErrorException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class UtilityCommands {
    @Command(aliases = {"exec"}, desc = "Run a command N times.", usage = "<count> <command>", min = 2)
    public static void exec(CommandContext args, CommandSender sender) throws CommandException {
        if (!sender.isOp()) {
            sender.sendMessage("no");
            sender.sendMessage("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            sender.sendMessage("just no");
            return;
        }

        int count = args.getInteger(0);
        for (int i = 0; i <= count; i++) {
            Bukkit.getServer().dispatchCommand(sender, args.getJoinedStrings(1));
        }
    }
}
