package ma.projet.beans;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class MariageId implements Serializable {

    private int hommeId;
    private int femmeId;
    private Date dateDebut;

    public MariageId() {
    }

    public MariageId(int hommeId, int femmeId, Date dateDebut) {
        this.hommeId = hommeId;
        this.femmeId = femmeId;
        this.dateDebut = dateDebut;
    }

    public int getHommeId() {
        return hommeId;
    }

    public void setHommeId(int hommeId) {
        this.hommeId = hommeId;
    }

    public int getFemmeId() {
        return femmeId;
    }

    public void setFemmeId(int femmeId) {
        this.femmeId = femmeId;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MariageId mariageId = (MariageId) o;
        return hommeId == mariageId.hommeId &&
                femmeId == mariageId.femmeId &&
                Objects.equals(dateDebut, mariageId.dateDebut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hommeId, femmeId, dateDebut);
    }
}
