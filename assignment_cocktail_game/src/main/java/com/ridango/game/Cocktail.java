package com.ridango.game;

public class Cocktail {

    private String name;
    private String instructions;
    private String category;
    private boolean isAlcoholic;
    private String glassType;
    private String pictureUrl;

    Cocktail() {}

    Cocktail(String name, String instructions, String category, boolean isAlcoholic, String glassType, String pictureUrl) {
        this.name = name;
        this.instructions = instructions;
        this.category = category;
        this.isAlcoholic = isAlcoholic;
        this.glassType = glassType;
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAlcoholic() {
        return isAlcoholic;
    }

    public String getGlassType() {
        return glassType;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
