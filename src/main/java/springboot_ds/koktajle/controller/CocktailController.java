package springboot_ds.koktajle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springboot_ds.koktajle.Response.DrinkResponse;
import springboot_ds.koktajle.Response.IngredientResponse;
import springboot_ds.koktajle.service.CocktailService;

@RestController

public class CocktailController {

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService){
        this.cocktailService =  cocktailService;
    }

    @GetMapping("/api/search")
    public Mono<ResponseEntity<DrinkResponse>> fetchCocktail(@RequestParam String name){
        System.out.println("DOSZEDL REQUEST.");
        return cocktailService.fetchDrinks(name)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/api/ingredient")
    public Mono<ResponseEntity<IngredientResponse>> fetchDrinksByIngredient(@RequestParam String ingredient){
        System.out.println("DOSZEDL REQUEST O FETCH NA INGREDIENT");
        return cocktailService.fetchDrinksByIngredient(ingredient)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/api/multiple_ingredients")
    public Mono<ResponseEntity<IngredientResponse>> fetchDrinksByIngredientMultiple(@RequestParam String ingredient){
        System.out.println("DOSZEDL REQUESTS, CZAS NA KLIKA FETCHY.");
        return cocktailService.fetchDrinksByMultipleIngredients(ingredient)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
