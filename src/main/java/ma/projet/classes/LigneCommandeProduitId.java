package ma.projet.classes;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LigneCommandeProduitId implements Serializable {

    private int commandeId;
    private int produitId;

    public LigneCommandeProduitId() {
    }

    public LigneCommandeProduitId(int commandeId, int produitId) {
        this.commandeId = commandeId;
        this.produitId = produitId;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigneCommandeProduitId that = (LigneCommandeProduitId) o;
        return commandeId == that.commandeId && produitId == that.produitId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandeId, produitId);
    }
}
