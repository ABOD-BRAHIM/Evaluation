package ma.projet.service;

import ma.projet.classes.Employe;
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

public class EmployeService implements IDao<Employe> {

    @Override
    public boolean create(Employe o) {
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
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
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
    public Employe findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Employe.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Employe> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Employe", Employe.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Afficher la liste des tâches réalisées par un employé.
     * Une tâche est considérée réalisée si dateFinReelle n'est pas null
     * (ou toutes les tâches prises en charge par l'employé).
     */
    public void afficherTachesRealiseesParEmploye(Employe employe) {
        if (employe == null) {
            System.out.println("Employe inexistant.");
            return;
        }
        Session session = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT et FROM EmployeTache et " +
                         "JOIN FETCH et.tache t " +
                         "LEFT JOIN FETCH t.projet " +
                         "WHERE et.employe = :employe AND et.dateFinReelle IS NOT NULL";
            Query<EmployeTache> query = session.createQuery(hql, EmployeTache.class);
            query.setParameter("employe", employe);
            List<EmployeTache> list = query.list();

            System.out.println("==================================================");
            System.out.println("Tâches réalisées par l'employé : " + employe.getNom() + " " + employe.getPrenom());
            System.out.println("==================================================");
            if (list.isEmpty()) {
                System.out.println("Aucune tâche réalisée trouvée pour cet employé.");
            } else {
                System.out.printf("%-5s %-20s %-20s %-15s %-15s%n", "Num", "Nom Tâche", "Projet", "Début Réelle", "Fin Réelle");
                System.out.println("---------------------------------------------------------------------------------");
                for (EmployeTache et : list) {
                    Tache t = et.getTache();
                    String nomProjet = (t.getProjet() != null) ? t.getProjet().getNom() : "-";
                    String debut = (et.getDateDebutReelle() != null) ? sdf.format(et.getDateDebutReelle()) : "-";
                    String fin = (et.getDateFinReelle() != null) ? sdf.format(et.getDateFinReelle()) : "-";
                    System.out.printf("%-5d %-20s %-20s %-15s %-15s%n", t.getId(), t.getNom(), nomProjet, debut, fin);
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
     * 2. Afficher la liste des projets gérés par un employé (en tant que chef de projet).
     */
    public void afficherProjetsGeresParEmploye(Employe employe) {
        if (employe == null) {
            System.out.println("Employe inexistant.");
            return;
        }
        Session session = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT p FROM Projet p WHERE p.chefDeProjet = :employe";
            Query<Projet> query = session.createQuery(hql, Projet.class);
            query.setParameter("employe", employe);
            List<Projet> projets = query.list();

            System.out.println("==================================================");
            System.out.println("Projets gérés par le chef de projet : " + employe.getNom() + " " + employe.getPrenom());
            System.out.println("==================================================");
            if (projets.isEmpty()) {
                System.out.println("Cet employé ne gère aucun projet actuellement.");
            } else {
                System.out.printf("%-5s %-25s %-15s %-15s%n", "ID", "Nom Projet", "Date Début", "Date Fin");
                System.out.println("-----------------------------------------------------------------");
                for (Projet p : projets) {
                    String debut = (p.getDateDebut() != null) ? sdf.format(p.getDateDebut()) : "-";
                    String fin = (p.getDateFin() != null) ? sdf.format(p.getDateFin()) : "-";
                    System.out.printf("%-5d %-25s %-15s %-15s%n", p.getId(), p.getNom(), debut, fin);
                }
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
