package com.fedebonel.recipemvc.datainitializer;

import com.fedebonel.recipemvc.model.Roles;
import com.fedebonel.recipemvc.model.*;
import com.fedebonel.recipemvc.repositories.CategoryRepository;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import com.fedebonel.recipemvc.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BootStrap Recipe initializer
 * <p>
 * (Pretty ugly but necessary to load data on start up if nothing gets loaded from DB)
 */
@Slf4j
@Component
@Profile("default")
public class DataInitializerH2 implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository uomRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializerH2(RecipeRepository recipeRepository,
                             UnitOfMeasureRepository uomRepository,
                             CategoryRepository categoryRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder) {
        this.recipeRepository = recipeRepository;
        this.uomRepository = uomRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recipeRepository.saveAll(getRecipes());
        initializeUsers();
        log.debug("Initialized data");
    }

    private void initializeUsers() {
        User adminSample = new User();
        adminSample.setUsername("admin");
        adminSample.setPassword(passwordEncoder.encode("pass"));
        adminSample.setActive(true);
        UserRole adminRole = new UserRole();
        adminRole.setRole("ROLE_" + Roles.ADMIN);
        adminRole.setUser(adminSample);
        adminSample.setUserRoles(List.of(adminRole));


        User userSample = new User();
        userSample.setUsername("user");
        userSample.setPassword(passwordEncoder.encode("pass"));
        userSample.setActive(true);
        UserRole userRole = new UserRole();
        userRole.setRole("ROLE_" + Roles.USER);
        userRole.setUser(userSample);
        userSample.setUserRoles(List.of(userRole));

        userRepository.saveAll(List.of(adminSample, userSample));
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
        Category mexican = categoryRepository
                .findByName("Mexican")
                .orElseThrow(RuntimeException::new);

        Category argentine = categoryRepository
                .findByName("Argentine")
                .orElseThrow(RuntimeException::new);

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
        guacamole.setUrl("https://www.simplyrecipes.com");

        Recipe tacos = new Recipe();
        tacos.setDescription("Spicy Grilled Chicken Tacos");
        tacos.setPrepTime(20);
        tacos.setCookTime(15);
        tacos.setServings(5);
        tacos.setDifficulty(Difficulty.MEDIUM);
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
        tacos.setUrl("https://www.simplyrecipes.com");

        Recipe empanadas = new Recipe();
        empanadas.setDescription("Empanadas Salteñas");
        empanadas.setPrepTime(30);
        empanadas.setCookTime(30);
        empanadas.setServings(12);
        empanadas.setDifficulty(Difficulty.MEDIUM);
        empanadas.getCategories().add(argentine);
        empanadas.setDirections("""
                Step 1
                Combine water and salt in a small saucepan over medium heat and warm through, stirring until salt is dissolved. Remove from heat and let cool for 2 to 3 minutes.
                
                Step 2
                Combine sifted flour and melted margarine in the bowl of a food processor; pulse until crumbly. Add salt water gradually to the food processor and pulse until a soft dough ball forms that easily separates from the edge of the bowl. Add more water, 1 teaspoon at a time, only if needed. Press dough into a ball and wrap tightly with plastic wrap. Refrigerate for at least 30 minutes.
                
                Step 3
                Preheat the oven to 425 degrees F (220 degrees C). Cover a baking sheet with aluminum foil and grease with vegetable oil. Set aside.
                
                Step 4
                Bring a saucepan of water to a boil. Add cubed potatoes and cook until soft, 8 to 10 minutes.
                
                Step 5
                Meanwhile, melt butter in a skillet over medium heat. Cook and stir onions and green onions until soft and translucent, about 5 minutes. Add red peppers and cook for 5 more minutes. Add ground beef and stir with a wooden spoon until fully browned, about 5 minutes. Season with salt, cayenne pepper, cumin, and paprika. Remove from heat.
                
                Step 6
                Drain potatoes and add to the filling. Toss in chopped eggs and let filling cool down for a few minutes.
                
                Step 7
                Remove pastry from the fridge and knead gently on a floured surface. Divide dough in half and roll out each piece to 1/8-inch thickness.
                
                Step 8
                Cut out 3-inch circles with a pastry cutter or sharp knife and wet the edges lightly with water. Add 1 large tablespoon of filling in the center of each disc. Fold the pastry over, press edges together, and seal with a fork. Transfer empanadas to the prepared baking sheet.
                
                Step 9
                Bake in the preheated oven until golden brown, 10 to 15 minutes. Remove from the oven and allow to cool just a little before serving.
                """);
        Notes empanadasNotes = new Notes();
        empanadasNotes.setRecipeNotes("""
                These are truly traditional beef empanadas from Salta, one of the Northern Provinces in Argentina. Made with ground beef, potatoes, hard-boiled eggs, and a fluffy homemade empanada pastry, these are just to die for!
                """);
        empanadas.setNote(empanadasNotes);

        empanadas.addIngredient(new Ingredient("water", BigDecimal.valueOf(1), tblspoon));
        empanadas.addIngredient(new Ingredient("kosher salt", BigDecimal.valueOf(1.5), tspoon));
        empanadas.addIngredient(new Ingredient("all-porpoise flour sifted", BigDecimal.valueOf(2), cup));
        empanadas.addIngredient(new Ingredient("lard, melted", BigDecimal.valueOf(0.24), cup));
        empanadas.addIngredient(new Ingredient("vegetable oil", BigDecimal.valueOf(1), tspoon));
        empanadas.addIngredient(new Ingredient("potatoes, peeled and cubed", BigDecimal.valueOf(2), each));
        empanadas.addIngredient(new Ingredient("onions, chopped", BigDecimal.valueOf(2), each));
        empanadas.addIngredient(new Ingredient("stalks green onions, finely chopped", BigDecimal.valueOf(2), each));
        empanadas.addIngredient(new Ingredient("medium bell peppers, seeded and chopped", BigDecimal.valueOf(2), each));
        empanadas.addIngredient(new Ingredient("ground beef", BigDecimal.valueOf(0.66), pound));
        empanadas.addIngredient(new Ingredient("cayenne pepper", BigDecimal.valueOf(0.5), tspoon));
        empanadas.addIngredient(new Ingredient("ground cumin", BigDecimal.valueOf(1), tspoon));
        empanadas.addIngredient(new Ingredient("paprika", BigDecimal.valueOf(1), tspoon));
        empanadas.addIngredient(new Ingredient("large boiled eggs, peeled and chopped", BigDecimal.valueOf(2), each));
        empanadas.setSource("All recipes");
        empanadas.setUrl("https://www.allrecipes.com/recipe/282433/empanadas-saltenas/");

        // Store the data
        result.add(guacamole);
        result.add(tacos);
        result.add(empanadas);

        return result;
    }
}












