package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjetService implements IDao<Projet> {

    @Override
    public boolean create(Projet o) {
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
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
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
    public Projet findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Projet.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Projet> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Projet", Projet.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Afficher la liste des tâches planifiées pour un projet.
     */
    public void afficherTachesPlanifiees(Projet projet) {
        if (projet == null) {
            System.out.println("Projet inexistant.");
            return;
        }
        Session session = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT t FROM Tache t WHERE t.projet = :projet";
            Query<Tache> query = session.createQuery(hql, Tache.class);
            query.setParameter("projet", projet);
            List<Tache> taches = query.list();

            System.out.println("==================================================");
            System.out.println("Tâches planifiées pour le projet : " + projet.getNom());
            System.out.println("==================================================");
            if (taches.isEmpty()) {
                System.out.println("Aucune tâche planifiée pour ce projet.");
            } else {
                System.out.printf("%-5s %-20s %-15s %-15s %-10s%n", "Num", "Nom", "Date Début", "Date Fin", "Prix");
                System.out.println("----------------------------------------------------------------------");
                for (Tache t : taches) {
                    String debut = (t.getDateDebut() != null) ? sdf.format(t.getDateDebut()) : "-";
                    String fin = (t.getDateFin() != null) ? sdf.format(t.getDateFin()) : "-";
                    System.out.printf("%-5d %-20s %-15s %-15s %-10.2f DH%n", t.getId(), t.getNom(), debut, fin, t.getPrix());
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
     * 2. Afficher la liste des tâches réalisées avec les dates réelles.
     * Exemple d'affichage attendu :
     * Projet : 4      Nom : Gestion de stock     Date début : 14 Janvier 2013
     * Liste des tâches:
     * Num Nom            Date Début Réelle   Date Fin Réelle
     * 12  Analyse        10/02/2013          20/02/2013
     * 13  Conception     10/03/2013          15/03/2013
     * 14  Développement  10/04/2013          25/04/2013
     */
    public void afficherTachesRealisees(Projet projet) {
        if (projet == null) {
            System.out.println("Projet inexistant.");
            return;
        }
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT et FROM EmployeTache et " +
                         "JOIN FETCH et.tache t " +
                         "WHERE t.projet = :projet AND et.dateDebutReelle IS NOT NULL";
            Query<EmployeTache> query = session.createQuery(hql, EmployeTache.class);
            query.setParameter("projet", projet);
            List<EmployeTache> list = query.list();

            SimpleDateFormat sdfProjet = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);
            SimpleDateFormat sdfTache = new SimpleDateFormat("dd/MM/yyyy");

            String debutProjet = (projet.getDateDebut() != null) ? sdfProjet.format(projet.getDateDebut()) : "Non définie";

            System.out.println("Projet : " + projet.getId() + "      Nom : " + projet.getNom() + "     Date début : " + debutProjet);
            System.out.println("Liste des tâches:");
            System.out.printf("%-5s %-15s %-20s %-15s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
            for (EmployeTache et : list) {
                Tache t = et.getTache();
                String debut = (et.getDateDebutReelle() != null) ? sdfTache.format(et.getDateDebutReelle()) : "-";
                String fin = (et.getDateFinReelle() != null) ? sdfTache.format(et.getDateFinReelle()) : "-";
                System.out.printf("%-5d %-15s %-20s %-15s%n", t.getId(), t.getNom(), debut, fin);
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
