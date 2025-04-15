package ui; // Déclaration du package qui contient les classes de l'interface utilisateur

// Importation des classes nécessaires
import dao.CategorieDAO;
import dao.ContactDAO;
import model.Categorie;
import model.Contact;

import javax.swing.*;              // Bibliothèque Swing pour l'interface graphique
import java.awt.*;                 // Pour la gestion des couleurs, layouts, etc.
import java.awt.event.ActionEvent; // Pour gérer les clics de boutons
import java.util.List;             // Pour manipuler les listes de données

// Classe principale qui représente la fenêtre principale de l'application
public class MainWindow extends JFrame {

    // Constructeur : ici, on crée et configure toute la fenêtre
    public MainWindow() {

        setTitle("Gestionnaire de Contacts");        // Titre affiché dans la barre de la fenêtre
        setSize(600, 600);                           // Dimensions de la fenêtre (largeur x hauteur)
        setDefaultCloseOperation(EXIT_ON_CLOSE);     // Fermer l'application quand la fenêtre est fermée
        setLocationRelativeTo(null);                 // Centrer la fenêtre sur l'écran
        setLayout(new BorderLayout());               // Utilisation d'un layout divisé en 5 zones

        // Création d'un panneau d'en-tête avec un fond en dégradé noir-vert
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);                             // Appel du comportement de base
                Graphics2D g2d = (Graphics2D) g;                     // Conversion en Graphics2D
                Color noir = new Color(0, 0, 0);                     // Couleur noire
                Color vert = new Color(0, 153, 0);                   // Couleur verte personnalisée
                GradientPaint gp = new GradientPaint(0, 0, noir, getWidth(), getHeight(), vert); // Création du dégradé
                g2d.setPaint(gp);                                    // Appliquer le dégradé
                g2d.fillRect(0, 0, getWidth(), getHeight());         // Dessiner le rectangle de fond
            }
        };
        headerPanel.setPreferredSize(new Dimension(600, 100));       // Hauteur du header
        headerPanel.setLayout(new BorderLayout());                   // Layout du header

        JLabel headerLabel = new JLabel("Gestionnaire de Contacts"); // Texte du titre
        headerLabel.setFont(new Font("Arial", Font.PLAIN, 16));      // Police utilisée
        headerLabel.setForeground(Color.WHITE);                      // Couleur du texte
        headerLabel.setHorizontalAlignment(SwingConstants.RIGHT);    // Aligné à droite
        headerLabel.setVerticalAlignment(SwingConstants.BOTTOM);     // Aligné en bas
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10)); // Marge intérieure bas/droite
        headerPanel.add(headerLabel, BorderLayout.SOUTH);            // Ajouter le titre en bas du panneau
        add(headerPanel, BorderLayout.NORTH);                        // Ajouter le header en haut de la fenêtre

        // Panneau principal contenant les champs du formulaire
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));     // Grille 7 lignes, 2 colonnes
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Marges internes

        // Champs de formulaire
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField telephoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField photoField = new JTextField();
        JButton photoBtn = new JButton("Choisir...");                // Bouton pour sélectionner une photo
        JComboBox<Categorie> categorieComboBox = new JComboBox<>();  // Liste déroulante pour les catégories

        // Chargement des catégories depuis la base de données
        List<Categorie> categories = new CategorieDAO().getAllCategories();
        for (Categorie c : categories) {
            categorieComboBox.addItem(c); // Ajouter chaque catégorie à la liste
        }

        // Ajout des composants dans la grille
        panel.add(new JLabel("Nom :"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom :"));
        panel.add(prenomField);
        panel.add(new JLabel("Téléphone :"));
        panel.add(telephoneField);
        panel.add(new JLabel("Email :"));
        panel.add(emailField);
        panel.add(new JLabel("Photo :"));
        panel.add(photoField);
        panel.add(new JLabel(""));       // Cellule vide pour aligner le bouton
        panel.add(photoBtn);
        panel.add(new JLabel("Catégorie :"));
        panel.add(categorieComboBox);

        add(panel, BorderLayout.CENTER); // Ajouter le formulaire au centre de la fenêtre

        // Création des boutons "Ajouter" et "Afficher"
        JButton ajouterBtn = new JButton("Ajouter le contact");
        JButton afficherBtn = new JButton("Afficher les contacts");

        JPanel btnPanel = new JPanel();  // Panneau pour regrouper les boutons
        btnPanel.add(ajouterBtn);        // Ajouter le bouton d'ajout
        btnPanel.add(afficherBtn);       // Ajouter le bouton d'affichage

        // Action du bouton "Choisir..." pour sélectionner une image
        photoBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();                // Explorateur de fichiers
            int res = chooser.showOpenDialog(this);                   // Ouvrir le sélecteur
            if (res == JFileChooser.APPROVE_OPTION) {                 // Si un fichier est sélectionné
                photoField.setText(chooser.getSelectedFile().getAbsolutePath()); // Remplir le champ photo
            }
        });

        // Action du bouton "Ajouter"
        ajouterBtn.addActionListener((ActionEvent e) -> {
            // Récupérer les données du formulaire
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String telephone = telephoneField.getText();
            String email = emailField.getText();
            String photo = photoField.getText();
            Categorie categorie = (Categorie) categorieComboBox.getSelectedItem();

            // Vérifier que nom et prénom ne sont pas vides
            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nom et prénom obligatoires !");
                return;
            }

            // Créer un nouveau contact et l'ajouter à la base de données
            Contact c = new Contact(nom, prenom, categorie, telephone, email, photo);
            new ContactDAO().ajouterContact(c);

            // Afficher un message de succès
            JOptionPane.showMessageDialog(this, "Contact ajouté avec succès !");

            // Réinitialiser le formulaire
            nomField.setText("");
            prenomField.setText("");
            telephoneField.setText("");
            emailField.setText("");
            photoField.setText("");
            categorieComboBox.setSelectedIndex(0);
        });

        // Action du bouton "Afficher"
        afficherBtn.addActionListener(e -> new ContactsTableWindow()); // Ouvrir une nouvelle fenêtre

        // Pied de page contenant uniquement les droits
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes

        JLabel droitLabel = new JLabel("© 2025 Mouloud, Abdelghani, Walid – Tous droits réservés");
        droitLabel.setHorizontalAlignment(SwingConstants.RIGHT);       // Aligné à droite
        footer.add(droitLabel, BorderLayout.EAST);                     // Ajouter le texte à droite

        // Création d'un panneau global pour le bas de la fenêtre
        JPanel panelGlobalSud = new JPanel(new BorderLayout());
        panelGlobalSud.add(btnPanel, BorderLayout.NORTH);  // Boutons au-dessus
        panelGlobalSud.add(footer, BorderLayout.SOUTH);    // Footer en bas

        add(panelGlobalSud, BorderLayout.SOUTH);           // Ajouter tout en bas de la fenêtre

        setVisible(true); // Rendre la fenêtre visible
    }
}
