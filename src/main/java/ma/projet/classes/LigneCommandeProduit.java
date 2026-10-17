package ma.projet.classes;

import javax.persistence.*;

@Entity
@Table(name = "ligne_commande_produit")
public class LigneCommandeProduit {

    @EmbeddedId
    private LigneCommandeProduitId id;

    @Column(nullable = false)
    private int quantite;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("commandeId")
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    public LigneCommandeProduit() {
    }

    public LigneCommandeProduit(Commande commande, Produit produit, int quantite) {
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
        this.id = new LigneCommandeProduitId(commande.getId(), produit.getId());
    }

    public LigneCommandeProduit(LigneCommandeProduitId id, int quantite, Commande commande, Produit produit) {
        this.id = id;
        this.quantite = quantite;
        this.commande = commande;
        this.produit = produit;
    }

    public LigneCommandeProduitId getId() {
        return id;
    }

    public void setId(LigneCommandeProduitId id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        if (commande != null) {
            if (this.id == null) this.id = new LigneCommandeProduitId();
            this.id.setCommandeId(commande.getId());
        }
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        if (produit != null) {
            if (this.id == null) this.id = new LigneCommandeProduitId();
            this.id.setProduitId(produit.getId());
        }
    }

    @Override
    public String toString() {
        return "LigneCommandeProduit{" +
                "produit=" + (produit != null ? produit.getReference() : "null") +
                ", quantite=" + quantite +
                '}';
    }
}
