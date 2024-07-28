import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks;
    private JTable table;
    private JTextField nameInput;
    private JTextArea descriptionInput;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;

    public TaskManager() {
        tasks = new ArrayList<>();
        tableModel = new DefaultTableModel(new String[]{"Nom", "Description"}, 0);
        table = new JTable(tableModel);
        nameInput = new JTextField();
        descriptionInput = new JTextArea();
        mainPanel = new JPanel(new BorderLayout(10, 10));
        initializeUI();
    }

    private void initializeUI() {
        nameInput.setColumns(10);
        descriptionInput.setColumns(10);
        descriptionInput.setRows(3);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(this::addTask);

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(this::deleteTask);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("Nom :"));
        inputPanel.add(nameInput);
        inputPanel.add(new JLabel("Description :"));
        inputPanel.add(new JScrollPane(descriptionInput));
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

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
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez remplir les deux champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            tasks.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une tâche à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}
