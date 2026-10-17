package ma.projet.classes;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employe_tache")
public class EmployeTache {

    @EmbeddedId
    private EmployeTacheId id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut_reelle")
    private Date dateDebutReelle;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin_reelle")
    private Date dateFinReelle;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("employeId")
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("tacheId")
    @JoinColumn(name = "tache_id")
    private Tache tache;

    public EmployeTache() {
    }

    public EmployeTache(Employe employe, Tache tache, Date dateDebutReelle, Date dateFinReelle) {
        this.employe = employe;
        this.tache = tache;
        this.dateDebutReelle = dateDebutReelle;
        this.dateFinReelle = dateFinReelle;
        this.id = new EmployeTacheId(employe.getId(), tache.getId());
    }

    public EmployeTache(EmployeTacheId id, Date dateDebutReelle, Date dateFinReelle, Employe employe, Tache tache) {
        this.id = id;
        this.dateDebutReelle = dateDebutReelle;
        this.dateFinReelle = dateFinReelle;
        this.employe = employe;
        this.tache = tache;
    }

    public EmployeTacheId getId() {
        return id;
    }

    public void setId(EmployeTacheId id) {
        this.id = id;
    }

    public Date getDateDebutReelle() {
        return dateDebutReelle;
    }

    public void setDateDebutReelle(Date dateDebutReelle) {
        this.dateDebutReelle = dateDebutReelle;
    }

    public Date getDateFinReelle() {
        return dateFinReelle;
    }

    public void setDateFinReelle(Date dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
        if (employe != null) {
            if (this.id == null) this.id = new EmployeTacheId();
            this.id.setEmployeId(employe.getId());
        }
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
        if (tache != null) {
            if (this.id == null) this.id = new EmployeTacheId();
            this.id.setTacheId(tache.getId());
        }
    }

    @Override
    public String toString() {
        return "EmployeTache{" +
                "employe=" + (employe != null ? employe.getNom() : "null") +
                ", tache=" + (tache != null ? tache.getNom() : "null") +
                ", dateDebutReelle=" + dateDebutReelle +
                ", dateFinReelle=" + dateFinReelle +
                '}';
    }
}
