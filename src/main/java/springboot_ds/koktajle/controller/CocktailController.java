package springboot_ds.koktajle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springboot_ds.koktajle.Response.Drink;
import springboot_ds.koktajle.service.CocktailService;

@RestController

public class CocktailController {

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService){
        this.cocktailService =  cocktailService;
    }

    @GetMapping("/api/search")
    public Mono<ResponseEntity<Drink>> fetchCocktail(@RequestParam String name){
        System.out.println("DOSZEDL REQUEST.");
        return cocktailService.fetchDrinks(name)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
