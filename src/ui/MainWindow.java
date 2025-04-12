package ui;

import dao.CategorieDAO;
import dao.ContactDAO;
import model.Categorie;
import model.Contact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// Classe principale de l'application, elle affiche le formulaire pour ajouter un contact
public class MainWindow extends JFrame {

    public MainWindow() {
        // Titre et dimensions de la fenetre
        setTitle("Gestionnaire de Contacts");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==== ENTETE AVEC IMAGE DE FOND ET TEXTE EN BAS A DROITE ====
        JPanel headerPanel = new JPanel() {
            // Chargement de l'image d'entete
            Image headerImage = new ImageIcon(getClass().getResource("adobestock_225991884_preview.jpeg")).getImage();

            // Dessiner l'image dans le panel
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(headerImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        headerPanel.setPreferredSize(new Dimension(600, 100));
        headerPanel.setLayout(new BorderLayout());

        // Texte sur l'image (titre de l'application)
        JLabel headerLabel = new JLabel("Gestionnaire de Contacts");
        headerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10)); // marge bas/droite
        headerPanel.add(headerLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // ==== FORMULAIRE CENTRAL ====
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Champs du formulaire
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField telephoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField photoField = new JTextField();
        JButton photoBtn = new JButton("Choisir...");
        JComboBox<Categorie> categorieComboBox = new JComboBox<>();

        // Charger les categories depuis la base
        List<Categorie> categories = new CategorieDAO().getAllCategories();
        for (Categorie c : categories) {
            categorieComboBox.addItem(c);
        }

        // Ajouter les composants dans la grille
        panel.add(new JLabel("Nom :"));
        panel.add(nomField);
        panel.add(new JLabel("Prenom :"));
        panel.add(prenomField);
        panel.add(new JLabel("Telephone :"));
        panel.add(telephoneField);
        panel.add(new JLabel("Email :"));
        panel.add(emailField);
        panel.add(new JLabel("Photo :"));
        panel.add(photoField);
        panel.add(new JLabel(""));
        panel.add(photoBtn);
        panel.add(new JLabel("Categorie :"));
        panel.add(categorieComboBox);

        add(panel, BorderLayout.CENTER);

        // ==== BOUTONS AJOUTER + AFFICHER ====
        JButton ajouterBtn = new JButton("Ajouter le contact");
        JButton afficherBtn = new JButton("Afficher les contacts");

        JPanel btnPanel = new JPanel();
        btnPanel.add(ajouterBtn);
        btnPanel.add(afficherBtn);

        // ==== ACTION : CHOISIR UNE PHOTO ====
        photoBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                photoField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        // ==== ACTION : AJOUTER UN CONTACT ====
        ajouterBtn.addActionListener((ActionEvent e) -> {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String telephone = telephoneField.getText();
            String email = emailField.getText();
            String photo = photoField.getText();
            Categorie categorie = (Categorie) categorieComboBox.getSelectedItem();

            // Verifier que nom et prenom ne sont pas vides
            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nom et prenom obligatoires !");
                return;
            }

            // Creer et enregistrer le contact dans la base
            Contact c = new Contact(nom, prenom, categorie, telephone, email, photo);
            new ContactDAO().ajouterContact(c);

            JOptionPane.showMessageDialog(this, "Contact ajoute avec succes !");

            // Vider le formulaire
            nomField.setText("");
            prenomField.setText("");
            telephoneField.setText("");
            emailField.setText("");
            photoField.setText("");
            categorieComboBox.setSelectedIndex(0);
        });

        // ==== ACTION : AFFICHER LA LISTE DES CONTACTS ====
        afficherBtn.addActionListener(e -> new ContactsTableWindow());

        // ==== FOOTER AVEC IMAGE + DROITS ====
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Logo a gauche
        ImageIcon icon = new ImageIcon(getClass().getResource("OIP.jpg"));
        Image img = icon.getImage().getScaledInstance(180, 50, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));

        // Texte a droite
        JLabel droitLabel = new JLabel("© 2025 Mouloud, Abdelghani, Walid – Tous droits reserves");
        droitLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Ajouter les composants au footer
        footer.add(imageLabel, BorderLayout.WEST);
        footer.add(droitLabel, BorderLayout.EAST);

        // Ajouter les boutons + le footer dans un panel en bas
        JPanel panelGlobalSud = new JPanel(new BorderLayout());
        panelGlobalSud.add(btnPanel, BorderLayout.NORTH);
        panelGlobalSud.add(footer, BorderLayout.SOUTH);

        add(panelGlobalSud, BorderLayout.SOUTH);

        setVisible(true);
    }
}
