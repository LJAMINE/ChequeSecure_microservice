# ChequeSecure – Guide de Démonstration

Guide pour présenter le projet ChequeSecure lors d'une démo.

---

## 1. Vue d'ensemble du projet

**ChequeSecure** est une plateforme de certification électronique de chèques bancaires au Maroc, basée sur une **architecture micro-services** avec Spring Cloud.

**Flux principal :** Un commerçant saisit un chèque → la Banque Centrale orchestre → l'Agence bancaire vérifie le solde et réserve le montant → retour CERTIFIE ou REFUSE.

---

## 2. Concepts et technologies utilisés

### Architecture

| Concept | Description |
|---------|-------------|
| **Micro-services** | 3 services métier : Commerçant, Banque Centrale, Agence Bancaire |
| **Séparation des responsabilités** | Chaque service a sa propre base de données (H2 en dev) |
| **Communication synchrone** | Appels HTTP REST entre services |

### Communication inter-services

| Concept | Utilisation | Service |
|---------|-------------|---------|
| **OpenFeign** | Appel déclaratif vers un service (URL fixe) | Commerçant → Banque Centrale |
| **RestTemplate** | Appel HTTP avec URL dynamique (depuis la base) | Banque Centrale → Agence |
| **Circuit Breaker** | Coupe les appels après trop d'échecs, évite les cascades | Banque Centrale |
| **Retry** | Réessais automatiques en cas d'erreur temporaire | Banque Centrale |
| **Fallback** | Réponse de secours quand un service est indisponible | Commerçant, Banque Centrale |

### Persistance et API

| Concept | Description |
|---------|-------------|
| **Spring Data JPA** | Accès aux données, repositories |
| **H2** | Base de données en mémoire (développement) |
| **DTO (Data Transfer Object)** | Objets d'échange entre couches, pas de partage d'entités |
| **MapStruct** | Mapping automatique Entity ↔ DTO |
| **REST API** | Endpoints documentés avec OpenAPI/Swagger |
| **Validation** | `@Valid`, `@NotBlank`, `@NotNull`, etc. |

### Structure des couches

```
Controller (REST) → Service (logique métier) → Repository (JPA)
       ↓                      ↓
   DTO Request/Response    Entity
       ↓                      ↓
   Mapper (MapStruct)  ←  Conversion Entity ↔ DTO
```

---

## 3. Les 3 services métier

| Service | Port | Rôle |
|---------|------|------|
| **Agence Bancaire** | 8083 | Gère les comptes, vérifie le solde, réserve le montant, enregistre les opérations |
| **Banque Centrale** | 8082 | Gère les agences, orchestre les certifications, route vers l'agence concernée |
| **Commerçant** | 8081 | Saisit les chèques, soumet à certification, consulte les résultats |

---

## 4. Flux de certification

```
Commerçant (8081)                    Banque Centrale (8082)              Agence (8083)
      |                                      |                                  |
      |  POST /certifications (OpenFeign)    |                                  |
      |------------------------------------->|                                  |
      |                                      |  1. Cherche agence par codeBanque|
      |                                      |  2. POST /comptes/{num}/certifier|
      |                                      |     (RestTemplate, URL dynamique) |
      |                                      |--------------------------------->|
      |                                      |                                  |
      |                                      |  CERTIFIE ou REFUSE               |
      |                                      |<---------------------------------|
      |  Response (statut, motifRefus)       |                                  |
      |<-------------------------------------|                                  |
```

---

## 5. Ordre de démarrage

1. **Agence Bancaire** (port 8083)
2. **Banque Centrale** (port 8082)
3. **Commerçant** (port 8081)

---

## 6. Scénario de démo (Postman)

### Option A : Données pré-chargées (data.sql)

Si les services ont démarré avec `data.sql`, un chèque existe déjà :

```
POST http://localhost:8081/api/commercant/cheques/CHQ-TEST-001/certifier
```

### Option B : Création manuelle complète

#### Étape 1 – Créer un compte (Agence)

```
POST http://localhost:8083/api/agence/comptes
Content-Type: application/json
```

```json
{
  "numeroCompte": "222222",
  "solde": 1000,
  "referenceClient": "REF002",
  "nomClient": "Client Test",
  "type": "COURANT",
  "dateOuverture": "2026-01-01"
}
```

#### Étape 2 – Créer une agence (Banque Centrale)

```
POST http://localhost:8082/api/banque-centrale/agences
Content-Type: application/json
```

```json
{
  "nom": "Agence Houara",
  "ville": "Houara",
  "adresse": "123 Bd Houara",
  "codeBanque": "111",
  "urlServiceWeb": "http://localhost:8083",
  "email": "houara@bank.ma",
  "telephone": "+212522123456"
}
```

#### Étape 3 – Créer un chèque (Commerçant)

```
POST http://localhost:8081/api/commercant/cheques
Content-Type: application/json
```

```json
{
  "numeroCheque": "CHQ-001",
  "codeBanque": "111",
  "numeroCompte": "222222",
  "cleRib": "12",
  "nomClient": "Youssef",
  "montant": 500
}
```

#### Étape 4 – Soumettre à certification

```
POST http://localhost:8081/api/commercant/cheques/CHQ-001/certifier
```

**Résultat attendu :** `statut: "CERTIFIE"` (si solde ≥ montant)

---

## 7. Endpoints de consultation (Postman)

### Commerçant (8081)

| Action | Méthode | URL |
|--------|---------|-----|
| Liste des chèques | GET | `http://localhost:8081/api/commercant/cheques?page=0&size=10` |
| Détail d'un chèque | GET | `http://localhost:8081/api/commercant/cheques/CHQ-001` |
| Recherche chèques | GET | `http://localhost:8081/api/commercant/cheques/search?statut=CERTIFIE&page=0&size=10` |

### Banque Centrale (8082)

| Action | Méthode | URL |
|--------|---------|-----|
| Liste des agences | GET | `http://localhost:8082/api/banque-centrale/agences?page=0&size=10` |
| Détail d'une agence | GET | `http://localhost:8082/api/banque-centrale/agences/111` |
| Historique certifications | GET | `http://localhost:8082/api/banque-centrale/certifications/historique?page=0&size=10` |

### Agence (8083)

| Action | Méthode | URL |
|--------|---------|-----|
| Liste des comptes | GET | `http://localhost:8083/api/agence/comptes?page=0&size=10` |
| Détail d'un compte | GET | `http://localhost:8083/api/agence/comptes/222222` |
| Opérations d'un compte | GET | `http://localhost:8083/api/agence/comptes/222222/operations?page=0&size=10` |

---

## 8. Swagger UI

Chaque service expose une documentation interactive :

- Commerçant : http://localhost:8081/swagger-ui.html
- Banque Centrale : http://localhost:8082/swagger-ui.html
- Agence : http://localhost:8083/swagger-ui.html

---

## 9. Règles métier à mentionner

- **Chèque :** cycle EN_ATTENTE → CERTIFIE ou REFUSE ; un chèque ne peut être certifié qu'une fois.
- **Agence :** suppression interdite si des certifications lui sont associées.
- **Compte :** type COURANT ou EPARGNE uniquement ; opération CERTIFICATION_CHEQUE enregistrée à chaque certification réussie.
- **Solde insuffisant :** retour REFUSE, aucune opération enregistrée.

---

## 10. Points clés pour la présentation

1. **OpenFeign** : interface déclarative, URL fixe, fallback en cas d'erreur.
2. **RestTemplate** : URL dynamique depuis la base (chaque agence a sa propre URL).
3. **Resilience4J** : Circuit Breaker + Retry pour tolérance aux pannes.
4. **Micro-services** : bases séparées, DTO par service, pas de partage d'entités.
5. **Flux complet** : Commerçant → Banque Centrale → Agence → réponse.
