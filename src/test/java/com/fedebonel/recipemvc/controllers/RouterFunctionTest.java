package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.config.WebConfig;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

public class RouterFunctionTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        WebConfig webConfig = new WebConfig();
        RouterFunction<?> routerToRecipes = webConfig.routeRecipes(recipeService);

        webTestClient = WebTestClient.bindToRouterFunction(routerToRecipes).build();
    }

    @Test
    void testGetRecipes() {
        when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        webTestClient.get().uri("/api/recipes")
                .exchange().expectStatus().isOk()
                .expectBodyList(Recipe.class);
    }
}
