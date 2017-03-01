package net.avicus.compendium.commands.exception;

import net.avicus.compendium.Paginator;
import net.avicus.compendium.locale.text.LocalizedNumber;
import net.avicus.compendium.plugin.Messages;

/**
 * An exception that is thrown when a user supplies an invalid page to a paginated command result.
 */
public class InvalidPaginationPageException extends TranslatableCommandErrorException {

    public InvalidPaginationPageException(Paginator<?> paginator) {
        super(Messages.ERRORS_INVALID_PAGE, new LocalizedNumber(paginator.getPageCount()));
    }
}
