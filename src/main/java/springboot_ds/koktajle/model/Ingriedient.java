package springboot_ds.koktajle.model;

import java.util.Objects;

public class Ingriedient {
    private String strDrink;
    private String strDrinkThumb;
    private int idDrink;


    public String getStrDrink() {
        return strDrink;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public int getIdDrink() {
        return idDrink;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingriedient)) return false;
        Ingriedient that = (Ingriedient) o;
        return idDrink == that.idDrink;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDrink);
    }
}
