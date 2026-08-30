package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleGame {

    public static final int WORD_LENGTH = 5;
    public static final int MAX_ATTEMPTS = 6;

    private final String answer;
    private int steps;
    private final WordleDictionary dictionary;
    private boolean isGuessed;
    private final List<String> history;
    private List<String> possibleWords;
    private final Random random = new Random();

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.steps = MAX_ATTEMPTS;
        this.isGuessed = false;
        this.history = new ArrayList<>();
        this.possibleWords = new ArrayList<>(dictionary.getWords());
    }

    public WordleGame(WordleDictionary dictionary, String answer) {
        this.dictionary = dictionary;
        this.answer = answer;
        this.steps = MAX_ATTEMPTS;
        this.isGuessed = false;
        this.history = new ArrayList<>();
        this.possibleWords = new ArrayList<>(dictionary.getWords());
    }

    public List<String> getHistory() {
        return history;
    }

    public boolean wordGuess(String word) throws WordNotFoundException,
            InvalidWordLengthException,
            InvalidCharacterException,
            GameStateException {
        if (!hasMoreAttempts()) {
            throw new GameStateException("Игра уже завершена");
        }

        if (word.length() != WORD_LENGTH) {
            throw new InvalidWordLengthException("Слово должно быть из " + WORD_LENGTH + " букв");
        }

        if (!isRussianLetters(word)) {
            throw new InvalidCharacterException("Слово должно содержать только русские буквы");
        }

        history.add(word);

        if (!dictionary.contains(word)) {
            throw new WordNotFoundException("Слово \"" + word + "\" не найдено в словаре.");
        }

        if (word.equals(answer)) {
            isGuessed = true;
            return true;
        }

        String hint = getHint(word);
        possibleWords = filterWords(possibleWords, word, hint);
        steps--;
        return false;
    }

    private boolean isRussianLetters(String word) {
        for (char c : word.toCharArray()) {
            if (!(c >= 'а' && c <= 'я') && c != 'ё') {
                return false;
            }
        }
        return true;
    }

    private List<String> filterWords(List<String> words, String guess, String hint) {
        List<String> filtered = new ArrayList<>();
        for (String word : words) {
            if (matchesHint(word, guess, hint)) {
                filtered.add(word);
            }
        }
        return filtered;
    }

    private boolean matchesHint(String word, String guess, String hint) {
        for (int i = 0; i < WORD_LENGTH; i++) {
            char c = guess.charAt(i);
            char h = hint.charAt(i);

            if (h == '+') {
                if (word.charAt(i) != c) return false;
            } else if (h == '-') {
                if (word.indexOf(c) != -1) return false;
            } else if (h == '^') {
                if (word.indexOf(c) == -1 || word.charAt(i) == c) return false;
            }
        }
        return true;
    }

    public boolean hasMoreAttempts() {
        return steps > 0;
    }

    public boolean isWordGuessed() {
        return isGuessed;
    }

    public String getAnswer() {
        return answer;
    }

    public String getHint(String guess) {
        char[] res = new char[WORD_LENGTH];
        boolean[] used = new boolean[WORD_LENGTH];

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                res[i] = '+';
                used[i] = true;
            }
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (res[i] == '+') {
                continue;
            }

            char guessChar = guess.charAt(i);
            boolean found = false;

            for (int j = 0; j < WORD_LENGTH; j++) {
                if (!used[j] && guessChar == answer.charAt(j)) {
                    res[i] = '^';
                    used[j] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                res[i] = '-';
            }
        }
        return new String(res);
    }

    public String getComputerSuggestion() {
        if (possibleWords.isEmpty()) {
            return dictionary.getRandomWord();
        }
        return possibleWords.get(random.nextInt(possibleWords.size()));
    }

    public int getRemainingAttempts() {
        return steps;
    }
}