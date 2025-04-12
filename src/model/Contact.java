package model;

// Classe qui represente un contact dans l'application
public class Contact {

    // Identifiant unique du contact (auto-incremente dans la base)
    private int id;

    // Nom de famille du contact
    private String nom;

    // Prenom du contact
    private String prenom;

    // Categorie associee (Famille, Amis, Travail...)
    private Categorie categorie;

    // Numero de telephone
    private String telephone;

    // Adresse email
    private String email;

    // Chemin du fichier image (photo du contact)
    private String photo;

    // Constructeur complet avec ID (utile pour la lecture depuis la base)
    public Contact(int id, String nom, String prenom, Categorie categorie, String telephone, String email, String photo) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.categorie = categorie;
        this.telephone = telephone;
        this.email = email;
        this.photo = photo;
    }

    // Constructeur sans ID (utilise pour l'ajout d'un nouveau contact)
    public Contact(String nom, String prenom, Categorie categorie, String telephone, String email, String photo) {
        this.nom = nom;
        this.prenom = prenom;
        this.categorie = categorie;
        this.telephone = telephone;
        this.email = email;
        this.photo = photo;
    }

    // Getters : permettent de recuperer les valeurs
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public Categorie getCategorie() { return categorie; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getPhoto() { return photo; }

    // Setters : permettent de modifier les valeurs
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoto(String photo) { this.photo = photo; }
}
