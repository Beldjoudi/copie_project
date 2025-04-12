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
    private List<Contact> allContacts;
    private JLabel photoLabel; // 🖼️ Label pour afficher la photo

    public ContactsTableWindow() {
        setTitle("Liste des Contacts");
        setSize(1000, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔍 Barre de recherche
        JPanel topPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        topPanel.add(new JLabel("🔍 Rechercher : "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 📋 Tableau des contacts
        String[] columns = {"Nom", "Prénom", "Téléphone", "Email", "Catégorie", "Photo"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 🖼️ Panneau photo
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

        // 🔄 Chargement initial
        chargerContacts();

        // 📌 Actions
        supprimerBtn.addActionListener(e -> supprimerContactSelectionne());
        modifierBtn.addActionListener(e -> modifierContactSelectionne());

        // 🔁 Sélection d’une ligne → afficher la photo
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                afficherPhotoSelectionnee();
            }
        });

        // 🔍 Recherche dynamique
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
        });

        setVisible(true);
    }

    private void chargerContacts() {
        ContactDAO dao = new ContactDAO();
        allContacts = dao.getAllContacts();
        afficherContacts(allContacts);
    }

    private void afficherContacts(List<Contact> contacts) {
        model.setRowCount(0);
        ids = new int[contacts.size()];

        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);
            ids[i] = c.getId();

            model.addRow(new Object[]{
                    c.getNom(),
                    c.getPrenom(),
                    c.getTelephone(),
                    c.getEmail(),
                    c.getCategorie().getNom(),
                    c.getPhoto()
            });
        }

        photoLabel.setIcon(null); // réinitialise la photo si on recharge
    }

    private void filtrer() {
        String texte = searchField.getText().toLowerCase();

        List<Contact> resultats = allContacts.stream()
                .filter(c -> c.getNom().toLowerCase().contains(texte)
                        || c.getPrenom().toLowerCase().contains(texte))
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
