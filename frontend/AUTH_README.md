# Authentification Frontend - Leboncoin

## 📁 Architecture

L'implémentation de l'authentification suit les bonnes pratiques Angular avec une architecture modulaire et propre :

```
src/app/
├── components/
│   ├── login/               # Page de connexion
│   ├── signup/              # Page d'inscription
│   └── home/                # Page d'accueil après connexion
├── services/
│   └── auth.service.ts      # Service d'authentification centralisé
├── models/
│   └── auth.model.ts        # Interfaces TypeScript pour les DTOs
├── guards/
│   └── auth.guard.ts        # Guards pour protéger les routes
├── interceptors/
│   └── auth.interceptor.ts  # Intercepteur HTTP pour ajouter le token JWT
└── app.config.ts            # Configuration de l'application
```

## 🎨 UI Library

L'application utilise **Angular Material** pour tous les composants UI :

- Formulaires avec validation
- Cartes (cards)
- Boutons
- Icônes
- Champs de saisie (inputs)
- Messages d'erreur
- Spinners de chargement

## 🔐 Fonctionnalités

### 1. Service d'Authentification (`AuthService`)

- ✅ Inscription (`register`)
- ✅ Connexion (`login`)
- ✅ Déconnexion (`logout`)
- ✅ Gestion du token JWT dans localStorage
- ✅ État d'authentification avec Angular Signals
- ✅ État de l'utilisateur courant

### 2. Composants

#### Login Component

- Formulaire réactif avec validation
- Champs : email + mot de passe
- Validation d'email
- Affichage/masquage du mot de passe
- Gestion des erreurs (401, 0, etc.)
- Lien vers la page d'inscription

#### Signup Component

- Formulaire réactif avec validation
- Champs : email + mot de passe + confirmation
- Validation personnalisée pour la correspondance des mots de passe
- Auto-login après inscription réussie
- Gestion des erreurs (409, 400, 0, etc.)
- Lien vers la page de connexion

#### Home Component

- Page d'accueil après connexion
- Affichage de l'email de l'utilisateur
- Bouton de déconnexion
- Toolbar Material

### 3. Guards

#### `authGuard`

- Protège les routes nécessitant une authentification
- Redirige vers `/login` si non authentifié

#### `guestGuard`

- Empêche l'accès aux pages login/signup si déjà authentifié
- Redirige vers `/` si déjà authentifié

### 4. Intercepteur HTTP

L'intercepteur `authInterceptor` ajoute automatiquement le token JWT dans le header `Authorization` de toutes les requêtes HTTP sortantes.

## 🔄 API Backend

Les composants appellent les endpoints suivants :

### Inscription

```
POST /api/auth/register
Body: { email: string, password: string }
Response: { id: number, email: string }
```

### Connexion

```
POST /api/auth/login
Body: { email: string, password: string }
Response: { token: string, user: { id: number, email: string } }
```

## 🛣️ Routes

| Route     | Composant       | Guard      | Description                               |
| --------- | --------------- | ---------- | ----------------------------------------- |
| `/`       | HomeComponent   | authGuard  | Page d'accueil (authentification requise) |
| `/login`  | LoginComponent  | guestGuard | Page de connexion                         |
| `/signup` | SignupComponent | guestGuard | Page d'inscription                        |

## 🎯 Bonnes Pratiques Implémentées

1. **Composants Standalone** : Utilisation des composants standalone Angular 17+
2. **Signals** : Utilisation d'Angular Signals pour la gestion d'état réactive
3. **Reactive Forms** : Formulaires réactifs avec validation
4. **Lazy Loading** : Chargement paresseux des composants via `loadComponent`
5. **Guards** : Protection des routes avec guards fonctionnels
6. **Intercepteurs** : Ajout automatique du token JWT
7. **Separation of Concerns** : Service centralisé pour l'authentification
8. **Type Safety** : Interfaces TypeScript pour tous les DTOs
9. **Error Handling** : Gestion complète des erreurs HTTP
10. **UX** : Indicateurs de chargement, messages d'erreur clairs, validation en temps réel

## 🚀 Utilisation

### Lancement de l'application

```bash
cd frontend
npm install
npm start
```

L'application sera accessible sur `http://localhost:4200`

### Flux utilisateur

1. **Première visite** : L'utilisateur est redirigé vers `/login`
2. **Inscription** : L'utilisateur peut cliquer sur "S'inscrire" pour créer un compte
3. **Connexion** : Après inscription ou directement via `/login`
4. **Token** : Le token JWT est stocké dans localStorage
5. **Accueil** : Redirection vers `/` après connexion réussie
6. **Navigation** : Le token est ajouté automatiquement à toutes les requêtes
7. **Déconnexion** : Le token est supprimé et l'utilisateur est redirigé vers `/login`

## 🔒 Sécurité

- ✅ Validation côté client des formulaires
- ✅ Stockage sécurisé du token JWT
- ✅ Intercepteur HTTP pour l'ajout automatique du token
- ✅ Guards pour protéger les routes
- ✅ Validation backend des données (DTOs avec Jakarta Validation)
