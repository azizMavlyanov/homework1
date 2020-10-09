package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FrequencyDictionaryPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        final List<String> listOfWords = new ArrayList<>();
        final String regex = "(\\b[a-zA-Z][-/a-zA-Z.0-9]*\\b)";
        final String cleanedText = text
                .replaceAll("\\\\n", "\n")
                .toLowerCase()
                .trim();
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(cleanedText);

        while (matcher.find()) {
            listOfWords.add(matcher.group());
        }

        Map<String, Long> wordFreq = listOfWords.stream()
                .collect(Collectors.groupingBy(String::toString,Collectors.counting()));
        Map<String, Long> sortedWordFreq = new TreeMap<>(wordFreq);

        return sortedWordFreq.keySet().stream()
                .map(key -> key + " " + sortedWordFreq.get(key) + "\n")
                .collect(Collectors.joining());
    }
}
