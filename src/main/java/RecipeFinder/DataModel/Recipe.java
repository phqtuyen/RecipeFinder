package RecipeFinder.DataModel;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.LocalDate;
import java.util.*;

public class Recipe {
    private String name;
//    private List<Ingredient> ingredients;
    private IngredientData ingredients;
    private LocalDate closetUsedBy;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIngredients(IngredientData ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientData getIngredients() {
        return this.ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void setClosetUsedBy(LocalDate closetUsedBy) {
        this.closetUsedBy = closetUsedBy;
    }

    public LocalDate getClosetUsedBy() {
        return this.closetUsedBy;
    }

    public boolean isCookable(IngredientData ingredients) {
        int count = 0;
        LocalDate closetUsedBy = null;
        for (Ingredient reqIngredient: this.ingredients)
            for (Ingredient ingredient: ingredients) {
                if (ingredient.matchRequirement(reqIngredient)) {
                    if (closetUsedBy != null)
                        closetUsedBy = ingredient.getUsedBy().isBefore(closetUsedBy) ? ingredient.getUsedBy() : closetUsedBy;
                    else
                        closetUsedBy = ingredient.getUsedBy();
                    count++;
                    break;
                }
            }
        if (count == this.ingredients.size()) {
            this.setClosetUsedBy(closetUsedBy);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

}
