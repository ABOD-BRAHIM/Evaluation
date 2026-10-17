package ma.projet;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestProjetApp {

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   DEMARRAGE DE L'APPLICATION GESTION DE PROJETS (EXERCICE 2)     ");
        System.out.println("==================================================================\n");

        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService employeTacheService = new EmployeTacheService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // 1. Creation des Employes
            System.out.println(">>> 1. Creation des Employes...");
            Employe emp1 = new Employe("ALAMI", "Ahmed", "0612345678");
            Employe emp2 = new Employe("BENANI", "Sara", "0698765432");
            Employe emp3 = new Employe("CHRAIBI", "Omar", "0655443322");

            employeService.create(emp1);
            employeService.create(emp2);
            employeService.create(emp3);
            System.out.println("Employes crees avec succes.\n");

            // 2. Creation d'un Projet avec emp1 comme Chef de projet
            System.out.println(">>> 2. Creation des Projets...");
            // Conforme a l'exemple du sujet : Projet : 4 | Nom : Gestion de stock | Date debut : 14 Janvier 2013
            Date debutProjet1 = sdf.parse("14/01/2013");
            Date finProjet1 = sdf.parse("31/12/2013");
            Projet proj1 = new Projet("Gestion de stock", debutProjet1, finProjet1, emp1);

            Date debutProjet2 = sdf.parse("01/02/2024");
            Date finProjet2 = sdf.parse("30/06/2024");
            Projet proj2 = new Projet("Application Mobile RH", debutProjet2, finProjet2, emp1);

            projetService.create(proj1);
            projetService.create(proj2);
            System.out.println("Projets crees avec succes.\n");

            // 3. Creation des Taches planifiees pour le Projet 1
            System.out.println(">>> 3. Creation des Taches...");
            // Conforme a l'exemple du sujet :
            // Analyse       (prix: 1200 DH > 1000)
            // Conception    (prix: 1500 DH > 1000)
            // Developpement (prix: 8000 DH > 1000)
            // Test & Recette(prix: 800 DH <= 1000)
            Tache t1 = new Tache("Analyse", sdf.parse("01/02/2013"), sdf.parse("25/02/2013"), 1200.0, proj1);
            Tache t2 = new Tache("Conception", sdf.parse("01/03/2013"), sdf.parse("20/03/2013"), 1500.0, proj1);
            Tache t3 = new Tache("Développement", sdf.parse("01/04/2013"), sdf.parse("30/04/2013"), 8000.0, proj1);
            Tache t4 = new Tache("Tests & Recette", sdf.parse("01/05/2013"), sdf.parse("15/05/2013"), 800.0, proj1);

            tacheService.create(t1);
            tacheService.create(t2);
            tacheService.create(t3);
            tacheService.create(t4);
            System.out.println("Taches creees avec succes.\n");

            // 4. Affectation et Realisation des Taches (EmployeTache)
            System.out.println(">>> 4. Affectation des Taches et Saisie des Dates Reelles...");
            // Reproduction fidele de l'affichage attendu pour proj1 :
            // 12 | Analyse       | 10/02/2013 | 20/02/2013 (assignee a emp2)
            // 13 | Conception    | 10/03/2013 | 15/03/2013 (assignee a emp2)
            // 14 | Developpement | 10/04/2013 | 25/04/2013 (assignee a emp3)
            EmployeTache et1 = new EmployeTache(emp2, t1, sdf.parse("10/02/2013"), sdf.parse("20/02/2013"));
            EmployeTache et2 = new EmployeTache(emp2, t2, sdf.parse("10/03/2013"), sdf.parse("15/03/2013"));
            EmployeTache et3 = new EmployeTache(emp3, t3, sdf.parse("10/04/2013"), sdf.parse("25/04/2013"));

            employeTacheService.create(et1);
            employeTacheService.create(et2);
            employeTacheService.create(et3);
            System.out.println("Affectations enregistrees avec succes.\n");

            System.out.println("==================================================================");
            System.out.println("           TESTS DES REQUETES ET FONCTIONNALITES EXIGÉES           ");
            System.out.println("==================================================================\n");

            // Test 1 (EmployeService) : Afficher la liste des taches realisees par un employe
            System.out.println("--- Test 1 (EmployeService) : Taches realisees par un employe ---");
            employeService.afficherTachesRealiseesParEmploye(emp2);

            // Test 2 (EmployeService) : Afficher la liste des projets geres par un employe (chef de projet)
            System.out.println("--- Test 2 (EmployeService) : Projets geres par un employe ---");
            employeService.afficherProjetsGeresParEmploye(emp1);

            // Test 3 (ProjetService) : Afficher la liste des taches planifiees pour un projet
            System.out.println("--- Test 3 (ProjetService) : Taches planifiees pour un projet ---");
            projetService.afficherTachesPlanifiees(proj1);

            // Test 4 (ProjetService) : Afficher la liste des taches realisees avec les dates reelles (Exemple sujet)
            System.out.println("--- Test 4 (ProjetService) : Taches realisees avec dates reelles (Exemple du Sujet) ---");
            projetService.afficherTachesRealisees(proj1);

            // Test 5 (TacheService) : Taches dont le prix > 1000 DH (Requete Nommee)
            System.out.println("--- Test 5 (TacheService) : Taches avec prix > 1000 DH (Requete Nommee) ---");
            tacheService.afficherTachesPrixSuperieurA1000();

            // Test 6 (TacheService) : Taches realisees entre deux dates
            System.out.println("--- Test 6 (TacheService) : Taches realisees entre deux dates ---");
            Date debutFiltre = sdf.parse("01/02/2013");
            Date finFiltre = sdf.parse("31/03/2013");
            tacheService.afficherTachesRealiseesEntreDates(debutFiltre, finFiltre);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            System.out.println("Fermeture propre de la SessionFactory.");
        }
    }
}
