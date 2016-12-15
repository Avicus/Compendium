package net.avicus.compendium.commands.exception;

import net.avicus.compendium.Paginator;
import net.avicus.compendium.locale.text.LocalizedNumber;
import net.avicus.compendium.plugin.Messages;

public class InvalidPaginationPageException extends TranslatableCommandErrorException {

    public InvalidPaginationPageException(Paginator<?> paginator) {
        super(Messages.ERRORS_INVALID_PAGE, new LocalizedNumber(paginator.getPageCount()));
    }
}
