package ma.projet;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestEtatCivilApp {

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   DEMARRAGE DE L'APPLICATION GESTION DE L'ETAT CIVIL (EXERCICE 3)");
        System.out.println("==================================================================\n");

        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // 1. Création de 10 femmes
            System.out.println(">>> 1. Création de 10 femmes...");
            Femme f1 = new Femme("RAMI", "Salima", "0611111111", "Casablanca", sdf.parse("15/05/1970"));
            Femme f2 = new Femme("ALI", "Amal", "0622222222", "Rabat", sdf.parse("20/08/1975"));
            Femme f3 = new Femme("ALAOUI", "Wafa", "0633333333", "Fes", sdf.parse("10/12/1980"));
            Femme f4 = new Femme("ALAMI", "Karima", "0644444444", "Marrakech", sdf.parse("05/03/1968")); // La plus âgée
            Femme f5 = new Femme("BENANI", "Nadia", "0655555555", "Tanger", sdf.parse("12/07/1985"));
            Femme f6 = new Femme("CHRAIBI", "Mouna", "0666666666", "Agadir", sdf.parse("25/09/1990"));
            Femme f7 = new Femme("TAZI", "Khadija", "0677777777", "Meknes", sdf.parse("18/02/1992"));
            Femme f8 = new Femme("IDRISSI", "Fatima", "0688888888", "Oujda", sdf.parse("01/01/1988"));
            Femme f9 = new Femme("BOUAZIZ", "Latifa", "0699999999", "Kenitra", sdf.parse("14/11/1982"));
            Femme f10 = new Femme("BERRADA", "Imane", "0610101010", "Tetouan", sdf.parse("30/06/1995"));

            femmeService.create(f1);
            femmeService.create(f2);
            femmeService.create(f3);
            femmeService.create(f4);
            femmeService.create(f5);
            femmeService.create(f6);
            femmeService.create(f7);
            femmeService.create(f8);
            femmeService.create(f9);
            femmeService.create(f10);
            System.out.println("10 femmes créées avec succès.\n");

            // 2. Création de 5 hommes
            System.out.println(">>> 2. Création de 5 hommes...");
            // Homme correspondant exactement à l'exemple du sujet : SAFI SAID
            Homme h1 = new Homme("SAFI", "Said", "0601010101", "Casablanca", sdf.parse("10/01/1965"));
            Homme h2 = new Homme("MANSOURI", "Karim", "0602020202", "Rabat", sdf.parse("15/04/1972"));
            Homme h3 = new Homme("EL FASSI", "Yassine", "0603030303", "Fes", sdf.parse("22/09/1978"));
            Homme h4 = new Homme("ZAHIR", "Rachid", "0604040404", "Marrakech", sdf.parse("05/11/1983"));
            Homme h5 = new Homme("KABBAJ", "Hassan", "0605050505", "Tanger", sdf.parse("19/06/1987"));

            hommeService.create(h1);
            hommeService.create(h2);
            hommeService.create(h3);
            hommeService.create(h4);
            hommeService.create(h5);
            System.out.println("5 hommes créés avec succès.\n");

            // 3. Création des Mariages
            System.out.println(">>> 3. Enregistrement des mariages...");
            // Pour h1 (SAFI Said) : reproduit fidèlement l'exemple d'affichage du sujet
            // En cours :
            // 1. Salima Rami   Début: 03/09/1990   Enfants: 4
            // 2. Amal Ali      Début: 03/09/1995   Enfants: 2
            // 3. Wafa Alaoui   Début: 04/11/2000   Enfants: 3
            // Échoué :
            // 1. Karima Alami  Début: 03/09/1989   Fin: 03/09/1990   Enfants: 0
            Mariage m1 = new Mariage(h1, f1, sdf.parse("03/09/1990"), null, 4);
            Mariage m2 = new Mariage(h1, f2, sdf.parse("03/09/1995"), null, 2);
            Mariage m3 = new Mariage(h1, f3, sdf.parse("04/11/2000"), null, 3);
            Mariage m4 = new Mariage(h1, f4, sdf.parse("03/09/1989"), sdf.parse("03/09/1990"), 0);

            // Pour h2 (Karim) : marié à 4 femmes entre 1990 et 2010 pour tester le Criteria count
            Mariage m5 = new Mariage(h2, f5, sdf.parse("15/06/1992"), null, 2);
            Mariage m6 = new Mariage(h2, f6, sdf.parse("10/01/1996"), null, 1);
            Mariage m7 = new Mariage(h2, f7, sdf.parse("20/05/2002"), null, 3);
            Mariage m8 = new Mariage(h2, f8, sdf.parse("12/09/2008"), null, 1);

            // Pour créer une femme mariée 2 fois ou plus : f4 (Karima) a été mariée avec h1, puis mariée avec h3
            Mariage m9 = new Mariage(h3, f4, sdf.parse("01/01/1995"), null, 2);

            // Pour f1 (Salima) : mariée aussi à h4 dans le passé puis divorce
            Mariage m10 = new Mariage(h4, f1, sdf.parse("01/01/1985"), sdf.parse("01/01/1988"), 1);

            mariageService.create(m1);
            mariageService.create(m2);
            mariageService.create(m3);
            mariageService.create(m4);
            mariageService.create(m5);
            mariageService.create(m6);
            mariageService.create(m7);
            mariageService.create(m8);
            mariageService.create(m9);
            mariageService.create(m10);
            System.out.println("Mariages créés avec succès.\n");

            System.out.println("==================================================================");
            System.out.println("                 TESTS DES FONCTIONNALITES DEMANDEES              ");
            System.out.println("==================================================================\n");

            // 1. Afficher la liste des femmes
            System.out.println("--- 1. Liste des femmes ---");
            List<Femme> femmes = femmeService.getAll();
            System.out.printf("%-5s %-15s %-15s %-15s %-15s%n", "ID", "Nom", "Prénom", "Téléphone", "Date Naissance");
            System.out.println("-------------------------------------------------------------------------");
            for (Femme f : femmes) {
                String dN = (f.getDateNaissance() != null) ? sdf.format(f.getDateNaissance()) : "-";
                System.out.printf("%-5d %-15s %-15s %-15s %-15s%n", f.getId(), f.getNom(), f.getPrenom(), f.getTelephone(), dN);
            }
            System.out.println();

            // 2. Afficher la femme la plus âgée
            System.out.println("--- 2. Femme la plus âgée ---");
            Femme plusAgee = femmeService.getFemmeLaPlusAgee();
            if (plusAgee != null) {
                System.out.println("La femme la plus âgée est : " + plusAgee.getNom() + " " + plusAgee.getPrenom() +
                        " née le " + sdf.format(plusAgee.getDateNaissance()));
            }
            System.out.println();

            // 3. Afficher les épouses d'un homme donné entre deux dates
            System.out.println("--- 3. Épouses d'un homme entre deux dates ---");
            Date dDebut = sdf.parse("01/01/1988");
            Date dFin = sdf.parse("31/12/1998");
            hommeService.afficherEpousesEntreDates(h1, dDebut, dFin);

            // 4. Afficher le nombre d'enfants d'une femme entre deux dates (requête native nommée)
            System.out.println("--- 4. Nombre d'enfants d'une femme entre deux dates (Requête native nommée) ---");
            int nbrEnfants = femmeService.getNombreEnfantsEntreDates(f1, sdf.parse("01/01/1980"), sdf.parse("31/12/2005"));
            System.out.println("Nombre d'enfants pour " + f1.getNom() + " " + f1.getPrenom() + " : " + nbrEnfants);
            System.out.println();

            // 5. Afficher les femmes mariées deux fois ou plus (requête nommée)
            System.out.println("--- 5. Femmes mariées au moins deux fois (Requête nommée) ---");
            List<Femme> multMaries = femmeService.getFemmesMarieesAuMoinsDeuxFois();
            for (Femme f : multMaries) {
                System.out.println("- " + f.getNom() + " " + f.getPrenom() + " (ID: " + f.getId() + ")");
            }
            System.out.println();

            // 6. Afficher les hommes mariés à 4 femmes entre deux dates (API Criteria)
            System.out.println("--- 6. Nombre d'hommes mariés à 4 femmes entre deux dates (API Criteria) ---");
            long nbrHommes4Femmes = femmeService.countHommesMariesQuatreFemmesEntreDates(sdf.parse("01/01/1985"), sdf.parse("31/12/2015"));
            System.out.println("Nombre d'hommes mariés à 4 femmes entre 1985 et 2015 : " + nbrHommes4Femmes);
            System.out.println();

            // 7. Afficher les mariages d'un homme avec tous les détails (format de l'exemple attendu)
            System.out.println("--- 7. Détails des mariages d'un homme (Exemple du Sujet) ---");
            hommeService.afficherMariagesDetails(h1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            System.out.println("Fermeture de la SessionFactory.");
        }
    }
}
