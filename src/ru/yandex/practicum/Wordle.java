package ru.yandex.practicum;

import java.util.List;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();

        try {
            List<String> wordList = loader.loadDictionary("words_ru.txt");

            WordleDictionary dictionary = new WordleDictionary(wordList);
            WordleGame game = new WordleGame(dictionary);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Угадайте слово из 5 букв. У вас 6 попыток.");
            System.out.println("+ — буква на месте, ^ — буква есть, но не на месте, - — буквы нет");

            while (game.hasMoreAttempts() && !game.isWordGuessed()) {
                System.out.println();
                System.out.println("Осталось попыток: " + game.getRemainingAttempts());
                System.out.print("Введите слово (или Enter для подсказки): ");
                String input = scanner.nextLine().toLowerCase().trim();

                if (input.isEmpty()) {
                    String suggestion = game.getComputerSuggestion();
                    System.out.println("Подсказка: " + suggestion);

                    try {
                        game.wordGuess(suggestion);
                        System.out.println(game.getHint(suggestion));
                    } catch (WordNotFoundException | InvalidWordLengthException |
                             InvalidCharacterException | GameStateException e) {
                        System.out.println("Ошибка при подсказке: " + e.getMessage());
                    }

                    if (game.isWordGuessed()) {
                        System.out.println("Поздравляю, вы угадали слово!");
                        break;
                    }

                    continue;
                }

                try {
                    boolean guessed = game.wordGuess(input);
                    String hint = game.getHint(input);
                    System.out.println(hint);

                    if (guessed) {
                        System.out.println("Поздравляю, вы угадали слово!");
                        break;
                    }
                } catch (InvalidWordLengthException | InvalidCharacterException e) {
                    System.out.println(e.getMessage());
                } catch (WordNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (GameStateException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }

            if (!game.isWordGuessed()) {
                System.out.println("Вы проиграли. Загаданное слово: " + game.getAnswer());
            }

            scanner.close();

        } catch (DictionaryLoadException e) {
            System.out.println(e.getMessage());
        }
    }
}