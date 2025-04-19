package org.example;

import javax.swing.*;
import java.awt.*;


public class EmployeeTask extends JFrame {
    private TasksManagement tasksManagement;
    private JTextField nameField, taskIdField, taskStatusField, startHourField, endHourField,empIdField;
    private JTextArea output;
    private JComboBox<String> taskTypeComboBox;

    public EmployeeTask() {
        tasksManagement = new TasksManagement();

        setTitle("Employee and Task Manager");
        setSize(600, 600);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(219, 255, 96, 232));

        JLabel nameLabel = new JLabel("Employee name: ");
        nameLabel.setBounds(20, 20, 120, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 20, 150, 25);
        add(nameField);
        nameField.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));

        JButton addEmployeeBtn = new JButton("Add Employee");
        addEmployeeBtn.setBounds(320, 20, 150, 25);
        add(addEmployeeBtn);
        addEmployeeBtn.setBackground(new Color(0, 153, 76));
        addEmployeeBtn.setForeground(Color.WHITE);
        addEmployeeBtn.setFont(new Font("Verdana", Font.BOLD, 14));
        addEmployeeBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JLabel taskIdLabel = new JLabel("Task ID: ");
        taskIdLabel.setBounds(20, 60, 120, 25);
        add(taskIdLabel);

        taskIdField = new JTextField();
        taskIdField.setBounds(150, 60, 150, 25);
        add(taskIdField);
        taskIdField.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));

        JLabel taskStatusLabel = new JLabel("Task Status: ");
        taskStatusLabel.setBounds(20, 100, 120, 25);
        add(taskStatusLabel);

        taskStatusField = new JTextField();
        taskStatusField.setBounds(150, 100, 150, 25);
        add(taskStatusField);
        taskStatusField.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));

        JLabel startLabel = new JLabel("Start Hour: ");
        startLabel.setBounds(20, 140, 150, 25);
        add(startLabel);

        startHourField = new JTextField();
        startHourField.setBounds(150, 140, 150, 25);
        add(startHourField);
        startHourField.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));

        JLabel endLabel = new JLabel("End Hour: ");
        endLabel.setBounds(20, 180 ,120, 25);
        add(endLabel);

        endHourField = new JTextField();
        endHourField.setBounds(150 ,180, 150 ,25);
        add(endHourField);
        endHourField.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));

        JLabel empIdLabel = new JLabel("Employee ID: ");
        empIdLabel.setBounds(20, 220, 120, 25);
        add(empIdLabel);

        empIdField = new JTextField();
        empIdField.setBounds(150, 220, 150, 25);
        add(empIdField);
        empIdField.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));

        JLabel taskTypeLabel = new JLabel("Task type: ");
        taskTypeLabel.setBounds(20, 260, 120, 25);
        add(taskTypeLabel);

        String[] taskTypes = {"SimpleTask", "ComplexTask"};
        taskTypeComboBox = new JComboBox<>(taskTypes);
        taskTypeComboBox.setBounds(150, 260, 150, 25);
        add(taskTypeComboBox);

        JButton addTaskBtn = new JButton("Add Task");
        addTaskBtn.setBounds(320, 100, 150, 25);
        add(addTaskBtn);
        addTaskBtn.setBackground(new Color(0, 153, 76));
        addTaskBtn.setForeground(Color.WHITE);
        addTaskBtn.setFont(new Font("Verdana", Font.BOLD, 14));
        addTaskBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JButton calculateBtn = new JButton("Calculate Work Duration");
        calculateBtn.setBounds(320, 140,200, 25);
        add(calculateBtn);
        calculateBtn.setBackground(new Color(0, 153, 76));
        calculateBtn.setForeground(Color.WHITE);
        calculateBtn.setFont(new Font("Verdana", Font.BOLD, 14));
        calculateBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));


        JButton showAllBtn = new JButton("Show All Data");
        showAllBtn.setBounds(320, 180, 150, 25);
        add(showAllBtn);
        showAllBtn.setBackground(new Color(0, 153, 76));
        showAllBtn.setForeground(Color.WHITE);
        showAllBtn.setFont(new Font("Verdana", Font.BOLD, 14));
        showAllBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        output = new JTextArea();
        output.setBounds(20, 270, 540, 250);
        output.setEditable(false);
        add(output);

        JButton modifyStatusBtn = new JButton("Modify Task Status");
        modifyStatusBtn.setBounds(320, 220, 200, 25);
        add(modifyStatusBtn);
        modifyStatusBtn.setBackground(new Color(0, 153, 76));
        modifyStatusBtn.setForeground(Color.WHITE);
        modifyStatusBtn.setFont(new Font("Verdana", Font.BOLD, 14));
        modifyStatusBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        addEmployeeBtn.addActionListener(e -> {
            String name = nameField.getText();
            if(!name.isEmpty()) {
                tasksManagement.addEmployee(name);
                output.append("Employee " + name + " was added " + "\n");
            }
        });

        addTaskBtn.addActionListener(e -> {
            try {
                int taskId = Integer.parseInt(taskIdField.getText());
                String status = taskStatusField.getText();
                int start = Integer.parseInt(startHourField.getText());
                int end = Integer.parseInt(endHourField.getText());
                int empId = Integer.parseInt(empIdField.getText());

                String selectedTaskType = (String) taskTypeComboBox.getSelectedItem();

                Task task;
                if ("SimpleTask".equals(selectedTaskType)) {
                    task = new SimpleTask(taskId, status, start, end);
                } else {
                    task = new ComplexTask(taskId, status, start, end);
                }

                tasksManagement.assignTaskToEmployee(empId, task);
                output.append("Assigned " + selectedTaskType + " with ID " + taskId + " to employee ID " + empId + "\n");

            } catch (Exception ex) {
                output.append("Error adding task \n");
            }
        });

        calculateBtn.addActionListener(e -> {
            try {
                int empId = Integer.parseInt(empIdField.getText());
                int duration = tasksManagement.calculateEmployeeWorkDuration(empId);
                output.append("Work duration for " + empId + " is: " + duration + "H\n");
            }catch (Exception ex) {
                output.append("Error calculating duration \n");
            }
        });

        modifyStatusBtn.addActionListener(e -> {
            try {
                int taskId = Integer.parseInt(taskIdField.getText());
                int empId = Integer.parseInt(empIdField.getText());
                String newStatus = taskStatusField.getText();

                tasksManagement.modifyTaskStatus(empId, taskId, newStatus);
                output.append("Statusul taskului " + taskId + " modifical la: " + newStatus + "\n");
            }catch (Exception ex) {
                output.append("Eroare la modificare");
            }
        });

        showAllBtn.addActionListener(e -> {
            output.append(tasksManagement.getAllDataString());
        });
        setVisible(true);
    }

}

