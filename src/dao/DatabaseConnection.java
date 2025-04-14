package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // URL de connexion à la base MySQL (nom de la base : gestion_contacts)
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_contacts";

    // Identifiant de connexion à la base (à personnaliser si besoin)
    private static final String USER = "root";

    // Mot de passe de l'utilisateur MySQL
    private static final String PASSWORD = "root";

    // Methode statique pour obtenir une connexion à la base
    public static Connection getConnection() throws SQLException {
        // Utilise DriverManager pour se connecter à la base avec les identifiants
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
