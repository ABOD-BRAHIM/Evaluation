package ma.projet;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class App {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   DEMARRAGE DE L'APPLICATION DE GESTION DE STOCK");
        System.out.println("==================================================\n");

        // Initialisation des services
        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();
        LigneCommandeService ligneCommandeService = new LigneCommandeService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // 1. Creation des Categories
            System.out.println(">>> 1. Creation des Categories...");
            Categorie c1 = new Categorie("CAT01", "Ordinateurs portables");
            Categorie c2 = new Categorie("CAT02", "Accessoires et Peripheriques");
            Categorie c3 = new Categorie("CAT03", "Composants et Stockage");

            categorieService.create(c1);
            categorieService.create(c2);
            categorieService.create(c3);
            System.out.println("Categories creees avec succes.\n");

            // 2. Creation des Produits
            System.out.println(">>> 2. Creation des Produits...");
            Produit p1 = new Produit("ES12", 120.0f, c1);
            Produit p2 = new Produit("ZR85", 100.0f, c2);
            Produit p3 = new Produit("EE85", 200.0f, c1);
            Produit p4 = new Produit("MS10", 80.0f, c2);   // <= 100 DH
            Produit p5 = new Produit("SSD500", 350.0f, c3); // > 100 DH

            produitService.create(p1);
            produitService.create(p2);
            produitService.create(p3);
            produitService.create(p4);
            produitService.create(p5);
            System.out.println("Produits crees avec succes.\n");

            // 3. Creation des Commandes avec differentes dates
            System.out.println(">>> 3. Creation des Commandes...");
            Date date1 = sdf.parse("14/03/2013");
            Date date2 = sdf.parse("25/04/2013");
            Date date3 = sdf.parse("10/01/2024");

            Commande cmd1 = new Commande(date1);
            Commande cmd2 = new Commande(date2);
            Commande cmd3 = new Commande(date3);

            commandeService.create(cmd1);
            commandeService.create(cmd2);
            commandeService.create(cmd3);
            System.out.println("Commandes creees avec succes.\n");

            // 4. Creation des Lignes de commande (association Commande - Produit - Quantite)
            System.out.println(">>> 4. Creation des Lignes de Commande...");
            // Lignes pour la commande 1 (cmd1 correspond a l'exemple de l'exercice) :
            // ES12 -> 7
            // ZR85 -> 14
            // EE85 -> 5
            LigneCommandeProduit lcp1 = new LigneCommandeProduit(cmd1, p1, 7);
            LigneCommandeProduit lcp2 = new LigneCommandeProduit(cmd1, p2, 14);
            LigneCommandeProduit lcp3 = new LigneCommandeProduit(cmd1, p3, 5);

            // Lignes pour cmd2
            LigneCommandeProduit lcp4 = new LigneCommandeProduit(cmd2, p4, 3);
            LigneCommandeProduit lcp5 = new LigneCommandeProduit(cmd2, p5, 2);

            // Lignes pour cmd3
            LigneCommandeProduit lcp6 = new LigneCommandeProduit(cmd3, p1, 1);

            ligneCommandeService.create(lcp1);
            ligneCommandeService.create(lcp2);
            ligneCommandeService.create(lcp3);
            ligneCommandeService.create(lcp4);
            ligneCommandeService.create(lcp5);
            ligneCommandeService.create(lcp6);
            System.out.println("Lignes de commandes creees avec succes.\n");

            System.out.println("==================================================");
            System.out.println("       TEST DES REQUETES ET FONCTIONNALITES       ");
            System.out.println("==================================================\n");

            // Test 1 : Afficher les produits par categorie
            System.out.println("--- Test 1 : Produits par Categorie ---");
            produitService.afficherProduitsParCategorie(c1);
            produitService.afficherProduitsParCategorie(c2);

            // Test 2 : Afficher les produits commandes entre deux dates
            System.out.println("--- Test 2 : Produits commandes entre deux dates ---");
            Date dDebut = sdf.parse("01/01/2013");
            Date dFin = sdf.parse("31/12/2013");
            produitService.afficherProduitsCommandesEntreDates(dDebut, dFin);

            // Test 3 : Afficher les produits d'une commande donnee (format exemple)
            System.out.println("--- Test 3 : Affichage des produits de la Commande ---");
            produitService.afficherProduitsParCommande(cmd1);

            // Test 4 : Afficher les produits dont le prix > 100 DH avec requete nommee
            System.out.println("--- Test 4 : Requete Nommee (Prix > 100 DH) ---");
            produitService.afficherProduitsPrixSuperieurA100();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fermeture propre de la SessionFactory
            HibernateUtil.shutdown();
            System.out.println("Fermeture de la SessionFactory effectuee avec succes.");
        }
    }
}
