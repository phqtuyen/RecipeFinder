package RecipeFinder.DataModel;

import java.util.*;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.*;

@JsonPropertyOrder({ "item", "amount", "unit", "usedBy", })
public class Ingredient {

    enum Unit
    {
        of("of"),
        ml("ml"),
        grams("grams"),
        slices("slices");

        private String strUnit;

        Unit(String unit) {
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

    private String item;
    private int amount;
    private Unit unit;
    private Date usedBy;

    public Ingredient() {}

    public void setUnit(String unit) {
        System.out.println("unit " + unit);
        Unit ingredientUni = Unit.isMember(unit);
        if (ingredientUni != null) {
            this.unit = ingredientUni;
        }
    }

    public String getItem() {
        return  this.item;
    }

    public void setItem(String item) {
        System.out.println("item " + item);
        this.item = item;
    }

    public int getAmount() {
        return  this.amount;
    }

    public void setAmount(int amount) {
        System.out.println("amount " + Integer.toString(amount));
        this.amount = amount;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public Date getUsedBy() {
        return this.usedBy;
    }

    public void setUsedBy(String usedBy) {
        System.out.println("Date " + usedBy);
        try {
            this.usedBy = new SimpleDateFormat("dd/MM/yyyy").parse(usedBy);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public String toString() {
        if (this.usedBy == null)
            return this.item + " " + Integer.toString(this.amount) + " " + this.unit.toString();
        return this.item + " " + Integer.toString(this.amount) + " " + this.unit.toString() + this.usedBy.toString();
    }
}

