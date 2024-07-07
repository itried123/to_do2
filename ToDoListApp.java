import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Abstract class Task
abstract class Task {
    protected String description;
    protected boolean isCompleted;

    public Task(String description) {
        this.description = description;
        this.isCompleted = false;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    public String getDescription() {
        return description;
    }

    public abstract String displayTask();
}

// Concrete class for ToDoTask
class ToDoTask extends Task {
    public ToDoTask(String description) {
        super(description);
    }

    @Override
    public String displayTask() {
        return "To-Do: " + description + (isCompleted ? " (Completed)" : "");
    }
}

// Concrete class for DeadlineTask
class DeadlineTask extends Task {
    private String deadline;

    public DeadlineTask(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }

    @Override
    public String displayTask() {
        return "Deadline Task: " + description + " (Deadline: " + deadline + ")" + (isCompleted ? " (Completed)" : "");
    }
}

// Concrete class for TimedTask
class TimedTask extends Task {
    private String timeline;

    public TimedTask(String description, String timeline) {
        super(description);
        this.timeline = timeline;
    }

    public String getTimeline() {
        return timeline;
    }

    @Override
    public String displayTask() {
        return "Timed Task: " + description + " (Timeline: " + timeline + ")" + (isCompleted ? " (Completed)" : "");
    }
}

// Class representing a To-Do List
class ToDoList {
    private List<Task> tasks;

    public ToDoList() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void markTaskCompleted(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsCompleted();
        }
    }
}

// Main class with JFrame GUI
public class ToDoListApp {
    private static ToDoList toDoList = new ToDoList();

    public static void main(String[] args) {
        JFrame frame = new JFrame("To-Do List Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        JTextField descriptionField = new JTextField();
        JTextField deadlineField = new JTextField();
        JTextField timelineField = new JTextField();
        JComboBox<String> taskTypeComboBox = new JComboBox<>(new String[]{"ToDo Task", "Deadline Task", "Timed Task"});

        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Deadline (for Deadline Task only):"));
        inputPanel.add(deadlineField);
        inputPanel.add(new JLabel("Timeline (for Timed Task only):"));
        inputPanel.add(timelineField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));

        JButton addButton = new JButton("Add Task");
        JButton displayButton = new JButton("Display Tasks");
        JButton markCompletedButton = new JButton("Mark Task Completed");

        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(markCompletedButton);

        // TextArea to display tasks
        JTextArea taskArea = new JTextArea();
        taskArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taskArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = descriptionField.getText();
                String deadline = deadlineField.getText();
                String timeline = timelineField.getText();
                String taskType = (String) taskTypeComboBox.getSelectedItem();

                Task task = null;
                switch (taskType) {
                    case "ToDo Task":
                        task = new ToDoTask(description);
                        break;
                    case "Deadline Task":
                        if (deadline.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Deadline is required for Deadline Task.");
                            return;
                        }
                        task = new DeadlineTask(description, deadline);
                        break;
                    case "Timed Task":
                        if (timeline.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Timeline is required for Timed Task.");
                            return;
                        }
                        task = new TimedTask(description, timeline);
                        break;
                }

                if (task != null) {
                    toDoList.addTask(task);
                    descriptionField.setText("");
                    deadlineField.setText("");
                    timelineField.setText("");
                    taskTypeComboBox.setSelectedIndex(0);
                    JOptionPane.showMessageDialog(frame, "Task added successfully.");
                }
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskArea.setText("");
                for (int i = 0; i < toDoList.getTasks().size(); i++) {
                    taskArea.append((i + 1) + ". " + toDoList.getTasks().get(i).displayTask() + "\n");
                }
            }
        });

        markCompletedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(frame, "Enter the index of the task to mark as completed:");
                try {
                    int index = Integer.parseInt(input) - 1; // Convert to 0-based index
                    toDoList.markTaskCompleted(index);
                    JOptionPane.showMessageDialog(frame, "Task marked as completed.");
                } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid task index.");
                }
            }
        });

        frame.setVisible(true);
    }
}
