package ru.yandex.practicum;

import java.util.List;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        List<String> wordList = loader.loadDictionary("words_ru.txt");

        if (wordList.isEmpty()) {
            System.out.println("Словарь пуст или не найден. Проверьте файл");
            return;
        }

        WordleDictionary dictionary = new WordleDictionary(wordList);
        WordleGame game = new WordleGame(dictionary);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Угадайте слово из 5 букв. У вас 6 попыток.");
        System.out.println("+ — буква на месте, ^ — буква есть, но не на месте, - — буквы нет");

        while (game.hasMoreAttempts() && !game.isWordGuessed()) {
            System.out.println();
            System.out.println("Осталось попыток: " + game.getRemainingAttempts());
            System.out.print("Введите Enter для подсказки: ");
            String input = scanner.nextLine().toLowerCase().trim();

            if (input.isEmpty()) {
                String suggestion = game.getComputerSuggestion();
                System.out.println("Подсказка: " + suggestion);
                continue;
            }

            if (input.length() != 5) {
                System.out.println("Слово должно быть из 5 букв!");
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
            } catch (WordNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        if (!game.isWordGuessed()) {
            System.out.println("Вы проиграли. Загаданное слово: " + game.getAnswer());
        }

        scanner.close();
    }
}