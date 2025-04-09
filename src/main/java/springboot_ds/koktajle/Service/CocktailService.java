package springboot_ds.koktajle.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springboot_ds.koktajle.Response.DrinkResponse;
import springboot_ds.koktajle.Response.IngredientListResponse;
import springboot_ds.koktajle.Response.IngredientResponse;
import springboot_ds.koktajle.model.Ingriedient;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CocktailService {
    private final String BASE_URL = "https://thecocktaildb.com/api/json/v1/1";
    private final WebClient webClient;

    public CocktailService(){
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
public Mono<DrinkResponse> fetchDrinks(String name){
        System.out.println("CZAS NA FETCH.");
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.
                        path("/search.php")
                        .queryParam("s",name)
                        .build())
                .retrieve()
                .bodyToMono(DrinkResponse.class)
                .timeout(Duration.ofSeconds(5))
                .doOnNext(response -> System.out.println("Fetched Response: " + response));
}

public Mono<IngredientListResponse> fetchIngredients(){
        System.out.println("CZAS NA FETCH SKLADNIKOW.");
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.
                        path("/list.php")
                        .queryParam("i","list")
                        .build())
                .retrieve()
                .bodyToMono(IngredientListResponse.class)
                .timeout(Duration.ofSeconds(5))
                .doOnNext(response -> System.out.println("Fetched Response: " + response));
}

public Mono<IngredientResponse> fetchDrinksByIngredient(String ingredient){
        System.out.println("CZAS NA FETCH PO SKLADNIKU: "+ingredient);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.
                        path("/filter.php")
                        .queryParam("i",ingredient)
                        .build())
                .retrieve()
                .bodyToMono(IngredientResponse.class)
                .timeout(Duration.ofSeconds(5))
                .doOnNext(response -> System.out.println("Fetched Response: " + response.toString()));
}
    public Mono<IngredientResponse> fetchDrinksByMultipleIngredients(String ingredientString) {
        String[] ingredients = ingredientString.split(";");
        System.out.println("MULTI-INGREDIENT FETCH: " + Arrays.toString(ingredients));

        // Create list of Mono<Set<Ingriedient>> (each one fetches drinks for a single ingredient)
        List<Mono<Set<Ingriedient>>> allFetches = Arrays.stream(ingredients)
                .map(this::fetchDrinksByIngredient)
                .map(mono -> mono.map(response ->
                        response.getDrinks().stream().collect(Collectors.toSet())
                ))
                .toList();

        // Combine all the Monos into a single Mono<List<Set<Ingriedient>>>
        return Flux.zip(allFetches, results -> {
            List<Set<Ingriedient>> sets = Arrays.stream(results)
                    .map(result -> (Set<Ingriedient>) result)
                    .toList();
            System.out.println("SETS: "+sets);

            // Get intersection of all sets
            Set<Ingriedient> intersection = new HashSet<>(sets.get(0));
            for (int i = 1; i < sets.size(); i++) {
                intersection.retainAll(sets.get(i));
            }

            // Return final IngredientResponse
            IngredientResponse response = new IngredientResponse();
            response.setDrinks(new ArrayList<>(intersection));
            System.out.println("RESPONSE FINAL: "+response.toString());
            return response;
        }).single();
    }
}
