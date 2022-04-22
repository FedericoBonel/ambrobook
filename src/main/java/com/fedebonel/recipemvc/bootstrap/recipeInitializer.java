package com.fedebonel.recipemvc.bootstrap;

import com.fedebonel.recipemvc.model.*;
import com.fedebonel.recipemvc.repositories.CategoryRepository;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BootStrap Recipe initializer
 *
 * (Pretty ugly but necessary to load data on start up if nothing gets loaded from DB)
 */
@Slf4j
@Component
public class recipeInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository uomRepository;
    private final CategoryRepository categoryRepository;

    public recipeInitializer(RecipeRepository recipeRepository, UnitOfMeasureRepository uomRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.uomRepository = uomRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recipeRepository.saveAll(getRecipes());
        log.debug("Initialized data");
    }

    private List<Recipe> getRecipes() {
        log.debug("Initializing data");
        List<Recipe> result = new ArrayList<>(2);

        log.debug("Loading UoMs");
        Optional<UnitOfMeasure> cupOptional = uomRepository.findByUnit("Cup");
        if (cupOptional.isEmpty()) throw new RuntimeException("Measure not found");

        Optional<UnitOfMeasure> tblspoonOptional = uomRepository.findByUnit("Tablespoon");
        if (tblspoonOptional.isEmpty()) throw new RuntimeException("Measure not found");

        Optional<UnitOfMeasure> tspoonOptional = uomRepository.findByUnit("Teaspoon");
        if (tspoonOptional.isEmpty()) throw new RuntimeException("Measure not found");

        Optional<UnitOfMeasure> pinchOptional = uomRepository.findByUnit("Pinch");
        if (pinchOptional.isEmpty()) throw new RuntimeException("Measure not found");

        Optional<UnitOfMeasure> ounceOptional = uomRepository.findByUnit("Ounce");
        if (ounceOptional.isEmpty()) throw new RuntimeException("Measure not found");

        Optional<UnitOfMeasure> poundOptional = uomRepository.findByUnit("Pound");
        if (poundOptional.isEmpty()) throw new RuntimeException("Measure not found");

        Optional<UnitOfMeasure> pintOptional = uomRepository.findByUnit("Pint");
        if (pintOptional.isEmpty()) throw new RuntimeException("Measure not found");

        Optional<UnitOfMeasure> eachOptional = uomRepository.findByUnit("Each");
        if (eachOptional.isEmpty()) throw new RuntimeException("Measure not found");

        UnitOfMeasure cup = cupOptional.get();
        UnitOfMeasure tblspoon = tblspoonOptional.get();
        UnitOfMeasure tspoon = tspoonOptional.get();
        UnitOfMeasure ounce = ounceOptional.get();
        UnitOfMeasure pinch = pinchOptional.get();
        UnitOfMeasure pound = poundOptional.get();
        UnitOfMeasure pint = pintOptional.get();
        UnitOfMeasure each = eachOptional.get();

        log.debug("Loading categories");
        Optional<Category> mexicanOptional = categoryRepository.findByName("Mexican");
        if (mexicanOptional.isEmpty()) throw new RuntimeException("Category not found");
        Category mexican = mexicanOptional.get();

        log.debug("Creating and saving recipes");
        Recipe guacamole = new Recipe();
        guacamole.setDescription("Perfect Guacamole");
        guacamole.setPrepTime(10);
        guacamole.setCookTime(0);
        guacamole.setServings(4);
        guacamole.setDifficulty(Difficulty.EASY);
        guacamole.getCategories().add(mexican);
        guacamole.setDirections("""
                Cut the avocado:
                Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.
                
                                
                How to make guacamole - scoring avocado
                Mash the avocado flesh:
                Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)
                                
                How to make guacamole - smashing avocado with fork
                Add remaining ingredients to taste:
                Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.
                                
                Add the chopped onion, cilantro, black pepper, and chilis. Chili peppers vary individually in their spiciness. So, start with a half of one chili pepper and add more to the guacamole to your desired degree of heat.
                                
                Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.
                                
                Serve immediately:
                If making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.)
                                
                Garnish with slices of red radish or jigama strips. Serve with your choice of store-bought tortilla chips or make your own homemade tortilla chips.
                                
                Refrigerate leftover guacamole up to 3 days.
                """);
        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("""
                Be careful handling chilis! If using, it's best to wear food-safe gloves. If no gloves are available, wash your hands thoroughly after handling, and do not touch your eyes or the area near your eyes for several hours afterwards.
                """);
        guacamole.setNote(guacNotes);

        guacamole.addIngredient(new Ingredient("ripe avocados", BigDecimal.valueOf(2), each));
        guacamole.addIngredient(new Ingredient("kosher salt", BigDecimal.valueOf(0.25), tspoon));
        guacamole.addIngredient(new Ingredient("fresh lime juice", BigDecimal.valueOf(1), tblspoon));
        guacamole.addIngredient(new Ingredient("red onions thinly sliced", BigDecimal.valueOf(3), tblspoon));
        guacamole.addIngredient(new Ingredient("serrano chilis, stems and seeds removed, minced", BigDecimal.valueOf(1), each));
        guacamole.addIngredient(new Ingredient("cilantro (leaves and tender stems), finely chopped", BigDecimal.valueOf(2), tblspoon));
        guacamole.addIngredient(new Ingredient("freshly ground black pepper", BigDecimal.valueOf(1), pinch));
        guacamole.addIngredient(new Ingredient("ripe tomato, chopped", BigDecimal.valueOf(0.5), each));
        guacamole.setSource("Simply Recipes");
        guacamole.setUrl("www.simplyrecipes.com");

        Recipe tacos = new Recipe();
        tacos.setDescription("Spicy Grilled Chicken Tacos");
        tacos.setPrepTime(20);
        tacos.setCookTime(15);
        tacos.setServings(5);
        tacos.setDifficulty(Difficulty.MODERATE);
        tacos.getCategories().add(mexican);
        tacos.setDirections("""
                Prepare a gas or charcoal grill for medium-high, direct heat
                Make the marinade and coat the chicken:
                In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.
                                
                Set aside to marinate while the grill heats and you prepare the rest of the toppings.
                                
                Spicy Grilled Chicken Tacos
                Grill the chicken:
                Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.
                                
                Warm the tortillas:
                Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.
                                
                Wrap warmed tortillas in a tea towel to keep them warm until serving.
                                
                Assemble the tacos:
                Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.
                """);
        Notes tacosNotes = new Notes();
        tacosNotes.setRecipeNotes("""
                Look for ancho chile powder with the Mexican ingredients at your grocery store, on buy it online. (If you can't find ancho chili powder, you replace the ancho chili, the oregano, and the cumin with 2 1/2 tablespoons regular chili powder, though the flavor won't be quite the same.)
                """);
        tacos.setNote(tacosNotes);
        tacos.addIngredient(new Ingredient("ancho chili powder", BigDecimal.valueOf(2), tblspoon));
        tacos.addIngredient(new Ingredient("dried oregano", BigDecimal.valueOf(1), tspoon));
        tacos.addIngredient(new Ingredient("salt", BigDecimal.valueOf(0.5), tspoon));
        tacos.addIngredient(new Ingredient("clove garlic", BigDecimal.valueOf(1), each));
        tacos.addIngredient(new Ingredient("finely grated orange zest", BigDecimal.valueOf(1), tblspoon));
        tacos.addIngredient(new Ingredient("olive oil", BigDecimal.valueOf(3), tblspoon));
        tacos.addIngredient(new Ingredient("boneless chicken things", BigDecimal.valueOf(5), each));
        tacos.setSource("Simply Recipes");
        tacos.setUrl("www.simplyrecipes.com");


        // Store the data
        result.add(guacamole);
        result.add(tacos);

        return result;
    }
}












