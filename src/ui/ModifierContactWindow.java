package ui;

import dao.CategorieDAO;
import dao.ContactDAO;
import model.Categorie;
import model.Contact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

// Fenetre pour modifier un contact existant
public class ModifierContactWindow extends JFrame {

    // Constructeur avec le contact a modifier + une fonction pour rafraichir la liste
    public ModifierContactWindow(Contact contact, Runnable onUpdateCallback) {
        setTitle("Modifier le contact");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel avec grille pour le formulaire
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));

        // Champs pre-remplis avec les valeurs du contact
        JTextField nomField = new JTextField(contact.getNom());
        JTextField prenomField = new JTextField(contact.getPrenom());
        JTextField telephoneField = new JTextField(contact.getTelephone());
        JTextField emailField = new JTextField(contact.getEmail());
        JTextField photoField = new JTextField(contact.getPhoto());
        JButton photoButton = new JButton("Choisir...");

        // ComboBox des categories
        JComboBox<Categorie> categorieComboBox = new JComboBox<>();
        List<Categorie> categories = new CategorieDAO().getAllCategories();
        for (Categorie cat : categories) {
            categorieComboBox.addItem(cat);
        }

        // Selectionner la categorie actuelle du contact
        categorieComboBox.setSelectedItem(contact.getCategorie());

        // Bouton pour enregistrer les modifications
        JButton modifierBtn = new JButton("Enregistrer les modifications");

        // Action pour choisir une nouvelle image
        photoButton.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                photoField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Action du bouton "Enregistrer"
        modifierBtn.addActionListener((ActionEvent e) -> {
            // Recuperer les nouvelles valeurs saisies
            contact.setNom(nomField.getText());
            contact.setPrenom(prenomField.getText());
            contact.setTelephone(telephoneField.getText());
            contact.setEmail(emailField.getText());
            contact.setPhoto(photoField.getText());
            contact.setCategorie((Categorie) categorieComboBox.getSelectedItem());

            // Mettre a jour dans la base
            new ContactDAO().modifierContact(contact);

            // Afficher confirmation
            JOptionPane.showMessageDialog(this, "Contact modifie !");

            // Recharger la liste dans la fenetre precedente
            onUpdateCallback.run();

            // Fermer la fenetre
            dispose();
        });

        // Ajouter les champs au formulaire
        panel.add(new JLabel("Nom :")); panel.add(nomField);
        panel.add(new JLabel("Prenom :")); panel.add(prenomField);
        panel.add(new JLabel("Telephone :")); panel.add(telephoneField);
        panel.add(new JLabel("Email :")); panel.add(emailField);
        panel.add(new JLabel("Photo :")); panel.add(photoField);
        panel.add(new JLabel("")); panel.add(photoButton);
        panel.add(new JLabel("Categorie :")); panel.add(categorieComboBox);
        panel.add(new JLabel("")); panel.add(modifierBtn);

        // Ajouter le formulaire a la fenetre
        add(panel);
        setVisible(true);
    }
}
