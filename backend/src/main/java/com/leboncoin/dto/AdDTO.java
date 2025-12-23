package com.leboncoin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leboncoin.entity.CategoryEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * 📦 DTO (Data Transfer Object) - Objet de transfert de données
 *
 * Responsabilités : ✅ Représenter les données échangées avec le frontend (JSON
 * ↔ Java) ✅ Définir les validations (@NotBlank, @NotNull, @Positive, etc.) ✅
 * Exposer uniquement les champs nécessaires (sécurité) ✅ Découpler l'API REST
 * de la structure de la base de données
 *
 * ❌ Ne contient PAS : - Logique métier - Annotations JPA (@Entity, @Table,
 * etc.) - Relations complexes (évite les boucles infinies en JSON)
 *
 * Avantages : 🔒 Sécurité : ne pas exposer toute l'Entity au frontend 🎯
 * Simplicité : format adapté au besoin de l'API 🔄 Stabilité : changer la DB
 * n'impacte pas l'API
 *
 * Flux : Frontend (JSON) ↔ DTO ↔ Service ↔ Entity ↔ Database
 */
public class AdDTO {

    private Integer id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Integer price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> images;

    @JsonProperty("imageObjectNames")
    private List<String> imageObjectNames;

    @NotNull(message = "Category is required")
    private CategoryEnum category;

    private Integer userId;

    private String userEmail;

    // Constructors
    public AdDTO() {
    }

    public AdDTO(Integer id, String title, String description, Integer price, List<String> images, List<String> imageObjectNames, CategoryEnum category, Integer userId, String userEmail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
        this.imageObjectNames = imageObjectNames;
        this.category = category;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImageObjectNames() {
        return imageObjectNames;
    }

    public void setImageObjectNames(List<String> imageObjectNames) {
        this.imageObjectNames = imageObjectNames;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
