package com.leboncoin.controller;

import com.leboncoin.entity.CategoryEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @GetMapping
    public ResponseEntity<List<CategoryEnum>> getAllCategories() {
        return ResponseEntity.ok(Arrays.asList(CategoryEnum.values()));
    }
}
