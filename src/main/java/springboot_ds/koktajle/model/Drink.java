package springboot_ds.koktajle.model;

public class Drink {
    private String idDrink;
    private String strDrink;
    private String strCategory;
    private String strAlcoholic;
    private String strInstructions;
    private String strDrinkThumb;

    // Getters and Setters
    public String getIdDrink() { return idDrink; }
    public void setIdDrink(String idDrink) { this.idDrink = idDrink; }

    public String getStrDrink() { return strDrink; }
    public void setStrDrink(String strDrink) { this.strDrink = strDrink; }

    public String getStrCategory() { return strCategory; }
    public void setStrCategory(String strCategory) { this.strCategory = strCategory; }

    public String getStrAlcoholic() { return strAlcoholic; }
    public void setStrAlcoholic(String strAlcoholic) { this.strAlcoholic = strAlcoholic; }

    public String getStrInstructions() { return strInstructions; }
    public void setStrInstructions(String strInstructions) { this.strInstructions = strInstructions; }

    public String getStrDrinkThumb() { return strDrinkThumb; }
    public void setStrDrinkThumb(String strDrinkThumb) { this.strDrinkThumb = strDrinkThumb; }
}
