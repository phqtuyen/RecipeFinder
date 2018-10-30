package RecipeFinder.DataModel;

import java.util.*;
import java.text.SimpleDateFormat;

public class Ingredient {

    enum Unit
    {
        of("of"),
        ml("ml"),
        grams("grams"),
        slices("slices");

        private String strUnit;

        private Unit(String unit) {
            this.strUnit = unit;
        }

        public String toString() {
            return this.strUnit;
        }

        public static Unit isMember(String strUnit) {
            for (Unit u: Unit.values()) {
                if (u.strUnit.equalsIgnoreCase(strUnit)) {
                    return u;
                }
            }
            return null;
        }
    }
    private String name;
    private int amount;
    private Unit unit;
    private Date usedBy;

    public Ingredient(String name, int amount, String unit, String usedBy) {
        this.name = name;
        this.amount = amount;

        Unit ingredientUni = Unit.isMember(unit);
        if (ingredientUni != null) {
            this.unit = ingredientUni;
        }

        try {
            this.usedBy = new SimpleDateFormat("dd/MM/yyyy").parse(usedBy);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Ingredient(String name, int amount, String unit) {
        this.name = name;
        this.amount = amount;
        Unit ingredientUni = Unit.isMember(unit);
        if (ingredientUni != null) {
            this.unit = ingredientUni;
        }
    }

    public String getName() {
        return  this.name;
    }

    public int getAmount() {
        return  this.amount;
    }

    public Unit getUnit() {
        return this.unit;
    }
}

