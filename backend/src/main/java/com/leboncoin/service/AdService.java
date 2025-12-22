package com.leboncoin.service;

import com.leboncoin.dto.AdDTO;
import com.leboncoin.entity.Ad;
import com.leboncoin.entity.Category;
import com.leboncoin.entity.User;
import com.leboncoin.repository.AdRepository;
import com.leboncoin.repository.CategoryRepository;
import com.leboncoin.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 🧠 SERVICE - Couche de logique métier
 *
 * Responsabilités : ✅ Implémenter la logique métier (règles de gestion) ✅
 * Orchestrer les appels au Repository ✅ Convertir Entity ↔ DTO (mappage) ✅
 * Gérer les transactions (@Transactional) ✅ Valider les règles métier complexes
 * ✅ Lever des exceptions métier (ResourceNotFoundException, etc.)
 *
 * Flux : Controller → Service → Repository → Database
 */
@Service
public class AdService {

    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public AdService(AdRepository adRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AdDTO createAd(AdDTO adDTO, String userEmail) {

        Category category = categoryRepository.findByName(adDTO.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found: " + adDTO.getCategory()));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Ad ad = new Ad();
        ad.setTitle(adDTO.getTitle());
        ad.setDescription(adDTO.getDescription());
        ad.setPrice(adDTO.getPrice());
        ad.setImages(adDTO.getImages());
        ad.setCategory(category);
        ad.setUser(user);

        Ad savedAd = adRepository.save(ad);

        return convertToDTO(savedAd);
    }

    @Transactional(readOnly = true)
    public List<AdDTO> getAllAds() {
        return adRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdDTO getAdById(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found with id: " + id));
        return convertToDTO(ad);
    }

    @Transactional
    public AdDTO deleteAdById(Integer id, String userEmail) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found with id: " + id));

        if (!ad.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own ads");
        }

        AdDTO deletedAdDTO = convertToDTO(ad);
        adRepository.delete(ad);
        return deletedAdDTO;
    }

    @Transactional
    public AdDTO updateAd(Integer id, AdDTO adDTO, String userEmail) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found with id: " + id));

        if (!ad.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only update your own ads");
        }

        Category category = categoryRepository.findByName(adDTO.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found: " + adDTO.getCategory()));

        ad.setTitle(adDTO.getTitle());
        ad.setDescription(adDTO.getDescription());
        ad.setPrice(adDTO.getPrice());
        ad.setImages(adDTO.getImages());
        ad.setCategory(category);

        Ad updatedAd = adRepository.save(ad);

        return convertToDTO(updatedAd);
    }

    @Transactional(readOnly = true)
    public Page<AdDTO> searchAds(String category, String title, Integer minPrice, Integer maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<Ad> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtre par catégorie
            if (category != null && !category.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("name"), category));
            }

            // Filtre par titre (recherche partielle case-insensitive)
            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                ));
            }

            // Filtre par prix minimum
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            // Filtre par prix maximum
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Ad> adsPage = adRepository.findAll(spec, pageable);
        return adsPage.map(this::convertToDTO);
    }

    private AdDTO convertToDTO(Ad ad) {
        AdDTO dto = new AdDTO();
        dto.setId(ad.getId());
        dto.setTitle(ad.getTitle());
        dto.setDescription(ad.getDescription());
        dto.setPrice(ad.getPrice());
        dto.setImages(ad.getImages());
        dto.setCategory(ad.getCategory().getName());
        dto.setUserId(ad.getUser().getId());
        dto.setUserEmail(ad.getUser().getEmail());
        return dto;
    }
}
