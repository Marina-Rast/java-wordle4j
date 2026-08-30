package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        List<String> words = List.of("герой", "гонец", "котёл", "носки", "ложка", "лилия");
        dictionary = new WordleDictionary(words);
    }

    @Test
    void testGameHasSixAttempts() {
        final WordleGame game = new WordleGame(dictionary);
        assertEquals(6, game.getRemainingAttempts());
    }

    @Test
    void testGameStartsNotGuessed() {
        final WordleGame game = new WordleGame(dictionary);
        assertFalse(game.isWordGuessed());
    }

    @Test
    void testGetAnswerReturnsWord() {
        final WordleGame game = new WordleGame(dictionary);
        String answer = game.getAnswer();
        assertNotNull(answer);
        assertEquals(5, answer.length());
    }

    @Test
    void testWordGuessCorrect() throws Exception {
        final WordleGame game = new WordleGame(dictionary);
        String answer = game.getAnswer();
        assertTrue(game.wordGuess(answer));
        assertTrue(game.isWordGuessed());
    }

    @Test
    void testWordGuessWrong() throws Exception {
        final WordleGame game = new WordleGame(dictionary);
        final String wrongWord = game.getAnswer().equals("носки") ? "ложка" : "носки";

        boolean result = game.wordGuess(wrongWord);
        assertFalse(result);
        assertFalse(game.isWordGuessed());
        assertEquals(5, game.getRemainingAttempts());
    }

    @Test
    void testWordGuessDecreasesAttempts() throws Exception {
        final WordleGame game = new WordleGame(dictionary);
        final String wrongWord = game.getAnswer().equals("носки") ? "ложка" : "носки";

        game.wordGuess(wrongWord);
        assertEquals(5, game.getRemainingAttempts());
    }

    @Test
    void testWordGuessAddsToHistory() throws Exception {
        final WordleGame game = new WordleGame(dictionary);
        String word = "носки";
        game.wordGuess(word);
        assertEquals(1, game.getHistory().size());
        assertEquals(word, game.getHistory().get(0));
    }

    @Test
    void testWordGuessThrowsExceptionWhenWordNotFound() {
        final WordleGame game = new WordleGame(dictionary);
        assertThrows(WordNotFoundException.class, () -> game.wordGuess("абвгд"));
        assertEquals(6, game.getRemainingAttempts());
    }

    @Test
    void testWordGuessThrowsExceptionWhenWordTooShort() {
        final WordleGame game = new WordleGame(dictionary);
        assertThrows(InvalidWordLengthException.class, () -> game.wordGuess("абвг"));
    }

    @Test
    void testWordGuessThrowsExceptionWhenWordTooLong() {
        final WordleGame game = new WordleGame(dictionary);
        assertThrows(InvalidWordLengthException.class, () -> game.wordGuess("абвгде"));
    }

    @Test
    void testWordGuessThrowsExceptionWhenInvalidCharacters() {
        final WordleGame game = new WordleGame(dictionary);
        assertThrows(InvalidCharacterException.class, () -> game.wordGuess("abcde"));
    }

    @Test
    void testGameStateExceptionWhenNoAttemptsLeft() throws Exception {
        final WordleGame game = new WordleGame(dictionary);
        final String wrongWord = game.getAnswer().equals("носки") ? "ложка" : "носки";

        for (int i = 0; i < 6; i++) {
            game.wordGuess(wrongWord);
        }

        assertThrows(GameStateException.class, () -> game.wordGuess(wrongWord));
    }

    @Test
    void testHasMoreAttempts() throws Exception {
        final WordleGame game = new WordleGame(dictionary);
        assertTrue(game.hasMoreAttempts());

        final String wrongWord = game.getAnswer().equals("носки") ? "ложка" : "носки";

        for (int i = 0; i < 6; i++) {
            game.wordGuess(wrongWord);
        }

        assertFalse(game.hasMoreAttempts());
    }

    @Test
    void testGetHint() {
        final WordleGame game = new WordleGame(dictionary, "герой");
        assertEquals("+^-^-", game.getHint("гонец"));
    }

    @Test
    void testGetHintAllCorrect() {
        final WordleGame game = new WordleGame(dictionary, "герой");
        assertEquals("+++++", game.getHint("герой"));
    }

    @Test
    void testGetHintAllWrong() {
        final WordleGame game = new WordleGame(dictionary, "герой");
        assertEquals("-----", game.getHint("лапша"));
    }

    @Test
    void testGetComputerSuggestion() {
        final WordleGame game = new WordleGame(dictionary);
        String suggestion = game.getComputerSuggestion();
        assertNotNull(suggestion);
        assertEquals(5, suggestion.length());
        assertTrue(dictionary.contains(suggestion));
    }
}