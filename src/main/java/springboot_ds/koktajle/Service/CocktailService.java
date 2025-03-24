package springboot_ds.koktajle.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import springboot_ds.koktajle.Response.Drink;

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
public Mono<Drink> fetchDrinks(String name){
        System.out.println("CZAS NA FETCH.");
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.
                        path("/search.php")
                        .queryParam("s",name)
                        .build())
                .retrieve()
                .bodyToMono(Drink.class)
                .doOnNext(response -> System.out.println("Fetched Response: " + response));
}
}
