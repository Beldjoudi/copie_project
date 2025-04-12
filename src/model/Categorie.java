package model;

// Classe qui represente une categorie dans le systeme
public class Categorie {

    // Identifiant unique de la categorie (correspond a l'ID dans la base)
    private int id;

    // Nom de la categorie (ex: Amis, Travail, Famille)
    private String nom;

    // Constructeur avec parametres
    public Categorie(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Accesseur pour id
    public int getId() {
        return id;
    }

    // Accesseur pour nom
    public String getNom() {
        return nom;
    }

    // Mutateur pour id
    public void setId(int id) {
        this.id = id;
    }

    // Mutateur pour nom
    public void setNom(String nom) {
        this.nom = nom;
    }

    // Methode utilisee automatiquement par les JComboBox
    @Override
    public String toString() {
        return nom; // Permet d'afficher uniquement le nom dans les listes deroulantes
    }
}
