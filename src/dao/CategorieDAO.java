package dao;

import model.Categorie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {
    public List<Categorie> getAllCategories() {
        List<Categorie> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM categories";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                categories.add(new Categorie(id, nom));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
}
