package model;

public class Contact {
    private int id;
    private String nom;
    private String prenom;
    private Categorie categorie;
    private String telephone;
    private String email;
    private String photo; // chemin vers l’image

    public Contact(int id, String nom, String prenom, Categorie categorie, String telephone, String email, String photo) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.categorie = categorie;
        this.telephone = telephone;
        this.email = email;
        this.photo = photo;
    }

    // Surcharge sans ID pour les nouveaux contacts
    public Contact(String nom, String prenom, Categorie categorie, String telephone, String email, String photo) {
        this.nom = nom;
        this.prenom = prenom;
        this.categorie = categorie;
        this.telephone = telephone;
        this.email = email;
        this.photo = photo;
    }

    // Getters & setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public Categorie getCategorie() { return categorie; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getPhoto() { return photo; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoto(String photo) { this.photo = photo; }
}
