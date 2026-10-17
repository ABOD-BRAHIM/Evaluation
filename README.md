# Projet Java – Hibernate et JPA
## 1. Requête HQL : produits dont le prix est supérieur à 100 DH

La console d'exécution affiche le résultat d'une requête Hibernate qui récupère les produits dont le prix est supérieur à 100 DH.

![Résultat de la requête HQL sur les produits](screnshot/Capture%20d%27%C3%A9cran%202026-10-17%20163715.png)

## 2. Affichage des tâches sur une période donnée

La console de l'application affiche les tâches réalisées dans une plage de dates spécifique, avec leurs détails et leurs prix.

![Affichage des tâches selon une plage de dates](screnshot/Capture%20d%27%C3%A9cran%202026-10-17%20175538.png)

## 3. Héritage JPA avec la stratégie JOINED

La classe abstraite `Personne` est une entité JPA de base utilisant la stratégie d'héritage `JOINED`. Elle regroupe les informations personnelles communes aux entités qui en héritent.

![Classe Personne utilisant la stratégie JOINED](screnshot/Capture%20d%27%C3%A9cran%202026-10-17%20182602.png)

## 4. Entité LigneCommandeProduit et clé primaire composée

La classe `LigneCommandeProduit` représente une ligne de commande associant une commande à un produit. Elle utilise l'annotation `@EmbeddedId` pour définir une clé primaire composée.

![Entité LigneCommandeProduit](screnshot/Capture%20d%27%C3%A9cran%202026-10-17%20182623.png)

## 5. Classe LigneCommandeProduitId

La classe `LigneCommandeProduitId`, annotée avec `@Embeddable`, contient les identifiants `commandeId` et `produitId`. Ensemble, ils constituent la clé primaire composée de chaque ligne de commande.

![Classe LigneCommandeProduitId](screnshot/Capture%20d%27%C3%A9cran%202026-10-17%20182635.png)

## 6. Service EmployeTacheService : opérations CRUD

La classe `EmployeTacheService` implémente des opérations de persistance, notamment `create` et `update`, pour gérer les tâches associées aux employés.

![Service EmployeTacheService](screnshot/Capture%20d%27%C3%A9cran%202026-10-17%20182646.png)

## 7. Service LigneCommandeService : gestion des transactions

La classe `LigneCommandeService` gère les transactions Hibernate nécessaires à l'ajout et à la mise à jour des lignes de commande dans la base de données.

![Service LigneCommandeService](screnshot/Capture%20d%27%C3%A9cran%202026-10-17%20182654.png)

## 8. Classe utilitaire HibernateUtil

La classe `HibernateUtil` charge le fichier `application.properties` et configure la `SessionFactory` en enregistrant les entités annotées utilisées dans le projet.

![Classe HibernateUtil](screnshot/Capture%20d%27%C3%A9cran%202026-10-17%20182703.png)

## 9. Tests de l'application d'état civil

Le fichier de test principal `TestEtatCivilApp` initialise les services de l'application d'état civil et exécute la création de dix instances de l'entité `Femme`.

![TestEtatCivilApp : création de dix instances de Femme](screnshot/Capture%20d%27%C3%A9cran%202026-10-17%20182712.png)



