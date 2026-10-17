package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mariages")
public class Mariage {

    @EmbeddedId
    private MariageId id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin")
    private Date dateFin;

    @Column(name = "nbr_enfant", nullable = false)
    private int nbrEnfant;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("hommeId")
    @JoinColumn(name = "homme_id")
    private Homme homme;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("femmeId")
    @JoinColumn(name = "femme_id")
    private Femme femme;

    public Mariage() {
    }

    public Mariage(Homme homme, Femme femme, Date dateDebut, Date dateFin, int nbrEnfant) {
        this.homme = homme;
        this.femme = femme;
        this.dateFin = dateFin;
        this.nbrEnfant = nbrEnfant;
        this.id = new MariageId(homme != null ? homme.getId() : 0, femme != null ? femme.getId() : 0, dateDebut);
    }

    public MariageId getId() {
        return id;
    }

    public void setId(MariageId id) {
        this.id = id;
    }

    public Date getDateDebut() {
        return id != null ? id.getDateDebut() : null;
    }

    public void setDateDebut(Date dateDebut) {
        if (this.id == null) {
            this.id = new MariageId();
        }
        this.id.setDateDebut(dateDebut);
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getNbrEnfant() {
        return nbrEnfant;
    }

    public void setNbrEnfant(int nbrEnfant) {
        this.nbrEnfant = nbrEnfant;
    }

    public Homme getHomme() {
        return homme;
    }

    public void setHomme(Homme homme) {
        this.homme = homme;
        if (homme != null) {
            if (this.id == null) this.id = new MariageId();
            this.id.setHommeId(homme.getId());
        }
    }

    public Femme getFemme() {
        return femme;
    }

    public void setFemme(Femme femme) {
        this.femme = femme;
        if (femme != null) {
            if (this.id == null) this.id = new MariageId();
            this.id.setFemmeId(femme.getId());
        }
    }

    @Override
    public String toString() {
        return "Mariage{" +
                "homme=" + (homme != null ? homme.getNom() + " " + homme.getPrenom() : "null") +
                ", femme=" + (femme != null ? femme.getNom() + " " + femme.getPrenom() : "null") +
                ", dateDebut=" + getDateDebut() +
                ", dateFin=" + dateFin +
                ", nbrEnfant=" + nbrEnfant +
                '}';
    }
}
