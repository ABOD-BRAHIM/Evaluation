package ma.projet.util;

import ma.projet.classes.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            // Chargement de la configuration depuis application.properties
            Properties properties = new Properties();
            try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (input != null) {
                    properties.load(input);
                    configuration.setProperties(properties);
                } else {
                    System.err.println("Le fichier application.properties est introuvable dans le classpath.");
                }
            }

            // Enregistrement des entites annotees (Exercice 1 : Stock)
            configuration.addAnnotatedClass(Categorie.class);
            configuration.addAnnotatedClass(Produit.class);
            configuration.addAnnotatedClass(Commande.class);
            configuration.addAnnotatedClass(LigneCommandeProduit.class);

            // Enregistrement des entites annotees (Exercice 2 : Gestion de projets)
            configuration.addAnnotatedClass(Employe.class);
            configuration.addAnnotatedClass(Projet.class);
            configuration.addAnnotatedClass(Tache.class);
            configuration.addAnnotatedClass(EmployeTache.class);

            // Enregistrement des entites annotees (Exercice 3 : Etat Civil)
            configuration.addAnnotatedClass(ma.projet.beans.Personne.class);
            configuration.addAnnotatedClass(ma.projet.beans.Homme.class);
            configuration.addAnnotatedClass(ma.projet.beans.Femme.class);
            configuration.addAnnotatedClass(ma.projet.beans.Mariage.class);

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
