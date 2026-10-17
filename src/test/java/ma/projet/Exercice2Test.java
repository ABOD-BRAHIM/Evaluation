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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class Exercice2Test {

    private static EmployeService employeService;
    private static ProjetService projetService;
    private static TacheService tacheService;
    private static EmployeTacheService employeTacheService;

    @BeforeClass
    public static void setUp() {
        employeService = new EmployeService();
        projetService = new ProjetService();
        tacheService = new TacheService();
        employeTacheService = new EmployeTacheService();
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    public void testPersistanceGestionProjets() {
        Employe emp = new Employe("TEST_NOM", "TEST_PRENOM", "0600000000");
        assertTrue(employeService.create(emp));
        assertNotNull(employeService.findById(emp.getId()));

        Projet proj = new Projet("TEST_PROJET", new Date(), new Date(), emp);
        assertTrue(projetService.create(proj));
        assertNotNull(projetService.findById(proj.getId()));

        Tache tache = new Tache("TEST_TACHE", new Date(), new Date(), 1500.0, proj);
        assertTrue(tacheService.create(tache));
        assertNotNull(tacheService.findById(tache.getId()));

        EmployeTache et = new EmployeTache(emp, tache, new Date(), new Date());
        assertTrue(employeTacheService.create(et));
    }

    @Test
    public void testNamedQueryTachesPrixSuperieurA1000() {
        List<Tache> taches = tacheService.getTachesPrixSuperieurA1000();
        assertNotNull(taches);
        for (Tache t : taches) {
            assertTrue("Le prix doit être strictement supérieur à 1000 DH", t.getPrix() > 1000.0);
        }
    }
}
