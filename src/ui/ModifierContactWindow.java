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

public class ModifierContactWindow extends JFrame {

    public ModifierContactWindow(Contact contact, Runnable onUpdateCallback) {
        setTitle("Modifier le contact");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));

        JTextField nomField = new JTextField(contact.getNom());
        JTextField prenomField = new JTextField(contact.getPrenom());
        JTextField telephoneField = new JTextField(contact.getTelephone());
        JTextField emailField = new JTextField(contact.getEmail());
        JTextField photoField = new JTextField(contact.getPhoto());
        JButton photoButton = new JButton("Choisir...");

        JComboBox<Categorie> categorieComboBox = new JComboBox<>();
        List<Categorie> categories = new CategorieDAO().getAllCategories();
        for (Categorie cat : categories) {
            categorieComboBox.addItem(cat);
        }
        categorieComboBox.setSelectedItem(contact.getCategorie());

        JButton modifierBtn = new JButton("Enregistrer les modifications");

        photoButton.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                photoField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        modifierBtn.addActionListener((ActionEvent e) -> {
            contact.setNom(nomField.getText());
            contact.setPrenom(prenomField.getText());
            contact.setTelephone(telephoneField.getText());
            contact.setEmail(emailField.getText());
            contact.setPhoto(photoField.getText());
            contact.setCategorie((Categorie) categorieComboBox.getSelectedItem());

            new ContactDAO().modifierContact(contact);
            JOptionPane.showMessageDialog(this, "Contact modifié !");
            onUpdateCallback.run(); // recharge la liste
            dispose(); // ferme la fenêtre
        });

        panel.add(new JLabel("Nom :")); panel.add(nomField);
        panel.add(new JLabel("Prénom :")); panel.add(prenomField);
        panel.add(new JLabel("Téléphone :")); panel.add(telephoneField);
        panel.add(new JLabel("Email :")); panel.add(emailField);
        panel.add(new JLabel("Photo :")); panel.add(photoField);
        panel.add(new JLabel("")); panel.add(photoButton);
        panel.add(new JLabel("Catégorie :")); panel.add(categorieComboBox);
        panel.add(new JLabel("")); panel.add(modifierBtn);

        add(panel);
        setVisible(true);
    }
}
