package ui; // Déclaration du package contenant l'interface utilisateur

// Importation des classes nécessaires
import dao.CategorieDAO;
import dao.ContactDAO;
import model.Categorie;
import model.Contact;

import javax.swing.*;              // Composants Swing (fenêtres, boutons, champs...)
import java.awt.*;                 // Layouts, couleurs, dimensionnements
import java.awt.event.ActionEvent; // Événements des boutons
import java.util.List;             // Pour gérer les listes de catégories

// Classe représentant la fenêtre de modification d’un contact
public class ModifierContactWindow extends JFrame {

    // Constructeur : prend un contact existant et une action de rappel à exécuter après modification
    public ModifierContactWindow(Contact contact, Runnable onUpdateCallback) {

        setTitle("Modifier le contact");            // Titre de la fenêtre
        setSize(450, 500);                          // Taille de la fenêtre
        setLocationRelativeTo(null);                // Centrage de la fenêtre à l'ouverture
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Fermer cette fenêtre sans quitter l'application

        // Panneau principal avec une marge intérieure
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panneau du formulaire avec une grille de 8 lignes et 2 colonnes
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 15));

        // Champs préremplis avec les données du contact
        JTextField nomField = new JTextField(contact.getNom());
        JTextField prenomField = new JTextField(contact.getPrenom());
        JTextField telephoneField = new JTextField(contact.getTelephone());
        JTextField emailField = new JTextField(contact.getEmail());
        JTextField photoField = new JTextField(contact.getPhoto());
        JButton photoButton = new JButton("Choisir une image"); // Bouton pour sélectionner une photo

        // Liste déroulante des catégories
        JComboBox<Categorie> categorieComboBox = new JComboBox<>();
        List<Categorie> categories = new CategorieDAO().getAllCategories(); // Récupération des catégories depuis la base
        for (Categorie cat : categories) {
            categorieComboBox.addItem(cat); // Ajout dans la liste
        }
        categorieComboBox.setSelectedItem(contact.getCategorie()); // Sélection de la catégorie actuelle du contact

        // Bouton pour enregistrer les modifications
        JButton modifierBtn = new JButton("Enregistrer les modifications");

        // Action pour choisir une nouvelle image
        photoButton.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();           // Explorateur de fichiers
            int result = chooser.showOpenDialog(this);           // Affiche la fenêtre de sélection
            if (result == JFileChooser.APPROVE_OPTION) {
                photoField.setText(chooser.getSelectedFile().getAbsolutePath()); // Remplit le champ avec le chemin choisi
            }
        });

        // Action du bouton "Modifier"
        modifierBtn.addActionListener((ActionEvent e) -> {
            // Mettre à jour les valeurs dans l’objet Contact
            contact.setNom(nomField.getText());
            contact.setPrenom(prenomField.getText());
            contact.setTelephone(telephoneField.getText());
            contact.setEmail(emailField.getText());
            contact.setPhoto(photoField.getText());
            contact.setCategorie((Categorie) categorieComboBox.getSelectedItem());

            // Mise à jour en base de données
            new ContactDAO().modifierContact(contact);

            // Message de confirmation
            JOptionPane.showMessageDialog(this, "Contact modifié !");

            // Exécute le rappel (rafraîchir l'affichage dans la fenêtre principale)
            onUpdateCallback.run();

            // Fermer cette fenêtre
            dispose();
        });

        // Ajout des champs dans le panneau de formulaire
        formPanel.add(new JLabel("Nom :"));        formPanel.add(nomField);
        formPanel.add(new JLabel("Prenom :"));     formPanel.add(prenomField);
        formPanel.add(new JLabel("Telephone :"));  formPanel.add(telephoneField);
        formPanel.add(new JLabel("Email :"));      formPanel.add(emailField);
        formPanel.add(new JLabel("Photo :"));      formPanel.add(photoField);
        formPanel.add(new JLabel(""));             formPanel.add(photoButton);
        formPanel.add(new JLabel("Categorie :"));  formPanel.add(categorieComboBox);

        // Panneau pour centrer le bouton en bas
        JPanel boutonPanel = new JPanel();
        boutonPanel.add(modifierBtn);

        // Ajout des panneaux au conteneur principal
        container.add(formPanel, BorderLayout.CENTER);
        container.add(boutonPanel, BorderLayout.SOUTH);

        // Ajout du conteneur à la fenêtre
        add(container);

        // Rendre la fenêtre visible
        setVisible(true);
    }
}
