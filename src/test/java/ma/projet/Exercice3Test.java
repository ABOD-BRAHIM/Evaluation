package ma.projet;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class Exercice3Test {

    private static HommeService hommeService;
    private static FemmeService femmeService;
    private static MariageService mariageService;

    @BeforeClass
    public static void setUp() {
        hommeService = new HommeService();
        femmeService = new FemmeService();
        mariageService = new MariageService();
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    public void testPersistanceEtatCivil() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Homme h = new Homme("TEST_H", "Karim", "0600000001", "Rabat", sdf.parse("1980-01-01"));
        assertTrue(hommeService.create(h));
        assertNotNull(hommeService.findById(h.getId()));

        Femme f = new Femme("TEST_F", "Sara", "0600000002", "Casablanca", sdf.parse("1985-05-15"));
        assertTrue(femmeService.create(f));
        assertNotNull(femmeService.findById(f.getId()));

        Mariage m = new Mariage(h, f, sdf.parse("2010-06-20"), null, 2);
        assertTrue(mariageService.create(m));
    }

    @Test
    public void testFemmeQueries() {
        Femme plusAgee = femmeService.getFemmeLaPlusAgee();
        // S'il y a des femmes en base, la requête ne doit pas lever d'erreur
        assertNotNull(femmeService.getAll());
    }
}
