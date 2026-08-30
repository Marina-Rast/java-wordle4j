package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    public List<String> loadDictionary(String fileName) throws DictionaryLoadException {
        List<String> allWords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("ё", "е").toLowerCase().trim();
                if (line.length() == 5) {
                    allWords.add(line);
                }
            }
        } catch (IOException e) {
            throw new DictionaryLoadException("Не удалось загрузить словарь: " + fileName, e);
        }

        if (allWords.isEmpty()) {
            throw new DictionaryLoadException("Словарь пуст или не содержит слов из 5 букв");
        }

        return allWords;
    }
}