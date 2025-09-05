# 🏡 Projet3 — Backend de plateforme de location immobilière

Projet3 est une application backend développée dans le cadre du parcours Développeur Full-Stack Java chez OpenClassrooms. Elle permet aux locataires potentiels de consulter des biens immobiliers et de contacter les propriétaires via une API sécurisée.

---

## 📚 Sommaire

1. [Description](#description)  
2. [Technologies](#technologies)  
3. [Installation](#installation)  
4. [Configuration](#configuration)  
5. [Utilisation de l'API](#utilisation-de-lapi)  
6. [Contribuer au projet](#contribuer-au-projet)  
7. [Contact](#contact)

---

## 📝 Description

Cette application backend repose sur **Spring Boot 3.5.3**, avec une architecture RESTful. Elle intègre :

- 🔐 Authentification via JWT  
- 🗄️ Persistance des données avec JPA/Hibernate  
- 🧰 Validation des entrées avec Hibernate Validator  
- 📦 Stockage des fichiers configurable  
- 📊 Logging avancé pour le debug  
- 📘 Documentation API avec Swagger (via Springdoc)

---

## ⚙️ Technologies

- Java 21  
- Spring Boot  
- Spring Security + OAuth2 Resource Server  
- Spring Data JPA  
- MySQL  
- Swagger (Springdoc OpenAPI)  
- Lombok  
- Maven

---

## 🛠️ Installation

### Prérequis

- Java 21+  
- Maven  
- MySQL Server  
- Git

### Étapes

```bash
forker le code front-end 
git clone https://github.com/ton-utilisateur/projet3.git
cd projet3
./mvnw install

Créer ton application Back-end suivant les attendues demandé.
configuration de l'application properties pour se connecté a la base de donnné gérer les images et le logage de l'aplication
exemple de véariables a definir :
DB_URL=jdbc:mysql://localhost:3306/projet3
DB_USER=projet3user
DB_PASSWORD=projet3password

JWT_SECRET=your256bitsecretkey
UPLOAD_DIR=uploads

injection de ses variables dans l'application properties 
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
pages.uploadDir=${UPLOAD_DIR:uploads}


📡 Utilisation de l'API

L’API est exposée par défaut sur :
http://localhost:8080

Le lancement de l'application se fait via la commande :
mvn spring-boot:run

Documentation Swagger
Une documentation interactive est disponible à l’adresse suivante :
http://localhost:8080/swagger-ui/index.html
Elle permet de tester les endpoints directement depuis le navigateur.

Exemple de requête POST (authentification)
POST /api/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "motdepasse"
}

Exemple de requête GET (avec JWT)
GET /api/properties
Authorization: Bearer <votre_token_jwt>
🔐 Les routes protégées nécessitent un token JWT valide dans l’en-tête Authorization.
