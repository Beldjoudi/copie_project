package dao;

import model.Categorie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    // Methode pour recuperer toutes les categories depuis la base de donnees
    public List<Categorie> getAllCategories() {
        List<Categorie> categories = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Requete SQL pour recuperer toutes les lignes de la table categories
            String sql = "SELECT * FROM categories";

            // Creation d'un Statement pour executer la requete
            Statement stmt = conn.createStatement();

            // Execution de la requete et stockage du resultat dans un ResultSet
            ResultSet rs = stmt.executeQuery(sql);

            // Parcourir les resultats ligne par ligne
            while (rs.next()) {
                int id = rs.getInt("id");         // lire la colonne id
                String nom = rs.getString("nom"); // lire la colonne nom

                // Ajouter un nouvel objet Categorie dans la liste
                categories.add(new Categorie(id, nom));
            }

        } catch (Exception e) {
            e.printStackTrace(); // Afficher l'erreur s'il y en a une
        }

        // Retourner la liste de toutes les categories
        return categories;
    }
}
