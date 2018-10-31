package RecipeFinder.DataModel;

import java.util.*;

public class RecipeData extends ArrayList<Recipe>{

    public RecipeData getCookableRecipes(IngredientData ingredients) {
        RecipeData temp = new RecipeData();
        for (Recipe recipe: this) {
            if (recipe.isCookable(ingredients))
                temp.add(recipe);
        }
        return  temp;
    }

    public void sortByDate() {
        Comparator<Recipe> byUsedByDate = new Comparator<Recipe>() {
            public int compare(Recipe left, Recipe right) {
                if (left.getClosetUsedBy().isBefore(right.getClosetUsedBy())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };

        Collections.sort(this, byUsedByDate);
    }


}
