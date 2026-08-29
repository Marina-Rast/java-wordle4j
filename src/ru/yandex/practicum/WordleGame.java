package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;

public class WordleGame {
    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private boolean isGuessed;

    public List<String> getHistory() {
        return history;
    }

    private List<String> history;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.steps = 6;
        this.isGuessed = false;
        this.history = new ArrayList<>();
    }

    public WordleGame(WordleDictionary dictionary, String answer) {
        this.dictionary = dictionary;
        this.answer = answer;
        this.steps = 6;
        this.isGuessed = false;
        this.history = new ArrayList<>();
    }

    public boolean wordGuess(String word) throws WordNotFoundException {
        history.add(word);
        if (!dictionary.contains(word)) {
            throw new WordNotFoundException("Слово \"" + word + "\" не найдено в словаре.");
        }
        if (word.equals(answer)) {
            isGuessed = true;
            return true;
        }
        steps--;
        return false;
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
        char[] res = new char[5];
        boolean[] used = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                res[i] = '+';
                used[i] = true;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (res[i] == '+') {
                continue;
            }

            char guessChar = guess.charAt(i);
            boolean found = false;

            for (int j = 0; j < 5; j++) {
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
        return dictionary.getRandomWord();
    }

    public int getRemainingAttempts() {
        return steps;
    }
}
