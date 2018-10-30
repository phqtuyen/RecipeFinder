package RecipeFinder.DataModel;

import java.util.*;

public class Recipe {
    private String name;
    private List<Ingredient> ingredients;

    public Recipe(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>()
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

}
