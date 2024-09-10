package com.ridango.game;

import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CocktailGameApplication implements CommandLineRunner {

	private static boolean PRINT_ANSWER = true; // For debugging purposes
	private static Cocktail currentCocktail;



	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}



	@Override public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		print("\n\nWelcome to Guess The Coctail!\nYour goal is to guess the coctail based on the hint!");
		print("Your current session high score is: 0\n");

		setCurrentCocktail(CocktailGameController.getNextCocktail());
		printNextHint();

		String input;
		while ((input = scanner.nextLine()) != null) {
			if (input.toLowerCase().equals(currentCocktail.getName().toLowerCase())) {
				handleCorrectGuess();
			} else {
				handleWrongGuess();
			}
			printNextHint();
			if (PRINT_ANSWER) print("(Psst, the answer is: " + currentCocktail.getName() + ")");
		}
		scanner.close();
	}



	public static void handleCorrectGuess() throws Exception {
		CocktailGameController.updateScore();

		print("\nCorrect!\n\nYour new score is: " + CocktailGameController.getScore() + "\n");

		if (CocktailGameController.maxDrinksReached()) {
			print("\nYou have guessed every single cocktail on the menu!");
			print("Since you have reached the end, we'll have to restart the game.");
			boolean newHighScore = CocktailGameController.restartGame();
			if (newHighScore) {
				print("\n\nYour NEW session high score is: " + CocktailGameController.getHighScore() + "\n");
			} else {
				print("\n\nYour final score was: " + CocktailGameController.getScore());
				print("Your current session high score is still: " + CocktailGameController.getHighScore() + "\n");
			}
			return;
		}

		currentCocktail = CocktailGameController.getNextCocktail();
	}



	public static void handleWrongGuess() throws Exception {
		print("\nIncorrect!\n");
		CocktailGameController.reduceAttempts();

		if (CocktailGameController.getAttempts() == 0) {
			print("Game over! You lose!\nThe correct cocktail was: " + currentCocktail.getName() + "\n\n");
			CocktailGameController.restartGame();
			return;
		}
		CocktailGameController.addLetters(currentCocktail);
	}



	private static void printNextHint() {
		print("The current hint is:");
		print(CocktailGameController.getCocktailHint());
		print(currentCocktail.getInstructions());
		String hints = CocktailGameController.getHints(currentCocktail);
		print(hints);
		print("Remaining attempts: " + CocktailGameController.getAttempts());
		print("Write your guess:\n");
	}



	public static void setCurrentCocktail(Cocktail cocktail) {
		currentCocktail = cocktail;
	}



	private static void print(String string) {
		System.out.println(string);
	}
}
