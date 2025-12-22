# API Leboncoin - Test Endpoints

## 1. Récupérer toutes les catégories
```bash
curl -X GET http://localhost:8080/api/categories
```

## 2. Créer une annonce
```bash
curl -X POST http://localhost:8080/api/ads \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Vélo de course",
    "description": "Vélo de course en excellent état, peu utilisé",
    "price": 450,
    "category": "BIKE",
    "images": [
      "https://example.com/image1.jpg",
      "https://example.com/image2.jpg"
    ]
  }'
```

## 3. Récupérer toutes les annonces
```bash
curl -X GET http://localhost:8080/api/ads
```

## 4. Récupérer une annonce par ID
```bash
curl -X GET http://localhost:8080/api/ads/1
```

## Exemples de création d'annonces pour chaque catégorie

### Voiture (CAR)
```bash
curl -X POST http://localhost:8080/api/ads \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Renault Clio 2018",
    "description": "Voiture en très bon état, 50 000 km",
    "price": 12000,
    "category": "CAR",
    "images": ["https://example.com/car.jpg"]
  }'
```

### Maison (HOUSE)
```bash
curl -X POST http://localhost:8080/api/ads \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Appartement T3 Paris",
    "description": "Bel appartement lumineux, 65m²",
    "price": 350000,
    "category": "HOUSE",
    "images": ["https://example.com/house.jpg"]
  }'
```

### Vêtement (CLOTHE)
```bash
curl -X POST http://localhost:8080/api/ads \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Veste en cuir",
    "description": "Veste en cuir noir, taille M",
    "price": 80,
    "category": "CLOTHE",
    "images": ["https://example.com/jacket.jpg"]
  }'
```

### Livre (BOOK)
```bash
curl -X POST http://localhost:8080/api/ads \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code - Robert Martin",
    "description": "Livre de programmation en excellent état",
    "price": 25,
    "category": "BOOK",
    "images": ["https://example.com/book.jpg"]
  }'
```

### Vélo (BIKE)
```bash
curl -X POST http://localhost:8080/api/ads \
  -H "Content-Type: application/json" \
  -d '{
    "title": "VTT Giant",
    "description": "VTT semi-rigide, excellent état",
    "price": 600,
    "category": "BIKE",
    "images": ["https://example.com/bike.jpg"]
  }'
```
