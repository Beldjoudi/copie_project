package dao;

import model.Categorie;
import model.Contact;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

    public void ajouterContact(Contact c) {
        String sql = "INSERT INTO contacts (nom, prenom, categorie_id, telephone, email, photo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setInt(3, c.getCategorie().getId());
            stmt.setString(4, c.getTelephone());
            stmt.setString(5, c.getEmail());
            stmt.setString(6, c.getPhoto());

            stmt.executeUpdate();
            System.out.println("Contact ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT c.*, cat.nom AS nom_categorie FROM contacts c JOIN categories cat ON c.categorie_id = cat.id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String telephone = rs.getString("telephone");
                String email = rs.getString("email");
                String photo = rs.getString("photo");

                // Construire la catégorie
                Categorie categorie = new Categorie(rs.getInt("categorie_id"), rs.getString("nom_categorie"));

                Contact contact = new Contact(id, nom, prenom, categorie, telephone, email, photo);
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    public void supprimerContact(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Contact supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void modifierContact(Contact c) {
        String sql = "UPDATE contacts SET nom=?, prenom=?, categorie_id=?, telephone=?, email=?, photo=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setInt(3, c.getCategorie().getId());
            stmt.setString(4, c.getTelephone());
            stmt.setString(5, c.getEmail());
            stmt.setString(6, c.getPhoto());
            stmt.setInt(7, c.getId());

            stmt.executeUpdate();
            System.out.println("Contact modifié avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
