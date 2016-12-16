package net.avicus.compendium.countdown;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import net.avicus.compendium.Paginator;
import net.avicus.compendium.TextStyle;
import net.avicus.compendium.commands.exception.InvalidPaginationPageException;
import net.avicus.compendium.commands.exception.TranslatableCommandErrorException;
import net.avicus.compendium.commands.exception.TranslatableCommandWarningException;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizedNumber;
import net.avicus.compendium.locale.text.UnlocalizedFormat;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.plugin.CompendiumPlugin;
import net.avicus.compendium.plugin.Messages;
import net.avicus.compendium.plugin.PlayersBase;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

import javax.annotation.Nullable;

public class CountdownCommands {

    @Command(aliases = {"countdowns"}, desc = "List active countdowns", usage = "(page)")
    @CommandPermissions("compendium.countdown.list")
    public static void list(CommandContext args, CommandSender source) throws CommandException {
        // 1-indexed
        int page = args.argsLength() > 0 ? args.getInteger(0) : 1;
        page--;

        Paginator<Map.Entry<Countdown, CountdownTask>> paginator = new Paginator<>(CompendiumPlugin.getInstance().getCountdownManager().getCountdowns().entrySet(), 7);

        if (paginator.getPageCount() == 0) {
            throw new TranslatableCommandWarningException(Messages.ERRORS_COUNTDOWN_COMMAND_CANCEL_NONE_ACTIVE);
        }

        if (!paginator.hasPage(page)) {
            throw new InvalidPaginationPageException(paginator);
        }

        // Page Header
        UnlocalizedText line = new UnlocalizedText("--------------", TextStyle.ofColor(ChatColor.RED).strike());
        UnlocalizedFormat header = new UnlocalizedFormat("{0} {1} ({2}/{3}) {4}");
        LocalizedNumber pageNumber = new LocalizedNumber(page + 1);
        LocalizedNumber pagesNumber = new LocalizedNumber(paginator.getPageCount());
        Localizable title = Messages.GENERIC_COUNTDOWN_COMMAND_HEADER.with(ChatColor.YELLOW);
        PlayersBase.message(source, header.with(line, title, pageNumber, pagesNumber, line));

        final UnlocalizedFormat format = new UnlocalizedFormat("{0}: {1}");
        final HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{Messages.GENERIC_COUNTDOWN_COMMAND_CANCEL_BUTTON.with().translate(PlayersBase.getLocale(source))});
        for (Map.Entry<Countdown, CountdownTask> countdown : paginator.getPage(page)) {
            LocalizedNumber number = new LocalizedNumber(countdown.getValue().getTaskId());
            number.style().color(ChatColor.YELLOW);
            number.style().click(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/countdown cancel " + countdown.getValue().getTaskId())).hover(hover);
            PlayersBase.message(source, format.with(number, countdown.getKey().getName()));
        }
    }

    @Command(aliases = {"countdown"}, desc = "Manage a countdown")
    @NestedCommand(Countdowns.class)
    public static void parent(CommandContext args, CommandSender source) {
    }

    public static class Countdowns {

        @Command(aliases = {"cancel"}, desc = "Cancel a countdown", max = 1, flags = "a")
        @CommandPermissions("compendium.countdown.cancel")
        public static void cancel(CommandContext args, CommandSender source) throws CommandException {
            final CountdownManager manager = CompendiumPlugin.getInstance().getCountdownManager();
            if (args.hasFlag('a')) {
                final int countdowns = manager.getCountdowns().size();
                switch (countdowns) {
                    case 0:
                        throw new TranslatableCommandErrorException(Messages.ERRORS_COUNTDOWN_COMMAND_CANCEL_NONE_ACTIVE);
                    case 1:
                        manager.cancelAll();
                        PlayersBase.message(source, Messages.GENERIC_COUNTDOWN_COMMAND_CANCEL_ALL_SINGULAR.with(ChatColor.GREEN));
                        break;
                    default:
                        manager.cancelAll();
                        PlayersBase.message(source, Messages.GENERIC_COUNTDOWN_COMMAND_CANCEL_ALL_PLURAL.with(ChatColor.GREEN, new LocalizedNumber(countdowns)));
                        break;
                }
            }
            else {
                @Nullable final CountdownTask countdown = manager.findByTaskId(args.getInteger(0));
                if (countdown == null)
                    throw new TranslatableCommandErrorException(Messages.ERRORS_COUNTDOWN_COMMAND_CANCEL_NO_SUCH_ID, new UnlocalizedText(args.getString(0)));

                manager.cancel(countdown.getCountdown());
                PlayersBase.message(source, Messages.GENERIC_COUNTDOWN_COMMAND_CANCEL_SINGULAR.with(ChatColor.GREEN, countdown.getCountdown().getName(), new LocalizedNumber(countdown.getTaskId())));
            }
        }
    }
}
