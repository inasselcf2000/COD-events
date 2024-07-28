import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Crée et affiche l'interface utilisateur dans le thread de l'interface
        // utilisateur
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestionnaire de Tâches");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            TaskManager taskManager = new TaskManager();
            frame.add(taskManager.getPanel());

            frame.setVisible(true);
        });
    }
}
