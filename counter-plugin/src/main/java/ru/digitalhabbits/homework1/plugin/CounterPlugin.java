package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CounterPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        text = text.replaceAll("\\\\n", "\\n")
                .toLowerCase().trim();
        int quantityOfLines = text.split("\r\n?|\n").length;
        int quantityOfWords = 0;
        int quantityOfCharacters = text.length();

        final String regex = "(\\b[a-zA-Z][-/a-zA-Z.0-9]*\\b)";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            quantityOfWords++;
        }

        return String.format("%s;%s;%s", quantityOfLines, quantityOfWords, quantityOfCharacters);
    }
}
