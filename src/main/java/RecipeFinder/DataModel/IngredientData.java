package RecipeFinder.DataModel;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class IngredientData extends ArrayList<Ingredient>{

    public IngredientData filterOutdatedItems() {
        IngredientData temp = new IngredientData();
        for (Ingredient ingredient: this) {
            if (ingredient.isUsable())
                temp.add(ingredient);
        }
        return temp;
    }

    public void sortByDate() {
        Comparator<Ingredient> byUsedByDate = new Comparator<Ingredient>() {
            public int compare(Ingredient left, Ingredient right) {
                if (left.getUsedBy().isBefore(right.getUsedBy())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };

        Collections.sort(this, byUsedByDate);
    }

}
