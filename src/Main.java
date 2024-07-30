import java.io.IOException;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame frame = new JFrame("Gestionnaire de Tâches");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);

                TaskManager taskManager = new TaskManager();
                frame.add(taskManager.getPanel());

                frame.setVisible(true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors du chargement des tâches : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
