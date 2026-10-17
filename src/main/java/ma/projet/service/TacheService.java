package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TacheService implements IDao<Tache> {

    @Override
    public boolean create(Tache o) {
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
    public boolean update(Tache o) {
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
    public boolean delete(Tache o) {
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
    public Tache findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Tache.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Tache> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Tache", Tache.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Afficher les tâches dont le prix est supérieur à 1000 DH (requête nommée).
     */
    public void afficherTachesPrixSuperieurA1000() {
        Session session = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Tache> query = session.getNamedQuery("Tache.findPrixSuperieurA1000");
            List<Tache> taches = query.list();

            System.out.println("==================================================");
            System.out.println("Tâches avec prix > 1000 DH (Requête Nommée) :");
            System.out.println("==================================================");
            if (taches.isEmpty()) {
                System.out.println("Aucune tâche trouvée avec un prix > 1000 DH.");
            } else {
                System.out.printf("%-5s %-20s %-12s %-12s %-12s %-20s%n", "Num", "Nom", "Début", "Fin", "Prix", "Projet");
                System.out.println("-----------------------------------------------------------------------------------------");
                for (Tache t : taches) {
                    String debut = (t.getDateDebut() != null) ? sdf.format(t.getDateDebut()) : "-";
                    String fin = (t.getDateFin() != null) ? sdf.format(t.getDateFin()) : "-";
                    String nomProjet = (t.getProjet() != null) ? t.getProjet().getNom() : "-";
                    System.out.printf("%-5d %-20s %-12s %-12s %-12.2f DH %-20s%n", t.getId(), t.getNom(), debut, fin, t.getPrix(), nomProjet);
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
     * 2. Afficher les tâches réalisées entre deux dates.
     * Basé sur les dates réelles de réalisation (dateDebutReelle et dateFinReelle dans EmployeTache).
     */
    public void afficherTachesRealiseesEntreDates(Date dateDebut, Date dateFin) {
        Session session = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT et FROM EmployeTache et " +
                         "JOIN FETCH et.tache t " +
                         "LEFT JOIN FETCH t.projet " +
                         "WHERE et.dateDebutReelle >= :dateDebut AND et.dateFinReelle <= :dateFin";
            Query<EmployeTache> query = session.createQuery(hql, EmployeTache.class);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            List<EmployeTache> list = query.list();

            System.out.println("==================================================");
            System.out.println("Tâches réalisées entre le " + sdf.format(dateDebut) + " et le " + sdf.format(dateFin) + " :");
            System.out.println("==================================================");
            if (list.isEmpty()) {
                System.out.println("Aucune tâche réalisée trouvée dans cet intervalle.");
            } else {
                System.out.printf("%-5s %-20s %-20s %-15s %-15s %-12s%n", "Num", "Nom Tâche", "Projet", "Début Réelle", "Fin Réelle", "Prix");
                System.out.println("-------------------------------------------------------------------------------------------------");
                for (EmployeTache et : list) {
                    Tache t = et.getTache();
                    String nomProjet = (t.getProjet() != null) ? t.getProjet().getNom() : "-";
                    String dReelle = (et.getDateDebutReelle() != null) ? sdf.format(et.getDateDebutReelle()) : "-";
                    String fReelle = (et.getDateFinReelle() != null) ? sdf.format(et.getDateFinReelle()) : "-";
                    System.out.printf("%-5d %-20s %-20s %-15s %-15s %-12.2f DH%n", t.getId(), t.getNom(), nomProjet, dReelle, fReelle, t.getPrix());
                }
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Tache> getTachesPrixSuperieurA1000() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.getNamedQuery("Tache.findPrixSuperieurA1000").list();
        } finally {
            if (session != null) session.close();
        }
    }
}
