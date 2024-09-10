package com.ridango;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ridango.game.Cocktail;
import com.ridango.game.CocktailGameApplication;
import com.ridango.game.CocktailGameController;

public class CocktailGameTests {

    private Cocktail cocktail;

    @BeforeEach
    public void setUp() throws Exception {
        CocktailGameController.restartGame();
        CocktailGameController.resetHighScore();
        cocktail = CocktailGameController.getNextCocktail();
    }

    @Test
    public void testHintSameLengthAsCocktailName() throws Exception {
        Assertions.assertThat(CocktailGameController.getCocktailHint().length()).isEqualTo(cocktail.getName().length());
    }

    @Test
    public void testFirstTryCorrectAnswerAwardsMaxPoints() throws Exception {
        CocktailGameApplication.handleCorrectGuess();
        Assertions.assertThat(CocktailGameController.getScore()).isEqualTo(CocktailGameController.MAX_ATTEMPTS);
    }

    @Test
    public void testMultipleGuessAwardsFewerPoints() throws Exception {
        CocktailGameApplication.setCurrentCocktail(cocktail);
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleCorrectGuess();
        Assertions.assertThat(CocktailGameController.getScore()).isEqualTo(CocktailGameController.MAX_ATTEMPTS - 2);
    }

    @Test
    public void testGameOver() throws Exception {
        CocktailGameApplication.setCurrentCocktail(cocktail);
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleWrongGuess();
        Assertions.assertThat(CocktailGameController.getUsedCocktails()).isNotEqualTo(cocktail);
    }

    @Test
    public void testGameRemembersHighScoreAfterGameOver() throws Exception {
        CocktailGameApplication.setCurrentCocktail(cocktail);
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleCorrectGuess();
        CocktailGameApplication.handleCorrectGuess();
        cocktail = CocktailGameController.getNextCocktail();
        CocktailGameApplication.setCurrentCocktail(cocktail);
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleWrongGuess();
        CocktailGameApplication.handleWrongGuess();
        Assertions.assertThat(CocktailGameController.getHighScore()).isNotEqualTo(CocktailGameController.getScore());
        Assertions.assertThat(CocktailGameController.getHighScore()).isEqualTo((CocktailGameController.MAX_ATTEMPTS * 2) - 1);
    }

    @Test
    public void testAssureNoRepeatCocktails() throws Exception {
        int games = 40;
        for (int i = 0; i < games; i++) {
            CocktailGameApplication.handleCorrectGuess();
        }
        List<Cocktail> usedCocktails = CocktailGameController.getUsedCocktails();
        List<String> usedCocktailNames = usedCocktails.stream().map((cocktail) -> cocktail.getName()).collect(Collectors.toList());
        Set<String> uniqueCocktails = new HashSet<String>(usedCocktailNames);
        Assertions.assertThat(uniqueCocktails.size()).isEqualTo(games + 1);
    }
}
