package com.fedebonel.recipemvc.datainitializer;

import com.fedebonel.recipemvc.model.Category;
import com.fedebonel.recipemvc.model.UnitOfMeasure;
import com.fedebonel.recipemvc.repositories.CategoryRepository;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class DataInitializerMySQL implements ApplicationListener<ContextRefreshedEvent> {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializerMySQL(UnitOfMeasureRepository unitOfMeasureRepository, CategoryRepository categoryRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (unitOfMeasureRepository.count() == 0) {
            log.debug("Unit of measures not found in database----------------------------");
            initializeUnitOfMeasures();
        }

        if (categoryRepository.count() == 0) {
            log.debug("Categories not found in database----------------------------");
            initializeCategories();
        }
    }

    private void initializeUnitOfMeasures() {
        log.debug("Initializing unit of measures----------------------------");
        UnitOfMeasure pinch = new UnitOfMeasure();
        pinch.setUnit("Pinch");
        unitOfMeasureRepository.save(pinch);

        UnitOfMeasure teaspoon = new UnitOfMeasure();
        teaspoon.setUnit("Teaspoon");
        unitOfMeasureRepository.save(teaspoon);

        UnitOfMeasure tablespoon = new UnitOfMeasure();
        tablespoon.setUnit("Tablespoon");
        unitOfMeasureRepository.save(tablespoon);

        UnitOfMeasure cup = new UnitOfMeasure();
        cup.setUnit("Cup");
        unitOfMeasureRepository.save(cup);

        UnitOfMeasure ounce = new UnitOfMeasure();
        ounce.setUnit("Ounce");
        unitOfMeasureRepository.save(ounce);

        UnitOfMeasure pound = new UnitOfMeasure();
        pound.setUnit("Pound");
        unitOfMeasureRepository.save(pound);

        UnitOfMeasure pint = new UnitOfMeasure();
        pint.setUnit("Pint");
        unitOfMeasureRepository.save(pint);

        UnitOfMeasure each = new UnitOfMeasure();
        each.setUnit("Each");
        unitOfMeasureRepository.save(each);
    }

    private void initializeCategories() {
        log.debug("Initializing categories----------------------------");
        Category ar = new Category();
        ar.setName("Argentine");
        categoryRepository.save(ar);

        Category it = new Category();
        it.setName("Italian");
        categoryRepository.save(it);

        Category amer = new Category();
        amer.setName("American");
        categoryRepository.save(amer);

        Category mex = new Category();
        mex.setName("Mexican");
        categoryRepository.save(mex);

        Category jp = new Category();
        jp.setName("Japanese");
        categoryRepository.save(jp);
    }
}
