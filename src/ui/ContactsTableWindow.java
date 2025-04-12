package ui;

import dao.CategorieDAO;
import dao.ContactDAO;
import model.Categorie;
import model.Contact;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContactsTableWindow extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private int[] ids;
    private JTextField searchField;
    private JComboBox<Categorie> categorieFilter;
    private JLabel photoLabel;
    private List<Contact> allContacts;

    public ContactsTableWindow() {
        setTitle("Liste des Contacts");
        setSize(1000, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔍 Recherche + Catégorie
        JPanel topPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();

        categorieFilter = new JComboBox<>();
        categorieFilter.addItem(new Categorie(0, "Toutes les catégories"));
        List<Categorie> categories = new CategorieDAO().getAllCategories();
        for (Categorie c : categories) {
            categorieFilter.addItem(c);
        }

        JPanel topFilters = new JPanel(new GridLayout(1, 2, 10, 0));
        topFilters.add(searchField);
        topFilters.add(categorieFilter);

        topPanel.add(new JLabel("🔍 Recherche + Catégorie :"), BorderLayout.NORTH);
        topPanel.add(topFilters, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 📋 Tableau
        String[] columns = {"Nom", "Prénom", "Téléphone", "Email", "Catégorie", "Photo"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 🖼️ Zone d'affichage de la photo
        photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(JLabel.CENTER);
        photoLabel.setVerticalAlignment(JLabel.CENTER);
        photoLabel.setPreferredSize(new Dimension(200, 200));
        photoLabel.setBorder(BorderFactory.createTitledBorder("Photo du contact"));
        add(photoLabel, BorderLayout.EAST);

        // 🔘 Boutons
        JButton supprimerBtn = new JButton("Supprimer le contact sélectionné");
        JButton modifierBtn = new JButton("Modifier le contact sélectionné");
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(supprimerBtn);
        buttonsPanel.add(modifierBtn);
        add(buttonsPanel, BorderLayout.SOUTH);

        // 🔄 Charger tous les contacts
        chargerContacts();

        // 🔁 Recherche texte
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
        });

        // 🔁 Filtrage par catégorie
        categorieFilter.addActionListener(e -> filtrer());

        // 🖱️ Sélection pour afficher photo
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                afficherPhotoSelectionnee();
            }
        });

        // 🔘 Actions boutons
        supprimerBtn.addActionListener(e -> supprimerContactSelectionne());
        modifierBtn.addActionListener(e -> modifierContactSelectionne());

        setVisible(true);
    }

    private void chargerContacts() {
        allContacts = new ContactDAO().getAllContacts();
        afficherContacts(allContacts);
    }

    private void afficherContacts(List<Contact> contacts) {
        model.setRowCount(0);
        ids = new int[contacts.size()];

        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);
            ids[i] = c.getId();
            model.addRow(new Object[]{
                    c.getNom(), c.getPrenom(), c.getTelephone(), c.getEmail(),
                    c.getCategorie().getNom(), c.getPhoto()
            });
        }

        photoLabel.setIcon(null); // Réinitialise la photo si on recharge
    }

    private void filtrer() {
        String texte = searchField.getText().toLowerCase();
        Categorie selectedCat = (Categorie) categorieFilter.getSelectedItem();

        List<Contact> resultats = allContacts.stream()
                .filter(c -> c.getNom().toLowerCase().contains(texte)
                        || c.getPrenom().toLowerCase().contains(texte))
                .filter(c -> selectedCat.getId() == 0 || c.getCategorie().getId() == selectedCat.getId())
                .toList();

        afficherContacts(resultats);
    }

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
            chargerContacts();
            JOptionPane.showMessageDialog(this, "Contact supprimé !");
        }
    }

    private void modifierContactSelectionne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact !");
            return;
        }

        int id = ids[selectedRow];
        String nom = (String) model.getValueAt(selectedRow, 0);
        String prenom = (String) model.getValueAt(selectedRow, 1);
        String telephone = (String) model.getValueAt(selectedRow, 2);
        String email = (String) model.getValueAt(selectedRow, 3);
        String categorieNom = (String) model.getValueAt(selectedRow, 4);
        String photo = (String) model.getValueAt(selectedRow, 5);

        CategorieDAO catDAO = new CategorieDAO();
        Categorie categorie = catDAO.getAllCategories()
                .stream()
                .filter(cat -> cat.getNom().equals(categorieNom))
                .findFirst()
                .orElse(null);

        Contact contact = new Contact(id, nom, prenom, categorie, telephone, email, photo);
        new ModifierContactWindow(contact, this::chargerContacts);
    }
}
