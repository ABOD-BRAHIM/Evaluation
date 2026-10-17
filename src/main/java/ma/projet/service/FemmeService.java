package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FemmeService implements IDao<Femme> {

    @Override
    public boolean create(Femme o) {
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
    public boolean update(Femme o) {
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
    public boolean delete(Femme o) {
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
    public Femme findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Femme.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Femme> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Femme", Femme.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Exécuter une requête native nommée retournant le nombre d’enfants d’une femme entre deux dates.
     */
    public int getNombreEnfantsEntreDates(Femme femme, Date dateDebut, Date dateFin) {
        if (femme == null) return 0;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            NativeQuery<?> query = session.getNamedNativeQuery("Femme.countEnfantsEntreDates");
            query.setParameter("femmeId", femme.getId());
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            Object result = query.uniqueResult();
            if (result instanceof Number) {
                return ((Number) result).intValue();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 2. Exécuter une requête nommée retournant les femmes mariées au moins deux fois.
     */
    public List<Femme> getFemmesMarieesAuMoinsDeuxFois() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Femme> query = session.getNamedQuery("Femme.findFemmesMarieesAuMoinsDeuxFois");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 3. Méthode utilisant l’API Criteria pour afficher le nombre d’hommes mariés à quatre femmes entre deux dates.
     */
    public long countHommesMariesQuatreFemmesEntreDates(Date dateDebut, Date dateFin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Mariage> root = cq.from(Mariage.class);

            // Filtre sur la date de début du mariage entre dateDebut et dateFin
            Predicate datePredicate = cb.between(root.get("id").get("dateDebut"), dateDebut, dateFin);

            // Group by homme, ayant count(distinct femme) = 4
            cq.select(root.get("homme").get("id"))
              .where(datePredicate)
              .groupBy(root.get("homme").get("id"))
              .having(cb.equal(cb.countDistinct(root.get("femme")), 4L));

            List<Long> hommesIds = session.createQuery(cq).getResultList();
            return (hommesIds != null) ? hommesIds.size() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Trouver et afficher la femme la plus âgée (date de naissance la plus ancienne).
     */
    public Femme getFemmeLaPlusAgee() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Femme f WHERE f.dateNaissance IS NOT NULL ORDER BY f.dateNaissance ASC";
            Query<Femme> query = session.createQuery(hql, Femme.class);
            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
}
