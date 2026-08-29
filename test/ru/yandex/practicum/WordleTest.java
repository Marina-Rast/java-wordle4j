package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        List<String> words = List.of("герой", "гонец", "котёл", "носки", "ложка", "лилия");
        dictionary = new WordleDictionary(words);
        game = new WordleGame(dictionary);
    }

    @Test
    void testGameHasSixAttempts() {
        assertEquals(6, game.getRemainingAttempts());
    }

    @Test
    void testWordGuessCorrect() throws WordNotFoundException {
        String answer = game.getAnswer();
        assertTrue(game.wordGuess(answer));
        assertTrue(game.isWordGuessed());
    }

    @Test
    void testWordGuessWrong() throws WordNotFoundException {
        String wrongWord = "носки";
        if (wrongWord.equals(game.getAnswer())) {
            wrongWord = "ложка";
        }

        boolean result = game.wordGuess(wrongWord);
        assertFalse(result);
        assertFalse(game.isWordGuessed());
        assertEquals(5, game.getRemainingAttempts());
    }

    @Test
    void testWordGuessDecreasesAttempts() throws WordNotFoundException {
        String wrongWord = "носки";
        if (wrongWord.equals(game.getAnswer())) {
            wrongWord = "ложка";
        }

        game.wordGuess(wrongWord);
        assertEquals(5, game.getRemainingAttempts());
    }

    @Test
    void testWordGuessThrowsExceptionWhenWordNotFound() {
        assertThrows(WordNotFoundException.class, () -> game.wordGuess("абвгд"));
        assertEquals(6, game.getRemainingAttempts());
    }

    @Test
    void testGetHint() throws WordNotFoundException {
        game = new WordleGame(dictionary, "герой");
        assertEquals("+^-^-", game.getHint("гонец"));
    }

    @Test
    void testGetHintAllCorrect() throws WordNotFoundException {
        game = new WordleGame(dictionary, "герой");
        assertEquals("+++++", game.getHint("герой"));
    }

    @Test
    void testGetHintAllWrong() throws WordNotFoundException {
        game = new WordleGame(dictionary, "герой");
        assertEquals("-----", game.getHint("лапша"));
    }

    @Test
    void testHasMoreAttempts() throws WordNotFoundException {
        assertTrue(game.hasMoreAttempts());

        String wrongWord = "носки";
        if (wrongWord.equals(game.getAnswer())) {
            wrongWord = "ложка";
        }

        for (int i = 0; i < 6; i++) {
            game.wordGuess(wrongWord);
        }

        assertFalse(game.hasMoreAttempts());
    }

    @Test
    void testGameStartsNotGuessed() {
        assertFalse(game.isWordGuessed());
    }

    @Test
    void testGetAnswerReturnsWord() {
        String answer = game.getAnswer();
        assertNotNull(answer);
        assertEquals(5, answer.length());
    }

    @Test
    void testGetComputerSuggestion() {
        String suggestion = game.getComputerSuggestion();
        assertNotNull(suggestion);
        assertEquals(5, suggestion.length());
        assertTrue(dictionary.contains(suggestion));
    }

    @Test
    void testWordGuessAddsToHistory() throws WordNotFoundException {
        String word = "носки";
        game.wordGuess(word);
        assertEquals(1, game.getHistory().size());
        assertEquals(word, game.getHistory().get(0));
    }
}