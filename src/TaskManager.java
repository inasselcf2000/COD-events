import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TaskManager {
    private List<Task> tasks;
    private JTable table;
    private JTextField nameInput;
    private JTextArea descriptionInput;
    private DefaultTableModel tableModel;
    private JTextField filterInput;
    private JPanel mainPanel;
    private boolean isModified; // Ajout du flag isModified
    private static final String SAVE_FILE = "tasks.ser";

    public TaskManager() throws IOException {
        tasks = new ArrayList<>();
        loadTasks();
        tableModel = new DefaultTableModel(new String[]{"Nom", "Description"}, 0);
        table = new JTable(tableModel);
        nameInput = new JTextField();
        descriptionInput = new JTextArea();
        filterInput = new JTextField();
        mainPanel = new JPanel(new BorderLayout(10, 10));
        initializeUI();
        refreshTable();
        isModified = false; // Initialisation du flag
    }

    private void initializeUI() {
        nameInput.setColumns(10);
        descriptionInput.setColumns(10);
        descriptionInput.setRows(3);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(this::addTask);

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(this::deleteTask);

        JButton editButton = new JButton("Modifier");
        editButton.addActionListener(this::editTask);

        JButton saveButton = new JButton("Sauvegarder");
        saveButton.addActionListener(this::saveTasks);

        filterInput.setColumns(10);
        filterInput.addActionListener(this::filterTasks);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.add(new JLabel("Nom :"));
        inputPanel.add(nameInput);
        inputPanel.add(new JLabel("Description :"));
        inputPanel.add(new JScrollPane(descriptionInput));
        inputPanel.add(addButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);
        inputPanel.add(saveButton);

        JPanel filterPanel = new JPanel(new BorderLayout(10, 10));
        filterPanel.add(new JLabel("Filtrer par nom :"), BorderLayout.WEST);
        filterPanel.add(filterInput, BorderLayout.CENTER);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        mainPanel.add(filterPanel, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void addTask(ActionEvent e) {
        String name = nameInput.getText();
        String description = descriptionInput.getText();
        if (!name.isEmpty() && !description.isEmpty()) {
            Task task = new Task(name, description);
            tasks.add(task);
            tableModel.addRow(new Object[]{task.getName(), task.getDescription()});
            nameInput.setText("");
            descriptionInput.setText("");
            isModified = true; // Marquer comme modifié
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez remplir les deux champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            tasks.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            isModified = true; // Marquer comme modifié
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une tâche à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editTask(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            Task task = tasks.get(selectedRow);
            String newName = JOptionPane.showInputDialog("Modifier le nom", task.getName());
            String newDescription = JOptionPane.showInputDialog("Modifier la description", task.getDescription());
            if (newName != null && newDescription != null) {
                task.setName(newName);
                task.setDescription(newDescription);
                tableModel.setValueAt(newName, selectedRow, 0);
                tableModel.setValueAt(newDescription, selectedRow, 1);
                isModified = true; // Marquer comme modifié
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une tâche à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTasks(ActionEvent e) {
        String filterText = filterInput.getText();
        List<Task> filteredTasks = tasks.stream()
            .filter(task -> task.getName().contains(filterText))
            .collect(Collectors.toList());
        refreshTable(filteredTasks);
    }

    private void refreshTable() {
        refreshTable(tasks);
    }

    private void refreshTable(List<Task> tasksToDisplay) {
        tableModel.setRowCount(0);
        for (Task task : tasksToDisplay) {
            tableModel.addRow(new Object[]{task.getName(), task.getDescription()});
        }
    }

    private void saveTasks(ActionEvent e) {
        if (isModified) { // Sauvegarder uniquement si modifié
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
                oos.writeObject(new ArrayList<>(tasks));
                JOptionPane.showMessageDialog(null, "Tâches sauvegardées avec succès.", "Information", JOptionPane.INFORMATION_MESSAGE);
                isModified = false; // Réinitialiser le flag
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la sauvegarde des tâches : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Aucune modification à sauvegarder.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadTasks() throws IOException {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
                tasks = (List<Task>) ois.readObject();
            } catch (ClassNotFoundException | IOException ex) {
                throw new IOException("Erreur lors du chargement des tâches : " + ex.getMessage(), ex);
            }
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}
