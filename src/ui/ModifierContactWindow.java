package ui;

import dao.CategorieDAO;
import dao.ContactDAO;
import model.Categorie;
import model.Contact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ModifierContactWindow extends JFrame {

    public ModifierContactWindow(Contact contact, Runnable onUpdateCallback) {
        setTitle("Modifier le contact");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 🧱 Panel principal avec marge intérieure
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 🧾 Formulaire avec GridLayout
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 15)); // espaces entre lignes et colonnes

        // 📝 Champs pré-remplis
        JTextField nomField = new JTextField(contact.getNom());
        JTextField prenomField = new JTextField(contact.getPrenom());
        JTextField telephoneField = new JTextField(contact.getTelephone());
        JTextField emailField = new JTextField(contact.getEmail());
        JTextField photoField = new JTextField(contact.getPhoto());
        JButton photoButton = new JButton("Choisir une image");

        JComboBox<Categorie> categorieComboBox = new JComboBox<>();
        List<Categorie> categories = new CategorieDAO().getAllCategories();
        for (Categorie cat : categories) {
            categorieComboBox.addItem(cat);
        }
        categorieComboBox.setSelectedItem(contact.getCategorie());

        // ✅ Bouton modifier
        JButton modifierBtn = new JButton("✅ Enregistrer les modifications");

        // 📷 Choisir une image
        photoButton.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                photoField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        // 💾 Enregistrer les modifications
        modifierBtn.addActionListener((ActionEvent e) -> {
            contact.setNom(nomField.getText());
            contact.setPrenom(prenomField.getText());
            contact.setTelephone(telephoneField.getText());
            contact.setEmail(emailField.getText());
            contact.setPhoto(photoField.getText());
            contact.setCategorie((Categorie) categorieComboBox.getSelectedItem());

            new ContactDAO().modifierContact(contact);
            JOptionPane.showMessageDialog(this, "Contact modifie !");
            onUpdateCallback.run(); // recharge la liste dans la fenetre principale
            dispose();
        });

        // 🧩 Ajouter les champs dans le formulaire
        formPanel.add(new JLabel("Nom :")); formPanel.add(nomField);
        formPanel.add(new JLabel("Prenom :")); formPanel.add(prenomField);
        formPanel.add(new JLabel("Telephone :")); formPanel.add(telephoneField);
        formPanel.add(new JLabel("Email :")); formPanel.add(emailField);
        formPanel.add(new JLabel("Photo :")); formPanel.add(photoField);
        formPanel.add(new JLabel("")); formPanel.add(photoButton);
        formPanel.add(new JLabel("Categorie :")); formPanel.add(categorieComboBox);

        // Panel pour centrer le bouton
        JPanel boutonPanel = new JPanel();
        boutonPanel.add(modifierBtn);

        container.add(formPanel, BorderLayout.CENTER);
        container.add(boutonPanel, BorderLayout.SOUTH);

        add(container);
        setVisible(true);
    }
}
