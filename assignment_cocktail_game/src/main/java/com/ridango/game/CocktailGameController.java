package com.ridango.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONObject;

public class CocktailGameController {

    private static final String URL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
    public static final int MAX_ATTEMPTS = 5;
	private static int MAX_DRINKS = 630; // It's actually 636, but I don't want to accidentally DDOS the website too much

	private static List<String> hintOrder = Arrays.asList("category", "alcoholic", "glass", "picture");
    private static List<Cocktail> usedCocktails = new ArrayList<>();
	private static List<Integer> revealOrder = new ArrayList<>();

    private static String currentCocktailHint = "";
	private static int revealQuantity = 1;
	private static int attempts = 5;

	private static int highScore = 0;
	private static int currentScore = 0;
	private static int drinkNumber = 1;



    public static String getCocktailHint() {
        return currentCocktailHint;
    }

    public static List<Cocktail> getUsedCocktails() {
        return usedCocktails;
    }

    public static int getAttempts() {
        return attempts;
    }

    public static void reduceAttempts() {
        attempts--;
    }

    public static int getScore() {
        return currentScore;
    }

    public static int getHighScore() {
        return highScore;
    }

    public static boolean maxDrinksReached() {
        return drinkNumber >= MAX_DRINKS;
    }



    public static Cocktail getNextCocktail() throws Exception {
        JSONObject jsonResponse = getRandomCocktail();
        Cocktail cocktail = createCocktail(jsonResponse);
        manageHintSystem(cocktail.getName());
        return cocktail;
    }



	private static JSONObject getRandomCocktail() throws Exception {
		JSONObject json;
		while (true) {
			URL url = new URL(URL);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String response = in.readLine();
            
			json = new JSONObject(response).getJSONArray("drinks").getJSONObject(0);
			String cocktailName = json.getString("strDrink");
			int count = (int) usedCocktails.stream().filter((cocktail) -> cocktail.getName().equals(cocktailName)).count();
			if (count == 0) break;
		}
		return json;
	}



    private static Cocktail createCocktail(JSONObject json) {
		String name = json.getString("strDrink");
		String instructions = json.getString("strInstructions").replaceAll("\n", " ");
		String category = json.getString("strCategory");
		boolean isAlcoholic = json.getString("strAlcoholic").equals("Alcoholic") ? true : false;
		String glassType = json.getString("strGlass");
		String pictureUrl = json.getString("strDrinkThumb");

		Cocktail cocktail = new Cocktail(name, instructions, category, isAlcoholic, glassType, pictureUrl);
		usedCocktails.add(cocktail);

        return cocktail;
	}



    private static void manageHintSystem(String cocktailName) {
		revealOrder.clear();
		revealOrder = IntStream.rangeClosed(0, cocktailName.length() - 1).boxed().collect(Collectors.toList());
		Collections.shuffle(revealOrder);

		Collections.shuffle(hintOrder);
		currentCocktailHint = "_".repeat(cocktailName.length());
		revealQuantity = (int) Math.max(Math.ceil((double) (cocktailName.length() - MAX_ATTEMPTS + 1) / (MAX_ATTEMPTS + 1)), 1);
    }



    public static String getHints(Cocktail currentCocktail) {
        int hintCount = Math.abs(attempts - MAX_ATTEMPTS);
        StringBuilder hints = new StringBuilder();
        for (int index = 0; index < hintCount; index++) {
            switch(hintOrder.get(index)) {
                case "category": {
                    hints.append("> Category: " + currentCocktail.getCategory() + "\n");
                    break;
                }
                case "alcoholic": {
                    hints.append("> Alcoholic: " + currentCocktail.isAlcoholic() + "\n"); 
                    break;
                }
                case "glass": {
                    hints.append("> Glass type: " + currentCocktail.getGlassType() + "\n");
                    break;
                }
                case "picture": {
                    hints.append("> Picture: " + currentCocktail.getPictureUrl() + "\n");
                    break;
                }
            }
        }
        return hints.toString();
    }



	public static void addLetters(Cocktail cocktail) {
        if (revealOrder.size() == 0) return;
		StringBuilder newHint = new StringBuilder(currentCocktailHint);
		int leftToReveal = revealQuantity;
		while (leftToReveal-- > 0) {
			newHint.setCharAt(revealOrder.get(0), cocktail.getName().charAt(revealOrder.get(0)));
			revealOrder.remove(0);
		}
		currentCocktailHint = newHint.toString();
	}



    public static void updateScore() {
        currentScore += attempts;
		attempts = MAX_ATTEMPTS;
		drinkNumber++;
    }



	public static boolean restartGame() throws Exception {
        boolean newHighScore = false;
		usedCocktails.clear();
		if (currentScore > highScore) {
			highScore = currentScore;
			newHighScore = true;
		}
		currentScore = 0;
		drinkNumber = 1;
		attempts = MAX_ATTEMPTS;
        return newHighScore;
	}



    public static void resetHighScore() {
        highScore = 0;
    }
}
