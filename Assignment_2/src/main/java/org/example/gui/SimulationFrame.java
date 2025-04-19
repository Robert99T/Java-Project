package org.example.gui;

import org.example.business.SimulationManager;
import org.example.business.SelectionPolicy;

import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {
    private JTextField tfClients, tfQueues, tfSimTime;
    private JTextField tfArrivalMin, tfArrivalMax, tfServiceMin, tfServiceMax;
    private JComboBox<String> strategyCombo;
    private JTextArea logArea;

    public SimulationFrame() {
        setTitle("Queues Simulation");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        tfClients = new JTextField();
        tfQueues = new JTextField();
        tfSimTime = new JTextField();
        tfArrivalMin = new JTextField();
        tfArrivalMax =new JTextField();
        tfServiceMin = new JTextField();
        tfServiceMax = new JTextField();

        strategyCombo = new JComboBox<>(new String[] {"SHORTEST_QUEUE", "SHORTEST_TIME"});

        inputPanel.add(new JLabel("Number of Clients"));
        inputPanel.add(tfClients);

        inputPanel.add(new JLabel("Number of Queues"));
        inputPanel.add(tfQueues);

        inputPanel.add(new JLabel("Simulation Time"));
        inputPanel.add(tfSimTime);

        inputPanel.add(new JLabel("Arrival Time Min"));
        inputPanel.add(tfArrivalMin);

        inputPanel.add(new JLabel("Arrival Time Max"));
        inputPanel.add(tfArrivalMax);

        inputPanel.add(new JLabel("Service Time Min"));
        inputPanel.add(tfServiceMin);

        inputPanel.add(new JLabel("Service Time Max"));
        inputPanel.add(tfServiceMax);

        inputPanel.add(new JLabel("Selection Policy:"));
        inputPanel.add(strategyCombo);

        JButton startButton = new JButton("Start Simulation");
        startButton.addActionListener(e -> startSimulation());

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        add(inputPanel, BorderLayout.NORTH);
        add(startButton, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void startSimulation() {
        try {
            int clients = Integer.parseInt(tfClients.getText());
            int queues = Integer.parseInt(tfQueues.getText());
            int simTime = Integer.parseInt(tfSimTime.getText());
            int arrivalMin = Integer.parseInt(tfArrivalMin.getText());
            int arrivalMax = Integer.parseInt(tfArrivalMax.getText());
            int serviceMin = Integer.parseInt(tfServiceMin.getText());
            int serviceMax = Integer.parseInt(tfServiceMax.getText());
            String selectedPolicy = (String) strategyCombo.getSelectedItem();
            SelectionPolicy policy = SelectionPolicy.valueOf(selectedPolicy);

            logArea.setText("");
            SimulationManager manager = new SimulationManager(clients, queues, simTime, policy, arrivalMin, arrivalMax, serviceMin, serviceMax, logArea);

            Thread simulationThread = new Thread(() -> {manager.run();});
            simulationThread.start();
        }catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Number");
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage());
        }
    }
}
