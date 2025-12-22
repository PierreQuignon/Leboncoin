package com.leboncoin.repository;

import com.leboncoin.entity.Category;
import com.leboncoin.entity.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(CategoryEnum name);
}
