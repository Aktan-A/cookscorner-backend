package com.neobis.cookscorner.util;

import com.neobis.cookscorner.enums.IngredientMeasureType;

import java.text.DecimalFormat;

public class IngredientTextUtil {

    private static String getMeasureText(IngredientMeasureType type) {
        switch (type) {
            case IngredientMeasureType.TEASPOON: return "tsp";
            case IngredientMeasureType.TABLESPOON: return "tbsp";
            case IngredientMeasureType.CUP: return "cup";
            case IngredientMeasureType.PINT: return "pt";
            case IngredientMeasureType.QUART: return "qt";
            case IngredientMeasureType.GALLON: return "gal";
            case IngredientMeasureType.OUNCE: return "oz";
            case IngredientMeasureType.FLUID_OUNCE: return "fl. oz";
            case IngredientMeasureType.POUND: return "lb";
            case IngredientMeasureType.MILLIMETER: return "ml";
            case IngredientMeasureType.LITER: return "L";
            case IngredientMeasureType.GRAM: return "g";
            case IngredientMeasureType.KILOGRAM: return "kg";
            default: return "";
        }
    }

    private static String getQuantityText(Double quantity) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String qtyString = decimalFormat.format(quantity);
        switch (qtyString) {
            case "0.25": return "1/4";
            case "0.3", "0.33": return "1/3";
            case "0.5": return "1/2";
            case "0.66": return "2/3";
            case "0.75": return "3/4";
            default: return qtyString;
        }
    }

    public static String getIngredientQuantityText(Double quantity, IngredientMeasureType type) {
        String quantityText = getQuantityText(quantity);
        String measureText = getMeasureText(type);
        return String.format("%s %s", quantityText, measureText);
    }

}
