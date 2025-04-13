package ui;

import dao.CategorieDAO;
import dao.ContactDAO;
import model.Categorie;
import model.Contact;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        setSize(1000, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🧱 Conteneur principal
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setContentPane(container);

        // 🔍 Barre de recherche et filtre
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setToolTipText("Rechercher un nom ou prenom");

        categorieFilter = new JComboBox<>();
        categorieFilter.setFont(new Font("SansSerif", Font.PLAIN, 14));
        categorieFilter.addItem(new Categorie(0, "Toutes les categories"));
        List<Categorie> categories = new CategorieDAO().getAllCategories();
        for (Categorie c : categories) {
            categorieFilter.addItem(c);
        }

        searchPanel.add(new JLabel("🔍 Rechercher : "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(categorieFilter, BorderLayout.EAST);

        topPanel.add(searchPanel);
        container.add(topPanel, BorderLayout.NORTH);

        // 📋 Tableau de contacts
        String[] columns = {"Nom", "Prenom", "Telephone", "Email", "Categorie", "Photo"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        // Centrer les textes dans les colonnes
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane, BorderLayout.CENTER);

        // 🖼️ Zone d'affichage de la photo
        photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(JLabel.CENTER);
        photoLabel.setVerticalAlignment(JLabel.CENTER);
        photoLabel.setPreferredSize(new Dimension(200, 200));
        photoLabel.setBorder(BorderFactory.createTitledBorder("Photo du contact"));
        container.add(photoLabel, BorderLayout.EAST);

        // 🔘 Boutons
        JButton supprimerBtn = new JButton("Supprimer");
        JButton modifierBtn = new JButton("Modifier");
        supprimerBtn.setBackground(new Color(220, 53, 69));
        supprimerBtn.setForeground(Color.WHITE);
        modifierBtn.setBackground(new Color(0, 123, 255));
        modifierBtn.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(modifierBtn);
        buttonPanel.add(supprimerBtn);

        container.add(buttonPanel, BorderLayout.SOUTH);

        // 🎯 Chargement initial
        chargerContacts();

        // 🔁 Recherche dynamique
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
        });

        categorieFilter.addActionListener(e -> filtrer());

        // 🖱️ Affichage photo si ligne selectionnee
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                afficherPhotoSelectionnee();
            }
        });

        // 🔘 Action boutons
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
                    c.getNom(),
                    c.getPrenom(),
                    c.getTelephone(),
                    c.getEmail(),
                    c.getCategorie().getNom(),
                    c.getPhoto()
            });
        }

        photoLabel.setIcon(null); // reset image
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
            JOptionPane.showMessageDialog(this, "Veuillez selectionner un contact !");
            return;
        }

        int idASupprimer = ids[selectedRow];
        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer ce contact ?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new ContactDAO().supprimerContact(idASupprimer);
            chargerContacts();
            JOptionPane.showMessageDialog(this, "Contact supprime !");
        }
    }

    private void modifierContactSelectionne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez selectionner un contact !");
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
