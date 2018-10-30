package RecipeFinder.DataModel;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.*;

public class Recipe {
    private String name;
    private List<Ingredient> ingredients;
//    @JsonCreator
//    public Recipe(String name) {
//        this.name = name;
//        this.ingredients = new ArrayList<>();
//    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    @Override
    public String toString() {
        return name;
    }

}
