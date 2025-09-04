// File: StudyTrackerGUI.java
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class StudyTrackerGUI extends JFrame {

    private final StudyTracker tracker;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField filterField;

    public StudyTrackerGUI() {
        // 1. Initialize the backend
        tracker = new StudyTracker();

        // 2. Set up the main window (JFrame)
        setTitle("Study Tracker 1.0");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // 3. Create the table and its model
        String[] columnNames = {"Date", "Subject", "Duration (h)", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Enable Sorting
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // 4. Create buttons for actions
        JButton addButton = new JButton("Add Log");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton summaryButton = new JButton("View Summary");

        // 5. Lay out the components
        JPanel buttonPanel = new JPanel(); // Panel for buttons
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(summaryButton);
        
        // Create Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter/Search:"));
        filterField = new JTextField(25);
        filterPanel.add(filterField);

        // Add components to the main frame's layout
        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 6. Add functionality to buttons (Action Listeners)
        addButton.addActionListener(e -> showLogDialog(null));
        
        editButton.addActionListener(e -> {
            int selectedRowInView = table.getSelectedRow();
            if (selectedRowInView >= 0) {
                int modelRow = table.convertRowIndexToModel(selectedRowInView);
                showLogDialog(modelRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a log to edit.", "No Log Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRowInView = table.getSelectedRow();
            if (selectedRowInView >= 0) {
                int modelRow = table.convertRowIndexToModel(selectedRowInView);
                int choice = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this log?", "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    tracker.deleteLog(modelRow);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a log to delete.", "No Log Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        summaryButton.addActionListener(e -> {
            String summary = tracker.getSummaryBySubject();
            JTextArea textArea = new JTextArea(summary);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Study Summary", JOptionPane.INFORMATION_MESSAGE);
        });

        // Add listener to the filter text field
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updateFilter(); }
            @Override public void removeUpdate(DocumentEvent e) { updateFilter(); }
            @Override public void changedUpdate(DocumentEvent e) { updateFilter(); }
        });

        // 7. Load initial data and make the window visible
        refreshTable();
        setVisible(true);
    }
    
    private void updateFilter() {
        String text = filterField.getText();
        if (text.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing data
        for (StudyLog log : tracker.getLogs()) {
            Object[] row = {
                log.getDate(),
                log.getSubject(),
                log.getDuration(),
                log.getDescription()
            };
            tableModel.addRow(row);
        }
    }

    private void showLogDialog(Integer modelIndex) {
        // Create components for the dialog
        JTextField dateField = new JTextField(10);
        JTextField subjectField = new JTextField(10);
        JTextField durationField = new JTextField(5);
        JTextField descriptionField = new JTextField(20);

        String dialogTitle;
        if (modelIndex != null) {
            dialogTitle = "Edit Log";
            StudyLog log = tracker.getLogs().get(modelIndex);
            dateField.setText(log.getDate().toString());
            subjectField.setText(log.getSubject());
            durationField.setText(String.valueOf(log.getDuration()));
            descriptionField.setText(log.getDescription());
        } else {
            dialogTitle = "Add New Log";
            dateField.setText(LocalDate.now().toString());
        }
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Subject:"));
        panel.add(subjectField);
        panel.add(new JLabel("Duration (hours):"));
        panel.add(durationField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(this, panel, dialogTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                String subject = subjectField.getText();
                double duration = Double.parseDouble(durationField.getText());
                String description = descriptionField.getText();

                if (subject.trim().isEmpty() || duration <= 0) {
                     JOptionPane.showMessageDialog(this, "Subject cannot be empty and duration must be positive.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                     return;
                }
                
                StudyLog newLog = new StudyLog(date, subject, duration, description);
                if (modelIndex != null) {
                    tracker.updateLog(modelIndex, newLog);
                } else {
                    tracker.addLog(newLog);
                }
                refreshTable();

            } catch (DateTimeParseException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date or duration format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        // Set a modern Look and Feel (Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, the default L&F will be used.
        }
        
        // Run the GUI on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(StudyTrackerGUI::new);
    }
}