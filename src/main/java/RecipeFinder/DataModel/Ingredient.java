package RecipeFinder.DataModel;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
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
    private LocalDate usedBy;

    public Ingredient() {}

    public void setUnit(String unit) {
        Unit ingredientUni = Unit.isMember(unit);
        if (ingredientUni != null) {
            this.unit = ingredientUni;
        }
    }

    public String getItem() {
        return  this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getAmount() {
        return  this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public LocalDate getUsedBy() {
        return this.usedBy;
    }

    public void setUsedBy(String usedBy) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            this.usedBy = LocalDate.parse(usedBy, formatter);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public boolean isUsable() {
        return this.usedBy != null && (this.usedBy.isAfter(LocalDate.now()) || this.usedBy.isEqual(LocalDate.now()));
    }

    public boolean matchRequirement(Ingredient requirement) {
        return requirement.getItem().equalsIgnoreCase(this.getItem())
                && requirement.getUnit() == this.getUnit() && requirement.getAmount() <= this.getAmount();
    }

    public String toString() {
        if (this.usedBy == null)
            return this.item + " " + Integer.toString(this.amount) + " " + this.unit.toString();
        return this.item + " " + Integer.toString(this.amount) + " " + this.unit.toString() + " " +this.usedBy.toString();
    }
}

