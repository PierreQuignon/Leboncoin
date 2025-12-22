package com.leboncoin.config;

import com.leboncoin.entity.Category;
import com.leboncoin.entity.CategoryEnum;
import com.leboncoin.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepository) {
        return args -> {
            // Initialiser les catégories si elles n'existent pas
            if (categoryRepository.count() == 0) {
                Arrays.stream(CategoryEnum.values())
                        .forEach(categoryEnum -> {
                            Category category = new Category(categoryEnum);
                            categoryRepository.save(category);
                        });
                System.out.println("Categories initialized: " + CategoryEnum.values().length);
            }
        };
    }
}
