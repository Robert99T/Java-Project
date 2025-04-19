package org.example.business;

import org.example.model.Client;
import org.example.model.Queue;
import org.example.utils.Logger;

import javax.swing.*;
import java.util.*;


public class SimulationManager implements Runnable {
    private Scheduler scheduler;
    private List<Client> generatedCients;
    private int simulationTimeLimit;
    private SelectionPolicy selectionPolicy;
    private JTextArea logArea;

    public SimulationManager(int clients, int queues, int simTime, SelectionPolicy policy, int arrivalMin, int arrivalMax, int serviceMin, int serviceMax, JTextArea logArea) {
        this.simulationTimeLimit = simTime;
        this.selectionPolicy = policy;
        this.generatedCients = generateRandomClients(clients, arrivalMin, arrivalMax, serviceMin, serviceMax);
        this.scheduler = new Scheduler(queues);
        this.logArea = logArea;
        scheduler.changeStrategy(policy);
    }

    private List<Client> generateRandomClients(int n, int arrivalMin, int arrivalMax,int serviceMin, int serviceMax){
        List<Client> list = new ArrayList<>();
        Random rand = new Random();
        for(int i = 1; i <= n; i++) {
            int arrival = arrivalMin + rand.nextInt(arrivalMax - arrivalMin + 1);
            int service = serviceMin + rand.nextInt(serviceMax - serviceMin + 1);
            list.add(new Client(i, arrival, service));
        }
        list.sort(Comparator.comparingInt(Client::getArrivalTime));
        return list;
    }
    @Override
    public void run() {
        int currentTime = 0;
        while(currentTime <= simulationTimeLimit && (!generatedCients.isEmpty() || !allQueuesEmpty())) {
            System.out.println("time " + currentTime);

            Iterator<Client> it = generatedCients.iterator();
            while(it.hasNext()) {
                Client c = it.next();
                if(c.getArrivalTime() <= currentTime) {
                    scheduler.dispatchClient(c);
                    it.remove();
                }
            }
        logStatus(currentTime);
            currentTime++;
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean allQueuesEmpty() {
        for(Queue q: scheduler.getQueues()) {
            if(!q.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    private void logStatus(int time) {
        StringBuilder sb = new StringBuilder();

        sb.append("Time ").append(time).append("\n");
        sb.append("Waiting clients: ").append(generatedCients).append("\n");

        int i = 1;
        for (Queue q : scheduler.getQueues()) {
            sb.append("Queue ").append(i).append(": ").append(q.getClients()).append("\n");
            i++;
        }
        sb.append("\n");

        System.out.print(sb.toString());


        org.example.utils.Logger.log(sb.toString());

        if (logArea != null) {
            SwingUtilities.invokeLater(() -> logArea.append(sb.toString()));
        }
    }

}
