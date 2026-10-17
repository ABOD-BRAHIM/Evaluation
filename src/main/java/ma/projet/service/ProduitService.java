package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProduitService implements IDao<Produit> {

    @Override
    public boolean create(Produit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean update(Produit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(Produit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Produit findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Produit.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Produit> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Produit", Produit.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Afficher la liste des produits par categorie
     */
    public void afficherProduitsParCategorie(Categorie categorie) {
        if (categorie == null) {
            System.out.println("Categorie introuvable.");
            return;
        }
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Produit> query = session.getNamedQuery("Produit.findByCategorie");
            query.setParameter("categorie", categorie);
            List<Produit> produits = query.list();

            System.out.println("==================================================");
            System.out.println("Produits de la categorie : " + categorie.getLibelle() + " (Code: " + categorie.getCode() + ")");
            System.out.println("==================================================");
            if (produits.isEmpty()) {
                System.out.println("Aucun produit trouve pour cette categorie.");
            } else {
                System.out.printf("%-10s %-15s %-10s%n", "ID", "Reference", "Prix");
                System.out.println("--------------------------------------------------");
                for (Produit p : produits) {
                    System.out.printf("%-10d %-15s %-10.2f DH%n", p.getId(), p.getReference(), p.getPrix());
                }
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 2. Afficher les produits commandes entre deux dates
     */
    public void afficherProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        Session session = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT DISTINCT lcp.produit FROM LigneCommandeProduit lcp " +
                         "WHERE lcp.commande.date BETWEEN :dateDebut AND :dateFin";
            Query<Produit> query = session.createQuery(hql, Produit.class);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            List<Produit> produits = query.list();

            System.out.println("==================================================");
            System.out.println("Produits commandes entre le " + sdf.format(dateDebut) + " et le " + sdf.format(dateFin) + " :");
            System.out.println("==================================================");
            if (produits.isEmpty()) {
                System.out.println("Aucun produit commande durant cette periode.");
            } else {
                System.out.printf("%-10s %-15s %-10s %-15s%n", "ID", "Reference", "Prix", "Categorie");
                System.out.println("--------------------------------------------------");
                for (Produit p : produits) {
                    String catName = (p.getCategorie() != null) ? p.getCategorie().getLibelle() : "-";
                    System.out.printf("%-10d %-15s %-10.2f DH %-15s%n", p.getId(), p.getReference(), p.getPrix(), catName);
                }
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 3. Afficher les produits commandes dans une commande donnee
     * Format demande :
     * Commande : 4     Date : 14 Mars 2013
     * Liste des produits :
     * Reference   Prix    Quantite
     * ES12        120 DH  7
     * ZR85        100 DH  14
     * EE85        200 DH  5
     */
    public void afficherProduitsParCommande(Commande commande) {
        if (commande == null) {
            System.out.println("Commande inexistante.");
            return;
        }
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT lcp FROM LigneCommandeProduit lcp " +
                         "JOIN FETCH lcp.produit " +
                         "WHERE lcp.commande = :commande";
            Query<LigneCommandeProduit> query = session.createQuery(hql, LigneCommandeProduit.class);
            query.setParameter("commande", commande);
            List<LigneCommandeProduit> lignes = query.list();

            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);
            String dateStr = (commande.getDate() != null) ? sdf.format(commande.getDate()) : "Non definie";

            System.out.println("Commande : " + commande.getId() + "     Date : " + dateStr);
            System.out.println("Liste des produits :");
            System.out.printf("%-12s %-10s %-10s%n", "Reference", "Prix", "Quantite");
            for (LigneCommandeProduit lcp : lignes) {
                Produit p = lcp.getProduit();
                int prixInt = Math.round(p.getPrix());
                System.out.printf("%-12s %-10s %-10d%n", p.getReference(), prixInt + " DH", lcp.getQuantite());
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 4. Afficher la liste des produits dont le prix est superieur a 100 DH,
     * en utilisant une requete nommee (NamedQuery).
     */
    public void afficherProduitsPrixSuperieurA100() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Produit> query = session.getNamedQuery("Produit.findPrixSuperieurA100");
            List<Produit> produits = query.list();

            System.out.println("==================================================");
            System.out.println("Liste des produits avec un prix superieur a 100 DH (Requete Nommee) :");
            System.out.println("==================================================");
            if (produits.isEmpty()) {
                System.out.println("Aucun produit trouve avec un prix > 100 DH.");
            } else {
                System.out.printf("%-10s %-15s %-10s %-15s%n", "ID", "Reference", "Prix", "Categorie");
                System.out.println("--------------------------------------------------");
                for (Produit p : produits) {
                    String catName = (p.getCategorie() != null) ? p.getCategorie().getLibelle() : "-";
                    System.out.printf("%-10d %-15s %-10.2f DH %-15s%n", p.getId(), p.getReference(), p.getPrix(), catName);
                }
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Retourne la liste des produits pour une categorie donnee
     */
    public List<Produit> getProduitsParCategorie(Categorie categorie) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Produit> query = session.getNamedQuery("Produit.findByCategorie");
            query.setParameter("categorie", categorie);
            return query.list();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Retourne la liste des produits commandes entre deux dates
     */
    public List<Produit> getProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT DISTINCT lcp.produit FROM LigneCommandeProduit lcp " +
                         "WHERE lcp.commande.date BETWEEN :dateDebut AND :dateFin";
            Query<Produit> query = session.createQuery(hql, Produit.class);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            return query.list();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Retourne la liste des produits dont le prix > 100 DH via requete nommee
     */
    public List<Produit> getProduitsPrixSuperieurA100() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.getNamedQuery("Produit.findPrixSuperieurA100").list();
        } finally {
            if (session != null) session.close();
        }
    }
}
