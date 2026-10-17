package ma.projet.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "femmes")
@NamedQueries({
    @NamedQuery(
        name = "Femme.findFemmesMarieesAuMoinsDeuxFois",
        query = "SELECT f FROM Femme f WHERE (SELECT COUNT(m) FROM Mariage m WHERE m.femme = f) >= 2"
    )
})
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "Femme.countEnfantsEntreDates",
        query = "SELECT IFNULL(SUM(m.nbr_enfant), 0) FROM mariages m " +
                "WHERE m.femme_id = :femmeId " +
                "AND m.date_debut BETWEEN :dateDebut AND :dateFin"
    )
})
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mariage> mariages = new ArrayList<>();

    public Femme() {
        super();
    }

    public Femme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(List<Mariage> mariages) {
        this.mariages = mariages;
    }
}
