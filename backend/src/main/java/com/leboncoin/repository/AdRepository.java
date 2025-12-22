package com.leboncoin.repository;

import com.leboncoin.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 💾 REPOSITORY - Couche d'accès aux données
 *
 * Responsabilités : ✅ Communiquer avec la base de données (CRUD) ✅ Exécuter les
 * requêtes SQL/JPQL ✅ Retourner des Entities (pas de DTO ici) ✅ Méthodes
 * fournies automatiquement par JpaRepository : - save(), findById(), findAll(),
 * deleteById(), etc. ✅ Définir des requêtes personnalisées si besoin
 *
 * ❌ Ne fait PAS : - Logique métier (validation, calculs) - Conversion DTO ↔
 * Entity (rôle du Service) - Gestion des transactions (rôle du Service)
 *
 * Flux : Service → Repository → PostgreSQL
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Integer>, JpaSpecificationExecutor<Ad> {
}
