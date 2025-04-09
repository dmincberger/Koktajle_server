package springboot_ds.koktajle.Response;

import springboot_ds.koktajle.model.IngredientList;

import java.util.List;

public class IngredientListResponse {

    private List<IngredientList> drinks;

    public List<IngredientList> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<IngredientList> drinks) {
        this.drinks = drinks;
    }
}
