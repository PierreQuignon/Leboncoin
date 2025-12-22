package com.leboncoin.controller;

import com.leboncoin.dto.AdDTO;
import com.leboncoin.service.AdService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 🎮 CONTROLLER - Couche de présentation (API REST)
 *
 * Responsabilités : ✅ Recevoir les requêtes HTTP (GET, POST, PUT, DELETE) ✅
 * Valider les données d'entrée (@Valid) ✅ Déléguer la logique métier au Service
 * ✅ Retourner les réponses HTTP (status codes + JSON)
 *
 * ❌ Ne fait PAS : - Logique métier (calculs, transformations, règles) - Accès
 * direct à la base de données - Conversion Entity ↔ DTO
 *
 * Flux : Frontend → Controller → Service
 */
@RestController
@RequestMapping("/api/ads")
@CrossOrigin(origins = "*")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @PostMapping
    public ResponseEntity<AdDTO> createAd(
            @Valid @RequestBody AdDTO adDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        AdDTO createdAd = adService.createAd(adDTO, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }

    @GetMapping
    public ResponseEntity<List<AdDTO>> getAllAds() {
        List<AdDTO> ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AdDTO>> searchAds(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AdDTO> ads = adService.searchAds(category, title, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdDTO> getAdById(@PathVariable Integer id) {
        AdDTO ad = adService.getAdById(id);
        return ResponseEntity.ok(ad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdDTO> updateAd(
            @PathVariable Integer id,
            @Valid @RequestBody AdDTO adDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        AdDTO updatedAd = adService.updateAd(id, adDTO, userDetails.getUsername());
        return ResponseEntity.ok(updatedAd);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdDTO> deleteAdById(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        AdDTO ad = adService.deleteAdById(id, userDetails.getUsername());
        return ResponseEntity.ok(ad);
    }
}
