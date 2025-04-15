package model;
/**
 * Classe représentant une catégorie à laquelle un contact peut appartenir
 * (par exemple : Famille, Amis, Travail, etc.).
 * Elle est utilisée pour classer les contacts dans l'application.
 */
public class Categorie {
    private int id;
    private String nom;

    //  Constructeur principal
    public Categorie(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    //  Getters et Setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }

    //  Affichage dans le JComboBox (affiche le nom au lieu de l'objet complet)
    @Override
    public String toString() {
        return nom;
    }

    //  Ajout de equals() pour comparer les objets Categorie correctement
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // si c'est exactement le même objet
        if (obj == null || getClass() != obj.getClass()) return false; // null ou classe différente
        Categorie other = (Categorie) obj;
        return this.id == other.id; // on considère égales deux catégories avec le même id
    }

    //  hashCode est toujours nécessaire avec equals (utilisé dans les listes, maps...)
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
