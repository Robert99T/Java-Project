package org.example.business;
import org.example.model.Client;
import org.example.model.Queue;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Queue> queues;
    private final int maxNoQueues;
    private Strategy strategy;

    public Scheduler(int maxNoQueues) {
        this.maxNoQueues = maxNoQueues;
        this.queues = new ArrayList<>();

        for(int i = 0; i < maxNoQueues; i++) {
            Queue q = new Queue();
            queues.add(q);
            Thread t = new Thread(q);
            t.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if(policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        } else {
            strategy = new TimeStrategy();
        }
    }

    public void dispatchClient(Client client) {
        strategy.addClient(queues, client);
    }
    public List<Queue> getQueues(){
        return queues;
    }

}
