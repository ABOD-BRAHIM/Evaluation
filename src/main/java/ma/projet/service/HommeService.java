package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme o) {
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
    public boolean update(Homme o) {
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
    public boolean delete(Homme o) {
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
    public Homme findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Homme.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Homme> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Homme", Homme.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Afficher les épouses d’un homme entre deux dates.
     */
    public void afficherEpousesEntreDates(Homme homme, Date dateDebut, Date dateFin) {
        if (homme == null) {
            System.out.println("Homme inexistant.");
            return;
        }
        Session session = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT m FROM Mariage m " +
                         "JOIN FETCH m.femme f " +
                         "WHERE m.homme = :homme AND m.id.dateDebut BETWEEN :dateDebut AND :dateFin";
            Query<Mariage> query = session.createQuery(hql, Mariage.class);
            query.setParameter("homme", homme);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            List<Mariage> mariages = query.list();

            System.out.println("==================================================");
            System.out.println("Épouses de " + homme.getNom() + " " + homme.getPrenom() +
                    " entre le " + sdf.format(dateDebut) + " et le " + sdf.format(dateFin) + " :");
            System.out.println("==================================================");
            if (mariages.isEmpty()) {
                System.out.println("Aucune épouse trouvée pour cet intervalle.");
            } else {
                System.out.printf("%-5s %-25s %-15s %-15s %-10s%n", "ID", "Nom de l'épouse", "Date Début", "Date Fin", "Enfants");
                System.out.println("-------------------------------------------------------------------------");
                for (Mariage m : mariages) {
                    String dFin = (m.getDateFin() != null) ? sdf.format(m.getDateFin()) : "En cours";
                    System.out.printf("%-5d %-25s %-15s %-15s %-10d%n",
                            m.getFemme().getId(),
                            m.getFemme().getNom() + " " + m.getFemme().getPrenom(),
                            sdf.format(m.getDateDebut()),
                            dFin,
                            m.getNbrEnfant());
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
     * Afficher les mariages d’un homme donné, avec les détails (femme, dates, nombre d’enfants).
     * Exemple d’affichage attendu :
     * Nom : SAFI SAID
     * Mariages En Cours :
     * 1. Femme : SALIMA RAMI   Date Début : 03/09/1990    Nbr Enfants : 4
     * 2. Femme : AMAL ALI      Date Début : 03/09/1995    Nbr Enfants : 2
     * 3. Femme : WAFA ALAOUI   Date Début : 04/11/2000    Nbr Enfants : 3
     * 
     * Mariages échoués :
     * 1. Femme : KARIMA ALAMI  Date Début : 03/09/1989    Date Fin : 03/09/1990    Nbr Enfants : 0
     */
    public void afficherMariagesDetails(Homme homme) {
        if (homme == null) {
            System.out.println("Homme inexistant.");
            return;
        }
        Session session = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT m FROM Mariage m " +
                         "JOIN FETCH m.femme f " +
                         "WHERE m.homme = :homme " +
                         "ORDER BY m.id.dateDebut ASC";
            Query<Mariage> query = session.createQuery(hql, Mariage.class);
            query.setParameter("homme", homme);
            List<Mariage> mariages = query.list();

            List<Mariage> enCours = new ArrayList<>();
            List<Mariage> echoues = new ArrayList<>();

            for (Mariage m : mariages) {
                if (m.getDateFin() == null) {
                    enCours.add(m);
                } else {
                    echoues.add(m);
                }
            }

            System.out.println("Nom : " + homme.getNom() + " " + homme.getPrenom());
            System.out.println("Mariages En Cours :");
            if (enCours.isEmpty()) {
                System.out.println("Aucun mariage en cours.");
            } else {
                int index = 1;
                for (Mariage m : enCours) {
                    System.out.printf("%d. Femme : %-15s Date Début : %-12s Nbr Enfants : %d%n",
                            index++,
                            m.getFemme().getNom() + " " + m.getFemme().getPrenom(),
                            sdf.format(m.getDateDebut()),
                            m.getNbrEnfant());
                }
            }
            System.out.println();

            System.out.println("Mariages échoués :");
            if (echoues.isEmpty()) {
                System.out.println("Aucun mariage échoué.");
            } else {
                int index = 1;
                for (Mariage m : echoues) {
                    System.out.printf("%d. Femme : %-15s Date Début : %-12s Date Fin : %-12s Nbr Enfants : %d%n",
                            index++,
                            m.getFemme().getNom() + " " + m.getFemme().getPrenom(),
                            sdf.format(m.getDateDebut()),
                            sdf.format(m.getDateFin()),
                            m.getNbrEnfant());
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
