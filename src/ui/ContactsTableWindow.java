package ui; // Le package ui regroupe toutes les classes de l’interface utilisateur

// Importation des classes nécessaires
import dao.CategorieDAO;
import dao.ContactDAO;
import model.Categorie;
import model.Contact;

import javax.swing.*; // Bibliothèque Swing pour les composants graphiques
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*; // Pour les layouts, couleurs, tailles, etc.
import java.util.List; // Pour manipuler des listes de données

// Classe qui affiche tous les contacts avec possibilité de rechercher, filtrer, modifier et supprimer
public class ContactsTableWindow extends JFrame {

    // Déclaration des composants nécessaires
    private JTable table;                       // Tableau d'affichage
    private DefaultTableModel model;            // Modèle de données du tableau
    private int[] ids;                          // Liste des IDs des contacts affichés
    private JTextField searchField;             // Champ de recherche texte
    private JComboBox<Categorie> categorieFilter; // Filtre des catégories
    private JLabel photoLabel;                  // Zone d'affichage de la photo
    private List<Contact> allContacts;          // Liste complète des contacts
/// ////////////////////////////////
    // Constructeur principal
    public ContactsTableWindow() {
        setTitle("Liste des Contacts");               // Titre de la fenêtre
        setSize(1000, 550);                           // Taille de la fenêtre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);   // Ferme uniquement cette fenêtre
        setLocationRelativeTo(null);                  // Centre la fenêtre

        // Création du conteneur principal avec marges
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setContentPane(container); // On utilise ce conteneur comme base

        // Création de la zone de recherche et de filtre par catégorie
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchField = new JTextField();                         // Champ texte pour rechercher un contact
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setToolTipText("Rechercher un nom ou prenom"); // Info bulle

        categorieFilter = new JComboBox<>();                    // Liste déroulante pour filtrer les catégories
        categorieFilter.setFont(new Font("SansSerif", Font.PLAIN, 14));
        categorieFilter.addItem(new Categorie(0, "Toutes les categories")); // Option par défaut

        // Remplissage de la liste des catégories depuis la base
        List<Categorie> categories = new CategorieDAO().getAllCategories();
        for (Categorie c : categories) {
            categorieFilter.addItem(c);
        }

        // Placement des composants dans le panneau de recherche
        searchPanel.add(new JLabel("Rechercher : "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(categorieFilter, BorderLayout.EAST);

        topPanel.add(searchPanel);
        container.add(topPanel, BorderLayout.NORTH); // Ajout du haut de la fenêtre

        // Définition des colonnes du tableau
        String[] columns = {"Nom", "Prenom", "Telephone", "Email", "Categorie", "Photo"};
        model = new DefaultTableModel(columns, 0); // Modèle vide avec entêtes
        table = new JTable(model);                 // Création du tableau
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(28);                    // Hauteur des lignes
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);      // Couleur des lignes du tableau

        // Centrage du texte dans toutes les cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Ajout du tableau dans un panneau défilant
        JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane, BorderLayout.CENTER);

        // Création du panneau à droite pour afficher la photo du contact sélectionné
        photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(JLabel.CENTER);
        photoLabel.setVerticalAlignment(JLabel.CENTER);
        photoLabel.setPreferredSize(new Dimension(200, 200));
        photoLabel.setBorder(BorderFactory.createTitledBorder("Photo du contact"));
        container.add(photoLabel, BorderLayout.EAST);

        // Création des boutons de modification et de suppression
        JButton supprimerBtn = new JButton("Supprimer");
        JButton modifierBtn = new JButton("Modifier");

        // Personnalisation des couleurs
        supprimerBtn.setBackground(new Color(220, 53, 69)); // Rouge
        supprimerBtn.setForeground(Color.WHITE);
        modifierBtn.setBackground(new Color(0, 123, 255));  // Bleu
        modifierBtn.setForeground(Color.WHITE);

        // Panneau contenant les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(modifierBtn);
        buttonPanel.add(supprimerBtn);
        container.add(buttonPanel, BorderLayout.SOUTH); // Ajout en bas de la fenêtre

        // Chargement initial des contacts depuis la base
        chargerContacts();

        // Mise en place du filtre dynamique par texte
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
        });

        // Ajout du filtre par catégorie
        categorieFilter.addActionListener(e -> filtrer());

        // Lorsqu'on sélectionne une ligne, on affiche la photo
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                afficherPhotoSelectionnee();
            }
        });

        // Action du bouton Supprimer
        supprimerBtn.addActionListener(e -> supprimerContactSelectionne());

        // Action du bouton Modifier
        modifierBtn.addActionListener(e -> modifierContactSelectionne());

        setVisible(true); // Affiche la fenêtre
    }

    // Récupère tous les contacts de la base de données
    private void chargerContacts() {
        allContacts = new ContactDAO().getAllContacts();
        afficherContacts(allContacts);
    }

    // Affiche la liste des contacts dans le tableau
    private void afficherContacts(List<Contact> contacts) {
        model.setRowCount(0); // Vide les anciennes lignes
        ids = new int[contacts.size()]; // Prépare le tableau d'IDs

        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);
            ids[i] = c.getId(); // On enregistre l'ID à la même position que la ligne

            model.addRow(new Object[]{
                    c.getNom(),
                    c.getPrenom(),
                    c.getTelephone(),
                    c.getEmail(),
                    c.getCategorie().getNom(),
                    c.getPhoto()
            });
        }

        photoLabel.setIcon(null); // Réinitialise la photo affichée
    }

    // Filtre les contacts selon le texte et la catégorie sélectionnée
    private void filtrer() {
        String texte = searchField.getText().toLowerCase();
        Categorie selectedCat = (Categorie) categorieFilter.getSelectedItem();

        List<Contact> resultats = allContacts.stream()
                .filter(c -> c.getNom().toLowerCase().contains(texte) || c.getPrenom().toLowerCase().contains(texte))
                .filter(c -> selectedCat.getId() == 0 || c.getCategorie().getId() == selectedCat.getId())
                .toList();

        afficherContacts(resultats);
    }

    // Affiche la photo correspondant à la ligne sélectionnée
    private void afficherPhotoSelectionnee() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            photoLabel.setIcon(null);
            return;
        }

        String cheminPhoto = (String) model.getValueAt(selectedRow, 5);
        if (cheminPhoto != null && !cheminPhoto.isEmpty()) {
            ImageIcon icon = new ImageIcon(cheminPhoto);
            Image image = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            photoLabel.setIcon(new ImageIcon(image));
        } else {
            photoLabel.setIcon(null);
        }
    }

    // Supprime le contact sélectionné après confirmation
    private void supprimerContactSelectionne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact !");
            return;
        }

        int idASupprimer = ids[selectedRow];
        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer ce contact ?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new ContactDAO().supprimerContact(idASupprimer);
            chargerContacts(); // Recharge la liste après suppression
            JOptionPane.showMessageDialog(this, "Contact supprimé !");
        }
    }

    // Ouvre la fenêtre de modification pour le contact sélectionné
    private void modifierContactSelectionne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact !");
            return;
        }

        // Récupération des données de la ligne sélectionnée
        int id = ids[selectedRow];
        String nom = (String) model.getValueAt(selectedRow, 0);
        String prenom = (String) model.getValueAt(selectedRow, 1);
        String telephone = (String) model.getValueAt(selectedRow, 2);
        String email = (String) model.getValueAt(selectedRow, 3);
        String categorieNom = (String) model.getValueAt(selectedRow, 4);
        String photo = (String) model.getValueAt(selectedRow, 5);

        // Récupération de l'objet Categorie correspondant
        CategorieDAO catDAO = new CategorieDAO();
        Categorie categorie = catDAO.getAllCategories()
                .stream()
                .filter(cat -> cat.getNom().equals(categorieNom))
                .findFirst()
                .orElse(null);

        // Création du contact et ouverture de la fenêtre de modification
        Contact contact = new Contact(id, nom, prenom, categorie, telephone, email, photo);
        new ModifierContactWindow(contact, this::chargerContacts);
    }
}
