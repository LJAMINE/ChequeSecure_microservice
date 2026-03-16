# ChequeSecure – Système de Certification Électronique de Chèques

## Architecture Micro-services avec Spring Cloud

---

## Contexte

ChequeSecure est une plateforme distribuée dédiée à la certification électronique de chèques bancaires au Maroc. Elle permet à un commerçant de saisir les informations d'un chèque via un système de reconnaissance optique (OCR), de les transmettre à la Banque Centrale qui orchestre la vérification auprès de la banque du client, et de recevoir en retour une certification numérique garantissant la validité du chèque.

L'objectif est de proposer une solution moderne, sécurisée et scalable, capable de gérer efficacement :

- La saisie et le suivi des chèques par le commerçant
- L'orchestration des vérifications par la Banque Centrale du Maroc
- La vérification du solde et la réservation du montant par l'agence bancaire du client
- La sécurisation des échanges via OAuth2 / OIDC avec Keycloak
- La communication synchrone inter-services via OpenFeign + Resilience4J

---

## Mission du Développeur

Concevoir et développer une application basée sur une architecture micro-services avec Spring Cloud, en suivant les bonnes pratiques : séparation des responsabilités, tolérance aux pannes, documentation des API et sécurisation via Keycloak.

L'application devra :

- Exposer des API RESTful documentées avec OpenAPI / Swagger
- Assurer la résilience des appels inter-services via Resilience4J
- Stocker les données dans des bases H2 ou PostgreSQL par service
- Être entièrement conteneurisée et déployable via Docker Compose

---

## Fonctionnalités Principales

### Module 1 : Commerçant-Service

- Saisir les informations d'un chèque (numéro, RIB, montant, nom du client)
- Consulter la liste de tous les chèques avec pagination
- Consulter le détail d'un chèque et son statut de certification
- Soumettre un chèque à la Banque Centrale pour demande de certification
- Mettre à jour automatiquement le statut du chèque (EN_ATTENTE, CERTIFIE, REFUSE)
- Rechercher un chèque par numéro, nom du client ou statut

### Module 2 : Banque-Centrale-Service

- Gérer le référentiel des agences bancaires (CRUD complet)
- Recevoir une demande de certification d'un commerçant
- Identifier la banque du client à partir du code banque du RIB
- Router la demande vers l'agence bancaire concernée via OpenFeign
- Recevoir la réponse de l'agence et retransmettre le résultat au commerçant
- Consulter l'historique des demandes de certification traitées

### Module 3 : Agence-Bancaire-Service

- Gérer les comptes bancaires (consultation, mise à jour du solde)
- Recevoir une demande de certification et vérifier le solde du compte
- Réserver le montant du chèque si le solde est suffisant via une opération CERTIFICATION_CHEQUE
- Enregistrer chaque opération dans l'historique du compte
- Retourner le résultat (CERTIFIE ou REFUSE) à la Banque Centrale
- Consulter l'historique des opérations d'un compte avec filtres (type, date)

---

## Règles de Gestion Globales

### Chèques (Commerçant-Service)

- Un chèque est identifié de manière unique par son numéro
- Un chèque ne peut être soumis à certification qu'une seule fois à la fois
- Le statut évolue selon le cycle : EN_ATTENTE → CERTIFIE | REFUSE
- Seul un chèque au statut CERTIFIE peut être accepté par le commerçant
- La date de certification est enregistrée automatiquement lors du changement de statut

### Agences Bancaires (Banque-Centrale-Service)

- Chaque agence est identifiée par un code banque unique, extrait du RIB du client
- L'URL du web service de l'agence est stockée et utilisée dynamiquement par OpenFeign pour le routage
- La suppression d'une agence est impossible si des certifications en cours lui sont associées

### Comptes (Agence-Bancaire-Service)

- Un compte est obligatoirement de type COURANT ou EPARGNE
- La certification d'un chèque génère obligatoirement une opération de type CERTIFICATION_CHEQUE
- Si le solde est insuffisant, aucune opération n'est enregistrée et le résultat retourné est REFUSE
- Chaque opération est horodatée et non modifiable après création

### Gestion des Rôles (bonus)

- ROLE_ADMIN : accès complet à tous les services (CRUD complet + consultation historique)
- ROLE_BANQUE_CENTRALE : gestion des agences + orchestration des certifications
- ROLE_AGENCE : gestion des comptes et traitement des opérations
- ROLE_COMMERCANT : saisie des chèques et consultation des résultats de certification

---

## Modélisation des Entités

### Commerçant-Service – Cheque

| Champ | Type | Contraintes | Description |
|-------|------|-------------|-------------|
| id | Long | PK, auto | Identifiant unique |
| numeroCheque | String | NotBlank, Unique | Numéro du chèque |
| codeBanque | String | NotBlank, Size(3) | Code banque extrait du RIB (3 chiffres) |
| numeroCompte | String | NotBlank | Numéro de compte du client |
| cleRib | String | NotBlank, Size(2) | Clé de contrôle du RIB (2 chiffres) |
| nomClient | String | NotBlank | Nom complet du titulaire du compte |
| montant | Double | NotNull, Min(0.01) | Montant du chèque en dirhams |
| statut | Enum | Défaut EN_ATTENTE | EN_ATTENTE, CERTIFIE, REFUSE |
| dateCreation | LocalDateTime | auto | Date et heure de saisie du chèque |
| dateCertification | LocalDateTime | nullable | Date de certification ou de refus |
| motifRefus | String | nullable, max 255 | Motif du refus si statut = REFUSE |

### Banque-Centrale-Service – AgenceBancaire

| Champ | Type | Contraintes | Description |
|-------|------|-------------|-------------|
| id | Long | PK, auto | Identifiant unique |
| nom | String | NotBlank | Nom de la banque / agence |
| ville | String | NotBlank | Ville du siège principal |
| adresse | String | NotBlank | Adresse postale complète |
| codeBanque | String | NotBlank, Unique, Size(3) | Code banque unique sur 3 chiffres |
| urlServiceWeb | String | NotBlank | URL du web service REST de l'agence |
| email | String | Email | Email de contact de l'agence |
| telephone | String | NotBlank | Numéro de téléphone de l'agence |
| actif | Boolean | Défaut true | Indique si l'agence est active |

### Agence-Bancaire-Service – Compte

| Champ | Type | Contraintes | Description |
|-------|------|-------------|-------------|
| id | Long | PK, auto | Identifiant unique |
| numeroCompte | String | NotBlank, Unique | Numéro de compte bancaire |
| solde | Double | Min(0) | Solde actuel du compte en dirhams |
| referenceClient | String | NotBlank | Référence / identifiant du titulaire |
| nomClient | String | NotBlank | Nom complet du titulaire |
| type | Enum | NotNull | COURANT, EPARGNE |
| dateOuverture | LocalDate | NotNull | Date d'ouverture du compte |
| actif | Boolean | Défaut true | Indique si le compte est actif |
| operations | List\<Operation\> | OneToMany, cascade | Historique complet des opérations |

### Agence-Bancaire-Service – Operation

| Champ | Type | Contraintes | Description |
|-------|------|-------------|-------------|
| id | Long | PK, auto | Identifiant unique |
| date | LocalDateTime | auto à la création | Date et heure de l'opération |
| montant | Double | NotNull, Min(0.01) | Montant de l'opération en dirhams |
| numeroCheque | String | nullable | Numéro du chèque concerné (si CERTIFICATION_CHEQUE) |
| type | Enum | NotNull | DEBIT, CREDIT, CERTIFICATION_CHEQUE |
| description | String | max 500 | Description libre de l'opération |
| soldeAvant | Double | NotNull | Solde du compte avant l'opération |
| soldeApres | Double | NotNull | Solde du compte après l'opération |
| compte | Compte | ManyToOne, NotNull | Compte bancaire associé |

---

## Tolérance aux Pannes – Resilience4J (Bonus)

### Pattern 1 : Circuit Breaker (Disjoncteur)

Le circuit passe par trois états :

- **CLOSED** (fonctionnement normal) : les appels passent normalement, taux d'échec surveillé
- **OPEN** (protection active) : appels bloqués, fallback retourné immédiatement
- **HALF_OPEN** (test de reprise) : nombre limité d'appels de test autorisés

```
CLOSED ──(trop d'échecs)──→ OPEN ──(délai écoulé)──→ HALF_OPEN
 ↑                                                        │
 └──────────────(appels de test OK)───────────────────────┘
                 └──────────────(appels de test KO)──→ OPEN
```

### Pattern 2 : Retry (Réessai automatique)

Réessai automatique avec délai croissant entre chaque tentative pour absorber les erreurs réseau transitoires.

### Pattern 3 : TimeLimiter (Timeout)

Coupe automatiquement tout appel dépassant un délai maximum configuré (ex: 3 secondes).

### Résumé des comportements

| Situation | Sans Resilience4J | Avec Resilience4J |
|-----------|-------------------|-------------------|
| Agence KO | Panne en cascade | Fallback : refus propre |
| Agence lente | Threads bloqués | Appel coupé après 3s |
| Erreur réseau brève | Échec immédiat | 3 tentatives automatiques |
| Trop d'échecs | Chaque appel échoue | Circuit ouvert, réponses immédiates |

---

## Communication Synchrone – OpenFeign

### Flux complet d'une certification

```
Commerçant-Service
 │
 │── POST /certifications (OpenFeign) ──→ Banque-Centrale-Service
 │                                           │
 │                                           │── POST /comptes/{num}/certifier (OpenFeign)
 │                                           │   ──→ Agence-Bancaire-Service
 │                                           │        │
 │                                           │        │── Vérifie le solde
 │                                           │        │── Réserve le montant
 │                                           │        │── Enregistre l'opération
 │                                           │        └── Retourne CERTIFIE | REFUSE
 │                                           │
 │                                           └── Retransmet le résultat au Commerçant
```

---

## Architecture Technique

```
 ┌─────────────────────────────┐
 │     Keycloak : 8080         │
 │  (OAuth2 / OIDC Provider)   │
 └──────────────┬──────────────┘
                │ JWT
 ┌──────────────▼──────────────┐
 │   Gateway Service : 9999    │
 │   Routage + Auth Filter JWT │
 └────┬─────────────┬─────────┘
      │             │
 ┌────▼───────┐ ┌───▼──────────────────┐
 │ Commercant │ │ BanqueCentrale-Svc   │
 │ Port 8081  │→│ Port 8082            │
 │ H2/Postgres│ │ OpenFeign            │
 └────────────┘ │ + Resilience4J CB    │
                └──────────┬───────────┘
                           │ OpenFeign
                ┌──────────▼───────────┐
                │ AgenceBancaire-Svc   │
                │ Port 8083            │
                │ H2/Postgres          │
                └──────────────────────┘

 ┌─────────────────────────────────────────┐
 │            INFRASTRUCTURE               │
 │  Discovery (Eureka) : 8761              │
 │  Config Service     : 8888              │
 └─────────────────────────────────────────┘
```

---

## Structure du Projet Maven

```
cheque-secure-app/
├── config-service/           ← Port 8888
├── discovery-service/        ← Port 8761
├── gateway-service/          ← Port 9999
├── commercant-service/       ← Port 8081
├── banque-centrale-service/  ← Port 8082
└── agence-bancaire-service/  ← Port 8083
```

---

## Déploiement Docker Compose

| Service | Image de base | Port exposé |
|---------|---------------|-------------|
| config-service | eclipse-temurin:17 | 8888 |
| discovery-service | eclipse-temurin:17 | 8761 |
| gateway-service | eclipse-temurin:17 | 9999 |
| commercant-service | eclipse-temurin:17 | 8081 |
| banque-centrale-service | eclipse-temurin:17 | 8082 |
| agence-bancaire-service | eclipse-temurin:17 | 8083 |
| keycloak | quay.io/keycloak/keycloak:latest | 8080 |
| postgres | postgres:16 | 5432 |

---

## Travail Demandé

### A. Partie Conception

1. Établir une architecture technique du projet montrant l'ensemble des micro-services et leurs interactions synchrones (OpenFeign + Resilience4J)
2. Établir un diagramme de classes montrant les entités métier : Cheque, AgenceBancaire, Compte, Operation
3. Établir un diagramme de séquence illustrant le flux complet d'une certification de chèque

### B. Partie Implémentation

4. Créer un projet Maven multi-modules incluant : commercant-service, banque-centrale-service, agence-bancaire-service, gateway-service, discovery-service, config-service
5. Développer et tester les micro-services techniques : discovery-service, gateway-service, config-service
6. Développer et tester commercant-service : Entités (Cheque), Repository, Service, DTO, Mapper (MapStruct), RestController, Swagger, appel OpenFeign vers banque-centrale-service
7. Développer et tester banque-centrale-service : Entités (AgenceBancaire), CRUD complet, OpenFeign vers agence-bancaire-service, Circuit Breaker Resilience4J (patterns Circuit Breaker + Retry + TimeLimiter), Swagger (Resilience4J est un bonus)
8. Développer et tester agence-bancaire-service : Entités (Compte, Operation), vérification du solde, réservation du montant, enregistrement de l'opération CERTIFICATION_CHEQUE, Swagger
9. Sécuriser l'application avec Keycloak (OAuth2 / OIDC) (bonus) : validation JWT dans la Gateway et les micro-services, gestion des rôles ADMIN / BANQUE_CENTRALE / AGENCE / COMMERCANT
10. Déployer l'ensemble de l'application avec Docker et Docker Compose
