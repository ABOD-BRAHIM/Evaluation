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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class AppTest {

    private static CategorieService categorieService;
    private static ProduitService produitService;
    private static CommandeService commandeService;
    private static LigneCommandeService ligneCommandeService;

    @BeforeClass
    public static void setUp() {
        categorieService = new CategorieService();
        produitService = new ProduitService();
        commandeService = new CommandeService();
        ligneCommandeService = new LigneCommandeService();
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    public void testCreateAndFindEntities() throws Exception {
        Categorie cat = new Categorie("TEST_CAT", "Test Categorie");
        assertTrue(categorieService.create(cat));
        assertNotNull(categorieService.findById(cat.getId()));

        Produit prod = new Produit("TEST_P1", 150.0f, cat);
        assertTrue(produitService.create(prod));
        assertNotNull(produitService.findById(prod.getId()));

        Commande cmd = new Commande(new Date());
        assertTrue(commandeService.create(cmd));
        assertNotNull(commandeService.findById(cmd.getId()));

        LigneCommandeProduit lcp = new LigneCommandeProduit(cmd, prod, 10);
        assertTrue(ligneCommandeService.create(lcp));
    }

    @Test
    public void testNamedQueryPrixSuperieurA100() {
        List<Produit> produits = produitService.getProduitsPrixSuperieurA100();
        assertNotNull(produits);
        for (Produit p : produits) {
            assertTrue("Le prix doit etre strictement superieur a 100", p.getPrix() > 100.0f);
        }
    }

    @Test
    public void testProduitsCommandesEntreDates() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date debut = sdf.parse("2010-01-01");
        Date fin = sdf.parse("2030-12-31");

        List<Produit> prods = produitService.getProduitsCommandesEntreDates(debut, fin);
        assertNotNull(prods);
    }
}
