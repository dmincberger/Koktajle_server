package springboot_ds.koktajle.Response;

import springboot_ds.koktajle.model.Ingriedient;

import java.util.ArrayList;
import java.util.List;

public class IngredientResponse {

    private List<Ingriedient> drinks;

    public List<springboot_ds.koktajle.model.Ingriedient> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<Ingriedient> ingriedients) {
        this.drinks = ingriedients;
    }
}
