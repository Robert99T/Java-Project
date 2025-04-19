package org.example.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable {

    private final BlockingQueue<Client> clients;
    private final AtomicInteger currentWaiting = new AtomicInteger(0);
    private boolean running;

    public Queue() {
        clients = new LinkedBlockingQueue<>();
        running = true;
    }

    public void addClient(Client client) {
        clients.add(client);
        currentWaiting.addAndGet(client.getServiceTime());
    }

    public int getCurrentWaiting() {
        return currentWaiting.get();
    }

    public boolean isEmpty() {
        return clients.isEmpty();
    }

    public BlockingQueue<Client> getClients() {
        return clients;
    }

    public void stopQueue() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Client currentClient = clients.take(); // scoate din coadă
                System.out.println("Started serving client " + currentClient.getId());

                while (currentClient.getServiceTime() > 0) {
                    Thread.sleep(1000);
                    currentClient.setServiceTime(currentClient.getServiceTime() - 1);
                    currentWaiting.decrementAndGet();
                    System.out.println("Client " + currentClient.getId() + " remaining time: " + currentClient.getServiceTime());
                }

                System.out.println("Client " + currentClient.getId() + " finished and removed.");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}