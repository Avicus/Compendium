package net.avicus.compendium.utils;

import net.avicus.compendium.locale.text.LocalizableFormat;
import net.avicus.compendium.locale.text.UnlocalizedFormat;

public class Formats {

    public static LocalizableFormat humanList(int size) {
        LocalizableFormat format;

        if (size == 1) {
            format = new UnlocalizedFormat("{0}");
        } else {
            String stringFormat = "";
            for (int i = 0; i <= size; i++) {
                stringFormat += "{" + i + "} ";

                if (i - 1 == size)
                    stringFormat += "& ";
                else if (i != size) {
                    stringFormat += ", ";
                }
            }

            format = new UnlocalizedFormat(stringFormat);
        }

        return format;
    }
}
